package com.plexpt.chatgpt.entity.embedding;

import com.plexpt.chatgpt.entity.billing.Usage;
import lombok.Data;

import java.util.List;

/**
 * 向量结果
 *
 * @author hq
 * @version 1.0
 * @date 2023/12/12
 */
@Data
public class EmbeddingResult {

    /**
     * The GPTmodel used for generating embeddings
     */
    String model;

    /**
     * The type of object returned, should be "list"
     */
    String object;

    /**
     * A list of the calculated embeddings
     */
    List<EmbeddingData> data;

    /**
     * The API usage for this request
     */
    Usage usage;
}
