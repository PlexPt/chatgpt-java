package com.plexpt.chatgpt.api;

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
import com.plexpt.chatgpt.entity.embedding.EmbeddingRequest;
import com.plexpt.chatgpt.entity.embedding.EmbeddingResult;
import com.plexpt.chatgpt.entity.images.Edits;
import com.plexpt.chatgpt.entity.images.Generations;
import com.plexpt.chatgpt.entity.images.ImagesRensponse;
import com.plexpt.chatgpt.entity.images.Variations;
import io.reactivex.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.*;

import java.io.File;

/**
 * API接口
 * API interface
 */
public interface Api {

    String DEFAULT_API_HOST = "https://api.openai.com/";

    /**
     * 聊天
     * Chat
     */
    @POST("v1/chat/completions")
    Single<ChatCompletionResponse> chatCompletion(@Body ChatCompletion chatCompletion);

    /**
     * 图像生成
     * Image generations
     */
    @POST("v1/images/generations")
    Single<ImagesRensponse> imageGenerations(@Body Generations generations);

    /**
     * 图像编辑
     * Image edits
     */
    @Multipart
    @POST("v1/images/edits")
    Single<ImagesRensponse> imageEdits(@Part() MultipartBody.Part image,
                                       @Part() MultipartBody.Part mask,
                                       @PartMap Edits edits);

    /**
     * 图像变体
     * Image variations
     */
    @Multipart
    @POST("v1/images/variations")
    Single<ImagesRensponse> imageVariations(@Part() MultipartBody.Part image,
                                            @PartMap Variations variations);

    /**
     * 生成语音
     * Create speech
     */
    @POST("v1/audio/speech")
    Single<ResponseBody> audioSpeech(@Body SpeechRequest speechRequest);

    /**
     * 音频转录
     * Audio transcriptions
     */
    @Multipart
    @POST("v1/audio/transcriptions")
    Single<AudioResponse> audioTranscriptions(@Part() MultipartBody.Part audio,
                                              @PartMap Transcriptions transcriptions);

    /**
     * 音频翻译
     * Audio translations
     */
    @Multipart
    @POST("v1/audio/translations")
    Single<AudioResponse> audioTranslations(@Part() MultipartBody.Part audio,
                                            @PartMap Transcriptions transcriptions);

    /**
     * 余额查询
     * Credit grants query
     */
    @GET("dashboard/billing/credit_grants")
    Single<CreditGrantsResponse> creditGrants();

    /**
     * 订阅查询
     * Subscription query
     */
    @GET("v1/dashboard/billing/subscription")
    Single<SubscriptionData> subscription();

    /**
     * 使用情况查询
     * Usage query
     */
    @GET("v1/dashboard/billing/usage")
    Single<UseageResponse> usage(@Query("start_date") String startDate,
                                 @Query("end_date") String endDate);

    /**
     * 生成向量
     * Create embeddings
     */
    @POST("v1/embeddings")
    Single<EmbeddingResult> createEmbeddings(@Body EmbeddingRequest request);

    /**
     * 列出文件
     * List files
     */
    @GET("/v1/files")
    Single<BaseResponse<FileResponse>> listFiles();

    /**
     * 上传文件
     * Upload file
     */
    @Multipart
    @POST("/v1/files")
    Single<FileResponse> uploadFile(@Part("purpose") RequestBody purpose, @Part MultipartBody.Part file);

    /**
     * 删除文件
     * Delete file
     */
    @DELETE("/v1/files/{file_id}")
    Single<DeleteResponse> deleteFile(@Path("file_id") String fileId);

    /**
     * 检索文件
     * Retrieve file
     */
    @GET("/v1/files/{file_id}")
    Single<FileResponse> retrieveFile(@Path("file_id") String fileId);

    /**
     * 检索文件内容
     * Retrieve file content
     */
    @Streaming
    @GET("/v1/files/{file_id}/content")
    Single<ResponseBody> retrieveFileContent(@Path("file_id") String fileId);

}
