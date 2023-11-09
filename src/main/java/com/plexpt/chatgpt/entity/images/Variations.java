package com.plexpt.chatgpt.entity.images;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.plexpt.chatgpt.entity.images.enums.ResponseFormat;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import java.io.File;
import java.util.HashMap;
import java.util.Objects;

/**
 * @Author matoooo
 * @Date 2023/8/25 14:08
 * @Description: TODO
 */

@JsonIgnoreProperties(ignoreUnknown = true)
public class Variations extends HashMap<String, RequestBody> {

    public Variations(int n, String size, String response_format){
        this.put("n",RequestBody.create(MediaType.parse("text/plain;charset=UTF-8"), String.valueOf(n)));
        this.put("size",RequestBody.create(MediaType.parse("text/plain;charset=UTF-8"), size));
        this.put("response_format",RequestBody.create(MediaType.parse("text/plain;charset=UTF-8"), response_format));
    }

    public static Variations ofURL(int n, String size) {
        return new Variations(n,size, ResponseFormat.URL.getValue());
    }

    public static Variations ofB64_JSON(int n, String size) {
        return new Variations(n,size,ResponseFormat.B64_JSON.getValue());
    }
}
