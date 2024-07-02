package com.plexpt.chatgpt.entity.chat;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * completion_tokens
 * integer
 *
 * Number of tokens in the generated completion.
 *
 * prompt_tokens
 * integer
 *
 * Number of tokens in the prompt.
 *
 * total_tokens
 * integer
 *
 * Total number of tokens used in the request (prompt + completion).
 *
 * @author pt
 */
@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CompletionUseage {

    Integer completion_tokens;
    Integer prompt_tokens;
    Integer total_tokens;

}
