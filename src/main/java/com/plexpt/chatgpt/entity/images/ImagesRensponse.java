package com.plexpt.chatgpt.entity.images;

import com.plexpt.chatgpt.util.fastjson.JSON;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Author matoooo
 * @Date 2023/8/25 11:20
 * @Description: TODO
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ImagesRensponse {

    private String code;
    private String msg;
    private List<Object> data;
    private long created;

    public List<String> getUrls() {
        List<String> urls = new ArrayList<>();
        for (Object datum : data) {
            if (datum instanceof Map) {
                Object url = ((Map<?, ?>) datum).get("url");
                if (url != null) {
                    urls.add(url.toString());
                }
            }
        }
        return urls;
    }
}
