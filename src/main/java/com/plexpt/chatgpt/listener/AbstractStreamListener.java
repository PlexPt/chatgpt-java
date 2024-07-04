package com.plexpt.chatgpt.listener;

import com.plexpt.chatgpt.entity.chat.ChatCompletionResponse;
import com.plexpt.chatgpt.util.fastjson.JSON;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import org.springframework.util.StringUtils;

import java.util.Objects;
import java.util.function.Consumer;

/**
 * EventSource listener for chat-related events.
 *
 * @author plexpt
 */
@Slf4j
public abstract class AbstractStreamListener extends EventSourceListener {

    protected String lastMessage = "";


    /**
     * Called when all new message are received.
     *
     * @param message the new message
     */
    @Setter
    @Getter
    protected Consumer<String> onComplete = s -> {

    };

    /**
     * Called when a new message is received.
     * 收到消息 单个字
     *
     * @param message the new message
     */
    public abstract void onMsg(String message);

    /**
     * Called when an error occurs.
     * 出错时调用
     *
     * @param throwable the throwable that caused the error
     * @param response  the response associated with the error, if any
     */
    public abstract void onError(Throwable throwable, String response);

    @Override
    public void onOpen(EventSource eventSource, Response response) {
        // do nothing
    }

    @Override
    public void onClosed(EventSource eventSource) {
        // do nothing
    }

    @Override
    public void onEvent(EventSource eventSource, String id, String type, String data) {
        if (data.equals("[DONE]")) {
            onComplete.accept(lastMessage);
            return;
        }

        ChatCompletionResponse response = JSON.parseObject(data, ChatCompletionResponse.class);

        String text = response.toPlainStringStream();

        if (!StringUtils.isEmpty(text)) {

            lastMessage += text;

            onMsg(text);

        }

    }


    @SneakyThrows
    @Override
    public void onFailure(EventSource eventSource, Throwable throwable, Response response) {

        try {
            log.error("Stream connection error: {}", throwable);

            String responseText = "";

            if (Objects.nonNull(response)) {
                responseText = response.body().string();
            }

            log.error("response：{}", responseText);

            this.onError(throwable, responseText);

        } catch (Exception e) {
            log.warn("onFailure error:{}", e);
            // do nothing

        } finally {
            eventSource.cancel();
        }
    }
}
