package com.github.plexpt.chatgpt;

import net.dreamlu.mica.http.HttpRequest;

import java.util.HashMap;
import java.util.Map;

import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import lombok.Data;
import okhttp3.Response;

@Data
public class Session {
    private Map<String, String> headers = new HashMap<>();
    private Map<String, String> cookies = new HashMap<>();
    private Map<String, String> proxies = new HashMap<>();
    String client;

    public Session() {
    }


    public Response post(String url, Map<String, Object> data) {
        getCookiesString();
        return HttpRequest.post(url)
                .addHeader(headers)
                .bodyJson(data)
                .execute()
                .response();
    }

    public Response post(String url, Map<String, Object> data, boolean followRedirects) {
        getCookiesString();

        return HttpRequest.post(url)
                .addHeader(headers)
                .followRedirects(followRedirects)
                .bodyJson(data)
                .execute()
                .response();
    }

    public Response get(String url, Map<String, String> data) {

        getCookiesString();
        Map<String, Object> map = new HashMap<>(data);
        Response response = HttpRequest.get(url)
                .addHeader(headers)
                .queryMap(map)
                .execute()
                .response();

        return response;
    }

    public HttpResponse get2(String url, Map<String, String> data) {
        getCookiesString();

        Map<String, Object> map = new HashMap<>(data);
        return HttpUtil.createGet(url)
                .addHeaders(headers)
                .cookie(getCookiesString())
                .form(map)
                .execute();
    }

    private String getCookiesString() {
        String result = "";
        for (Map.Entry<String, String> entry : cookies.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            result = result + key + "=" + value + "; ";
        }
        headers.put("cookie", result);
        return result;
    }

    public String getString(String url, Map<String, String> data) {

        getCookiesString();
        Map<String, Object> map = new HashMap<>(data);
        return HttpRequest.get(url)
                .addHeader(headers)
                .queryMap(map)
                .execute()
                .asString();
    }

    public Response get(String url, Map<String, String> data, boolean followRedirects) {
        getCookiesString();
        Map<String, Object> map = new HashMap<>(data);

        return HttpRequest.get(url)
                .addHeader(headers)
                .followRedirects(followRedirects)
                .queryMap(map)
                .execute()
                .response();
    }

    public Response post(String url, Map<String, String> headers, String payload) {

        getCookiesString();
        return HttpRequest.post(url)
                .addHeader(headers)
                .bodyJson(payload)
                .execute()
                .response();
    }
}
