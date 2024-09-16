package com.plexpt.chatgpt.entity.chat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Content
 */
@Data
@Builder
@Slf4j
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class Content {
    /**
     * 输入类型：text、image_url
     *
     * @see Type
     */
    private String type;
    private String text;
    @JsonProperty("image_url")
    private ImageUrl imageUrl;


    public interface Type {
        String TEXT = "text";
        String IMAGE_URL = "image_url";
    }

    public static Content ofText(String str) {
        Content content = new Content();
        content.setType(Type.TEXT);
        content.setText(str);
        return content;
    }

    public static Content ofImage(String url) {
        Content content = new Content();
        content.setType(Type.IMAGE_URL);
        content.setImageUrl(ImageUrl.ofURL(url));

        return content;
    }

    public static List<Content> textWithImages(String text, List<String> urls) {
        List<Content> contentList = new ArrayList<>();
        Content textContent = Content.ofText(text);
        contentList.add(textContent);

        for (String url : urls) {
            Content imageContent = Content.ofImage(url);
            contentList.add(imageContent);
        }

        return contentList;
    }
}
