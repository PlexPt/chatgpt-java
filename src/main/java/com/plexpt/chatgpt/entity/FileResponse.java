package com.plexpt.chatgpt.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 上传到 OpenAI 的文件
 * A file uploaded to OpenAI
 * <p>
 * https://beta.openai.com/docs/api-reference/files
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class FileResponse {

    /**
     * 文件的唯一ID
     * The unique id of this file.
     */
    private String id;

    /**
     * 返回的对象类型，应为 "file"
     * The type of object returned, should be "file".
     */
    private String object;

    /**
     * 文件大小（以字节为单位）
     * File size in bytes.
     */
    private Long bytes;

    /**
     * 创建时间（以秒为单位的纪元时间）
     * The creation time in epoch seconds.
     */
    @JsonProperty("created_at")
    private Long createdAt;

    /**
     * 文件名
     * The name of the file.
     */
    private String filename;

    /**
     * 文件用途的描述
     * Description of the file's purpose.
     */
    private String purpose;

    /**
     * 文件的当前状态，可以是 uploaded, processed, pending, error, deleting 或 deleted
     * The current status of the file, which can be either uploaded, processed, pending, error, deleting or deleted.
     */
    private String status;

    /**
     * 文件状态的附加详细信息
     * 如果文件处于错误状态，这将包括描述错误的消息
     * Additional details about the status of the file.
     * If the file is in the error state, this will include a message describing the error.
     */
    @JsonProperty("status_details")
    private String statusDetails;


}
