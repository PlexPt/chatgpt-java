package com.github.plexpt.chatgpt.api.conversation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Content {
    public String content_type;
    public ArrayList<String> parts;
}
