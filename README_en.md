

# ChatGPT Java Api

![stable](https://img.shields.io/badge/stability-stable-brightgreen.svg)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.plexpt/chatgpt)](https://maven-badges.herokuapp.com/maven-central/com.github.plexpt/chatgpt)



[简体中文文档](https://github.com/PlexPt/chatgpt-java/blob/main/README_zh.md).

SDK for OpenAI ChatGPT. If you find it helpful, please give it a star in the upper right corner.

# Features

|     Feature      |   Support   |
| :--------------: | :---------: |
|     GPT 3.5      |  Supported  |
|     GPT 4.0      |  Supported  |
|   GPT 4.0-32k    |  Supported  |
| Streaming Dialog |  Supported  |
| Blocking Dialog  |  Supported  |
|    Front-end     |    None     |
|     Context      |  Supported  |
|  Token Compute   | Coming Soon |
|  Multiple Keys   |  Supported  |
|      Proxy       |  Supported  |
|  Reverse Proxy   |  Supported  |

![image](https://user-images.githubusercontent.com/36258159/205534498-acc59484-c4b4-487d-89a7-d7b884af709b.png)

![image](https://user-images.githubusercontent.com/15922823/206353660-47d99158-a664-4ade-b2f1-e2cc8ac68b74.png)

## USE

#### maven

```
<dependency>
    <groupId>com.github.plexpt</groupId>
    <artifactId>chatgpt</artifactId>
    <version>4.2.0</version>
</dependency>
```

#### gradle

```
implementation group: 'com.github.plexpt', name: 'chatgpt', version: '4.2.0'
```

### Quick Start

```

       ChatGPT chatGPT = ChatGPT.builder()
                .apiKey("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")
                .build()
                .init();
                
        String res = chatGPT.chat("hello！");
        System.out.println(res);
```
### Advanced Usage

```java
   //Proxy is required in some contry
      Proxy proxy = Proxys.http("127.0.0.1", 1080);

      ChatGPT chatGPT = ChatGPT.builder()
                .apiKey("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")
                .proxy(proxy)
                .timeout(900)
                .apiHost("https://api.openai.com/") //Reverse proxy address
                .build()
                .init();
  
        Message system = Message.ofSystem("You are now a poet, specializing in writing seven character quatrains");
        Message message = Message.of("Write a seven-character quatrain poem titled: Future!");

        ChatCompletion chatCompletion = ChatCompletion.builder()
                .model(ChatCompletion.Model.GPT_3_5_TURBO.getName())
                .messages(Arrays.asList(system, message))
                .maxTokens(3000)
                .temperature(0.9)
                .build();
        ChatCompletionResponse response = chatGPT.chatCompletion(chatCompletion);
        Message res = response.getChoices().get(0).getMessage();
        System.out.println(res);
```

### Streaming Usage

```java
 

      ChatGPTStream chatGPTStream = ChatGPTStream.builder()
                .timeout(600)
                .apiKey("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")
                .build()
                .init();

                
        ConsoleStreamListener listener = new ConsoleStreamListener();
        Message message = Message.of("Write a seven-character quatrain poem titled: Future!");
        ChatCompletion chatCompletion = ChatCompletion.builder()
                .messages(Arrays.asList(message))
                .build();
        chatGPTStream.streamChatCompletion(chatCompletion, listener);

```

### Using Spring SseEmitter with Streaming

Refer to [SseStreamListener](https://chat.plexpt.com/src/main/java/com/plexpt/chatgpt/listener/SseStreamListener.java)

```java

    @GetMapping("/chat/sse")
    @CrossOrigin
    public SseEmitter sseEmitter(String prompt) {
     

       ChatGPTStream chatGPTStream = ChatGPTStream.builder()
                .timeout(600)
                .apiKey("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")
                .apiHost("https://api.openai.com/")
                .build()
                .init();
        
        SseEmitter sseEmitter = new SseEmitter(-1L);

        SseStreamListener listener = new SseStreamListener(sseEmitter);
        Message message = Message.of(prompt);

        listener.setOnComplate(msg -> {
            //The answer is complete, do something 
        });
        chatGPTStream.streamChatCompletion(Arrays.asList(message), listener);


        return sseEmitter;
    }

```



# Star History

[![Star History Chart](https://api.star-history.com/svg?repos=PlexPt/chatgpt-java&type=Date)](https://star-history.com/#PlexPt/chatgpt-java&Date)
