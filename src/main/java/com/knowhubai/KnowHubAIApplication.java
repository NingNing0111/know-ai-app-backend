package com.knowhubai;

import org.springframework.ai.autoconfigure.openai.OpenAiAutoConfiguration;
import org.springframework.ai.autoconfigure.vectorstore.pgvector.PgVectorStoreAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Project: com.knowhubai
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/5/18 14:53
 * @Description:
 */
@SpringBootApplication(
        exclude = {OpenAiAutoConfiguration.class, PgVectorStoreAutoConfiguration.class}
)
public class KnowHubAIApplication {
    public static void main(String[] args) {
        SpringApplication.run(KnowHubAIApplication.class, args);
    }
}
