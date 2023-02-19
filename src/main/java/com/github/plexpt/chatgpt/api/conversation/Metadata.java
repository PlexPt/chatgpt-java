package com.github.plexpt.chatgpt.api.conversation;

import lombok.Data;

@Data
public class Metadata {
    public String message_type;
    public String model_slug;
    public FinishDetails finish_details;
}
