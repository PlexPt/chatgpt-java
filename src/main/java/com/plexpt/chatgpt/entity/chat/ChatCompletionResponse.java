package com.plexpt.chatgpt.entity.chat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.plexpt.chatgpt.entity.billing.Usage;
import lombok.Data;

import java.util.List;

/**
 * chat答案类
 *
 * @author plexpt
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatCompletionResponse {
    private String id;
    private String object;
    private long created;
    private String model;
    @JsonProperty("system_fingerprint")
    private String systemFingerprint;
    private List<ChatChoice> choices;
    private Usage usage;
}
