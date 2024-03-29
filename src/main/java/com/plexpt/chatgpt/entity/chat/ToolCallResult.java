package com.plexpt.chatgpt.entity.chat;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ToolCallResult {

    String id;

    String type;

    FunctionCallResult function;
}
