package com.github.plexpt.chatgpt.client;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.github.plexpt.chatgpt.api.conversation.ConversationRequest;
import com.github.plexpt.chatgpt.api.conversation.ConversationResponse;
import com.github.plexpt.chatgpt.api.conversations.ConversationsResponse;
import com.github.plexpt.chatgpt.api.model.ModelResponse;
import okhttp3.ConnectionPool;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Body;

import javax.net.ssl.*;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static java.time.Duration.ofSeconds;

public class ChatGPTService {
    private static final String BASE_URL = "https://chatgpt.duti.tech";


    final ChatGPTApi chatGPTApi;
    final ConversationApi conversationApi;



    /**
     * Creates a new ChatGPTService that wraps ChatGPTApi
     *
     * @param token ChatGPT Access token string "sk-XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
     */
    public ChatGPTService(final String token)  {
        this(token, BASE_URL, ofSeconds(10));
    }

    public ChatGPTService(final String token, final String baseUrl, final Duration timeout) {

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new AuthenticationInterceptor(token))
                .connectionPool(new ConnectionPool(5, 1, TimeUnit.SECONDS))
                .readTimeout(timeout.toMillis(), TimeUnit.MILLISECONDS)
                        .hostnameVerifier((hostname, session) -> true)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(JacksonConverterFactory.create(mapper))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        Retrofit noParseRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)

                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        this.chatGPTApi = retrofit.create(ChatGPTApi.class);
        this.conversationApi = retrofit.create(ConversationApi.class);
    }

    public ModelResponse getModels() {
        return chatGPTApi.getModels().blockingGet();
    }

    public ConversationsResponse getConversations(int offset, int limit) {
        return chatGPTApi.getConversations(offset, limit).blockingGet();
    }

    public ResponseBody getConversation(ConversationRequest request) {
        return conversationApi.getConversation(request).blockingGet();
    }

}
