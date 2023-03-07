package com.github.plexpt.chatgpt.api.conversation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FinishDetails {
    public String type;
    public String stop;
}
