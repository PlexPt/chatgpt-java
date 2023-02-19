package com.github.plexpt.chatgpt.client;

import com.github.plexpt.chatgpt.api.conversation.ConversationRequest;
import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Streaming;

public interface ConversationApi {

    @Streaming
    @POST("api/conversation")
    Single<ResponseBody> getConversation(@Body ConversationRequest request);
}
