package com.plexpt.chatgpt.entity.chat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.plexpt.chatgpt.util.TokensUtil;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * chat
 *
 * @author plexpt
 * @link https://platform.openai.com/docs/overview
 */
@Data
@Builder
@Slf4j
@AllArgsConstructor
@NoArgsConstructor(force = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatCompletion {

    @NonNull
    @Builder.Default
    private String model = "gpt-3.5-turbo";

    @NonNull
    private List<Message> messages;
    /**
     * 使用什么取样温度，0到2之间。越高越奔放。越低越保守。
     * <p>
     * 不要同时改这个和topP
     */
    private Double temperature;

    /**
     * 0-1
     * 建议0.9
     * 不要同时改这个和temperature
     */
    @JsonProperty("top_p")
    private Double topP;


    @JsonProperty("tool_choice")
    String toolChoice;

    List<ChatTool> tools;

    /**
     * 结果数。
     */
    @Builder.Default
    Integer n = 1;


    /**
     * 是否流式输出.
     * default:false
     */
    @Builder.Default
    Boolean stream = false;
    /**
     * 停用词
     * <p>
     * stop 参数用于指定 API 生成令牌时应停止的序列。该参数可以是字符串、字符串数组或 null，最多可以包含 4 个序列。这是一个可选参数，默认值为 null。
     * <p>
     * 参数说明
     * 类型：string、array 或 null
     * 可选：是
     * 默认值：null
     * 用途：指定在生成的文本中，API 遇到这些序列时停止生成后续令牌
     * 使用场景
     * 单个停止序列：
     * <p>
     * 如果指定一个字符串，API 在生成文本时遇到该字符串就会停止。例如，如果设置 stop 参数为 "END", 当生成的文本包含 "END" 时，API 将停止生成后续文本。
     * 多个停止序列：
     * <p>
     * 如果指定一个字符串数组，API 在生成文本时遇到任何一个字符串都会停止。例如，如果设置 stop 参数为 ["END", "STOP"], 当生成的文本包含 "END" 或 "STOP" 时，API 将停止生成后续文本。
     * 不使用停止序列：
     * <p>
     * 如果将 stop 参数设置为 null 或不设置，API 将根据其默认行为生成文本，直到达到最大令牌限制或结束标记。
     */
    List<String> stop;
    /**
     * 3.5 最大支持4096
     * 4.0 最大32k
     */
    @JsonProperty("max_tokens")
    Integer maxTokens;


    /**
     * Optional
     * Defaults to 0
     * Number between -2.0 and 2.0. Positive values penalize new tokens based on whether they appear in the text so far,
     * increasing the model's likelihood to talk about new topics.
     */
    @JsonProperty("presence_penalty")
    Double presencePenalty;

    /**
     * -2.0 ~~ 2.0 Defaults to 0
     */
    @JsonProperty("frequency_penalty")
    Double frequencyPenalty;

    /**
     * Optional
     * Defaults to null
     */
    @JsonProperty("logit_bias")
    Map logitBias;
    /**
     * 用户唯一值，确保接口不被重复调用
     */
    String user;

    /**
     * 返回格式  当前只有gpt-3.5-turbo-1106和gpt-4-1106-preview 支持json_object格式返回
     */
    @JsonProperty("response_format")
    ResponseFormat responseFormat;

    /**
     * boolean or null
     * <p>
     * Optional
     * Defaults to false
     */
    Boolean logprobs;

    /**
     * integer or null
     * <p>
     * Optional
     */
    @JsonProperty("top_logprobs")
    Integer topLogprobs;

    Integer seed;

    /**
     * Specifies the latency tier to use for processing the request. This parameter is relevant for customers subscribed to the scale tier service:
     * <p>
     * If set to 'auto', the system will utilize scale tier credits until they are exhausted.
     * If set to 'default', the request will be processed using the default service tier with a lower uptime SLA and no latency guarentee.
     * When this parameter is set, the response body will include the service_tier utilized.
     */
    @JsonProperty("service_tier")
    String serviceTier;

    @JsonProperty("stream_options")
    StreamOption streamOptions;

    @JsonProperty("parallel_tool_calls")
    Boolean parallelToolCalls;

    /**
     * model
     */
    public interface Model {
        /**
         * gpt-3.5-turbo
         */
        String GPT_3_5_TURBO = "gpt-3.5-turbo";
        /**
         * GPT4.0
         */
        String GPT4 = "gpt-4";
        String GPT4o = "gpt-4o";
        String GPT4oMini = "gpt-4o-mini";

    }

    /**
     * 计算token
     *
     * @return
     */
    public int countTokens() {
        return TokensUtil.tokens(this.model, this.messages);
    }
}


