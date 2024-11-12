package com.plexpt.chatgpt.util;

import okhttp3.OkHttpClient;

/**
 * okhhttp自定义程序接口
 *
 * <p>
 * date: 2024/11/12 20:22
 *
 * @author imyuyu
 */
@FunctionalInterface
public interface OkHttpCustomizer {

    /**
     * 自定义okhttp client builder
     * @param builder
     * @return
     */
    OkHttpClient.Builder customize(OkHttpClient.Builder builder);

}
