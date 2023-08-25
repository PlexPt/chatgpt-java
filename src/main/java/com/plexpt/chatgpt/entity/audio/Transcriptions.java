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

    public Transcriptions(File file, String model){
        this.put("file",RequestBody.create(MediaType.parse("multipart/form-data;charset=UTF-8"), file));
        this.put("model",RequestBody.create(MediaType.parse("text/plain;charset=UTF-8"), model));
    }

    public Transcriptions(File file, String model, String prompt){
        this.put("file",RequestBody.create(MediaType.parse("multipart/form-data;charset=UTF-8"), file));
        this.put("model",RequestBody.create(MediaType.parse("text/plain;charset=UTF-8"), model));
        this.put("prompt",RequestBody.create(MediaType.parse("text/plain;charset=UTF-8"), prompt));
    }

    public static Transcriptions of(File file, String model) {
        return new Transcriptions(file,model);
    }
    public static Transcriptions of(File file, String model, String prompt) {
        return new Transcriptions(file,model,prompt);
    }
}
