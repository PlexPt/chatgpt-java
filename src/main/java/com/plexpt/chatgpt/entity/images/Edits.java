package com.plexpt.chatgpt.entity.images;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.plexpt.chatgpt.entity.images.enums.ResponseFormat;
import okhttp3.MediaType;
import okhttp3.RequestBody;

import java.io.File;
import java.util.HashMap;

/**
 * @Author matoooo
 * @Date 2023/8/25 11:45
 * @Description: TODO
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Edits extends HashMap<String, RequestBody> {

    public Edits(String prompt, int n, String size,String response_format){
        this.put("prompt",RequestBody.create(MediaType.parse("text/plain;charset=UTF-8"), prompt));
        this.put("n",RequestBody.create(MediaType.parse("text/plain;charset=UTF-8"), String.valueOf(n)));
        this.put("size",RequestBody.create(MediaType.parse("text/plain;charset=UTF-8"), size));
        this.put("response_format",RequestBody.create(MediaType.parse("text/plain;charset=UTF-8"), response_format));
    }
    public static Edits ofURL(String prompt, int n, String size) {
        return new Edits(prompt,n,size,ResponseFormat.URL.getValue());
    }

    public static Edits ofB64_JSON(String prompt, int n, String size) {
        return new Edits(prompt,n,size,ResponseFormat.B64_JSON.getValue());
    }

}
