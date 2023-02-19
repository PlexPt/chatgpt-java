package com.github.plexpt.chatgpt.api.conversation;

import lombok.Data;

@Data
public class ConversationResponse {
    public Message message;
    public String conversation_id;
    public Object error;
}
