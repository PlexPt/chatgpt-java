package com.plexpt.chatgpt;

import com.plexpt.chatgpt.entity.audio.AudioResponse;
import com.plexpt.chatgpt.entity.audio.Transcriptions;
import com.plexpt.chatgpt.entity.audio.enums.AudioModel;
import com.plexpt.chatgpt.entity.chat.ChatCompletion;
import com.plexpt.chatgpt.entity.chat.ChatCompletionResponse;
import com.plexpt.chatgpt.entity.chat.Message;
import com.plexpt.chatgpt.entity.images.Generations;
import com.plexpt.chatgpt.entity.images.ImagesRensponse;
import com.plexpt.chatgpt.entity.images.Variations;
import com.plexpt.chatgpt.util.Proxys;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;

import java.io.File;
import java.net.Proxy;
import java.util.Arrays;
import java.util.List;


@Slf4j
public class Test {
    public static void main(String[] args) {

        System.out.println("test");
    }

    private ChatGPT chatGPT;

    @Before
    public void before() {
        Proxy proxy = Proxys.http("127.0.0.1", 1080);

        chatGPT = ChatGPT.builder()
                .apiKey("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")
                .timeout(900)
                .proxy(proxy)
                .apiHost("https://api.openai.com/") //代理地址
                .build()
                .init();

//        CreditGrantsResponse response = chatGPT.creditGrants();
//        log.info("余额：{}", response.getTotalAvailable());
    }

    @org.junit.Test
    public void chat() {
        Message system = Message.ofSystem("你现在是一个诗人，专门写七言绝句");
        Message message = Message.of("写一段七言绝句诗，题目是：火锅！");

        ChatCompletion chatCompletion = ChatCompletion.builder()
                .model(ChatCompletion.Model.GPT_3_5_TURBO)
                .messages(Arrays.asList(system, message))
                .maxTokens(3000)
                .temperature(0.9)
                .build();
        ChatCompletionResponse response = chatGPT.chatCompletion(chatCompletion);
        Message res = response.getChoices().get(0).getMessage();
        System.out.println(res);
    }

    @org.junit.Test
    public void img() {

        File file = new File("微信图片_20230606140621.png");
        Variations variations = Variations.ofURL(1, "256x256");
        Generations generations = Generations.ofURL("一只鲨鱼和一直蜜蜂结合成一种动物", 1, "256x256");
        ImagesRensponse imagesRensponse = chatGPT.imageVariation(file, variations);
        System.out.println(imagesRensponse.getCreated());
        System.out.println(imagesRensponse.getCode());
        System.out.println(imagesRensponse.getMsg());
        List<Object> data = imagesRensponse.getData();
        for (Object o : data) {
            System.out.println(o.toString());
        }

    }

    @org.junit.Test
    public void audio() {
        File file = new File("D:\\Jenny.mp3");
        Transcriptions transcriptions = Transcriptions.of("whisper-1", AudioModel.WHISPER1.getValue());
        AudioResponse response = chatGPT.audioTranscription(file, transcriptions);
        System.out.println(response.getText());
    }

    @org.junit.Test
    public void chatmsg() {
        String res = chatGPT.chat("写一段七言绝句诗，题目是：火锅！");
        System.out.println(res);
    }

    /**
     * 测试tokens数量计算
     */
    @org.junit.Test
    public void tokens() {
        Message system = Message.ofSystem("你现在是一个诗人，专门写七言绝句");
        Message message = Message.of("写一段七言绝句诗，题目是：火锅！");

        ChatCompletion chatCompletion1 = ChatCompletion.builder()
                .model(ChatCompletion.Model.GPT_3_5_TURBO)
                .messages(Arrays.asList(system, message))
                .maxTokens(3000)
                .temperature(0.9)
                .build();
        ChatCompletion chatCompletion2 = ChatCompletion.builder()
                .model(ChatCompletion.Model.GPT4)
                .messages(Arrays.asList(system, message))
                .maxTokens(3000)
                .temperature(0.9)
                .build();

        log.info("{} tokens: {}", chatCompletion1.getModel(), chatCompletion1.countTokens());
        log.info("{} tokens: {}", chatCompletion2.getModel(), chatCompletion2.countTokens());
    }

}
