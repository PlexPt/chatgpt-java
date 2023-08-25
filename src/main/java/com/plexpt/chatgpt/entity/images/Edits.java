package com.plexpt.chatgpt.entity.images;

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

public class Edits extends HashMap<String, RequestBody> {

    public Edits(File image,String prompt, int n, String size,String response_format){
        this.put("image",RequestBody.create(MediaType.parse("multipart/form-data;charset=UTF-8"), image));
        this.put("prompt",RequestBody.create(MediaType.parse("text/plain;charset=UTF-8"), prompt));
        this.put("n",RequestBody.create(MediaType.parse("text/plain;charset=UTF-8"), String.valueOf(n)));
        this.put("size",RequestBody.create(MediaType.parse("text/plain;charset=UTF-8"), size));
        this.put("response_format",RequestBody.create(MediaType.parse("text/plain;charset=UTF-8"), response_format));
    }
    public Edits(File image,String prompt, int n, String size,File mask,String response_format){
        this.put("image",RequestBody.create(MediaType.parse("multipart/form-data;charset=UTF-8"), image));
        this.put("prompt",RequestBody.create(MediaType.parse("text/plain;charset=UTF-8"), prompt));
        this.put("n",RequestBody.create(MediaType.parse("text/plain;charset=UTF-8"), String.valueOf(n)));
        this.put("size",RequestBody.create(MediaType.parse("text/plain;charset=UTF-8"), size));
        this.put("mask",RequestBody.create(MediaType.parse("multipart/form-data;charset=UTF-8"), mask));
        this.put("response_format",RequestBody.create(MediaType.parse("text/plain;charset=UTF-8"), response_format));
    }
    public static Edits ofURL(File image,String prompt, int n, String size) {
        return new Edits(image,prompt,n,size,ResponseFormat.URL.getValue());
    }
    public static Edits ofURL(File image,String prompt, int n, String size,File mask) {
        return new Edits(image,prompt,n,size,mask,ResponseFormat.URL.getValue());
    }

    public static Edits ofB64_JSON(File image,String prompt, int n, String size) {
        return new Edits(image,prompt,n,size,ResponseFormat.B64_JSON.getValue());
    }
    public static Edits ofB64_JSON(File image,String prompt, int n, String size,File mask) {
        return new Edits(image,prompt,n,size,mask,ResponseFormat.B64_JSON.getValue());
    }
}
