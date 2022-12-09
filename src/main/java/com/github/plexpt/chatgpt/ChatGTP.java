package com.github.plexpt.chatgpt;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import cn.hutool.core.io.FileUtil;
import lombok.SneakyThrows;


public class ChatGTP {

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


    @SneakyThrows
    public static void main(String[] args) {

        System.out.println("ChatGPT - A command-line interface to OpenAI's ChatGPT (https://chat" +
                ".openai.com/chat)");
        System.out.println("Repo: github.com/acheong08/ChatGPT");
        System.out.println("Type '!help' to show commands");
        System.out.println("Press enter twice to submit your question.\n");


        if (FileUtil.exist("config.json")) {

            String configString = FileUtil.readUtf8String(new File("config.json"));

            Map<String, String> params = JSON.parseObject(configString,
                    new TypeReference<Map<String, String>>() {
                    });

            Chatbot chatbot = new Chatbot(params, null);

            String prompt;
            while (true) {
                prompt = getInput("\nYou:\n");
                if (prompt.startsWith("!")) {
                    if (prompt.equals("!help")) {
                        System.out.println("\n!help - Show this message");
                        System.out.println("!reset - Forget the current conversation");
                        System.out.println("!refresh - Refresh the session authentication");
                        System.out.println("!rollback - Rollback the conversation by 1 message");
                        System.out.println("!config - Show the current configuration");
                        System.out.println("!exit - Exit the program");
                        continue;
                    } else if (prompt.equals("!reset")) {
                        chatbot.resetChat();
                        System.out.println("Chat session reset.");
                        continue;
                    } else if (prompt.equals("!refresh")) {
                        chatbot.refreshSession();
                        System.out.println("Session refreshed.\n");
                        continue;
                    } else if (prompt.equals("!rollback")) {
                        chatbot.rollbackConversation();
                        System.out.println("Chat session rolled back.");
                        continue;
                    } else if (prompt.equals("!config")) {
                        System.out.println(JSON.toJSONString(chatbot.getConfig()));
                        continue;
                    } else if (prompt.equals("!exit")) {
                        break;
                    }
                }

                if (Arrays.asList(args).contains("--text")) {
                    try {
                        System.out.println("Chatbot: ");
                        String out = "text";
                        Map<String, Object> message = chatbot.getChatResponse(prompt, out);
                        System.out.println(message.get("message"));
                    } catch (Exception e) {
                        System.out.println("Something went wrong!");
                        e.printStackTrace();
                        System.out.println(e);
                    }
                } else {

                    try {
                        System.out.println("Chatbot: ");
                        Map<String, Object> message = chatbot.getChatResponse(prompt, "stream");
                        // Split the message by newlines
                        String[] messageParts = message.get("message").toString().split("\n");

                        for (String part : messageParts) {
                            String[] wrappedParts = part.split("\n");
                            for (String wrappedPart : wrappedParts) {
                                System.out.println(wrappedPart);
                            }
                        }

                    } catch (Exception e) {
                        System.out.println("Something went wrong!");
                        e.printStackTrace();
                    }
                }

            }
        } else {
            System.out.println("Please create and populate config.json to continue");
            if (!FileUtil.exist("config.json")) {
                FileUtil.writeUtf8String("", "config.json");
            }
        }

    }
}

