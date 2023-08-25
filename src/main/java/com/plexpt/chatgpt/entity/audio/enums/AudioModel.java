package com.plexpt.chatgpt.entity.audio.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AudioModel {
    WHISPER1("whisper-1"),
    ;
    private String value;
}
