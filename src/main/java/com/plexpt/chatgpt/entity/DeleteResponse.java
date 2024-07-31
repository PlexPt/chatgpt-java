package com.plexpt.chatgpt.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * 删除对象时的响应
 * A response when deleting an object
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeleteResponse {

    /**
     * 对象的ID
     * The id of the object.
     */
    private String id;

    /**
     * 删除的对象类型，例如 "file" 或 "model"
     * The type of object deleted, for example "file" or "model"
     */
    private String object;

    /**
     * 如果成功删除则为 true
     * True if successfully deleted
     */
    private boolean deleted;


}
