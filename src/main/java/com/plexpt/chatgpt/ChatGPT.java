package com.plexpt.chatgpt;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import com.plexpt.chatgpt.api.Api;
import com.plexpt.chatgpt.entity.BaseResponse;
import com.plexpt.chatgpt.entity.DeleteResponse;
import com.plexpt.chatgpt.entity.FileResponse;
import com.plexpt.chatgpt.entity.audio.AudioResponse;
import com.plexpt.chatgpt.entity.audio.SpeechRequest;
import com.plexpt.chatgpt.entity.audio.Transcriptions;
import com.plexpt.chatgpt.entity.billing.CreditGrantsResponse;
import com.plexpt.chatgpt.entity.billing.SubscriptionData;
import com.plexpt.chatgpt.entity.billing.UseageResponse;
import com.plexpt.chatgpt.entity.chat.ChatCompletion;
import com.plexpt.chatgpt.entity.chat.ChatCompletionResponse;
import com.plexpt.chatgpt.entity.chat.Message;
import com.plexpt.chatgpt.entity.embedding.EmbeddingRequest;
import com.plexpt.chatgpt.entity.embedding.EmbeddingResult;
import com.plexpt.chatgpt.entity.images.Edits;
import com.plexpt.chatgpt.entity.images.Generations;
import com.plexpt.chatgpt.entity.images.ImagesRensponse;
import com.plexpt.chatgpt.entity.images.Variations;
import com.plexpt.chatgpt.exception.ChatException;
import com.plexpt.chatgpt.util.fastjson.JSON;
import io.reactivex.Single;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.File;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.Proxy;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.plexpt.chatgpt.util.FormatDateUtil.formatDate;


/**
 * open ai 客户端
 *
 * @author plexpt
 */

