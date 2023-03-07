package com.github.plexpt.chatgpt.api.conversation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConversationResponse {
    public Message message;
    public String conversation_id;
    public Object error;
}
