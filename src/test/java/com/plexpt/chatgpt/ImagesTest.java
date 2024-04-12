package com.plexpt.chatgpt;

import com.plexpt.chatgpt.entity.images.Generations;
import com.plexpt.chatgpt.entity.images.ImagesRensponse;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * @author xiejiay (^_−)☆
 */
public class ImagesTest {

    @Test
    public void generations() {
        Images images = Images.builder()
                .apiHost("https://api.openai.com/")
                .apiKey("***")
                .build().init();
        ImagesRensponse generations = images.generations(Generations.builder()
                .prompt("黑白摄影，一位中年人手持一张空白的日历，表情深思，背景为朦胧的城市街景，光圈f/2.8，ISO 100，焦距50mm。\n" +
                        "关键词：黑白摄影、空白日历、深思表情、朦胧城市背景")
                .model(Generations.Model.DALL_E_3.getName())
                .size("1792x1024")
                .style("natural")
                .quality("hd")
                .build());
        List<String> data = generations.getUrls();
        System.out.println(data);
        Assert.assertNotNull(data);
    }
}