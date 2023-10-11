package com.plexpt.chatgpt;

import com.plexpt.chatgpt.entity.audio.AudioResponse;
import com.plexpt.chatgpt.entity.audio.Transcriptions;
import com.plexpt.chatgpt.entity.audio.enums.AudioModel;
import com.plexpt.chatgpt.entity.images.Generations;
import com.plexpt.chatgpt.entity.images.ImagesRensponse;
import com.plexpt.chatgpt.entity.images.Variations;
import com.plexpt.chatgpt.util.Proxys;

import java.io.File;
import java.net.Proxy;
import java.util.List;

/**
 * @Author matoooo
 * @Date 2023/8/25 14:48
 * @Description: TODO
 */
public class Test {
    public static void main(String[] args) {
        Proxy proxys = Proxys.http("127.0.0.1",10809);
        Images images = Images.builder()
                .proxy(proxys)
                .apiKey("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")
                .apiHost("https://api.openai.com/")
                .timeout(900)
                .build()
                .init();

        File file = new File("C:\\Users\\马同徽\\Pictures\\微信图片_20230606140621.png");
        Variations variations = Variations.ofURL(1,"256x256");
        Generations generations = Generations.ofURL("一只鲨鱼和一直蜜蜂结合成一种动物",1,"256x256");
        ImagesRensponse imagesRensponse = images.variations(file,variations);
        System.out.println(imagesRensponse.getCreated());
        System.out.println(imagesRensponse.getCode());
        System.out.println(imagesRensponse.getMsg());
        List<Object> data = imagesRensponse.getData();
        for(Object o:data){
            System.out.println(o.toString());
        }
        /*Audio audio = Audio.builder()
                .proxy(proxys)
                .apiKey("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")
                .apiHost("https://api.openai.com/")
                .timeout(900)
                .build()
                .init();
        File file = new File("D:\\Jenny.mp3");
        Transcriptions transcriptions = Transcriptions.of(file, AudioModel.WHISPER1.getValue());
        AudioResponse response = audio.transcriptions(transcriptions);
        System.out.println(response.getText());*/
    }
}
