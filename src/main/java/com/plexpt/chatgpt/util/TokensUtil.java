package com.plexpt.chatgpt.util;

import cn.hutool.core.util.StrUtil;
import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.EncodingRegistry;
import com.plexpt.chatgpt.entity.chat.ChatCompletion;
import com.plexpt.chatgpt.entity.chat.Message;
import lombok.experimental.UtilityClass;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@UtilityClass
public class TokensUtil {

    private static final Map<String, Encoding> modelEncodingMap = new HashMap<>();
    private static final EncodingRegistry encodingRegistry = Encodings.newDefaultEncodingRegistry();

    static {
        for (ChatCompletion.Model model : ChatCompletion.Model.values()) {
            Optional<Encoding> encodingForModel = encodingRegistry.getEncodingForModel(model.getName());
            encodingForModel.ifPresent(encoding -> modelEncodingMap.put(model.getName(), encoding));
        }
    }

    /**
     * 计算tokens
     * @param modelName 模型名称
     * @param messages 消息列表
     * @return 计算出的tokens数量
     */

    public static int tokens(String modelName, List<Message> messages) {
        Encoding encoding = modelEncodingMap.get(modelName);
        if (encoding == null) {
            throw new IllegalArgumentException("Unsupported model: " + modelName);
        }

        int tokensPerMessage = 0;
        int tokensPerName = 0;
        if (modelName.startsWith("gpt-4")) {
            tokensPerMessage = 3;
            tokensPerName = 1;
        } else if (modelName.startsWith("gpt-3.5-turbo")) {
            tokensPerMessage = 4; // every message follows <|start|>{role/name}\n{content}<|end|>\n
            tokensPerName = -1; // if there's a name, the role is omitted
        }
        int sum = 0;
        for (Message message : messages) {
            sum += tokensPerMessage;
            sum += encoding.countTokens(message.getContent());
            sum += encoding.countTokens(message.getRole());
            if (StrUtil.isNotBlank(message.getName())) {
                sum += encoding.countTokens(message.getName());
                sum += tokensPerName;
            }
        }
        sum += 3;
        return sum;
    }
}