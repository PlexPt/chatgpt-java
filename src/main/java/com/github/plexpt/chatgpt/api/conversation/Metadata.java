package com.github.plexpt.chatgpt.api.conversation;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Metadata {
//    public String timestamp_;
    public String message_type;
    public String model_slug;
    public FinishDetails finish_details;
}
