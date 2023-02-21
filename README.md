
<h1 style="text-align: center; color: hotpink; -webkit-animation: rainbow 5s infinite; -moz-animation: rainbow 5s infinite; -o-animation: rainbow 5s infinite; animation: rainbow 5s infinite;">ChatGPT Java Api</h1>


![stable](https://img.shields.io/badge/stability-stable-brightgreen.svg)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.plexpt/chatgpt)](https://maven-badges.herokuapp.com/maven-central/com.github.plexpt/chatgpt)

[English Doc](https://github.com/PlexPt/chatgpt-java/blob/main/README_en.md).

OpenAI ChatGPT 的逆向工程SDK。直接使用网页最新ChatGPT。

可扩展用于聊天机器人等。觉得不错请右上角Star

感谢 [revChatGPT](https://github.com/acheong08/ChatGPT).

## 2月21日更新，配合https://github.com/acheong08/ChatGPT-Proxy-V1 亲测可使用

## PRO版

高级版推出，但是 因为源码被盗卖，现暂不公开，需要的可购买

|          |   开源免费版   |                 PRO版                 |               PRO MAX版               |                            API版                             |
| :------: | :------------: | :-----------------------------------: | :-----------------------------------: | :----------------------------------------------------------: |
|   价格   |    开源免费    |                 49.9                  |                 99.9                  |                              98                              |
|   地址   |     本仓库     | [购买](https://fk.fq.mk/?cid=0&tid=3) | [购买](https://fk.fq.mk/?cid=0&tid=4) | [购买](https://fk.fq.mk/?cid=0&tid=7)    [演示站](https://javachat.plexpt.com) |
|   使用   | 需要自行过CF盾 |             详细教程+源码             |        详细教程+源码+1对1指导         |                          教程+源码                           |
|   优势   |                |   可用，后续持续更新（2月21日更新）   |        专业指导（2月21日更新）        |                         有前端，稳定                         |
| 1对1指导 |       无       |                  无                   |                  有                   |                              无                              |
| 登录方式 |  sessionToken  |        sessionToken、邮箱密码         |        sessionToken、邮箱密码         |                            APIKEY                            |
| 使用方式 |     阻塞式     |              阻塞式+流式              |              阻塞式+流式              |                             在线                             |
|   前端   |       无       |                  无                   |                  无                   |                           有（H5）                           |

## API版
API版极稳定，基于Java8 + springboot

实例站点https://javachat.plexpt.com/

#### 项目合作洽谈请联系微信 plexpt（在微信里自行搜索并添加好友，请注明来意，如有关于仓库问题需讨论请参考下文入群讨论，不要加此微信）。

## QQ群：645132635

# 另外请Star我的另一个项目 [ChatGPT中文使用指南](https://github.com/PlexPt/awesome-chatgpt-prompts-zh)

# 功能
![image](https://user-images.githubusercontent.com/15922823/206353660-47d99158-a664-4ade-b2f1-e2cc8ac68b74.png)

![image](https://user-images.githubusercontent.com/15922823/206615422-23c5e587-d29a-4f04-8d0d-f8dd7c19da37.png)

可以写代码，写小说，写作文、演讲稿、工作报告、读书笔记、合同等

自白：
> 我是一个训练有素的大型语言模型，可以帮助你回答各种问题，比如关于政治、历史、科学、技术、艺术等方面的问题。我可以帮助你理解一些概念，并为你提供有价值的信息和见解。你可以尝试提出一些问题，看看我能不能帮到你。
> 除了回答问题，我还可以帮助你写一些文章或小说。你只需要给我一些提示和背景信息，我就可以根据你提供的信息来为你写一篇文章或小说。我会尽力为你写出一篇优秀的文章，帮助你实现你的写作目标。
> 除了回答问题和写作，我还可以帮助你进行文本生成。如果你想生成一些随机的文本，你可以向我提供一些模板和关键词，我就可以根据你提供的信息生成一些随机的文本。这些文本可能是一些名言、句子、段落或者小故事，它们都可以根据你提供的信息来生成。我会尽力为你生成有趣、有用的文本，帮助你实现你的目标。
> 还有很多其他的事情，我可以帮助你完成。比如，如果你想知道某个单词的含义，你可以问我，我会告诉你这个单词的含义。如果你想了解一些事情的细节，你也可以问我，我会尽力为你提供有价值的信息。如果你有任何问题，都可以问我，我会尽力为你解答。
> 甚至这个代码库也是我写的。

1. 回答问题。我能够通过文字回答问题，为用户提供信息。

2. 提供建议。我能够根据用户的需求提供专业的建议。

3. 计算数学问题。我能够快速准确地计算简单的数学问题。

4. 翻译文本。我能够快速准确地翻译文本，让用户更好地理解内容。

5. 生成文本。我能够根据提供的信息生成文本，为用户提供更多信息。

6. 写作。我能够根据用户的需求进行写作，为用户提供高质量的文字内容。

7. 提供知识。我能够根据用户的需求提供专业的知识，为用户提供更多信息。

8. 提供解决方案。我能够根据用户提出的问题提供解决方案，帮助用户解决实际问题。

9. 提供娱乐。我能够根据用户的需求提供娱乐内容，为用户提供欢乐和放松。

10. 聊天。我能够通过文字和用户进行交流，提供娱乐和放松。





## 使用

maven
```
<dependency>
    <groupId>com.github.plexpt</groupId>
    <artifactId>chatgpt</artifactId>
    <version>1.2.0</version>
</dependency>
```

gradle
```
implementation group: 'com.github.plexpt', name: 'chatgpt', version: '1.2.0'
```

启动https://github.com/acheong08/ChatGPT-Proxy
记住地址proxyaddr

然后
```
  Chatbot chatbot = new Chatbot("sessionToken","cf_clearance","user-agent");
  chatbot.setHost("http://proxyaddr:5000");

  Map<String, Object> chatResponse = chatbot.getChatResponse("hello");
  System.out.println(chatResponse.get("message"));
```
注意：一个Chatbot实例为一个Session，会持有对话上下文，可以回复：继续 让机器人接着说。如果需要对话互相隔离，new 多个Chatbot实例即可。

### sessionToken获取
https://github.com/acheong08/ChatGPT/wiki/Setup#token-authentication

1. 通过 https://chat.openai.com/chat 注册并登录。
2. 打开浏览器开发者工具，切换到 Application 标签页。
3. 在左侧的 Storage - Cookies 中找到 __Secure-next-auth.session-token 一行并复制其值
4. 找到 cf_clearance 复制
5. 在network中获取 user-agent 复制

注意：如果提示403. 请检查以上参数是否正确，是否过期。并且获取以上参数的浏览器要和本程序在同一IP地址

### 注册教程

https://juejin.cn/post/7173447848292253704

https://mirror.xyz/boxchen.eth/9O9CSqyKDj4BKUIil7NC1Sa1LJM-3hsPqaeW_QjfFBc

### 也可以控制台直接使用
1. 下载
2. 编辑 config.json 里的sessionToken
3. 运行 run.bat
4. 注意：输入之后需要回车两次

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
