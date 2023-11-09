package com.plexpt.chatgpt.entity.chat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author plexpt
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Message {
    /**
     * 目前支持三种角色参考官网，进行情景输入：https://platform.openai.com/docs/guides/chat/introduction
     */
    private String role;
    private String content;
    private String name;

    @JsonProperty("function_call")
    private FunctionCallResult functionCall;

    public Message(String role, String content) {
        this.role = role;
        this.content = content;
    }

    public static Message of(String content) {

        return new Message(Message.Role.USER.getValue(), content);
    }

    public static Message ofSystem(String content) {

        return new Message(Role.SYSTEM.getValue(), content);
    }

    public static Message ofAssistant(String content) {

        return new Message(Role.ASSISTANT.getValue(), content);
    }

    public static Message ofFunction(String function) {

        return new Message(Role.FUNCTION.getValue(), function);
    }

    @Getter
    @AllArgsConstructor
    public enum Role {

        SYSTEM("system"),
        USER("user"),
        ASSISTANT("assistant"),

        FUNCTION("function"),
        ;
        private String value;
    }

}
