package com.knowhubai.model.dto;

/**
 * @Project: com.ningning0111.model.dto
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/4/2 19:36
 * @Description:
 */


public record ChatOptions(String model,Integer maxHistoryLength,String chatType,Float temperature) {
    /**
     *
     * @param model LLM model. enum class LLMModels
     * @param maxHistoryLength
     * @param chatType RAG or simple
     * @param temperature
     */
}

