package com.plexpt.chatgpt.entity.chat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

/**
 * @author plexpt
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatChoice {
    //    {
    //      "index": 0,
    //      "message": {
    //        "role": "assistant",
    //        "content": null,
    //        "tool_calls": [
    //          {
    //            "id": "call_abc123",
    //            "type": "function",
    //            "function": {
    //              "name": "get_current_weather",
    //              "arguments": "{\n\"location\": \"Boston, MA\"\n}"
    //            }
    //          }
    //        ]
    //      },
    //      "logprobs": null,
    //      "finish_reason": "tool_calls"
    //    }
    private long index;
    /**
     * 请求参数stream为true返回是delta
     */
    @JsonProperty("delta")
    private Message delta;
    /**
     * 请求参数stream为false返回是message
     */
    @JsonProperty("message")
    private Message message;
    @JsonProperty("finish_reason")
    private String finishReason;
}
