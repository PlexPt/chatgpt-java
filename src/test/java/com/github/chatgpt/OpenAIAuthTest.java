package com.github.chatgpt;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

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
    public void getNewConversation() {

        chatGPTService.getNewConversation("Hi");

        Scanner scanner = new Scanner(System.in);
        String s= scanner.nextLine();

//        System.out.println(result.get(result.size() - 1).getMessage().getContent().getParts().get(0).toString());
//
    }

//    @Test
//    public void getContinueConversation() {
//
//        List<ConversationResponse> result1 = chatGPTService.getContinueConversation("My Name is adam.", null, java.util.UUID.randomUUID().toString());
//        System.out.println(result1.get(result1.size() - 1).getMessage().getContent().getParts().get(0).toString());
//
//        List<ConversationResponse> result2 = chatGPTService.getContinueConversation("What's my name?", result1.get(result1.size() - 1) .getConversation_id(), result1.get(result1.size() - 1).getMessage().getId());
//        System.out.println(result2.get(result2.size() - 1).getMessage().getContent().getParts().get(0).toString());
//
//    }


}
