package com.plexpt.chatgpt;

import com.alibaba.fastjson.JSON;
import com.plexpt.chatgpt.api.Api;
import com.plexpt.chatgpt.entity.BaseResponse;
import com.plexpt.chatgpt.entity.billing.CreditGrantsResponse;
import com.plexpt.chatgpt.entity.billing.SubscriptionData;
import com.plexpt.chatgpt.entity.billing.UseageResponse;
import com.plexpt.chatgpt.entity.chat.ChatCompletion;
import com.plexpt.chatgpt.entity.chat.ChatCompletionResponse;
import com.plexpt.chatgpt.entity.chat.Message;
import com.plexpt.chatgpt.exception.ChatException;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.Proxy;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.Header;
import com.sun.istack.internal.NotNull;
import io.reactivex.Single;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;


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
     * 请求失败重试次数 默认3,如果为0则不做重试操作
     */
    @Builder.Default
    private int rentryTimes = 3;

    /**
     * 初始化
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
        }).addInterceptor(new ChatGPT.OkhttpInterceptor(rentryTimes));

        client.connectTimeout(timeout, TimeUnit.SECONDS);
        client.writeTimeout(timeout, TimeUnit.SECONDS);
        client.readTimeout(timeout, TimeUnit.SECONDS);
        if (Objects.nonNull(proxy)) {
            client.proxy(proxy);
        }
        OkHttpClient httpClient = client.build();
        this.okHttpClient = httpClient;


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
     * @param chatCompletion 问答参数
     * @return 答案
     */
    public ChatCompletionResponse chatCompletion(ChatCompletion chatCompletion) {
        Single<ChatCompletionResponse> chatCompletionResponse =
                this.apiClient.chatCompletion(chatCompletion);
        return chatCompletionResponse.blockingGet();
    }

    /**
     * 简易版
     *
     * @param messages 问答参数
     */
    public ChatCompletionResponse chatCompletion(List<Message> messages) {
        ChatCompletion chatCompletion = ChatCompletion.builder().messages(messages).build();
        return this.chatCompletion(chatCompletion);
    }

    /**
     * 直接问
     */
    public String chat(String message) {
        ChatCompletion chatCompletion = ChatCompletion.builder()
                .messages(Arrays.asList(Message.of(message)))
                .build();
        ChatCompletionResponse response = this.chatCompletion(chatCompletion);
        return response.getChoices().get(0).getMessage().getContent();
    }

    /**
     * 余额查询
     *
     * @return
     */
    public CreditGrantsResponse creditGrants() {
        Single<CreditGrantsResponse> creditGrants = this.apiClient.creditGrants();
        return creditGrants.blockingGet();
    }


    /**
     * 余额查询
     *
     * @return
     */
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
     * 余额查询
     *
     * @return
     */
    public static BigDecimal balance(String key) {
        ChatGPT chatGPT = ChatGPT.builder()
                .apiKey(key)
                .build()
                .init();

        return chatGPT.balance();
    }

    public static String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(date);
    }


    /**
     * 考虑梯子稳定性 以及 openai官方请求错误，设置请求重试
     * 在多线程批量任务中可以使用
     *
     * @author zhuzi
     */
    public static class OkhttpInterceptor implements Interceptor {
        // 最大重试次数
        private int maxRentry;

        public OkhttpInterceptor(int maxRentry) {
            this.maxRentry = maxRentry;
        }

        @NotNull
        @Override
        public Response intercept(@NotNull Chain chain) {
            /* 递归 2次下发请求，如果仍然失败 则返回 null ,但是 intercept must not return null.
             * 返回 null 会报 IllegalStateException 异常
             * */
            return retry(chain, 0);
        }

        Response retry(Chain chain, int retryCent) {
            Request original = chain.request();
            Response response = null;

            //次数配置重试
            try {
                response = chain.proceed(original);
            } catch (Exception e) {
                log.error("请求异常:"+e.getMessage());
//                if (maxRentry > retryCent&&!response.isSuccessful()) {
//                    response = retry(chain, retryCent + 1);
//                }
                throw new ChatException("error");
            }
            if (!response.isSuccessful()) {
                if (maxRentry > retryCent) {
                    retry(chain, retryCent + 1);
                }else{
                    String errorMsg = null;
                    try {
                        errorMsg = response.body().string();
                    } catch (IOException e) {}
                    log.error("请求返回异常：{}", errorMsg+",重试次数："+retryCent);
                    BaseResponse baseResponse = JSON.parseObject(errorMsg, BaseResponse.class);
                    if (Objects.nonNull(baseResponse.getError())) {
                        log.error(baseResponse.getError().getMessage());
                        throw new ChatException(baseResponse.getError().getMessage());
                    }
                    throw new ChatException("error");
                }


            }
            return response;

        }
    }


}
