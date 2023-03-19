package com.plexpt.chatgpt.listener;

import com.alibaba.fastjson.JSON;
import com.plexpt.chatgpt.entity.chat.ChatChoice;
import com.plexpt.chatgpt.entity.chat.ChatCompletionResponse;
import com.plexpt.chatgpt.entity.chat.Message;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Response;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;

/**
 * sse
 *
 * @author plexpt
 */
@Slf4j
public abstract class AbstractStreamListener extends EventSourceListener {

    protected String last = "";
    @Setter
    @Getter
    protected Consumer<String> onComplate = s -> {

    };


    /**
     * 收到消息 单个字
     */
    public abstract void onMsg(String msg);


    /**
     * 出错了
     */
    public abstract void onError(Throwable t, String response);

    @Override
    public void onOpen(EventSource eventSource, Response response) {
    }

    @Override
    public void onClosed(EventSource eventSource) {
    }

    @Override
    public void onEvent(EventSource eventSource, String id, String type, String data) {
        if (data.equals("[DONE]")) {
            log.info("回答完成：{}", last);
            onComplate.accept(last);
            return;
        }

        ChatCompletionResponse response = JSON.parseObject(data, ChatCompletionResponse.class);
        // 读取Json
        List<ChatChoice> choices = response.getChoices();
        if (choices == null || choices.isEmpty()) {
            return;
        }
        Message delta = choices.get(0).getDelta();
        String text = delta.getContent();

        if (text != null) {
            last += text;

            onMsg(text);

        }

    }


    @SneakyThrows
    @Override
    public void onFailure(EventSource eventSource, Throwable t, Response response) {

        try {
            log.error("stream连接异常:{}", t);

            String res = "";

            if (Objects.nonNull(response)) {
                res = response.body().string();
            }

            log.error("response：{}", res);

            String seq = "Your access was terminated due to violation of our policies";

            if (StrUtil.contains(res, seq)) {
                log.error("检测到号被封了");
            }

            onError(t, res);

        } catch (Exception e) {

        } finally {
            eventSource.cancel();
        }
    }
}
