package com.github.plexpt.chatgpt.api.conversation;

import lombok.Data;

@Data
public class Author {
    public String role;
    public Object name;
    public Metadata metadata;

}
