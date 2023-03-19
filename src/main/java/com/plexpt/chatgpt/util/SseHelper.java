package com.plexpt.chatgpt.util;

import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SseHelper {


    public void complete(SseEmitter sseEmitter) {

        try {
            sseEmitter.complete();
        } catch (Exception e) {

        }
    }

    public void send(SseEmitter sseEmitter, Object data) {

        try {
            sseEmitter.send(data);
        } catch (Exception e) {

        }
    }
}
