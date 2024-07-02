package com.plexpt.chatgpt.util.fastjson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.plexpt.chatgpt.util.fastjson.exception.ParseException;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author use
 */
@Slf4j
public class JSON {


    // 时间日期格式
    private static final String STANDARD_FORMAT = "yyyy-MM-dd HH:mm:ss";


    private static ObjectMapper objectMapper = new ObjectMapper();

    //以静态代码块初始化
    static {
        //对象的所有字段全部列入序列化
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //取消默认转换timestamps形式
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        //忽略空Bean转json的错误
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        //所有的日期格式都统一为以下的格式，即yyyy-MM-dd HH:mm:ss
        objectMapper.setDateFormat(new SimpleDateFormat(STANDARD_FORMAT));
        //忽略 在json字符串中存在，但在java对象中不存在对应属性的情况。防止错误
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }


    public static String toJSONString(Object obj) {
        if (obj == null) {
            return null;
        }
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new ParseException(e);
        }
    }

    public static String toJSONString(Object obj, boolean pretty) {
        if (!pretty) {
            return toJSONString(obj);
        }
        try {
            return obj instanceof String ? (String) obj : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (Exception e) {
            throw new ParseException(e);

        }
    }

    /**
     * 对象转Json格式字符串(格式化的Json字符串)
     *
     * @param obj 对象
     * @return 美化的Json格式字符串
     */
    public static <T> String obj2StringPretty(T obj) {
        if (obj == null) {
            return null;
        }
        try {
            return obj instanceof String ? (String) obj : objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.warn("Parse Object to String error : {}", e.getMessage());
            return null;
        }
    }

    public static Object parse(String jsonString) {
        if (isJsonObj(jsonString)) {
            return parseObject(jsonString);
        }
        if (isJsonArray(jsonString)) {
            return parseArray(jsonString);
        }
        try {
            return objectMapper.readValue(jsonString, JsonNode.class);
        } catch (Exception e) {
            throw new ParseException(e);
        }
    }

    public static JSONObject parseObject(String jsonString) {
        try {
            final Map map = objectMapper.readValue(jsonString, Map.class);
            return mapToJsonObject(map);
        } catch (Exception e) {
            throw new ParseException(e);
        }

    }

    public static <T> T parseObject(String jsonString, Class<T> clazz) {
        try {
            return objectMapper.readValue(jsonString, clazz);
        } catch (Exception e) {
            throw new ParseException(e);
        }
    }

    public static JSONArray parseArray(String jsonString) {
        try {
            final List list = objectMapper.readValue(jsonString, List.class);
            return listConvertToJsonArray(list);
        } catch (Exception e) {
            throw new ParseException(e);
        }
    }


    public static <T> List<T> parseArray(String jsonString, Class<T> clazz) {
        try {
            //return objectMapper.readValue(jsonString, new TypeReference<List<T>>() {
            //});
            JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, clazz);
            return objectMapper.readValue(jsonString, javaType);
        } catch (Exception e) {
            throw new ParseException(e);
        }
    }

    //将json数组字符串转为指定对象List列表或者Map集合
    public static <T> T parseJSONArray(String jsonArray, TypeReference<T> reference) {
        T t = null;
        try {
            t = objectMapper.readValue(jsonArray, reference);
        } catch (Exception e) {
            throw new ParseException(e);
        }
        return t;
    }

    public static JsonNode parseJSONObject(String jsonString) {
        JsonNode jsonNode = null;
        try {
            jsonNode = objectMapper.readTree(jsonString);
        } catch (JsonProcessingException e) {
            log.error("JSONString转为JsonNode失败：{}", e.getMessage());
        }
        return jsonNode;
    }

    public static JsonNode parseJSONObject(Object object) {
        JsonNode jsonNode = objectMapper.valueToTree(object);
        return jsonNode;
    }

    public static String toJSONString(JsonNode jsonNode) {
        String jsonString = null;
        try {
            jsonString = objectMapper.writeValueAsString(jsonNode);
        } catch (JsonProcessingException e) {
            log.error("JsonNode转JSONString失败：{}", e.getMessage());
        }
        return jsonString;
    }


    /**
     * 是否为JSON字符串，首尾都为大括号或中括号判定为JSON字符串
     *
     * @param str 字符串
     * @return 是否为JSON字符串
     */
    public static boolean isJson(String str) {
        return isJsonObj(str) || isJsonArray(str);
    }

    /**
     * 是否为JSONObject字符串，首尾都为大括号或中括号判定为JSON字符串
     *
     * @param str 字符串
     * @return 是否为JSON字符串
     */
    public static boolean isJsonObj(String str) {

        if (isBlank(str)) {
            return false;
        }
        return isWrap(str.trim(), '{', '}');
    }

    /**
     * 是否为JSONObject字符串，首尾都为大括号或中括号判定为JSON字符串
     *
     * @param str 字符串
     * @return 是否为JSON字符串
     */
    public static boolean isJsonArray(String str) {
        if (isBlank(str)) {
            return false;
        }
        return isWrap(str.trim(), '[', ']');
    }

    private static boolean isBlank(String str) {
        return str == null || str.trim().length() == 0;
    }

    private static boolean isWrap(String str, char start, char end) {
        if (isBlank(str)) {
            return false;
        }
        return str.charAt(0) == start && str.charAt(str.length() - 1) == end;
    }

    private static JSONArray listConvertToJsonArray(List list) {
        List<Object> jsonObjects = new ArrayList<>(list.size());
        for (Object obj : list) {
            jsonObjects.add(mapToJsonObject((Map<String, Object>) obj));
        }
        return new JSONArray(jsonObjects);
    }

    /**
     * jackson parse出来的是map和list,所以把map和list转换为jsonObject和jsonArray
     *
     * @param map
     * @return
     */
    private static JSONObject mapToJsonObject(Map<String, Object> map) {
        JSONObject jsonObject = new JSONObject(map.size());
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            final Object value = entry.getValue();
            if (value instanceof Map) {
                jsonObject.put(entry.getKey(), mapToJsonObject((Map<String, Object>) value));
            } else if (value instanceof List) {
                final List listVal = (List) value;
                JSONArray objects = new JSONArray(listVal.size());
                for (Object o : listVal) {
                    if (o instanceof Map) {
                        objects.add(mapToJsonObject((Map<String, Object>) o));
                    } else if (o instanceof List) {
                        objects.add(listConvertToJsonArray((List) o));
                    }
                }
                jsonObject.put(entry.getKey(), objects);
            } else {
                jsonObject.put(entry.getKey(), value);
            }
        }
        return jsonObject;
    }

    public static void main(String[] args) {
        String str = "[{\"id\":\"czpv7jeb08baq8\",\"name\":\"{\\\"ja\\\":\\\"一行二列\\\",\\\"en\\\":\\\"1 Row/2 Columns\\\",\\\"zh\\\":\\\"一行两列\\\"}\",\"required\":false,\"tempAttr\":{\"xtype\":\"twoColumns\",\"controlType\":\"grid\",\"name\":{\"ja\":\"一行二列\",\"en\":\"1 Row/2 Columns\",\"zh\":\"一行两列\"},\"icon\":\"&#xe610;\",\"isChooseDefault\":false,\"caption\":{\"ja\":\"一行二列\",\"en\":\"1 Row/2 Columns\",\"zh\":\"一行两列\"},\"pid\":0,\"id\":\"czpv7jeb08baq8\",\"required\":false,\"actived\":false,\"vModel\":\"twoColumns_czpv7jeb08baq8\"}},{\"id\":\"cqv0lb6zv3w19n\",\"name\":\"{\\\"ja\\\":\\\"\\\",\\\"en\\\":\\\"\\\",\\\"zh\\\":\\\"一行两列左侧\\\"}\",\"required\":true,\"tempAttr\":{\"xtype\":\"div\",\"name\":{\"ja\":\"\",\"en\":\"\",\"zh\":\"一行两列左侧\"},\"isChooseDefault\":false,\"pid\":\"czpv7jeb08baq8\",\"id\":\"cqv0lb6zv3w19n\",\"required\":true,\"actived\":false}},{\"id\":\"c6ps5bwq4f6x1o\",\"name\":\"{\\\"ja\\\":\\\"単数行入力ボックス\\\",\\\"en\\\":\\\"Single-line Box\\\",\\\"zh\\\":\\\"单行输入框\\\"}\",\"required\":false,\"tempAttr\":{\"xtype\":\"input\",\"defaultValue\":\"\",\"checkboxValue\":\"1\",\"icon\":\"icondanhang\",\"caption\":{\"ja\":\"\",\"en\":\"Single line input field\",\"zh\":\"单行输入框\"},\"captionMaxLength\":100,\"pid\":\"cqv0lb6zv3w19n\",\"captionMinLength\":10,\"visibleState\":\"default\",\"required\":false,\"labelPosition\":\"top\",\"placeholderMaxLength\":100,\"isEncrypted\":false,\"name\":{\"ja\":\"単数行入力ボックス\",\"en\":\"Single-line Box\",\"zh\":\"单行输入框\"},\"isChooseDefault\":false,\"placeholder\":{\"ja\":\"\",\"en\":\"\",\"zh\":\"请输入\"},\"id\":\"c6ps5bwq4f6x1o\",\"maxLength\":400,\"actived\":false,\"vModel\":\"input_c6ps5bwq4f6x1o\"}},{\"id\":\"c8wrg7tlxpopdd\",\"name\":\"{\\\"ja\\\":\\\"\\\",\\\"en\\\":\\\"\\\",\\\"zh\\\":\\\"一行两列右侧\\\"}\",\"required\":true,\"tempAttr\":{\"xtype\":\"div\",\"name\":{\"ja\":\"\",\"en\":\"\",\"zh\":\"一行两列右侧\"},\"isChooseDefault\":false,\"pid\":\"czpv7jeb08baq8\",\"id\":\"c8wrg7tlxpopdd\",\"required\":true,\"actived\":false}},{\"caption\":{\"en\":\"number\",\"ja\":\"\",\"zh\":\"数字输入框\"},\"id\":\"cgrck7x66l5fxq\",\"name\":\"{\\\"ja\\\":\\\"数字入力ボックス\\\",\\\"en\\\":\\\"Number Box\\\",\\\"zh\\\":\\\"数字输入框\\\"}\",\"required\":false,\"tempAttr\":{\"xtype\":\"number\",\"defaultValue\":\"\",\"checkboxValue\":\"1\",\"rangeMin\":\"\",\"icon\":\"iconnum\",\"caption\":{\"ja\":\"\",\"en\":\"number\",\"zh\":\"数字输入框\"},\"captionMaxLength\":100,\"pid\":\"c8wrg7tlxpopdd\",\"float\":0,\"required\":false,\"unit\":{\"ja\":\"\",\"en\":\"\",\"zh\":\"\"},\"labelPosition\":\"top\",\"rangeMax\":\"\",\"placeholderMaxLength\":100,\"unitMaxLength\":10,\"valueType\":0,\"isEncrypted\":false,\"name\":{\"ja\":\"数字入力ボックス\",\"en\":\"Number Box\",\"zh\":\"数字输入框\"},\"isChooseDefault\":false,\"placeholder\":{\"ja\":\"\",\"en\":\"\",\"zh\":\"请输入\"},\"id\":\"cgrck7x66l5fxq\",\"actived\":false,\"vModel\":\"number_cgrck7x66l5fxq\"},\"unit\":{\"en\":\"\",\"ja\":\"\",\"zh\":\"\"}},{\"caption\":{\"en\":\"Multiple lines of input field\",\"ja\":\"\",\"zh\":\"多行输入框\"},\"id\":\"cwx5dyzrbtns6z\",\"maxLength\":30000,\"name\":\"{\\\"ja\\\":\\\"複数行入力ボックス\\\",\\\"en\\\":\\\"Multi-line Box\\\",\\\"zh\\\":\\\"多行输入框\\\"}\",\"required\":false,\"tempAttr\":{\"xtype\":\"textarea\",\"defaultValue\":\"\",\"checkboxValue\":\"1\",\"icon\":\"iconduohang\",\"caption\":{\"ja\":\"\",\"en\":\"Multiple lines of input field\",\"zh\":\"多行输入框\"},\"captionMaxLength\":100,\"pid\":0,\"captionMinLength\":10,\"visibleState\":\"default\",\"rows\":2,\"required\":false,\"labelPosition\":\"top\",\"placeholderMaxLength\":100,\"isEncrypted\":false,\"name\":{\"ja\":\"複数行入力ボックス\",\"en\":\"Multi-line Box\",\"zh\":\"多行输入框\"},\"isChooseDefault\":false,\"placeholder\":{\"ja\":\"\",\"en\":\"\",\"zh\":\"请输入\"},\"id\":\"cwx5dyzrbtns6z\",\"maxLength\":30000,\"actived\":false,\"vModel\":\"textarea_cwx5dyzrbtns6z\"}},{\"caption\":{\"en\":\"amount\",\"ja\":\"\",\"zh\":\"金额\"},\"id\":\"c2w6rckildjrpe\",\"isCapital\":\"true\",\"name\":\"{\\\"ja\\\":\\\"金額\\\",\\\"en\\\":\\\"Amount\\\",\\\"zh\\\":\\\"金额\\\"}\",\"required\":false,\"tempAttr\":{\"xtype\":\"currency\",\"is_capital\":true,\"defaultValue\":\"\",\"checkboxValue\":\"1\",\"rangeMin\":\"\",\"icon\":\"iconYUAN1\",\"caption\":{\"ja\":\"\",\"en\":\"amount\",\"zh\":\"金额\"},\"captionMaxLength\":100,\"pid\":0,\"float\":0,\"isChooseCurrency\":true,\"required\":false,\"unit\":\"0\",\"labelPosition\":\"top\",\"rangeMax\":\"\",\"placeholderMaxLength\":100,\"isEncrypted\":false,\"name\":{\"ja\":\"金額\",\"en\":\"Amount\",\"zh\":\"金额\"},\"isChooseDefault\":false,\"placeholder\":{\"ja\":\"\",\"en\":\"\",\"zh\":\"请输入金额\"},\"id\":\"c2w6rckildjrpe\",\"actived\":false,\"vModel\":\"currency_c2w6rckildjrpe\"},\"unit\":{\"en\":\"\",\"ja\":\"\",\"zh\":\"0\"}},{\"id\":\"c8ob6ogo2amfbw\",\"name\":\"{\\\"ja\\\":\\\"分割線\\\",\\\"en\\\":\\\"Split Line\\\",\\\"zh\\\":\\\"分割线\\\"}\",\"required\":false,\"tempAttr\":{\"xtype\":\"divider\",\"color\":\"#d9d9d9\",\"icon\":\"iconDividingline\",\"caption\":{\"ja\":\"分割線\",\"en\":\"Split Line\",\"zh\":\"分割线\"},\"dividerClassName\":\"solid-line\",\"pid\":0,\"required\":false,\"labelPosition\":\"top\",\"name\":{\"ja\":\"分割線\",\"en\":\"Split Line\",\"zh\":\"分割线\"},\"isChooseDefault\":false,\"id\":\"c8ob6ogo2amfbw\",\"actived\":false,\"vModel\":\"divider_c8ob6ogo2amfbw\"}},{\"caption\":{\"en\":\"pull-down\",\"ja\":\"\",\"zh\":\"下拉菜单\"},\"id\":\"ckfr0w61nxpa4k\",\"name\":\"{\\\"ja\\\":\\\"ドロップダウンメニュー\\\",\\\"en\\\":\\\"Drop-down Menu\\\",\\\"zh\\\":\\\"下拉菜单\\\"}\",\"options\":[{\"optionId\":\"ckfr0w61nxpa4kc6v2e634v9f9eo\",\"text\":\"{\\\"ja\\\":\\\"\\\",\\\"en\\\":\\\"\\\",\\\"zh\\\":\\\"选项1\\\"}\",\"value\":\"ckfr0w61nxpa4kc6v2e634v9f9eo\"},{\"optionId\":\"ckfr0w61nxpa4kc79zwi1yqs0uot\",\"text\":\"{\\\"ja\\\":\\\"\\\",\\\"en\\\":\\\"\\\",\\\"zh\\\":\\\"选项2\\\"}\",\"value\":\"ckfr0w61nxpa4kc79zwi1yqs0uot\"},{\"optionId\":\"ckfr0w61nxpa4kc5kqk06xnfcj9d\",\"text\":\"{\\\"ja\\\":\\\"\\\",\\\"en\\\":\\\"\\\",\\\"zh\\\":\\\"选项3\\\"}\",\"value\":\"ckfr0w61nxpa4kc5kqk06xnfcj9d\"}],\"required\":false,\"tempAttr\":{\"xtype\":\"select\",\"chainData\":{},\"defaultValue\":\"\",\"checkboxValue\":\"1\",\"icon\":\"iconpullup\",\"caption\":{\"ja\":\"\",\"en\":\"pull-down\",\"zh\":\"下拉菜单\"},\"captionMaxLength\":100,\"pid\":0,\"required\":false,\"labelPosition\":\"top\",\"placeholderMaxLength\":100,\"showSearch\":true,\"name\":{\"ja\":\"ドロップダウンメニュー\",\"en\":\"Drop-down Menu\",\"zh\":\"下拉菜单\"},\"options\":[{\"hasManyShow\":[],\"hasManyHide\":[],\"option_id\":\"ckfr0w61nxpa4kc6v2e634v9f9eo\",\"text\":{\"ja\":\"\",\"en\":\"\",\"zh\":\"选项1\"},\"value\":\"ckfr0w61nxpa4kc6v2e634v9f9eo\"},{\"hasManyShow\":[],\"hasManyHide\":[],\"option_id\":\"ckfr0w61nxpa4kc79zwi1yqs0uot\",\"text\":{\"ja\":\"\",\"en\":\"\",\"zh\":\"选项2\"},\"value\":\"ckfr0w61nxpa4kc79zwi1yqs0uot\"},{\"hasManyShow\":[],\"hasManyHide\":[],\"option_id\":\"ckfr0w61nxpa4kc5kqk06xnfcj9d\",\"text\":{\"ja\":\"\",\"en\":\"\",\"zh\":\"选项3\"},\"value\":\"ckfr0w61nxpa4kc5kqk06xnfcj9d\"}],\"isChooseDefault\":false,\"placeholder\":{\"ja\":\"\",\"en\":\"\",\"zh\":\"请选择\"},\"id\":\"ckfr0w61nxpa4k\",\"actived\":false,\"vModel\":\"select_ckfr0w61nxpa4k\"}},{\"id\":\"cij9uj19quo7p2\",\"name\":\"{\\\"ja\\\":\\\"カードグループ分け\\\",\\\"en\\\":\\\"Card Group\\\",\\\"zh\\\":\\\"卡片分组\\\"}\",\"required\":false,\"tempAttr\":{\"xtype\":\"cardGroup\",\"controlType\":\"grid\",\"name\":{\"ja\":\"カードグループ分け\",\"en\":\"Card Group\",\"zh\":\"卡片分组\"},\"icon\":\"&#xe610;\",\"isChooseDefault\":false,\"caption\":{\"ja\":\"カードグループ分け\",\"en\":\"Card Group\",\"zh\":\"卡片分组\"},\"pid\":0,\"id\":\"cij9uj19quo7p2\",\"required\":false,\"actived\":false,\"vModel\":\"cardGroup_cij9uj19quo7p2\"}},{\"caption\":{\"en\":\"radio button\",\"ja\":\"\",\"zh\":\"单选框\"},\"id\":\"cof6ezra2lizdr\",\"name\":\"{\\\"ja\\\":\\\"ラジオボックス\\\",\\\"en\\\":\\\"Radio Button\\\",\\\"zh\\\":\\\"单选框\\\"}\",\"options\":[{\"optionId\":\"cof6ezra2lizdrcm4i6c1hqpma0g\",\"text\":\"{\\\"ja\\\":\\\"\\\",\\\"en\\\":\\\"\\\",\\\"zh\\\":\\\"选项1\\\"}\",\"value\":\"cof6ezra2lizdrcm4i6c1hqpma0g\"},{\"optionId\":\"cof6ezra2lizdrc814hlvykcb0q3\",\"text\":\"{\\\"ja\\\":\\\"\\\",\\\"en\\\":\\\"\\\",\\\"zh\\\":\\\"选项2\\\"}\",\"value\":\"cof6ezra2lizdrc814hlvykcb0q3\"},{\"optionId\":\"cof6ezra2lizdrcho1eavxds1ujq\",\"text\":\"{\\\"ja\\\":\\\"\\\",\\\"en\\\":\\\"\\\",\\\"zh\\\":\\\"选项3\\\"}\",\"value\":\"cof6ezra2lizdrcho1eavxds1ujq\"}],\"required\":false,\"tempAttr\":{\"xtype\":\"radio\",\"chainData\":{},\"checkboxValue\":\"1\",\"defaultValue\":\"\",\"icon\":\"iconradio\",\"caption\":{\"ja\":\"\",\"en\":\"radio button\",\"zh\":\"单选框\"},\"captionMaxLength\":100,\"pid\":\"cij9uj19quo7p2\",\"type\":1,\"required\":false,\"labelPosition\":\"top\",\"placeholderMaxLength\":100,\"name\":{\"ja\":\"ラジオボックス\",\"en\":\"Radio Button\",\"zh\":\"单选框\"},\"options\":[{\"hasManyShow\":[],\"hasManyHide\":[],\"option_id\":\"cof6ezra2lizdrcm4i6c1hqpma0g\",\"text\":{\"ja\":\"\",\"en\":\"\",\"zh\":\"选项1\"},\"value\":\"cof6ezra2lizdrcm4i6c1hqpma0g\"},{\"hasManyShow\":[],\"hasManyHide\":[],\"option_id\":\"cof6ezra2lizdrc814hlvykcb0q3\",\"text\":{\"ja\":\"\",\"en\":\"\",\"zh\":\"选项2\"},\"value\":\"cof6ezra2lizdrc814hlvykcb0q3\"},{\"hasManyShow\":[],\"hasManyHide\":[],\"option_id\":\"cof6ezra2lizdrcho1eavxds1ujq\",\"text\":{\"ja\":\"\",\"en\":\"\",\"zh\":\"选项3\"},\"value\":\"cof6ezra2lizdrcho1eavxds1ujq\"}],\"isChooseDefault\":false,\"placeholder\":{\"ja\":\"\",\"en\":\"\",\"zh\":\"请勾选\"},\"id\":\"cof6ezra2lizdr\",\"actived\":false,\"vModel\":\"radio_cof6ezra2lizdr\"}},{\"caption\":{\"en\":\"check box\",\"ja\":\"\",\"zh\":\"多选框\"},\"id\":\"ctac6cut8gkpc1\",\"name\":\"{\\\"ja\\\":\\\"複数選択リストボックス\\\",\\\"en\\\":\\\"Checkbox\\\",\\\"zh\\\":\\\"多选框\\\"}\",\"options\":[{\"optionId\":\"ctac6cut8gkpc1cicy4dcqi7ej4x\",\"text\":\"{\\\"ja\\\":\\\"\\\",\\\"en\\\":\\\"\\\",\\\"zh\\\":\\\"选项1\\\"}\",\"value\":\"ctac6cut8gkpc1cicy4dcqi7ej4x\"},{\"optionId\":\"ctac6cut8gkpc1c34wh8qndhzlj6\",\"text\":\"{\\\"ja\\\":\\\"\\\",\\\"en\\\":\\\"\\\",\\\"zh\\\":\\\"选项2\\\"}\",\"value\":\"ctac6cut8gkpc1c34wh8qndhzlj6\"},{\"optionId\":\"ctac6cut8gkpc1c1k0ng96ro7xtw\",\"text\":\"{\\\"ja\\\":\\\"\\\",\\\"en\\\":\\\"\\\",\\\"zh\\\":\\\"选项3\\\"}\",\"value\":\"ctac6cut8gkpc1c1k0ng96ro7xtw\"}],\"required\":false,\"tempAttr\":{\"xtype\":\"checkbox\",\"chainData\":{},\"max\":\"\",\"defaultValue\":[],\"checkboxValue\":\"1\",\"icon\":\"iconcheck-square\",\"caption\":{\"ja\":\"\",\"en\":\"check box\",\"zh\":\"多选框\"},\"captionMaxLength\":100,\"pid\":\"cij9uj19quo7p2\",\"type\":1,\"required\":false,\"min\":\"\",\"labelPosition\":\"top\",\"placeholderMaxLength\":100,\"name\":{\"ja\":\"複数選択リストボックス\",\"en\":\"Checkbox\",\"zh\":\"多选框\"},\"options\":[{\"hasManyShow\":[],\"hasManyHide\":[],\"option_id\":\"ctac6cut8gkpc1cicy4dcqi7ej4x\",\"text\":{\"ja\":\"\",\"en\":\"\",\"zh\":\"选项1\"},\"value\":\"ctac6cut8gkpc1cicy4dcqi7ej4x\"},{\"hasManyShow\":[],\"hasManyHide\":[],\"option_id\":\"ctac6cut8gkpc1c34wh8qndhzlj6\",\"text\":{\"ja\":\"\",\"en\":\"\",\"zh\":\"选项2\"},\"value\":\"ctac6cut8gkpc1c34wh8qndhzlj6\"},{\"hasManyShow\":[],\"hasManyHide\":[],\"option_id\":\"ctac6cut8gkpc1c1k0ng96ro7xtw\",\"text\":{\"ja\":\"\",\"en\":\"\",\"zh\":\"选项3\"},\"value\":\"ctac6cut8gkpc1c1k0ng96ro7xtw\"}],\"isChooseDefault\":false,\"placeholder\":{\"ja\":\"\",\"en\":\"\",\"zh\":\"请勾选\"},\"id\":\"ctac6cut8gkpc1\",\"value\":[],\"actived\":false,\"vModel\":\"checkbox_ctac6cut8gkpc1\"}},{\"caption\":{\"en\":\"personnel\",\"ja\":\"\",\"zh\":\"人员\"},\"id\":\"cyk8p1c0uetxe1\",\"name\":\"{\\\"ja\\\":\\\"メンバー\\\",\\\"en\\\":\\\"Personnel\\\",\\\"zh\\\":\\\"人员\\\"}\",\"range\":\"all\",\"required\":false,\"tempAttr\":{\"xtype\":\"linkMan\",\"defaultValueUserList\":[],\"checkboxValue\":\"1\",\"defaultValue\":[],\"checkedValue\":false,\"multiple\":true,\"icon\":\"iconpeople\",\"caption\":{\"ja\":\"\",\"en\":\"personnel\",\"zh\":\"人员\"},\"range\":\"all\",\"defaultValueAsign\":\"designated\",\"captionMaxLength\":100,\"pid\":0,\"required\":false,\"listId\":[],\"labelPosition\":\"top\",\"placeholderMaxLength\":100,\"name\":{\"ja\":\"メンバー\",\"en\":\"Personnel\",\"zh\":\"人员\"},\"isChooseDefault\":false,\"placeholder\":{\"ja\":\"\",\"en\":\"\",\"zh\":\"请输入姓名、邮箱前缀\"},\"id\":\"cyk8p1c0uetxe1\",\"actived\":false,\"vModel\":\"linkMan_cyk8p1c0uetxe1\"}},{\"caption\":{\"en\":\"department\",\"ja\":\"\",\"zh\":\"部门\"},\"id\":\"cze76ccvnaltku\",\"name\":\"{\\\"ja\\\":\\\"部門\\\",\\\"en\\\":\\\"Department\\\",\\\"zh\\\":\\\"部门\\\"}\",\"required\":false,\"tempAttr\":{\"xtype\":\"department\",\"defaultValueDepartmentList\":[],\"checkboxValue\":\"1\",\"defaultValue\":[],\"multiple\":true,\"icon\":\"iconvisitor_tenant\",\"caption\":{\"ja\":\"\",\"en\":\"department\",\"zh\":\"部门\"},\"defaultValueAsign\":\"designated\",\"captionMaxLength\":100,\"pid\":0,\"rows\":2,\"required\":false,\"labelPosition\":\"top\",\"placeholderMaxLength\":100,\"name\":{\"ja\":\"部門\",\"en\":\"Department\",\"zh\":\"部门\"},\"isChooseDefault\":false,\"placeholder\":{\"ja\":\"\",\"en\":\"\",\"zh\":\"请输入部门\"},\"id\":\"cze76ccvnaltku\",\"departments\":[],\"actived\":true,\"vModel\":\"department_cze76ccvnaltku\"}},{\"id\":\"c62ezbfftcxzvq\",\"name\":\"{\\\"ja\\\":\\\"カードグループ分け\\\",\\\"en\\\":\\\"Card Group\\\",\\\"zh\\\":\\\"卡片分组\\\"}\",\"required\":false,\"tempAttr\":{\"xtype\":\"cardGroup\",\"controlType\":\"grid\",\"name\":{\"ja\":\"カードグループ分け\",\"en\":\"Card Group\",\"zh\":\"卡片分组\"},\"icon\":\"&#xe610;\",\"isChooseDefault\":false,\"caption\":{\"ja\":\"カードグループ分け\",\"en\":\"Card Group\",\"zh\":\"卡片分组\"},\"pid\":0,\"id\":\"c62ezbfftcxzvq\",\"required\":false,\"actived\":false,\"vModel\":\"cardGroup_c62ezbfftcxzvq\"}},{\"caption\":{\"en\":\"date\",\"ja\":\"\",\"zh\":\"日期\"},\"id\":\"cqd2fejebws7tl\",\"name\":\"{\\\"ja\\\":\\\"日付\\\",\\\"en\\\":\\\"Date\\\",\\\"zh\\\":\\\"日期\\\"}\",\"required\":false,\"tempAttr\":{\"timeZoneConfiguration\":\"absoluteTime\",\"defaultValue\":\"\",\"isShowTimeZone\":false,\"icon\":\"iconcalendar\",\"caption\":{\"ja\":\"\",\"en\":\"date\",\"zh\":\"日期\"},\"pid\":\"c62ezbfftcxzvq\",\"systemTime\":\"\",\"required\":false,\"dateType\":\"date\",\"limitDateSavedList\":[],\"labelPosition\":\"top\",\"placeholderMaxLength\":100,\"isEncrypted\":false,\"isChooseDefault\":false,\"disabled\":false,\"placeholder\":{\"ja\":\"\",\"en\":\"\",\"zh\":\"选择日期\"},\"id\":\"cqd2fejebws7tl\",\"actived\":false,\"vModel\":\"date_cqd2fejebws7tl\",\"defaultDate\":false,\"xtype\":\"date\",\"checkboxValue\":\"1\",\"captionMaxLength\":100,\"defaultType\":\"\",\"name\":{\"ja\":\"日付\",\"en\":\"Date\",\"zh\":\"日期\"},\"defaultValueTimeZone\":{\"zh_name\":\"(GMT+8:00)中国标准时间 - 上海\",\"code\":\"Asia/Shanghai\",\"en_name\":\"(GMT+8:00)China Standard Time - Shanghai\",\"id\":\"Asia/Shanghai\",\"ja_name\":\"(GMT+8:00)中国標準時 - 上海\"}}},{\"id\":\"cpe1hovktz9dzx\",\"name\":\"{\\\"ja\\\":\\\"日付区間\\\",\\\"en\\\":\\\"Date Range\\\",\\\"zh\\\":\\\"日期区间\\\"}\",\"required\":false,\"tempAttr\":{\"dateDiff\":true,\"timeZoneConfiguration\":\"absoluteTime\",\"defaultValue\":[],\"isShowTimeZone\":false,\"icon\":\"iconDateinterval\",\"caption\":{\"ja\":\"\",\"en\":\"date interval\",\"zh\":\"日期区间\"},\"pid\":\"c62ezbfftcxzvq\",\"required\":false,\"dateType\":\"daterange\",\"limitDateSavedList\":[],\"labelPosition\":\"top\",\"placeholderMaxLength\":100,\"isEncrypted\":false,\"isChooseDefault\":false,\"disabled\":false,\"end\":\"\",\"placeholder\":{\"ja\":\"\",\"en\":\"\",\"zh\":\"选择日期\"},\"id\":\"cpe1hovktz9dzx\",\"actived\":false,\"vModel\":\"daterange_cpe1hovktz9dzx\",\"xtype\":\"daterange\",\"checkboxValue\":\"1\",\"start\":\"\",\"captionMaxLength\":100,\"name\":{\"ja\":\"日付区間\",\"en\":\"Date Range\",\"zh\":\"日期区间\"},\"defaultValueTimeZone\":{\"zh_name\":\"(GMT+8:00)中国标准时间 - 上海\",\"code\":\"Asia/Shanghai\",\"en_name\":\"(GMT+8:00)China Standard Time - Shanghai\",\"id\":\"Asia/Shanghai\",\"ja_name\":\"(GMT+8:00)中国標準時 - 上海\"}}},{\"caption\":{\"en\":\"photo\",\"ja\":\"\",\"zh\":\"图片\"},\"id\":\"c23t346jet3152\",\"name\":\"{\\\"ja\\\":\\\"画像\\\",\\\"en\\\":\\\"Image\\\",\\\"zh\\\":\\\"图片\\\"}\",\"required\":false,\"tempAttr\":{\"xtype\":\"pic\",\"checkboxValue\":\"1\",\"defaultValue\":[],\"icon\":\"iconpic1\",\"caption\":{\"ja\":\"\",\"en\":\"photo\",\"zh\":\"图片\"},\"captionMaxLength\":100,\"pid\":0,\"required\":false,\"dateType\":\"pic\",\"defaultValuePicList\":[],\"labelPosition\":\"top\",\"isEncrypted\":false,\"name\":{\"ja\":\"画像\",\"en\":\"Image\",\"zh\":\"图片\"},\"placeholder\":\"\",\"id\":\"c23t346jet3152\",\"actived\":false,\"vModel\":\"pic_c23t346jet3152\"}},{\"caption\":{\"en\":\"file\",\"ja\":\"\",\"zh\":\"附件\"},\"id\":\"czwy03e4xteml2\",\"name\":\"{\\\"ja\\\":\\\"添付ファイル\\\",\\\"en\\\":\\\"Attachment\\\",\\\"zh\\\":\\\"附件\\\"}\",\"required\":false,\"tempAttr\":{\"xtype\":\"attachment\",\"checkboxValue\":\"1\",\"defaultValue\":[],\"multiple\":true,\"icon\":\"iconEnclosure\",\"caption\":{\"ja\":\"\",\"en\":\"file\",\"zh\":\"附件\"},\"captionMaxLength\":100,\"pid\":0,\"required\":false,\"labelPosition\":\"top\",\"isEncrypted\":false,\"name\":{\"ja\":\"添付ファイル\",\"en\":\"Attachment\",\"zh\":\"附件\"},\"isChooseDefault\":false,\"id\":\"czwy03e4xteml2\",\"actived\":false,\"vModel\":\"attachment_czwy03e4xteml2\"}},{\"caption\":{\"en\":\"The rich text box\",\"ja\":\"\",\"zh\":\"富文本框\"},\"id\":\"cmfym7k5hwngoo\",\"maxLength\":30000,\"name\":\"{\\\"ja\\\":\\\"リッチテキストボックス\\\",\\\"en\\\":\\\"Rich Textbox\\\",\\\"zh\\\":\\\"富文本框\\\"}\",\"required\":false,\"tempAttr\":{\"xtype\":\"richText\",\"checkboxValue\":\"1\",\"icon\":\"icondanju1\",\"caption\":{\"ja\":\"\",\"en\":\"The rich text box\",\"zh\":\"富文本框\"},\"captionMaxLength\":100,\"pid\":0,\"captionMinLength\":10,\"rows\":2,\"required\":false,\"labelPosition\":\"top\",\"placeholderMaxLength\":20,\"name\":{\"ja\":\"リッチテキストボックス\",\"en\":\"Rich Textbox\",\"zh\":\"富文本框\"},\"isChooseDefault\":false,\"placeholder\":{\"ja\":\"\",\"en\":\"\",\"zh\":\"请输入\"},\"id\":\"cmfym7k5hwngoo\",\"maxLength\":30000,\"actived\":false,\"vModel\":\"richText_cmfym7k5hwngoo\"}},{\"caption\":{\"en\":\"design formulas\",\"ja\":\"\",\"zh\":\"计算公式\"},\"id\":\"c0dbn6mvyvd7a7\",\"name\":\"{\\\"ja\\\":\\\"計算式\\\",\\\"en\\\":\\\"Formula\\\",\\\"zh\\\":\\\"计算公式\\\"}\",\"required\":false,\"tempAttr\":{\"xtype\":\"calculator\",\"edit\":true,\"checkboxValue\":\"2\",\"icon\":\"iconjisuan\",\"caption\":{\"ja\":\"\",\"en\":\"design formulas\",\"zh\":\"计算公式\"},\"captionMaxLength\":40,\"pid\":0,\"required\":false,\"unit\":{\"ja\":\"\",\"en\":\"\",\"zh\":\"\"},\"decimalPlaces\":2,\"labelPosition\":\"top\",\"unitMaxLength\":20,\"name\":{\"ja\":\"計算式\",\"en\":\"Formula\",\"zh\":\"计算公式\"},\"isChooseDefault\":false,\"formula\":[{\"xtype\":\"number\",\"defaultValue\":\"\",\"checkboxValue\":\"1\",\"rangeMin\":\"\",\"icon\":\"iconnum\",\"caption\":\"数字输入框\",\"captionMaxLength\":100,\"pid\":\"c8wrg7tlxpopdd\",\"type\":\"ControlVariable\",\"float\":0,\"required\":false,\"unit\":{\"ja\":\"\",\"en\":\"\",\"zh\":\"\"},\"labelPosition\":\"top\",\"rangeMax\":\"\",\"placeholderMaxLength\":100,\"unitMaxLength\":10,\"valueType\":0,\"isEncrypted\":false,\"name\":{\"ja\":\"数字入力ボックス\",\"en\":\"Number Box\",\"zh\":\"数字输入框\"},\"isChooseDefault\":false,\"placeholder\":{\"ja\":\"\",\"en\":\"\",\"zh\":\"请输入\"},\"id\":\"cgrck7x66l5fxq\",\"actived\":false,\"vModel\":\"number_cgrck7x66l5fxq\"},{\"caption\":\"+\",\"type\":\"Operator\"},{\"xtype\":\"currency\",\"is_capital\":true,\"defaultValue\":\"\",\"checkboxValue\":\"1\",\"rangeMin\":\"\",\"icon\":\"iconYUAN1\",\"caption\":\"金额\",\"captionMaxLength\":100,\"pid\":0,\"type\":\"ControlVariable\",\"float\":0,\"isChooseCurrency\":true,\"required\":false,\"unit\":\"0\",\"labelPosition\":\"top\",\"rangeMax\":\"\",\"placeholderMaxLength\":100,\"isEncrypted\":false,\"name\":{\"ja\":\"金額\",\"en\":\"Amount\",\"zh\":\"金额\"},\"isChooseDefault\":false,\"placeholder\":{\"ja\":\"\",\"en\":\"\",\"zh\":\"请输入金额\"},\"id\":\"c2w6rckildjrpe\",\"actived\":false,\"vModel\":\"currency_c2w6rckildjrpe\"}],\"placeholder\":{\"ja\":\"\",\"en\":\"\",\"zh\":\"计算公式\"},\"id\":\"c0dbn6mvyvd7a7\",\"actived\":false,\"vModel\":\"calculator_c0dbn6mvyvd7a7\"}},{\"caption\":{\"en\":\"association form\",\"ja\":\"\",\"zh\":\"关联表单\"},\"id\":\"cxs430hjxewxx3\",\"isMultiSelect\":false,\"linkFbKey\":\"1b243704fbb44e869d644be0d771bae3\",\"name\":\"{\\\"ja\\\":\\\"関連フォーム\\\",\\\"en\\\":\\\"Associated Form\\\",\\\"zh\\\":\\\"关联表单\\\"}\",\"required\":false,\"tempAttr\":{\"xtype\":\"linkForm\",\"formNameJSON\":\"{\\\"zh\\\":\\\"yy回归验证的表单\\\",\\\"en\\\":\\\"yy回归验证的表单\\\",\\\"ja\\\":\\\"yy回归验证的表单\\\"}\",\"checkboxValue\":\"1\",\"icon\":\"iconpreviewlist\",\"caption\":{\"ja\":\"\",\"en\":\"association form\",\"zh\":\"关联表单\"},\"captionMaxLength\":100,\"isMultiple\":true,\"pid\":0,\"required\":false,\"link_fb_key\":\"1b243704fbb44e869d644be0d771bae3\",\"labelPosition\":\"top\",\"link_form_name\":\"{\\\"en\\\":\\\"\\\",\\\"ja\\\":\\\"\\\",\\\"zh\\\":\\\"yy回归验证的表单\\\"}\",\"name\":{\"ja\":\"関連フォーム\",\"en\":\"Associated Form\",\"zh\":\"关联表单\"},\"isChooseDefault\":false,\"id\":\"cxs430hjxewxx3\",\"actived\":false,\"vModel\":\"linkForm_cxs430hjxewxx3\"}},{\"id\":\"ctjjv0p6ncflwy\",\"name\":\"{\\\"ja\\\":\\\"説明テキスト\\\",\\\"en\\\":\\\"Caption\\\",\\\"zh\\\":\\\"说明文字\\\"}\",\"required\":false,\"tempAttr\":{\"xtype\":\"text\",\"hidden\":false,\"checkboxValue\":\"1\",\"icon\":\"iconhint\",\"caption\":{\"ja\":\"\",\"en\":\"Explanatory text Explanatory text Explanatory text\",\"zh\":\"说明文字说明文字说明文字说明文字\"},\"pid\":0,\"url\":\"\",\"required\":false,\"labelPosition\":\"top\",\"name\":{\"ja\":\"説明テキスト\",\"en\":\"Caption\",\"zh\":\"说明文字\"},\"isChooseDefault\":false,\"placeholder\":{\"ja\":\"\",\"en\":\"\",\"zh\":\"请输入说明文字\"},\"id\":\"ctjjv0p6ncflwy\",\"actived\":false,\"vModel\":\"text_ctjjv0p6ncflwy\"}},{\"id\":\"c54t2z1vvg43g8\",\"name\":\"{\\\"ja\\\":\\\"明细サブテーブル\\\",\\\"en\\\":\\\"Detailed Sub-Form\\\",\\\"zh\\\":\\\"明细子表\\\"}\",\"required\":false,\"tempAttr\":{\"col\":2,\"xtype\":\"table\",\"checkboxValue\":\"1\",\"icon\":\"&#xe610;\",\"caption\":{\"ja\":\"明细サブテーブル\",\"en\":\"Detailed Sub-Form\",\"zh\":\"明细子表\"},\"pid\":0,\"isSupportImport\":false,\"required\":false,\"head\":[{\"id\":\"ctap8wfw183ks1\",\"name\":\"{\\\"ja\\\":\\\"単数行入力ボックス\\\",\\\"en\\\":\\\"Single-line Box\\\",\\\"zh\\\":\\\"单行输入框\\\"}\",\"required\":false,\"tempAttr\":{\"xtype\":\"input\",\"defaultValue\":\"\",\"checkboxValue\":\"1\",\"fromDetailDetailTable\":true,\"icon\":\"icondanhang\",\"caption\":{\"ja\":\"\",\"en\":\"\",\"zh\":\"单行输入框\"},\"captionMaxLength\":100,\"captionMinLength\":10,\"visibleState\":\"default\",\"required\":false,\"labelPosition\":\"top\",\"placeholderMaxLength\":100,\"isEncrypted\":false,\"name\":{\"ja\":\"単数行入力ボックス\",\"en\":\"Single-line Box\",\"zh\":\"单行输入框\"},\"isChooseDefault\":false,\"placeholder\":{\"ja\":\"\",\"en\":\"\",\"zh\":\"请输入\"},\"id\":\"ctap8wfw183ks1\",\"maxLength\":400,\"actived\":false,\"vModel\":\"input_ctap8wfw183ks1\"}},{\"caption\":{\"en\":\"\",\"ja\":\"\",\"zh\":\"金额\"},\"id\":\"cvz4d9sbvbqgsm\",\"isCapital\":\"true\",\"name\":\"{\\\"ja\\\":\\\"金額\\\",\\\"en\\\":\\\"Amount\\\",\\\"zh\\\":\\\"金额\\\"}\",\"required\":false,\"tempAttr\":{\"xtype\":\"currency\",\"is_capital\":true,\"defaultValue\":\"\",\"checkboxValue\":\"1\",\"rangeMin\":\"\",\"fromDetailDetailTable\":true,\"icon\":\"iconYUAN1\",\"caption\":{\"ja\":\"\",\"en\":\"\",\"zh\":\"金额\"},\"captionMaxLength\":100,\"float\":0,\"isChooseCurrency\":true,\"required\":false,\"unit\":\"0\",\"labelPosition\":\"top\",\"rangeMax\":\"\",\"placeholderMaxLength\":100,\"isEncrypted\":false,\"name\":{\"ja\":\"金額\",\"en\":\"Amount\",\"zh\":\"金额\"},\"isChooseDefault\":false,\"placeholder\":{\"ja\":\"\",\"en\":\"\",\"zh\":\"请输入金额\"},\"id\":\"cvz4d9sbvbqgsm\",\"actived\":false,\"vModel\":\"currency_cvz4d9sbvbqgsm\"},\"unit\":{\"en\":\"\",\"ja\":\"\",\"zh\":\"0\"}}],\"controlType\":\"grid\",\"children\":[[{\"xtype\":\"input\",\"defaultValue\":\"\",\"checkboxValue\":\"1\",\"fromDetailDetailTable\":true,\"icon\":\"icondanhang\",\"caption\":{\"ja\":\"\",\"en\":\"\",\"zh\":\"单行输入框\"},\"captionMaxLength\":100,\"captionMinLength\":10,\"visibleState\":\"default\",\"required\":false,\"labelPosition\":\"top\",\"placeholderMaxLength\":100,\"isEncrypted\":false,\"name\":{\"ja\":\"単数行入力ボックス\",\"en\":\"Single-line Box\",\"zh\":\"单行输入框\"},\"isChooseDefault\":false,\"placeholder\":{\"ja\":\"\",\"en\":\"\",\"zh\":\"请输入\"},\"id\":\"ctap8wfw183ks1\",\"maxLength\":400,\"actived\":false,\"vModel\":\"input_ctap8wfw183ks1\"}],[{\"xtype\":\"currency\",\"is_capital\":true,\"defaultValue\":\"\",\"checkboxValue\":\"1\",\"rangeMin\":\"\",\"fromDetailDetailTable\":true,\"icon\":\"iconYUAN1\",\"caption\":{\"ja\":\"\",\"en\":\"\",\"zh\":\"金额\"},\"captionMaxLength\":100,\"float\":0,\"isChooseCurrency\":true,\"required\":false,\"unit\":\"0\",\"labelPosition\":\"top\",\"rangeMax\":\"\",\"placeholderMaxLength\":100,\"isEncrypted\":false,\"name\":{\"ja\":\"金額\",\"en\":\"Amount\",\"zh\":\"金额\"},\"isChooseDefault\":false,\"placeholder\":{\"ja\":\"\",\"en\":\"\",\"zh\":\"请输入金额\"},\"id\":\"cvz4d9sbvbqgsm\",\"actived\":false,\"vModel\":\"currency_cvz4d9sbvbqgsm\"}]],\"name\":{\"ja\":\"明细サブテーブル\",\"en\":\"Detailed Sub-Form\",\"zh\":\"明细子表\"},\"width\":\"100%\",\"isChooseDefault\":false,\"row\":1,\"id\":\"c54t2z1vvg43g8\",\"actived\":false,\"vModel\":\"table_c54t2z1vvg43g8\"}}]";

        final Object parse = JSON.parseArray(str);

        System.out.println();
    }


}
