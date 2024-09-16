package com.plexpt.chatgpt.entity.chat;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Data
@Builder
@Slf4j
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class ImageUrl {

    private String url;

    private String detail;


    public static ImageUrl ofURL(String imageURL) {
        ImageUrl image = new ImageUrl();
        image.setUrl(imageURL);
        return image;
    }
}
