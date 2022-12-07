

# ChatGPT Java Api
[![PyPi](https://img.shields.io/pypi/v/revChatGPT.svg)](https://pypi.python.org/pypi/revChatGPT)
[![PyPi](https://img.shields.io/pypi/dm/revChatGPT.svg)](https://pypi.python.org/pypi/revChatGPT)


OpenAI ChatGPT 的逆向工程SDK。可扩展用于聊天机器人等。

感谢 [revChatGPT](https://github.com/acheong08/ChatGPT).

# 功能
![image](https://user-images.githubusercontent.com/36258159/205534498-acc59484-c4b4-487d-89a7-d7b884af709b.png)

## 使用

maven
```
<dependency>
    <groupId>com.github.plexpt</groupId>
    <artifactId>chatgpt</artifactId>
    <version>1.0.0</version>
</dependency>
```

gradle
```
implementation group: 'com.github.plexpt', name: 'toolkit', version: '2022.7.0'
```


然后
```
  Chatbot chatbot = new Chatbot("sessionToken");
  Map<String, Object> chatResponse = chatbot.getChatResponse("hello");
  System.out.println(chatResponse.get("message"));
```
### sessionToken获取
https://github.com/acheong08/ChatGPT/wiki/Setup#token-authentication

注册教程

https://juejin.cn/post/7173447848292253704


### 也可以控制台直接使用
1. 下载
2. 编辑 config.json 里的sessionToken
3. 运行 run.bat

# Awesome ChatGPT
[My list](https://github.com/stars/acheong08/lists/awesome-chatgpt)

If you have a cool project you want added to the list, open an issue.

# Disclaimers
这不是官方的 OpenAI 产品。这是一个个人项目，与 OpenAI 没有任何关联。

### This is a library and not intended for direct CLI use
CLI 功能仅用于演示和测试。不支持验证码（对于不干净的 IP 地址）

### CLI use
[@rawandahmad698](https://github.com/rawandahmad698) has a much better CLI tool at

**[PyChatGPT](https://github.com/rawandahmad698/PyChatGPT)** supports captcha!

# Star History

[![Star History Chart](https://api.star-history.com/svg?repos=PlexPt/chatgpt-java&type=Date)](https://star-history.com/#PlexPt/chatgpt-java&Date)
