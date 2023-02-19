package com.github.plexpt.chatgpt.api.conversation;

import lombok.Data;

@Data
public class FinishDetails {
    public String type;
    public String stop;
}
