package com.plexpt.chatgpt.util;

import com.knuddels.jtokkit.Encodings;
import com.knuddels.jtokkit.api.Encoding;
import com.knuddels.jtokkit.api.EncodingRegistry;
import com.knuddels.jtokkit.api.EncodingType;
import com.knuddels.jtokkit.api.ModelType;
import com.plexpt.chatgpt.entity.chat.Message;
import lombok.experimental.UtilityClass;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@UtilityClass
public class TokensUtil {

    public static EncodingRegistry registry = Encodings.newDefaultEncodingRegistry();
    public static Encoding encoding = registry.getEncoding(EncodingType.CL100K_BASE);
    public static Encoding encoding4o = registry.getEncoding(EncodingType.O200K_BASE);


    /**
     * 计算text信息的tokens
     *
     * @param text
     * @return
     */
    public static int countTextTokens(String text) {
        return encoding4o.countTokens(text);
    }

    /**
     * 计算text信息的tokens
     *
     * @param text
     * @return
     */
    public static int countTextTokens(String text, String model) {
        if (StringUtils.startsWithIgnoreCase(model, ModelType.GPT_3_5_TURBO.getName())) {
            return encoding.countTokens(text);
        }

        return encoding4o.countTokens(text);
    }


    /**
     * 获取modelType
     *
     * @param name
     * @return
     */
    public static ModelType getModelTypeByName(String name) {
        Optional<ModelType> optional = ModelType.fromName(name);

        return optional.orElse(ModelType.GPT_3_5_TURBO);
    }

    /**
     * 通过模型名称计算messages获取编码数组
     * 参考官方的处理逻辑：
     * <a href=https://github.com/openai/openai-cookbook/blob/main/examples/How_to_count_tokens_with_tiktoken.ipynb>https://github.com/openai/openai-cookbook/blob/main/examples/How_to_count_tokens_with_tiktoken.ipynb</a>
     *
     * @param messages 消息体
     * @return
     */
    public static int tokens(List<Message> messages, String model) {
        if (CollectionUtils.isEmpty(messages)) {
            return 0;
        }
        Encoding encodingUsed = encoding;
        //"gpt-3.5-turbo"
        // every message follows <|start|>{role/name}\n{content}<|end|>\n
        int tokensPerMessage = 4;
        // if there's a name, the role is omitted
        int tokensPerName = -1;

        if (StringUtils.startsWithIgnoreCase(model, ModelType.GPT_4.getName())) {
            tokensPerMessage = 3;
            tokensPerName = 1;
        }
        if (StringUtils.startsWithIgnoreCase(model, ModelType.GPT_4O.getName())) {
            encodingUsed = encoding4o;
        }

        int sum = 0;
        for (final Message message : messages) {
            sum += tokensPerMessage;
            sum += encodingUsed.countTokens(message.getContent());
            sum += encodingUsed.countTokens(message.getRole());
            if (!StringUtils.isEmpty(message.getName())) {
                sum += encodingUsed.countTokens(message.getName());
                sum += tokensPerName;
            }
        }

        // every reply is primed with <|start|>assistant<|message|>
        sum += 3;

        return sum;
    }

    /**
     * 计算tokens
     *
     * @param modelName 模型名称
     * @param messages  消息列表
     * @return 计算出的tokens数量
     */

    public static int tokens(String modelName, List<Message> messages) {
        return tokens(messages, modelName);
    }
}
