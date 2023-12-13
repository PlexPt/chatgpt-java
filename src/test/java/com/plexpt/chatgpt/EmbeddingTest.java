package com.plexpt.chatgpt;

import com.plexpt.chatgpt.entity.embedding.EmbeddingResult;
import org.junit.Before;
import org.junit.Test;

/**
 * @author hq
 * @version 1.0
 * @date 2023/12/13
 */
public class EmbeddingTest {

    private Embedding embedding;

    @Before
    public void before() {
        //Proxy proxy = Proxys.http("127.0.0.1", 1080);

        embedding = Embedding.builder()
                .apiKey("sk-6MeitSJhboJdWhGJLZTaH1T3BlbkFJdmbrrY7dgAnucJo6Arn7G")
                .timeout(900)
                //.proxy(proxy)
                .apiHost("https://api.openai.com/") //代理地址
                .build()
                .init();
    }
    
    
    @Test
    public void setEmbedding(){
        EmbeddingResult embeddingResult = embedding.createEmbeddings("123445", "user1");
        System.out.println(embeddingResult);
    }
}
