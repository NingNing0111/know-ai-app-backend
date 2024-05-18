package com.knowhubai.service.impl;

import com.knowhubai.service.AIApiService;
import com.knowhubai.common.BaseResponse;
import com.knowhubai.common.ResultUtils;
import com.knowhubai.model.dto.DrawImageDTO;
import com.knowhubai.model.dto.DrawOptions;
import com.knowhubai.model.entity.AIApi;
import com.knowhubai.service.DrawService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.image.ImageClient;
import org.springframework.ai.image.ImagePrompt;
import org.springframework.ai.image.ImageResponse;
import org.springframework.ai.openai.OpenAiImageClient;
import org.springframework.ai.openai.OpenAiImageOptions;
import org.springframework.ai.openai.api.OpenAiImageApi;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

/**
 * @Project: com.ningning0111.service.impl
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/4/8 21:08
 * @Description:
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class DrawServiceImpl implements DrawService {
    private final AIApiService aiApiService;
    @Override
    public BaseResponse drawImage(DrawImageDTO drawImageDTO) {
        ImageClient imageClient = imageClient();
        DrawOptions drawOptions = drawImageDTO.options();
        // 图片配置信息
        OpenAiImageOptions options = OpenAiImageOptions.builder()
                .withModel(drawOptions.model())
                .withHeight(drawOptions.height())
                .withWidth(drawOptions.width())
                .withResponseFormat(drawOptions.format())
                .build();
        // 绘制图片
        ImageResponse imageResponse = imageClient.call(new ImagePrompt(drawImageDTO.prompt(),options));
        return ResultUtils.success(imageResponse.getResults());

    }

    // 创建AI绘画客户端
    private ImageClient imageClient(){
        AIApi aiApi = aiApiService.randomGetOne();
        OpenAiImageApi openAiImageApi = new OpenAiImageApi(
                aiApi.getBaseUrl(),
                aiApi.getApiKey(),
                RestClient.builder()
        );
        return new OpenAiImageClient(openAiImageApi);
    }
}
