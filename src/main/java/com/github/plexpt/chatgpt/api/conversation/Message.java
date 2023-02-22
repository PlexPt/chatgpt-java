package com.github.plexpt.chatgpt.api.conversation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Message {
    public String id;
    public Author author;
    public String role;
    public Object user;
    public Object create_time;
    public Object update_time;
    public Content content;
    public Object end_turn;
    public double weight;
    public Metadata metadata;
    public String recipient;
}
