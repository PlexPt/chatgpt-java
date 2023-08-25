package com.plexpt.chatgpt;

import com.plexpt.chatgpt.entity.images.Generations;
import com.plexpt.chatgpt.entity.images.ImagesRensponse;
import com.plexpt.chatgpt.util.Proxys;

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

        Generations generations = Generations.ofURL("一只鲨鱼和一直蜜蜂结合成一种动物",1,"256x256");
        ImagesRensponse imagesRensponse = images.generations(generations);
        System.out.println(imagesRensponse.getCreated());
        System.out.println(imagesRensponse.getCode());
        System.out.println(imagesRensponse.getMsg());
        List<Object> data = imagesRensponse.getData();
        for(Object o:data){
            System.out.println(o.toString());
        }
    }
}
