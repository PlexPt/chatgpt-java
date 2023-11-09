package com.plexpt.chatgpt.entity.images;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.plexpt.chatgpt.entity.images.enums.ResponseFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private int n;
    private String size;
    private String response_format;

    public static Generations ofURL(String prompt,int n,String size) {
        return new Generations(prompt,n,size, ResponseFormat.URL.getValue());
    }

    public static Generations ofB64_JSON(String prompt,int n,String size) {
        return new Generations(prompt,n,size, ResponseFormat.B64_JSON.getValue());
    }
}
