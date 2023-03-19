package com.plexpt.chatgpt.entity;

import java.util.List;

import lombok.Data;

/**
 * @author plexpt
 */
@Data
public class BaseResponse<T> {
    private String object;
    private List<T> data;
    private Error error;


    @Data
    public class Error {
        private String message;
        private String type;
        private String param;
        private String code;
    }
}
