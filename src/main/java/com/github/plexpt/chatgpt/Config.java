package com.github.plexpt.chatgpt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Config {
    private String email;
    private String password;
    private String userAgent;
    private String cfClearance;
    private String session_token;
}
