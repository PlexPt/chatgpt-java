package com.plexpt.chatgpt.entity.billing;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.plexpt.chatgpt.entity.chat.CompletionTokensDetails;
import lombok.Data;

/**
 * @author plexpt
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Usage {
    @JsonProperty("prompt_tokens")
    private long promptTokens;
    @JsonProperty("completion_tokens")
    private long completionTokens;
    @JsonProperty("total_tokens")
    private long totalTokens;

    @JsonIgnoreProperties(ignoreUnknown = true)
    private CompletionTokensDetails completion_tokens_details;


}
