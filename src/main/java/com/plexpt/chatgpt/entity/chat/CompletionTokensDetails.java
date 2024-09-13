package com.plexpt.chatgpt.entity.chat;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CompletionTokensDetails {

    Long reasoning_tokens;
}
