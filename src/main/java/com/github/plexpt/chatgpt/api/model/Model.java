package com.github.plexpt.chatgpt.api.model;

import lombok.Data;

import java.util.ArrayList;

@Data
public class Model {
    public String slug;
    public int max_tokens;
    public String title;
    public String description;
    public ArrayList<Object> tags;
}
