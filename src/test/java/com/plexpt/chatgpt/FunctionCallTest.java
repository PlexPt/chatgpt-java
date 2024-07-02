package com.plexpt.chatgpt;

import com.plexpt.chatgpt.entity.chat.*;
import com.plexpt.chatgpt.util.Proxys;
import com.plexpt.chatgpt.util.fastjson.JSON;
import com.plexpt.chatgpt.util.fastjson.JSONObject;
import org.junit.Before;

import java.net.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FunctionCallTest {

    private ChatGPT chatGPT;

    @Before
    public void before() {
        Proxy proxy = Proxys.http("127.0.0.1", 1080);

        chatGPT = ChatGPT.builder()
                .apiKey("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")
                .timeout(900)
                .proxy(proxy)
                .apiHost("https://api.openai.com/") //代理地址
                .build()
                .init();

    }

    @org.junit.Test
    public void chat() {
        List<ChatTool> functions = new ArrayList<>();
        ChatTool function = new ChatTool();
        function.setType("function");
        ChatToolFunction toolFunction = new ChatToolFunction();
        function.setFunction(toolFunction);

        toolFunction.setName("getCurrentWeather");
        toolFunction.setDescription("获取给定位置的当前天气");
        toolFunction.setParameters(ChatToolFunction.ChatParameter.builder()
                .type("object")
                .required(Arrays.asList("location"))
                .properties(JSON.parseObject("{\n" +
                        "          \"location\": {\n" +
                        "            \"type\": \"string\",\n" +
                        "            \"description\": \"The city and state, e.g. San Francisco, " +
                        "CA\"\n" +
                        "          },\n" +
                        "          \"unit\": {\n" +
                        "            \"type\": \"string\",\n" +
                        "            \"enum\": [\"celsius\", \"fahrenheit\"]\n" +
                        "          }\n" +
                        "        }"))
                .build());
        functions.add(function);

        Message message = Message.of("上海的天气怎么样？");
        ChatCompletion chatCompletion = ChatCompletion.builder()
                .model(ChatCompletion.Model.GPT_3_5_TURBO)
                .messages(Arrays.asList(message))
                .tools(functions)
                .maxTokens(8000)
                .temperature(0.9)
                .build();
        ChatCompletionResponse response = chatGPT.chatCompletion(chatCompletion);
        ChatChoice choice = response.getChoices().get(0);
        Message res = choice.getMessage();
        System.out.println(res);
        if ("function_call".equals(choice.getFinishReason())) {

            List<ToolCallResult> calls = res.getToolCalls();
            for (ToolCallResult call : calls) {

                String functionCallName = call.getFunction().getName();

                if ("getCurrentWeather".equals(functionCallName)) {
                    String arguments = call.getFunction().getArguments();
                    JSONObject jsonObject = JSON.parseObject(arguments);
                    String location = jsonObject.getString("location");
                    String unit = jsonObject.getString("unit");
                    String weather = getCurrentWeather(location, unit);

                    callWithWeather(weather, res, functions);
                }
            }

        }


    }

    private void callWithWeather(String weather, Message res, List<ChatTool> functions) {


        Message message = Message.of("上海的天气怎么样？");
        Message function1 = Message.ofFunction(weather);
        function1.setName("getCurrentWeather");
        ChatCompletion chatCompletion = ChatCompletion.builder()
                .model(ChatCompletion.Model.GPT_3_5_TURBO)
                .messages(Arrays.asList(message, res, function1))
                .tools(functions)
                .maxTokens(8000)
                .temperature(0.9)
                .build();
        ChatCompletionResponse response = chatGPT.chatCompletion(chatCompletion);
        ChatChoice choice = response.getChoices().get(0);
        Message res2 = choice.getMessage();
        //上海目前天气晴朗，气温为 22 摄氏度。
        System.out.println(res2.getContent());
    }

    public String getCurrentWeather(String location, String unit) {
        return "{ \"temperature\": 22, \"unit\": \"celsius\", \"description\": \"晴朗\" }";
    }
}
