package com.github.chatgpt;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.github.plexpt.chatgpt.api.conversation.Content;
import com.github.plexpt.chatgpt.api.conversation.ConversationRequest;
import com.github.plexpt.chatgpt.api.conversation.ConversationResponse;
import com.github.plexpt.chatgpt.api.conversation.Message;
import com.github.plexpt.chatgpt.api.conversations.ConversationsResponse;
import com.github.plexpt.chatgpt.api.model.ModelResponse;
import com.github.plexpt.chatgpt.client.ChatGPTService;
import okhttp3.ResponseBody;
import org.junit.Test;

import javax.xml.ws.Response;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

public class OpenAIAuthTest {

    private final ChatGPTService chatGPTService;

    public OpenAIAuthTest() {
        chatGPTService = new ChatGPTService(System.getenv("ACCESSTOKEN"));
    }

    @Test
    public void getModels(){
        ModelResponse result = chatGPTService.getModels();

        assertFalse(result.models.isEmpty());
        assertEquals(result.models.get(0).description, "The standard ChatGPT model");
    }
    @Test
    public void getConversations() {
        ConversationsResponse result = chatGPTService.getConversations(0, 10);
        assertFalse(result.items.isEmpty());
    }

    @Test
    public void getConversation() {
        ArrayList<String> parts = new ArrayList<>();
        ArrayList<Message> messages = new ArrayList<>();

        parts.add("Hi There");

        Content content = Content.builder()
                .content_type("text")
                .parts(parts).build();

        Message message = Message.builder()
                .id("71d419a8-ab0a-4f76-b17c-13edeea8c087")
                .role("user")
                .content(content)
                .build();

        messages.add(message);

        ConversationRequest conversationRequest = ConversationRequest.builder()
                .action("next")
                .messages(messages)
                .conversation_id(null)
                .parent_message_id("55b6f907-1c3c-4111-9b15-df8ff71ab1ca")
                .model("text-davinci-002-render-sha")
                .build();

        ResponseBody result = chatGPTService.getConversation(conversationRequest);

        try {
            String body =  result.string();

            Map<String, Object> chatData = new HashMap<>();
            for (String s : body.split("\n")) {
                if ((s == null) || "".equals(s)) {
                    continue;
                }
                if (s.contains("data: [DONE]")) {
                    continue;
                }

                String part = s.substring(5);
                ConversationResponse emp = new ObjectMapper().readValue(part, ConversationResponse.class);
                System.out.println(emp.getMessage().getContent().getParts());
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
