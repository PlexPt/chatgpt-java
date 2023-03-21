package com.plexpt.chatgpt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.plexpt.chatgpt.api.Api;
import com.plexpt.chatgpt.entity.chat.ChatCompletion;
import com.plexpt.chatgpt.entity.chat.Message;

import java.net.Proxy;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.ContentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import okhttp3.sse.EventSources;


/**
 * open ai 客户端
 *
 * @author plexpt
 */

@Slf4j
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatGPTStream {

    private String apiKey;
    private List<String> apiKeyList;

    private OkHttpClient okHttpClient;
    /**
     * 连接超时
     */
    @Builder.Default
    private long timeout = 90;

    /**
     * 网络代理
     */
    @Builder.Default
    private Proxy proxy = Proxy.NO_PROXY;
    /**
     * 反向代理
     */
    @Builder.Default
    private String apiHost = Api.DEFAULT_API_HOST;

    /**
     * 初始化
     */
    public ChatGPTStream init() {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.connectTimeout(timeout, TimeUnit.SECONDS);
        client.writeTimeout(timeout, TimeUnit.SECONDS);
        client.readTimeout(timeout, TimeUnit.SECONDS);
        if (Objects.nonNull(proxy)) {
            client.proxy(proxy);
        }

        okHttpClient = client.build();

        return this;
    }


    /**
     * 流式输出
     */
    public void streamChatCompletion(ChatCompletion chatCompletion,
                                     EventSourceListener eventSourceListener) {

        chatCompletion.setStream(true);

        try {
            EventSource.Factory factory = EventSources.createFactory(okHttpClient);
            ObjectMapper mapper = new ObjectMapper();
            String requestBody = mapper.writeValueAsString(chatCompletion);
            String key = apiKey;
            if (apiKeyList != null && !apiKeyList.isEmpty()) {
                key = RandomUtil.randomEle(apiKeyList);
            }


            Request request = new Request.Builder()
                    .url(apiHost + "v1/chat/completions")
                    .post(RequestBody.create(MediaType.parse(ContentType.JSON.getValue()),
                            requestBody))
                    .header("Authorization", "Bearer " + key)
                    .build();
            factory.newEventSource(request, eventSourceListener);

        } catch (Exception e) {
            log.error("请求出错：{}", e);
        }
    }

    /**
     * 流式输出
     */
    public void streamChatCompletion(List<Message> messages,
                                     EventSourceListener eventSourceListener) {
        ChatCompletion chatCompletion = ChatCompletion.builder()
                .messages(messages)
                .stream(true)
                .build();
        streamChatCompletion(chatCompletion, eventSourceListener);
    }


}
