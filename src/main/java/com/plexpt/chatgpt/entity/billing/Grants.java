package com.plexpt.chatgpt.entity.billing;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

import lombok.Data;

/**
 * @author plexpt
 */
@Data
public class Grants {
    private String object;
    @JsonProperty("data")
    private List<Datum> data;

}
