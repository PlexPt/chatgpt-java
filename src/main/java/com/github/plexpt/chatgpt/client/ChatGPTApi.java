package com.github.plexpt.chatgpt.client;
import com.github.plexpt.chatgpt.api.ChatGPTResponse;
import com.github.plexpt.chatgpt.api.conversation.ConversationRequest;
import com.github.plexpt.chatgpt.api.conversation.ConversationResponse;
import com.github.plexpt.chatgpt.api.conversations.ConversationsResponse;
import com.github.plexpt.chatgpt.api.model.Model;
import com.github.plexpt.chatgpt.api.model.ModelResponse;
import io.reactivex.Single;
import retrofit2.http.*;

public interface ChatGPTApi {

    @GET("api/models")
    Single<ModelResponse> getModels();

    @GET("api/conversations")
    Single<ConversationsResponse> getConversations(
            @Query("offset") int offset,
            @Query("limit") int limit);



}
