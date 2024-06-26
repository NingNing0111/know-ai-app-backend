package com.knowhubai.model.dto;

/**
 * @Project: com.ningning0111.model.dto
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/4/8 21:17
 * @Description:
 */

public record DrawOptions(String model,Integer width, Integer height,String format) {
    /**
     * @param model AI绘图模型 dall-e-3或dall-e-2（默认）
     * @param width 图片宽度 dall-e-2(256、512或1024)、dall-e-3(1024, 1792)
     * @param height 图片高度 dall-e-2(256、512或1024)、dall-e-3(1024、1792)
     *               dall-e-2支持width * height，且width==height 的大小
     *               dall-e-3支持width * height,且width=1792 & width != height 的大小
     * @param format 响应的格式 url或b64_json
     *
     */
}