@Slf4j
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatGPT {
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


    /**
     * 初始化：与服务端建立连接，成功后可直接与服务端进行对话
     */
    public ChatGPT init() {
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
                throw new ChatException("ChatGPT init error!");
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
     * 最新版的GPT-3.5 chat completion 更加贴近官方网站的问答模型
     *
     * @param chatCompletion 问答参数，即咨询的内容
     * @return 服务端的问答响应
     */
    public ChatCompletionResponse chatCompletion(ChatCompletion chatCompletion) {
        Single<ChatCompletionResponse> chatCompletionResponse =
                this.apiClient.chatCompletion(chatCompletion);
        return chatCompletionResponse.blockingGet();
    }

    /**
     * 支持多个问答参数来与服务端进行对话
     *
     * @param messages 问答参数，即咨询的内容
     * @return 服务端的问答响应
     */
    public ChatCompletionResponse chatCompletion(List<Message> messages) {
        ChatCompletion chatCompletion = ChatCompletion.builder().messages(messages).build();
        return this.chatCompletion(chatCompletion);
    }

    /**
     * 与服务端进行对话
     *
     * @param message 问答参数，即咨询的内容
     * @return 服务端的问答响应
     */
    public String chat(String message) {
        ChatCompletion chatCompletion = ChatCompletion.builder()
                .messages(Arrays.asList(Message.of(message)))
                .build();
        ChatCompletionResponse response = this.chatCompletion(chatCompletion);
        return response.getChoices().get(0).getMessage().getContent();
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


    public ImagesRensponse imageGeneration(Generations generations) {
        Single<ImagesRensponse> imagesRensponse = this.apiClient.imageGenerations(generations);
        return imagesRensponse.blockingGet();
    }

    public ImagesRensponse imageEdit(File image, File mask, Edits edits) {
        RequestBody i = RequestBody.create(MediaType.parse("multipart/form-data;charset=UTF-8"), image);
        MultipartBody.Part iPart = MultipartBody.Part.createFormData("image", image.getName(), i);

        RequestBody m = RequestBody.create(MediaType.parse("multipart/form-data;charset=UTF-8"), mask);
        MultipartBody.Part mPart = MultipartBody.Part.createFormData("mask", mask.getName(), m);

        Single<ImagesRensponse> imagesRensponse =
                this.apiClient.imageEdits(iPart, mPart, edits);
        return imagesRensponse.blockingGet();
    }

    public ImagesRensponse imageVariation(File image, Variations variations) {
        RequestBody i = RequestBody.create(MediaType.parse("multipart/form-data;charset=UTF-8"), image);
        MultipartBody.Part iPart = MultipartBody.Part.createFormData("image", image.getName(), i);
        Single<ImagesRensponse> imagesRensponse =
                this.apiClient.imageVariations(iPart, variations);
        return imagesRensponse.blockingGet();
    }


    public AudioResponse audioTranscription(File audio, Transcriptions transcriptions) {
        RequestBody a = RequestBody.create(MediaType.parse("multipart/form-data;charset=UTF-8"), audio);
        MultipartBody.Part aPart = MultipartBody.Part.createFormData("image", audio.getName(), a);
        Single<AudioResponse> audioResponse =
                this.apiClient.audioTranscriptions(aPart, transcriptions);
        return audioResponse.blockingGet();
    }

    public InputStream audioSpeech(SpeechRequest speechRequest) {
        Single<ResponseBody> audioResponse = this.apiClient.audioSpeech(speechRequest);
        ResponseBody response = audioResponse.blockingGet();
        InputStream stream = response.byteStream();
        return stream;
    }

    public AudioResponse audioTranslation(File audio, Transcriptions transcriptions) {
        RequestBody a = RequestBody.create(MediaType.parse("multipart/form-data;charset=UTF-8"), audio);
        MultipartBody.Part aPart = MultipartBody.Part.createFormData("image", audio.getName(), a);
        Single<AudioResponse> audioResponse =
                this.apiClient.audioTranslations(aPart, transcriptions);
        return audioResponse.blockingGet();
    }

    /**
     * 余额查询
     *
     * @return 余额总金额
     */
    @Deprecated
    public BigDecimal balance() {
        Single<SubscriptionData> subscription = apiClient.subscription();
        SubscriptionData subscriptionData = subscription.blockingGet();
        BigDecimal total = subscriptionData.getHardLimitUsd();
        DateTime start = DateUtil.offsetDay(new Date(), -90);
        DateTime end = DateUtil.offsetDay(new Date(), 1);

        Single<UseageResponse> usage = apiClient.usage(formatDate(start), formatDate(end));
        UseageResponse useageResponse = usage.blockingGet();
        BigDecimal used = useageResponse.getTotalUsage().divide(BigDecimal.valueOf(100));

        return total.subtract(used);
    }

    /**
     * 新建连接进行余额查询
     *
     * @return 余额总金额
     */
    @Deprecated
    public static BigDecimal balance(String key) {
        ChatGPT chatGPT = ChatGPT.builder()
                .apiKey(key)
                .build()
                .init();

        return chatGPT.balance();
    }

    /**
     * 余额查询
     *
     * @return 余额总金额及明细
     */
    @Deprecated
    public CreditGrantsResponse creditGrants() {
        Single<CreditGrantsResponse> creditGrants = this.apiClient.creditGrants();
        return creditGrants.blockingGet();
    }

    /**
     * 列出文件
     * List files
     */
    public BaseResponse<FileResponse> listFiles() {
        Single<BaseResponse<FileResponse>> files = this.apiClient.listFiles();
        return files.blockingGet();
    }

    /**
     * 上传文件
     * Upload file
     *
     * @param purpose 文件用途
     *                The purpose of the file
     * @param file    文件部分
     *                The file part
     * @return 文件响应
     *         File response
     */
    public FileResponse uploadFile(String purpose, MultipartBody.Part file) {
        RequestBody purposeBody = RequestBody.create(MultipartBody.FORM, purpose);
        Single<FileResponse> files = this.apiClient.uploadFile(purposeBody, file);
        return files.blockingGet();
    }

    /**
     * 删除文件
     * Delete file
     *
     * @param fileId 文件ID
     *               The file ID
     * @return 删除响应
     *         Delete response
     */
    public DeleteResponse deleteFile(String fileId) {
        return this.apiClient.deleteFile(fileId).blockingGet();
    }

    /**
     * 检索文件
     * Retrieve file
     *
     * @param fileId 文件ID
     *               The file ID
     * @return 文件响应
     *         File response
     */
    public FileResponse retrieveFile(String fileId) {
        return this.apiClient.retrieveFile(fileId).blockingGet();
    }

    /**
     * 检索文件内容
     * Retrieve file content
     *
     * @param fileId 文件ID
     *               The file ID
     * @return 文件内容响应
     *         File content response
     */
    public ResponseBody retrieveFileContent(String fileId) {
        return this.apiClient.retrieveFileContent(fileId).blockingGet();
    }

}
