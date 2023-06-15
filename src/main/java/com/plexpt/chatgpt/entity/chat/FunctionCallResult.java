package com.plexpt.chatgpt.entity.chat;


import lombok.Data;

@Data
public class FunctionCallResult {

    String name;

    String arguments;
}
