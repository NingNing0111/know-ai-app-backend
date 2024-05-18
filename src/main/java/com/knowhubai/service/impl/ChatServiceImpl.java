package com.knowhubai.service.impl;

import cn.hutool.core.util.StrUtil;
import com.knowhubai.common.ApplicationConstant;
import com.knowhubai.common.ErrorCode;
import com.knowhubai.exception.BusinessException;
import com.knowhubai.model.dto.ChatDTO;
import com.knowhubai.model.dto.ChatOptions;
import com.knowhubai.model.entity.AIApi;
import com.knowhubai.service.AIApiService;
import com.knowhubai.service.ChatService;
import com.knowhubai.service.KnowFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.StreamingChatClient;
import org.springframework.ai.chat.messages.*;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Project: com.ningning0111.service.impl
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/4/2 20:00
 * @Description:
 */
@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {
    private final AIApiService aiApiService;
    private final KnowFileService knowFileService;


    // 流式普通对话
    @Override
    public Flux<ChatResponse> simpleChat(ChatDTO chatRequest) {
        if (StrUtil.isBlank(chatRequest.prompt())){
            return Flux.error(new RuntimeException(String.valueOf(ErrorCode.PROMPT_ERROR)));
        }
        StreamingChatClient streamingChatClient = randomGetStreamingChatClient(chatRequest.chatOptions());
        List<Message> messages = transformAiMessage(chatRequest.messages());
        String prompt = chatRequest.prompt();
        UserMessage userMessage = new UserMessage(prompt);
        messages.add(userMessage);
        messages = checkMessageLength(messages,chatRequest);
        return streamingChatClient.stream(new Prompt(messages));
    }
    // 流式RAG对话
    @Override
    public Flux<ChatResponse> ragChat(ChatDTO chatRequest) {
        if (StrUtil.isBlank(chatRequest.prompt())){
            return Flux.error(new RuntimeException(String.valueOf(ErrorCode.PROMPT_ERROR)));
        }
        StreamingChatClient streamingChatClient = randomGetStreamingChatClient(chatRequest.chatOptions());
        String prompt = chatRequest.prompt();
        List<Message> messages = transformAiMessage(chatRequest.messages());
        messages = checkMessageLength(messages, chatRequest);
        Message systemMessage = similaritySearch(prompt);
        messages.add(0,systemMessage);
        return streamingChatClient.stream(new Prompt(messages));
    }




    // 向量数据库检索 返回系统提示信息（该信息包含了查询到的一组文档）
    private Message similaritySearch(String prompt){
        VectorStore vectorStore = knowFileService.randomGetVectorStore();
        List<Document> listOfSimilarDocuments = vectorStore.similaritySearch(prompt);
        // 将Document列表中每个元素的content内容进行拼接获得documents
        String documents = listOfSimilarDocuments.stream().map(Document::getContent).collect(Collectors.joining());
        // 使用Spring AI 提供的模板方式构建SystemMessage对象
        Message systemMessage = new SystemPromptTemplate(ApplicationConstant.SYSTEM_PROMPT).createMessage(Map.of("documents", documents));
        return systemMessage;
    }

    // 构建OpenAI流式客户端
    private StreamingChatClient randomGetStreamingChatClient(ChatOptions options){
        OpenAiApi openAiApi = randomOpenAiApi();
        return new OpenAiChatClient(openAiApi, OpenAiChatOptions
                .builder()
                .withTemperature(options.temperature())
                .withModel(options.model())
                .build());
    }

    // 构建OpenAI非流式对话客户端
    private ChatClient randomGetChatClient(ChatOptions options){
        OpenAiApi openAiApi = randomOpenAiApi();
        return new OpenAiChatClient(openAiApi,OpenAiChatOptions.builder()
                .withTemperature(options.temperature())
                .withModel(options.model())
                .build());
    }

    // 随机构建一个OpenAI的API
    private OpenAiApi randomOpenAiApi(){
        AIApi oneApi = aiApiService.randomGetOne();
        return new OpenAiApi(oneApi.getBaseUrl(), oneApi.getApiKey());
    }

    // 保证消息长度在配置的长度范围内
    private List<Message> checkMessageLength(List<Message> messages, ChatDTO chatRequest){
        if(!messages.isEmpty()&&messages.get(0).getMessageType() == MessageType.SYSTEM){
            messages.remove(0);
        }
        Integer maxMessageLength = chatRequest.chatOptions().maxHistoryLength();
        int currMessageSize = messages.size();
        if(currMessageSize > maxMessageLength){
            messages = messages.subList(currMessageSize-maxMessageLength,currMessageSize);
        }
        return messages;
    }

    // Message转换
    private List<Message> transformAiMessage(List<com.knowhubai.model.dto.Message> messages){
        List<Message> aiMessage = new ArrayList<>();
        for(com.knowhubai.model.dto.Message message: messages){
            String role = message.role();
            String content = message.content();
            MessageType aiMessageType = MessageType.fromValue(role);
            switch (aiMessageType){
                case USER -> aiMessage.add(new UserMessage(content));
                case SYSTEM -> aiMessage.add(new SystemMessage(content));
                case ASSISTANT -> aiMessage.add(new AssistantMessage(content));
                case FUNCTION -> aiMessage.add(new FunctionMessage(content));
                default -> throw new BusinessException(ErrorCode.PARAMS_ERROR,"对话列表存在未知类别:" + role);
            }
        }
        return aiMessage;
    }

}
