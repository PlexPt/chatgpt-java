package com.plexpt.chatgpt.entity.audio;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 语音请求对象
 * Speech request object
 */
@Data
@Builder
@Slf4j
@AllArgsConstructor
@NoArgsConstructor(force = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SpeechRequest {

    /**
     * 模型
     * Model https://platform.openai.com/docs/models/tts
     *
     * One of the available TTS models: tts-1 or tts-1-hd
     */
    @Builder.Default
    private String model = "tts-1";

    /**
     * 输入文本
     * Input text
     * The text to generate audio for. The maximum length is 4096 characters.
     */
    private String input;

    /**
     * 声音
     * Voice
     * The voice to use when generating the audio. Supported voices are alloy, echo, fable, onyx, nova, and shimmer.
     * Previews of the voices are available in the Text to speech guide.
     *
     * https://platform.openai.com/docs/guides/text-to-speech/voice-options
     */
    @Builder.Default
    private String voice ="alloy";

    /**
     * 响应格式
     * Response format
     * The format to audio in. Supported formats are mp3, opus, aac, flac, wav, and pcm.
     */
    private String response_format;

    /**
     * 速度
     * Speed
     *
     * The speed of the generated audio. Select a value from 0.25 to 4.0. 1.0 is the default.
     */
    private Double speed;

}
