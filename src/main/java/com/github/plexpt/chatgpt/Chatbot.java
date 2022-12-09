package com.github.plexpt.chatgpt;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.google.gson.Gson;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import lombok.Data;
import lombok.SneakyThrows;
import okhttp3.Response;

@Data
public class Chatbot {
    private Map<String, String> config = new HashMap<>();
    private String conversationId;
    private String parentId;
    private Map<String, String> headers;

    private String conversationIdPrev;
    private String parentIdPrev;


    private final Gson gson = new Gson();

    public Chatbot(Map<String, String> config, String conversationId) {
        this.config = config;
        this.conversationId = conversationId;
        this.parentId = UUID.randomUUID().toString();
        if (config.containsKey("session_token") || (config.containsKey("email")
                && config.containsKey("password"))) {
            refreshSession();
        }
    }

    public Chatbot(String sessionToken) {
        config.put("session_token", sessionToken);
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
        if (!config.containsKey("Authorization")) {
            config.put("Authorization", "");
        } else if (config.get("Authorization") == null) {
            config.put("Authorization", "");
        }
        this.headers = new HashMap<String, String>() {{
            put("Host", "chat.openai.com");
            put("Accept", "text/event-stream");
            put("Authorization", "Bearer " + config.get("Authorization"));
            put("Content-Type", "application/json");
            put("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1" +
                    ".15 (KHTML, like Gecko) " +
                    "Version/16.1 Safari/605.1.15");
            put("X-Openai-Assistant-App-Id", "");
            put("Connection", "close");
            put("Accept-Language", "en-US,en;q=0.9");
            put("Referer", "https://chat.openai.com/chat");
        }};

    }


    Map<String, Object> getChatStream(Map<String, Object> data) {
        String url = "https://chat.openai.com/backend-api/conversation";

        String body = HttpUtil.createPost(url)
                .headerMap(headers, true)
                .body(JSON.toJSONString(data), "application/json")
                .execute()
                .body();

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
    public Map<String, Object> getChatText(Map<String, Object> data) {

        // Create request session
        Session session = new Session();

        // set headers
        session.setHeaders(this.headers);

        // Set multiple cookies
        session.getCookies().put("__Secure-next-auth.session-token", config.get("session_token"));
        session.getCookies().put("__Secure-next-auth.callback-url", "https://chat.openai.com/");

        // Set proxies
        setupProxy(session);

        HttpResponse response = session.post2("https://chat.openai.com/backend-api/conversation",
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

    private void setupProxy(Session session) {
        if (config.get("proxy") != null && !config.get("proxy").equals("")) {
            Map<String, String> proxies = new HashMap<>();
            proxies.put("http", config.get("proxy"));
            proxies.put("https", config.get("proxy"));
            session.setProxies(proxies);
        }
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
        if (!config.containsKey("session_token") && (!config.containsKey("email") ||
                !config.containsKey("password"))) {
            throw new RuntimeException("No tokens provided");
        } else if (config.containsKey("session_token")) {
            String sessionToken = config.get("session_token");
            if (sessionToken == null || sessionToken.equals("")) {
                throw new RuntimeException("No tokens provided");
            }
            Session session = new Session();

            // Set proxies
            setupProxy(session);

            // Set cookies
            session.getCookies().put("__Secure-next-auth.session-token", config.get(
                    "session_token"));

            String urlSession = "https://chat.openai.com/api/auth/session";
            HttpResponse response = session.get2(urlSession,
                    Collections.singletonMap(
                            "User-Agent",
                            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15" +
                                    " (KHTML," +
                                    " like Gecko) Version/16.1 Safari/605.1.15 "
                    ));

            try {
                String name = "__Secure-next-auth.session-token";
                String cookieValue = response.getCookieValue(name);
                config.put("session_token", cookieValue);

                String body = response.body();
                System.out.println("session_token: " + cookieValue);
                JSONObject responseObject = JSON.parseObject(body);

                String accessToken = responseObject.getString("accessToken");
                System.out.println("accessToken: " + accessToken);

                config.put("Authorization", accessToken);

                this.refreshHeaders();
            } catch (Exception e) {
                System.out.println("Error refreshing session");
                throw new Exception("Error refreshing session", e);
            }
        } else if (config.containsKey("email") && config.containsKey("password")) {
            try {
                this.login(config.get("email"), config.get("password"));
            } catch (Exception e) {
                System.out.println("Error refreshing session: ");
                System.out.println(e);
                throw e;
            }
        } else {
            throw new RuntimeException("No tokens provided");
        }
    }


    public void login(String email, String password) {
        System.out.println("Logging in...");
        boolean useProxy = false;
        String proxy = null;
        if (config.containsKey("proxy")) {
            if (!config.get("proxy").equals("")) {
                useProxy = true;
                proxy = config.get("proxy");
            }
        }
        OpenAIAuth auth = new OpenAIAuth(email, password, useProxy, proxy);
        try {
            auth.begin();
        } catch (Exception e) {
            // if RuntimeException with e as "Captcha detected" fail
            if (e.getMessage().equals("Captcha detected")) {
                System.out.println("Captcha not supported. Use session tokens instead.");
                throw new RuntimeException("Captcha detected", e);
            }
            throw new RuntimeException("Error logging in", e);
        }
        if (auth.getAccessToken() != null) {
            config.put("Authorization", auth.getAccessToken());
            if (auth.getSessionToken() != null) {
                config.put("session_token", auth.getSessionToken());
            } else {
                String possibleTokens = auth.getSession().getCookies().get("__Secure-next-auth" +
                        ".session-token");
                if (possibleTokens != null) {
                    if (possibleTokens.length() > 1) {
                        config.put("session_token", possibleTokens);
//                        config.put("session_token", possibleTokens[0]);
                    } else {
                        try {
                            config.put("session_token", possibleTokens);
                        } catch (Exception e) {
                            throw new RuntimeException("Error logging in", e);
                        }
                    }
                }
            }
            this.refreshHeaders();
        } else {
            throw new RuntimeException("Error logging in");
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
