package com.github.plexpt.chatgpt.api.conversations;

import lombok.Data;

import java.util.ArrayList;

@Data
public class ConversationsResponse {

    public ArrayList<Conversations> items;
    public int total;
    public int limit;
    public int offset;
}
