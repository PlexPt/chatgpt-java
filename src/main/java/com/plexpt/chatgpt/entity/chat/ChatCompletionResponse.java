package com.plexpt.chatgpt.entity.chat;

import com.plexpt.chatgpt.entity.billing.Usage;

import java.util.List;

import lombok.Data;

/**
 * chat答案类
 *
 * @author plexpt
 */
@Data
public class ChatCompletionResponse {
    private String id;
    private String object;
    private long created;
    private String model;
    private List<ChatChoice> choices;
    private Usage usage;
}
