package com.plexpt.chatgpt.entity.billing;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class SubscriptionData {

    /**
     * 赠送金额：美元
     */
    @JsonProperty("hard_limit_usd")
    private BigDecimal hardLimitUsd;
}
