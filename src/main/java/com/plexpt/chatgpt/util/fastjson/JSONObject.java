package com.plexpt.chatgpt.util.fastjson;


import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

import static com.plexpt.chatgpt.util.fastjson.TypeUtils.*;


public class JSONObject extends JSON implements Map<String, Object> {

    private Map<String, Object> map;

    public JSONObject(Map<String, Object> map) {
        this.map = map;
    }

    public JSONObject() {
        map = new HashMap<>();
    }

    public JSONObject(int size) {
        map = new HashMap<>(size);
    }

    public String toJSONString() {
        return JSON.toJSONString(map);
    }

    public JSONObject getJSONObject(String key) {
        Object value = map.get(key);

        if (value instanceof JSONObject) {
            return (JSONObject) value;
        }

        if (value instanceof Map) {
            return new JSONObject((Map) value);
        }

        if (value instanceof String) {
            return JSON.parseObject((String) value);
        }

        return JSON.parseObject(JSON.toJSONString(value));
    }

    public JSONArray getJSONArray(String key) {
        Object value = map.get(key);

        if (value == null) {
            return new JSONArray();
        }

        if (value instanceof JSONArray) {
            return (JSONArray) value;
        }

        if (value instanceof List) {
            return new JSONArray((List) value);
        }

        if (value instanceof String) {
            return JSON.parseArray((String) value);
        }

        return JSON.parseArray(JSON.toJSONString(value));
    }

    public <T> T getObject(String key, Class<T> clazz) {
        Object obj = map.get(key);
        if (clazz == obj.getClass()) {
            return (T) obj;
        }
        if (clazz == String.class) {
            return JSON.parseObject((String) obj, clazz);
        }

        return JSON.parseObject(JSON.toJSONString(obj), clazz);
    }


    public Boolean getBoolean(String key) {
        Object value = get(key);

        if (value == null) {
            return null;
        }

        return castToBoolean(value);
    }


    public boolean getBooleanValue(String key) {
        Object value = get(key);

        Boolean booleanVal = castToBoolean(value);
        if (booleanVal == null) {
            return false;
        }

        return booleanVal.booleanValue();
    }

    public Byte getByte(String key) {
        Object value = get(key);

        return castToByte(value);
    }

    public byte getByteValue(String key) {
        Object value = get(key);

        Byte byteVal = castToByte(value);
        if (byteVal == null) {
            return 0;
        }

        return byteVal.byteValue();
    }

    public Short getShort(String key) {
        Object value = get(key);

        return castToShort(value);
    }

    public short getShortValue(String key) {
        Object value = get(key);

        Short shortVal = castToShort(value);
        if (shortVal == null) {
            return 0;
        }

        return shortVal.shortValue();
    }

    public Integer getInteger(String key) {
        Object value = get(key);

        return castToInt(value);
    }

    public int getIntValue(String key) {
        Object value = get(key);

        Integer intVal = castToInt(value);
        if (intVal == null) {
            return 0;
        }

        return intVal.intValue();
    }

    public Long getLong(String key) {
        Object value = get(key);

        return castToLong(value);
    }

    public long getLongValue(String key) {
        Object value = get(key);

        Long longVal = castToLong(value);
        if (longVal == null) {
            return 0L;
        }

        return longVal.longValue();
    }

    public Float getFloat(String key) {
        Object value = get(key);

        return castToFloat(value);
    }

    public float getFloatValue(String key) {
        Object value = get(key);

        Float floatValue = castToFloat(value);
        if (floatValue == null) {
            return 0F;
        }

        return floatValue.floatValue();
    }

    public Double getDouble(String key) {
        Object value = get(key);

        return castToDouble(value);
    }

    public double getDoubleValue(String key) {
        Object value = get(key);

        Double doubleValue = castToDouble(value);
        if (doubleValue == null) {
            return 0D;
        }

        return doubleValue.doubleValue();
    }

    public BigDecimal getBigDecimal(String key) {
        Object value = get(key);

        return castToBigDecimal(value);
    }

    public BigInteger getBigInteger(String key) {
        Object value = get(key);

        return castToBigInteger(value);
    }

    public String getString(String key) {
        Object value = get(key);

        if (value == null) {
            return null;
        }

        return value.toString();
    }

    public <T> T toJavaObject(Class<T> clazz) {
        if (clazz == Map.class || clazz == JSONObject.class || clazz == JSONArray.class) {
            return (T) this;
        }
        return parseObject(toJSONString(), clazz);
    }

    @Override
    public String toString() {
        return toJSONString();
    }

    @Override
    public Object remove(Object key) {
        return map.remove(key);
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    @Override
    public Object get(Object key) {
        return map.get(key);
    }

    @Override
    public Object put(String key, Object value) {
        return map.put(key, value);
    }

    @Override
    public void putAll(Map<? extends String, ?> m) {
        map.putAll(m);
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public Set<String> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<Object> values() {
        return map.values();
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return map.entrySet();
    }

    @Override
    public boolean equals(Object o) {
        return map.equals(o);
    }

    @Override
    public int hashCode() {
        return map.hashCode();
    }
}
