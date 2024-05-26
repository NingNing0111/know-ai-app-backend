package com.knowhubai.common;

/**
 * @Project: com.ningning0111.common
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/4/2 17:42
 * @Description:
 */
public class ApplicationConstant {
    public final static String API_VERSION = "/api/v1";
    public final static String APPLICATION_NAME = "know-hub-ai";

    public final static String DEFAULT_BASE_URL = "https://api.openai.com";
    public final static String SYSTEM_PROMPT = """
                Context information is below.
                ---------------------
                {context}
                ---------------------
                Given the context information and not prior knowledge, answer the question.
                You need to respond with content in context first, and then respond with your own database. When the given context doesn't help you answer the question, just say "I don't know."
                        
                Question: {question}
                Answer:
                        
            """;
}
