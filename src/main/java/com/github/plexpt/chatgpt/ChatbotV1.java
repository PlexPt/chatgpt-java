package com.github.plexpt.chatgpt;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import lombok.Data;
import lombok.SneakyThrows;
import okhttp3.Response;


public class ChatbotV1 {
    private String conversationId = null;

    private String accessToken;
    private String parentId;
    private Map<String, String> headers = new HashMap<>();

    private String host = "https://chatgpt.duti.tech";

    private String conversationIdPrev;
    private String parentIdPrev;

    /**
     * 初始化
     *
     * @param accessToken
     */
    public ChatbotV1(String accessToken) {
        this.accessToken = accessToken;
        this.parentId = UUID.randomUUID().toString();
        refreshSession();
    }

    // Resets the conversation ID and parent ID
    public void resetChat() {
        this.conversationId = null;
        this.parentId = UUID.randomUUID().toString();
    }


    // Refreshes the headers -- Internal use only
    public void refreshHeaders() {

        if (StrUtil.isEmpty(accessToken)) {
            accessToken = "";
        }
        this.headers = new HashMap<String, String>() {{
            put("Accept", "text/event-stream");
            put("Authorization", "Bearer " + accessToken);
            put("Content-Type", "application/json");
            put("X-Openai-Assistant-App-Id", "");
            put("Connection", "close");
            put("Accept-Language", "en-US,en;q=0.9");
            put("Referer", "https://chat.openai.com/chat");
        }};

    }


    Map<String, Object> getChatStream(Map<String, Object> data) {
        String url = host + "/api/conversation";

        String body = HttpUtil.createPost(url)
                .headerMap(headers, true)
                .body(JSON.toJSONString(data), "application/json")
                .execute()
                .body();
                System.out.println(body);
        String message = "";
        Map<String, Object> chatData = new HashMap<>();
        for (String s : body.split("\n")) {
            if ((s == null) || "".equals(s)) {
                continue;
            }
            if (s.contains("data: [DONE]")) {
                continue;
            }

            String part = s.substring(5);
            JSONObject lineData = JSON.parseObject(part);

            try {

                JSONArray jsonArray = lineData.getJSONObject("message")
                        .getJSONObject("content")
                        .getJSONArray("parts");

                if (jsonArray.size() == 0) {
                    continue;
                }
                message = jsonArray.getString(0);

                conversationId = lineData.getString("conversation_id");
                parentId = (lineData.getJSONObject("message")).getString("id");

                chatData.put("message", message);
                chatData.put("conversation_id", conversationId);
                chatData.put("parent_id", parentId);
            } catch (Exception e) {
                System.out.println("getChatStream Exception: " + part);
                //  e.printStackTrace();
                continue;
            }

        }
        return chatData;

    }

    // Gets the chat response as text -- Internal use only
    public void getModels() {

        // Create request session
        Session session = new Session();

        // set headers
        session.setHeaders(this.headers);

        // Set proxies
        setupProxy(session);

        HttpResponse response = session.get2(host + "/api/models");
        String body = response.body();
        System.out.println(body);
    }

    // Gets the chat response as text -- Internal use only
    public Map<String, Object> getChatText(Map<String, Object> data) {

        // Create request session
        Session session = new Session();

        // set headers
        session.setHeaders(this.headers);

        // Set proxies
        setupProxy(session);

        HttpResponse response = session.post2(host + "/api/conversation",
                data);
        String body = response.body();

        String errorDesc = "";


        String message = "";
        Map<String, Object> chatData = new HashMap<>();
        for (String s : body.split("\n")) {
            if ((s == null) || "".equals(s)) {
                continue;
            }
            if (s.contains("data: [DONE]")) {
                continue;
            }

            String part = s.substring(5);

            try {
                JSONObject lineData = JSON.parseObject(part);

                JSONArray jsonArray = lineData.getJSONObject("message")
                        .getJSONObject("content")
                        .getJSONArray("parts");

                if (jsonArray.size() == 0) {
                    continue;
                }
                message = jsonArray.getString(0);

                conversationId = lineData.getString("conversation_id");
                parentId = (lineData.getJSONObject("message")).getString("id");

                chatData.put("message", message);
                chatData.put("conversation_id", conversationId);
                chatData.put("parent_id", parentId);
            } catch (Exception e) {
                System.out.println("getChatStream Exception: " + part);
                //  e.printStackTrace();
                continue;
            }

        }
        return chatData;
    }

    private void setupProxy(Session session) {
//        if (config.get("proxy") != null && !config.get("proxy").equals("")) {
//            Map<String, String> proxies = new HashMap<>();
//            proxies.put("http", config.get("proxy"));
//            proxies.put("https", config.get("proxy"));
//            session.setProxies(proxies);
//        }
    }

    public Map<String, Object> getChatResponse(String prompt, String output) {
        Map<String, Object> data = new HashMap<>();
        data.put("action", "next");
        data.put("conversation_id", this.conversationId);
        data.put("parent_message_id", this.parentId);
        data.put("model", "text-davinci-002-render");

        Map<String, Object> message = new HashMap<>();
        message.put("id", UUID.randomUUID().toString());
        message.put("role", "user");
        Map<String, Object> content = new HashMap<>();
        content.put("content_type", "text");
        content.put("parts", Collections.singletonList(prompt));
        message.put("content", content);
        data.put("messages", Collections.singletonList(message));

        this.conversationIdPrev = this.conversationId;
        this.parentIdPrev = this.parentId;

        if (output.equals("text")) {
            return this.getChatText(data);
        } else if (output.equals("stream")) {
            return this.getChatStream(data);
        } else {
            throw new RuntimeException("Output must be either 'text' or 'stream'");
        }
    }

    public Map<String, Object> getChatResponse(String prompt) {
        return this.getChatResponse(prompt, "text");
    }


    @SneakyThrows
    public void refreshSession() {

        if (accessToken == null || accessToken.equals("")) {
            throw new RuntimeException("No tokens provided");
        }


        try {

            this.refreshHeaders();
        } catch (Exception e) {
            System.out.println("Error refreshing session");
            throw new Exception("Error refreshing session", e);
        }
    }

    public void rollbackConversation() {
        this.conversationId = this.conversationIdPrev;
        this.parentId = this.parentIdPrev;
    }

    @SneakyThrows
    public static JSONObject resJson(Response response) {
        JSONObject responseObject = null;
        String text = response.body().string();
        try {
            response.body().close();
            responseObject = JSON.parseObject(text);
        } catch (Exception e) {
            System.out.println("json err, body: " + text);
            throw new RuntimeException(e);
        }

        return responseObject;
    }
}
