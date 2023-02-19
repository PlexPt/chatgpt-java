package com.github.plexpt.chatgpt.api.conversations;

import lombok.Data;

import java.util.Date;

@Data
public class Conversations {
    public String id;
    public String title;
    public Date create_time;
}
