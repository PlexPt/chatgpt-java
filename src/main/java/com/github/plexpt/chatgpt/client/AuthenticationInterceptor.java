package com.github.plexpt.chatgpt.client;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * OkHttp Interceptor that adds an authorization token header
 */
public class AuthenticationInterceptor implements Interceptor {

    private final String token;

    AuthenticationInterceptor(String token) {
        this.token = token;
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request()
                .newBuilder()
                .addHeader("Authorization", "Bearer " + token)
//                .addHeader("Accept",  "text/event-stream")
//
//                .addHeader("Content-Type",  "application/json")
//                .addHeader("X-Openai-Assistant-App-Id",  "")
//                .addHeader("Connection", "close")
//                .addHeader("Accept-Language", "en-US,en;q=0.9")
//                .addHeader("Referer", "https://chat.openai.com/chat")
                .build();
        return chain.proceed(request);
    }
}
