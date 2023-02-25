package com.github.plexpt.chatgpt.api.conversation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConversationRequest {
    public String action;
    public ArrayList<Message> messages;
    public Object conversation_id;
    public String parent_message_id;
    public String model;
}
