package com.plexpt.chatgpt.entity.audio;

import okhttp3.MediaType;
import okhttp3.RequestBody;

import java.io.File;
import java.util.HashMap;

/**
 * @Author matoooo
 * @Date 2023/8/25 14:14
 * @Description: TODO
 */
public class Transcriptions extends HashMap<String, RequestBody> {

    public Transcriptions(String model, String prompt){
        this.put("model",RequestBody.create(MediaType.parse("text/plain;charset=UTF-8"), model));
        this.put("prompt",RequestBody.create(MediaType.parse("text/plain;charset=UTF-8"), prompt));
    }

    public static Transcriptions of(String model, String prompt) {
        return new Transcriptions(model,prompt);
    }
}
