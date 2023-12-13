package com.plexpt.chatgpt.entity.embedding;

import lombok.Data;

import java.util.List;

/**
 * 向量
 *
 * @author hq
 * @version 1.0
 * @date 2023/12/12
 */
@Data
public class EmbeddingData {

    /**
     * The type of object returned, should be "embedding"
     */
    String object;

    /**
     * The embedding vector
     */
    List<Double> embedding;

    /**
     * The position of this embedding in the list
     */
    Integer index;
}