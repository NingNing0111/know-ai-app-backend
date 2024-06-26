package com.knowhubai.service;

import com.knowhubai.common.BaseResponse;
import com.knowhubai.model.dto.AddApiDTO;
import com.knowhubai.model.dto.OneApiDTO;
import com.knowhubai.model.entity.AIApi;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * @Project: com.ningning0111.service
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/4/2 17:09
 * @Description:
 */
public interface AIApiService {
    /**
     * 加载OneApi列表
     */
    void reloadOneApiInfo();

    AIApi randomGetOne();



    BaseResponse addOneApi(AddApiDTO request);

    BaseResponse selectApi(PageRequest pageRequest);

    BaseResponse changeApi(Long id);

    BaseResponse selectById(Long id);

    BaseResponse deleteById(Long id);

    BaseResponse deleteByIds(List<Long> ids);

    BaseResponse change(OneApiDTO oneApiDTO);
}
