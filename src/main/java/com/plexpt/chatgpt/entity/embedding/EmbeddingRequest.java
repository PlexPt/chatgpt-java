package com.plexpt.chatgpt.entity.embedding;

import lombok.*;

import java.util.List;

/**
 * 生成向量请求参数
 *
 * @author hq
 * @version 1.0
 * @date 2023/12/12
 */

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class EmbeddingRequest {

    /**
     * 向量模型
     */
    private String model;

    /**
     * 需要转成向量的文本
     */
    private List<String> input;

    /**
     * 代表最终用户的唯一标识符，这将有助于 OpenAI 监控和检测滥用行为
     */
    private String user;


    /**
     * 向量模型枚举
     *
     * @author hq
     * @version 1.0
     * @date 2023/12/12
     */
    @Getter
    @AllArgsConstructor
    public enum EmbeddingModelEnum {
        /**
         * text-embedding-ada-002
         */
        TEXT_EMBEDDING_ADA_002("text-embedding-ada-002"),
        ;

        /**
         * modelName
         */
        private final String modelName;
    }
}
