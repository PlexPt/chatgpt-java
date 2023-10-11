package com.plexpt.chatgpt.entity.images;

import lombok.Data;

import java.util.List;

/**
 * @Author matoooo
 * @Date 2023/8/25 11:20
 * @Description: TODO
 */
@Data
public class ImagesRensponse {

    private String code;
    private String msg;
    private List<Object> data;
    private long created;

}
