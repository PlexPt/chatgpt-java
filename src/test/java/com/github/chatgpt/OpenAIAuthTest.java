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

        parts.add("Generate a poem about yourself");

        Content content = Content.builder()
                .content_type("text")
                .parts(parts).build();

        Message message = Message.builder()
                .id(java.util.UUID.randomUUID().toString())
                .role("user")
                .content(content)
                .build();

        messages.add(message);

        ConversationRequest conversationRequest = ConversationRequest.builder()
                .action("next")
                .messages(messages)
                .conversation_id(null)
                .parent_message_id(java.util.UUID.randomUUID().toString())
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
