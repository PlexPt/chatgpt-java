package com.plexpt.chatgpt;

import com.plexpt.chatgpt.entity.chat.Message;
import com.plexpt.chatgpt.listener.ConsoleStreamListener;
import com.plexpt.chatgpt.util.Proxys;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

import cn.hutool.core.util.NumberUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;


/**
 * open ai 客户端
 *
 * @author plexpt
 */

@Slf4j

public class ConsoleChatGPT {

    public static Proxy proxy = Proxy.NO_PROXY;

    public static void main(String[] args) {

        System.out.println("ChatGPT - Java command-line interface");
        System.out.println("Press enter twice to submit your question.");
        System.out.println();
        System.out.println("按两次回车以提交您的问题！！！");
        System.out.println("按两次回车以提交您的问题！！！");
        System.out.println("按两次回车以提交您的问题！！！");


        System.out.println();
        System.out.println("Please enter APIKEY, press Enter twice to submit:");
        String key = getInput("请输入APIKEY，按两次回车以提交:\n");
        check(key);

        // 询问用户是否使用代理  国内需要代理
        System.out.println("是否使用代理？(y/n): ");
        System.out.println("use proxy？(y/n): ");
        String useProxy = getInput("按两次回车以提交:\n");
        if (useProxy.equalsIgnoreCase("y")) {

            // 输入代理地址
            System.out.println("请输入代理类型(http/socks): ");
            String type = getInput("按两次回车以提交:\n");

            // 输入代理地址
            System.out.println("请输入代理IP: ");
            String proxyHost = getInput("按两次回车以提交:\n");

            // 输入代理端口
            System.out.println("请输入代理端口: ");
            String portStr = getInput("按两次回车以提交:\n");
            Integer proxyPort = Integer.parseInt(portStr);

            if (type.equals("http")) {
                proxy = Proxys.http(proxyHost, proxyPort);
            } else {
                proxy = Proxys.socks5(proxyHost, proxyPort);
            }

        }

//        System.out.println("Inquiry balance...");
//        System.out.println("查询余额中...");
//        BigDecimal balance = getBalance(key);
//        System.out.println("API KEY balance: " + balance.toPlainString());
//
//        if (!NumberUtil.isGreater(balance, BigDecimal.ZERO)) {
//            System.out.println("API KEY 余额不足: ");
//            return;
//        }


        while (true) {
            String prompt = getInput("\nYou:\n");

            ChatGPTStream chatGPT = ChatGPTStream.builder()
                    .apiKey(key)
                    .proxy(proxy)
                    .build()
                    .init();

            System.out.println("AI: ");


            //卡住
            CountDownLatch countDownLatch = new CountDownLatch(1);

            Message message = Message.of(prompt);
            ConsoleStreamListener listener = new ConsoleStreamListener() {
                @Override
                public void onError(Throwable throwable, String response) {
                    throwable.printStackTrace();
                    countDownLatch.countDown();
                }
            };

            listener.setOnComplate(msg -> {
                countDownLatch.countDown();
            });
            chatGPT.streamChatCompletion(Arrays.asList(message), listener);

            try {
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }


    }

    private static BigDecimal getBalance(String key) {

        ChatGPT chatGPT = ChatGPT.builder()
                .apiKey(key)
                .proxy(proxy)
                .build()
                .init();

        return chatGPT.balance();
    }

    private static void check(String key) {
        if (key == null || key.isEmpty()) {
            throw new RuntimeException("请输入正确的KEY");
        }
    }

    @SneakyThrows
    public static String getInput(String prompt) {
        System.out.print(prompt);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        List<String> lines = new ArrayList<>();
        String line;
        try {
            while ((line = reader.readLine()) != null && !line.isEmpty()) {
                lines.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines.stream().collect(Collectors.joining("\n"));
    }

}

