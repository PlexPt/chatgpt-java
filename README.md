<h1 style="text-align: center; color: hotpink; -webkit-animation: rainbow 5s infinite; -moz-animation: rainbow 5s infinite; -o-animation: rainbow 5s infinite; animation: rainbow 5s infinite;">ChatGPT Java API</h1>

![stable](https://img.shields.io/badge/stability-stable-brightgreen.svg)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.plexpt/chatgpt)](https://maven-badges.herokuapp.com/maven-central/com.github.plexpt/chatgpt)

[English Doc](https://github.com/PlexPt/chatgpt-java/blob/main/README_en.md).


OpenAI ChatGPT SDK


# 功能特性

|     功能      | 特性  |
|:-----------:|:---:|
|   GPT 3.5   | 支持  |
|   GPT 4.0   | 支持  |
|   GPT 4o    | 支持  |
| GPT 4o-mini | 支持  |
|    函数调用     | 支持  |
|    流式对话     | 支持  |
|     上下文     | 支持  |
|   计算Token   | 支持  |
|   多KEY轮询    | 支持  |
|     代理      | 支持  |
|    反向代理     | 支持  |


## 使用指南

参考Demo https://github.com/PlexPt/chatgpt-online-springboot

最新版本 [![Maven Central](https://img.shields.io/maven-central/v/com.github.plexpt/chatgpt)](https://maven-badges.herokuapp.com/maven-central/com.github.plexpt/chatgpt)

maven
```
<dependency>
    <groupId>com.github.plexpt</groupId>
    <artifactId>chatgpt</artifactId>
    <version>6.0.0</version>
</dependency>
```

gradle
```
implementation group: 'com.github.plexpt', name: 'chatgpt', version: '6.0.0'
```



### 最简使用


```java
      //国内需要代理
      Proxy proxy = Proxys.http("127.0.0.1", 1081);
     //socks5 代理
    // Proxy proxy = Proxys.socks5("127.0.0.1", 1080);

      ChatGPT chatGPT = ChatGPT.builder()
                .apiKey("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")
                .proxy(proxy)
                .apiHost("https://api.openai.com/") //反向代理地址
                .build()
                .init();
                
        String res = chatGPT.chat("写一段七言绝句诗，题目是：火锅！");
        System.out.println(res);

```
也可以使用这个类进行测试 [ConsoleChatGPT](src/main/java/com/plexpt/chatgpt/ConsoleChatGPT.java)


### 进阶使用

```java
      //国内需要代理 国外不需要
      Proxy proxy = Proxys.http("127.0.0.1", 1080);

      ChatGPT chatGPT = ChatGPT.builder()
                .apiKey("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")
                .proxy(proxy)
                .timeout(900)
                .apiHost("https://api.openai.com/") //反向代理地址
                .build()
                .init();
     
        Message system = Message.ofSystem("你现在是一个诗人，专门写七言绝句");
        Message message = Message.of("写一段七言绝句诗，题目是：火锅！");

        ChatCompletion chatCompletion = ChatCompletion.builder()
                .model(ChatCompletion.Model.GPT_3_5_TURBO.getName())
                .messages(Arrays.asList(system, message))
                .maxTokens(3000)
                .temperature(0.9)
                .build();
        ChatCompletionResponse response = chatGPT.chatCompletion(chatCompletion);
        System.out.println(response.toPlainString());

```


### 计算token数


```java
        Message system = Message.ofSystem("你现在是一个诗人，专门写七言绝句");
        Message message = Message.of("写一段七言绝句诗，题目是：火锅！");

        ChatCompletion chatCompletion1 = ChatCompletion.builder()
        .model(ChatCompletion.Model.GPT_3_5_TURBO)
        .messages(Arrays.asList(system, message))
        .maxTokens(3000)
        .temperature(0.9)
        .build();
        ChatCompletion chatCompletion2 = ChatCompletion.builder()
        .model(ChatCompletion.Model.GPT4)
        .messages(Arrays.asList(system, message))
        .maxTokens(3000)
        .temperature(0.9)
        .build();

        log.info("{} tokens: {}", chatCompletion1.getModel(), chatCompletion1.countTokens());
        log.info("{} tokens: {}", chatCompletion2.getModel(), chatCompletion2.countTokens());

```

### 函数调用（Function Call）

```java
      //国内需要代理 国外不需要
          Proxy proxy = Proxys.http("127.0.0.1", 1080);

                  chatGPT = ChatGPT.builder()
                  .apiKey("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")
                  .timeout(900)
                  .proxy(proxy)
                  .apiHost("https://api.openai.com/") //代理地址
                  .build()
                  .init();

        List<ChatFunction> functions = new ArrayList<>();
        ChatFunction function = new ChatFunction();
        function.setName("getCurrentWeather");
        function.setDescription("获取给定位置的当前天气");
        function.setParameters(ChatFunction.ChatParameter.builder()
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
        .model(ChatCompletion.Model.GPT_3_5_TURBO_0613.getName())
        .messages(Arrays.asList(message))
        .functions(functions)
        .maxTokens(8000)
        .temperature(0.9)
        .build();
        ChatCompletionResponse response = chatGPT.chatCompletion(chatCompletion);
        ChatChoice choice = response.getChoices().get(0);
        Message res = choice.getMessage();
        System.out.println(res);
        if ("function_call".equals(choice.getFinishReason())) {

        FunctionCallResult functionCall = res.getFunctionCall();
        String functionCallName = functionCall.getName();

        if ("getCurrentWeather".equals(functionCallName)) {
        String arguments = functionCall.getArguments();
        JSONObject jsonObject = JSON.parseObject(arguments);
        String location = jsonObject.getString("location");
        String unit = jsonObject.getString("unit");
        String weather = getCurrentWeather(location, unit);

        callWithWeather(weather, res, functions);
        }
        }


    private void callWithWeather(String weather, Message res, List<ChatFunction> functions) {


        Message message = Message.of("上海的天气怎么样？");
        Message function1 = Message.ofFunction(weather);
        function1.setName("getCurrentWeather");
        ChatCompletion chatCompletion = ChatCompletion.builder()
        .model(ChatCompletion.Model.GPT_3_5_TURBO_0613.getName())
        .messages(Arrays.asList(message, res, function1))
        .functions(functions)
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

```

### 流式使用

```java
      //国内需要代理 国外不需要
      Proxy proxy = Proxys.http("127.0.0.1", 1080);

      ChatGPTStream chatGPTStream = ChatGPTStream.builder()
                .timeout(600)
                .apiKey("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")
                .proxy(proxy)
                .apiHost("https://api.openai.com/")
                .build()
                .init();

                
        ConsoleStreamListener listener = new ConsoleStreamListener();
        Message message = Message.of("写一段七言绝句诗，题目是：火锅！");
        ChatCompletion chatCompletion = ChatCompletion.builder()
                .messages(Arrays.asList(message))
                .build();
        chatGPTStream.streamChatCompletion(chatCompletion, listener);

```

### 流式配合Spring SseEmitter使用

参考 [SseStreamListener](src/main/java/com/plexpt/chatgpt/listener/SseStreamListener.java)

你可能在找这个，参考Demo https://github.com/PlexPt/chatgpt-online-springboot

```java
  

    @GetMapping("/chat/sse")
    @CrossOrigin
    public SseEmitter sseEmitter(String prompt) {
       //国内需要代理 国外不需要
       Proxy proxy = Proxys.http("127.0.0.1", 1080);

       ChatGPTStream chatGPTStream = ChatGPTStream.builder()
                .timeout(600)
                .apiKey("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")
                .proxy(proxy)
                .apiHost("https://api.openai.com/")
                .build()
                .init();
        
        SseEmitter sseEmitter = new SseEmitter(-1L);

        SseStreamListener listener = new SseStreamListener(sseEmitter);
        Message message = Message.of(prompt);

        listener.setOnComplate(msg -> {
            //回答完成，可以做一些事情
        });
        chatGPTStream.streamChatCompletion(Arrays.asList(message), listener);


        return sseEmitter;
    }

```



## 多KEY自动轮询

只需替换chatGPT构造部分

```
chatGPT = ChatGPT.builder()
        .apiKeyList(
               // 从数据库或其他地方取出多个KEY
                Arrays.asList("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",
                        "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",
                        "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",
                        "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",
                        ))
        .timeout(900)
        .proxy(proxy)
        .apiHost("https://api.openai.com/") //代理地址
        .build()
        .init();
```

## 上下文

参考  [ChatContextHolder.java](src/main/java/com/plexpt/chatgpt/util/ChatContextHolder.java) 


#### 另外请看看我的另一个项目 [ChatGPT中文使用指南](https://github.com/PlexPt/awesome-chatgpt-prompts-zh)

公众号

 <img src="https://user-images.githubusercontent.com/15922823/218004565-bb632624-b376-4f01-8ce2-d7065107bf4a.png" width="300"/> 

# 云服务器

点击👇🏻传送链接，购买云服务器：

- [**阿里云服务器**](https://reurl.cc/NqQXyx)
- [**【腾讯云】云服务器等爆品抢先购，低至4.2元/月**](https://url.cn/B7m0OYnG)

 
#### 项目合作洽谈请点击 联系微信 https://work.weixin.qq.com/kfid/kfc6913bb4906e0e597

### QQ群：645132635

# Star History

[![Star History Chart](https://api.star-history.com/svg?repos=PlexPt/chatgpt-java&type=Date)](https://star-history.com/#PlexPt/chatgpt-java&Date)
