package com.plexpt.chatgpt.entity.images;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.plexpt.chatgpt.entity.images.enums.ResponseFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * @Author matoooo
 * @Date 2023/8/25 10:59
 * @Description: 图片生成
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Generations {
    private String prompt;
    /**
     * Optional Defaults to dall-e-2
     * <br/>
     * The model to use for image generation.
     */
    private String model;
    private int n;
    /**
     * Optional Defaults to standard
     * <br/>
     * dall-e-3可以使用hd
     */
    private String quality;
    /**
     * The size of the generated images. Must be one of 256x256, 512x512, or 1024x1024 for dall-e-2. Must be one of 1024x1024, 1792x1024, or 1024x1792 for dall-e-3
     */
    private String size;
    private String response_format;
    /**
     * Optional
     * Defaults to vivid
     * <br/>
     * The style of the generated images. Must be one of vivid or natural. Vivid causes the model to lean towards generating hyper-real and dramatic images. Natural causes the model to produce more natural, less hyper-real looking images. This param is only supported for dall-e-3.
     */
    private String style;


    @Getter
    @AllArgsConstructor
    public enum Model {
        DALL_E_2("dall-e-2"),
        DALL_E_3("dall-e-3"),
        ;
        private String name;
    }

    public static Generations ofURL(String prompt, int n, String size) {
        return Generations.builder()
                .prompt(prompt)
                .n(n)
                .size(size)
                .response_format(ResponseFormat.URL.getValue())
                .build();
    }

    public static Generations ofB64_JSON(String prompt, int n, String size) {
        return Generations.builder()
                .prompt(prompt)
                .n(n)
                .size(size)
                .response_format(ResponseFormat.B64_JSON.getValue())
                .build();
    }
}
