package com.knowhubai.controller;

import com.knowhubai.common.ApplicationConstant;
import com.knowhubai.common.ChatType;
import com.knowhubai.common.ErrorCode;
import com.knowhubai.exception.BusinessException;
import com.knowhubai.model.dto.ChatDTO;
import com.knowhubai.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.Objects;


/**
 * @Project: com.ningning0111.controller
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/4/2 16:38
 * @Description:
 */
@Tag(name = "ChatController", description = "对话接口")
@RestController
@RequiredArgsConstructor
@RequestMapping(ApplicationConstant.API_VERSION + "/chat")
@Slf4j
public class ChatController {
    private final ChatService chatService;

    @Operation(summary = "stream", description = "流式对话接口")
    @PostMapping(value = "/stream")
    public Flux<ChatResponse> streamRagChat(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestBody ChatDTO chatRequest
    ) {
        String chatTypeStr = chatRequest.chatOptions().chatType();
        ChatType chatType = ChatType.getChatType(chatTypeStr);
        switch (Objects.requireNonNull(chatType)) {
            case RAG -> {
                return chatService.ragChat(chatRequest).flatMapSequential(Flux::just);
            }
            case SIMPLE -> {
                return chatService.simpleChat(chatRequest).flatMapSequential(Flux::just);
            }
            default -> throw new BusinessException(ErrorCode.PARAMS_ERROR, "暂不支持[" + chatTypeStr + "]对话");
        }
    }


}
