package com.plexpt.chatgpt.entity.images;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.List;

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

}
