package com.github.chatgpt;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.TimeUnit;

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
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.observers.TestObserver;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.schedulers.TestScheduler;
import io.reactivex.subscribers.TestSubscriber;
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
    public void getNewStreamConversation() throws InterruptedException {

        TestObserver<ConversationResponse> testObserver = new TestObserver<>();
        Observable<ConversationResponse> observer =
                chatGPTService.getNewStreamConversation("Hi");

        observer.subscribe(testObserver);
        testObserver.assertNotComplete();
        testObserver.assertNoValues();

        // If more than 15 second, test is failed
        testObserver.await(15, TimeUnit.SECONDS);

        testObserver.assertComplete();
        testObserver.assertNoErrors();

    }
    @Test
    public void getNewConversation() {
        List<ConversationResponse> result = chatGPTService.getNewConversation("Hi");
        assertFalse(result.isEmpty());
    }

}
