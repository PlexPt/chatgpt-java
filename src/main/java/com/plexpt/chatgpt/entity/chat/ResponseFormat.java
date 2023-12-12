package com.plexpt.chatgpt.entity.chat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

/**
 * @author hq
 * @version 1.0
 * @date 2023/12/11
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor(force = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseFormat {

    public String type = Type.TEXT.getValue();

    @Getter
    @AllArgsConstructor
    public enum Type {
        JSON_OBJECT("json_object"),
        TEXT("text");
        private final String value;
    }
}
