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
    @Builder.Default
    private double temperature = 0.9;

    /**
     * 0-1
     * 建议0.9
     * 不要同时改这个和temperature
     */
    @JsonProperty("top_p")
    @Builder.Default
    private double topP = 0.9;


    /**
     * auto
     */
    String function_call;

    @JsonProperty("tool_choice")
    String toolChoice;

    List<ChatTool> tools;

    List<ChatFunction> functions;

    /**
     * 结果数。
     */
    @Builder.Default
    private Integer n = 1;


    /**
     * 是否流式输出.
     * default:false
     */
    @Builder.Default
    private boolean stream = false;
    /**
     * 停用词
     */
    private List<String> stop;
    /**
     * 3.5 最大支持4096
     * 4.0 最大32k
     */
    @JsonProperty("max_tokens")
    private Integer maxTokens;


    @JsonProperty("presence_penalty")
    private double presencePenalty;

    /**
     * -2.0 ~~ 2.0
     */
    @JsonProperty("frequency_penalty")
    private double frequencyPenalty;

    @JsonProperty("logit_bias")
    private Map logitBias;
    /**
     * 用户唯一值，确保接口不被重复调用
     */
    private String user;

    /**
     * 返回格式  当前只有gpt-3.5-turbo-1106和gpt-4-1106-preview 支持json_object格式返回
     */
    @JsonProperty("response_format")
    private ResponseFormat responseFormat;


    @Getter
    @AllArgsConstructor
    public enum Model {
        /**
         * gpt-3.5-turbo
         */
        GPT_3_5_TURBO("gpt-3.5-turbo"),
        GPT_3_5_TURBO_0613("gpt-3.5-turbo-0613"),
        GPT_3_5_TURBO_16K("gpt-3.5-turbo-16k"),
        /**
         * 临时模型，不建议使用
         */
        GPT_3_5_TURBO_0301("gpt-3.5-turbo-0301"),
        GPT_3_5_TURBO_1106("gpt-3.5-turbo-1106"),
        GPT_3_5_TURBO_0125("gpt-3.5-turbo-0125"),
        GPT_3_5_TURBO_INSTRUCT("gpt-3.5-turbo-instruct"),
        /**
         * GPT4.0
         */
        GPT_4("gpt-4"),
        GPT4Turbo("gpt-4-1106-preview"),
        GPT4Turbo0125("gpt-4-0125-preview"),
        GPT_4VP("gpt-4-vision-preview"),
        GPT_4V("gpt-4-vision-preview"),
        /**
         * 临时模型，不建议使用
         */
        GPT_4_0314("gpt-4-0314"),
        /**
         * 支持函数
         */
        GPT_4_0613("gpt-4-0613"),
        /**
         * GPT4.0 超长上下文
         */
        GPT_4_32K("gpt-4-32k"),
        /**
         * GPT4.0 超长上下文
         */
        GPT_4_32K_0613("gpt-4-32k-0613"),
        /**
         * 临时模型，不建议使用
         */
        GPT_4_32K_0314("gpt-4-32k-0314"),
        /**
         * GPT4.0 Omni
         */        
        GPT_4_OMNI("gpt-4o"),
        GPT_4_OMNI_0314("gpt-4o-0314"),
        GPT_4_OMNI_0613("gpt-4o-0613"),
        GPT_4_OMNI_32K_0314("gpt-4o-32k-0314"),
        GPT_4_OMNI_32K_0613("gpt-4o-32k-0613"),        
        ;
        private String name;
    }

    public int countTokens() {
        return TokensUtil.tokens(this.model, this.messages);
    }
}


