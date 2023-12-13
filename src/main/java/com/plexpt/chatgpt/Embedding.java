package com.plexpt.chatgpt;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import com.alibaba.fastjson.JSON;
import com.plexpt.chatgpt.api.Api;
import com.plexpt.chatgpt.entity.BaseResponse;
import com.plexpt.chatgpt.entity.embedding.EmbeddingRequest;
import com.plexpt.chatgpt.entity.embedding.EmbeddingResult;
import com.plexpt.chatgpt.exception.ChatException;
import io.reactivex.Single;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.net.Proxy;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * 向量client
 *
 * @author hq
 * @version 1.0
 * @date 2023/12/12
 */
@Slf4j
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Embedding {

    /**
     * keys
     */
    private String apiKey;

    private List<String> apiKeyList;
    /**
     * 自定义api host使用builder的方式构造client
     */
    @Builder.Default
    private String apiHost = Api.DEFAULT_API_HOST;
    private Api apiClient;
    private OkHttpClient okHttpClient;
    /**
     * 超时 默认300
     */
    @Builder.Default
    private long timeout = 300;
    /**
     * okhttp 代理
     */
    @Builder.Default
    private Proxy proxy = Proxy.NO_PROXY;


    public Embedding init() {
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        client.addInterceptor(chain -> {
            Request original = chain.request();
            String key = apiKey;
            if (apiKeyList != null && !apiKeyList.isEmpty()) {
                key = RandomUtil.randomEle(apiKeyList);
            }
            Request request = original.newBuilder()
                    .header(Header.AUTHORIZATION.getValue(), "Bearer " + key)
                    .header(Header.CONTENT_TYPE.getValue(), ContentType.JSON.getValue())
                    .method(original.method(), original.body())
                    .build();
            return chain.proceed(request);
        }).addInterceptor(chain -> {
            Request original = chain.request();
            Response response = chain.proceed(original);
            if (!response.isSuccessful()) {
                String errorMsg = response.body().string();
                log.error("请求异常：{}", errorMsg);
                BaseResponse baseResponse = JSON.parseObject(errorMsg, BaseResponse.class);
                if (Objects.nonNull(baseResponse.getError())) {
                    log.error(baseResponse.getError().getMessage());
                    throw new ChatException(baseResponse.getError().getMessage());
                }
                throw new ChatException("error");
            }
            return response;
        });

        client.connectTimeout(timeout, TimeUnit.SECONDS);
        client.writeTimeout(timeout, TimeUnit.SECONDS);
        client.readTimeout(timeout, TimeUnit.SECONDS);
        if (Objects.nonNull(proxy)) {
            client.proxy(proxy);
        }
        this.okHttpClient = client.build();
        this.apiClient = new Retrofit.Builder()
                .baseUrl(this.apiHost)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create())
                .build()
                .create(Api.class);
        return this;
    }


    /**
     * 生成向量
     */
    public EmbeddingResult createEmbeddings(EmbeddingRequest request) {
        Single<EmbeddingResult> embeddingResultSingle = this.apiClient.createEmbeddings(request);
        return embeddingResultSingle.blockingGet();
    }


    /**
     * 生成向量
     */
    public EmbeddingResult createEmbeddings(String input, String user) {
        EmbeddingRequest request = EmbeddingRequest.builder()
                .input(Collections.singletonList(input))
                .model(EmbeddingRequest.EmbeddingModelEnum.TEXT_EMBEDDING_ADA_002.getModelName())
                .user(user)
                .build();
        Single<EmbeddingResult> embeddingResultSingle = this.apiClient.createEmbeddings(request);
        return embeddingResultSingle.blockingGet();
    }

}
