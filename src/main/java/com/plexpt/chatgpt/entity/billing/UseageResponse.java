package com.plexpt.chatgpt.entity.billing;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

import lombok.Data;

/**
 * 余额查询接口返回值
 *
 * @author plexpt
 */
@Data
public class UseageResponse {

    /**
     * 总使用金额：美元
     */
    @JsonProperty("total_usage")
    private BigDecimal totalUsage;

}
