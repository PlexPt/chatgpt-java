package com.github.plexpt.chatgpt.api.model;

import lombok.Data;

import java.util.List;

@Data
public class ModelResponse {
    public List<Model> models;
}
