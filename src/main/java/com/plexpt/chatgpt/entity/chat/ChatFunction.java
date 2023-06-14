package com.plexpt.chatgpt.entity.chat;


import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatFunction {

    String name;
    String description;

    ChatParameter parameters;


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ChatParameter {

        String type;
        List<String> required;
        Object properties;
    }

}
