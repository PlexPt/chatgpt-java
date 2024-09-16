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

    private Integer index;
    /**
     * 请求参数stream为true返回是delta
     */
    private MessageResponse delta;
    /**
     * 请求参数stream为false返回是message
     */
    private MessageResponse message;
    /**
     * The reason the model stopped generating tokens. This will be stop if the model hit a natural stop point or a provided stop sequence, length if the maximum number of tokens specified in the request was reached, content_filter if content was omitted due to a flag from our content filters, tool_calls if the model called a tool, or function_call (deprecated) if the model called a function.
     * 用于指示模型停止生成令牌的原因。这个参数在响应体中返回，用于解释生成过程为什么终止。
     * <p>
     * 可能的值
     * stop：
     * <p>
     * 模型达到了一个自然的停止点或遇到了提供的停止序列。
     * 例如，生成的文本包含一个定义的停止序列（如 "END"），或者模型认为已经生成了一个完整的句子或段落。
     * length：
     * <p>
     * 达到了请求中指定的最大令牌数。
     * 例如，请求中设置了 max_tokens 为 50，当生成的令牌数量达到 50 时，模型停止生成。
     * content_filter：
     * <p>
     * 由于内容过滤器的标志，部分内容被省略。
     * 例如，生成的文本可能包含不适当的内容，触发了内容过滤器，导致生成过程提前终止。
     * tool_calls：
     * <p>
     * 模型调用了一个工具。
     * 这种情况通常用于增强模型的功能，如调用外部API或执行特定任务。
     */
    @JsonProperty("finish_reason")
    private String finishReason;

}
