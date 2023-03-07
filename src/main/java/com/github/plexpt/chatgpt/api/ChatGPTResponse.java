package com.github.plexpt.chatgpt.api;

import lombok.Data;

import java.util.List;

/**
 * A wrapper class to fit the ChatGPT engine and search endpoints
 */
@Data
public class ChatGPTResponse<T> {
    /**
     * A list containing the actual results
     */
    public List<T> data;

    /**
     * The type of object returned, should be "list"
     */
    public String object;
}
