package com.knowhubai.service.impl;

import cn.hutool.core.util.StrUtil;
import com.knowhubai.common.ApplicationConstant;
import com.knowhubai.common.BaseResponse;
import com.knowhubai.common.ErrorCode;
import com.knowhubai.common.ResultUtils;
import com.knowhubai.exception.BusinessException;
import com.knowhubai.model.dto.AddApiDTO;
import com.knowhubai.model.dto.OneApiDTO;
import com.knowhubai.model.entity.AIApi;
import com.knowhubai.repository.AIApiRepository;
import com.knowhubai.service.AIApiService;
import groovy.util.logging.Slf4j;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Vector;

/**
 * @Project: com.ningning0111.service
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/4/2 17:12
 * @Description:
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AIApiServiceImpl implements AIApiService {
    @Value("${spring.ai.openai.base-url}")
    private String defaultBaseUrl = ApplicationConstant.DEFAULT_BASE_URL;
    @Value("${spring.ai.openai.api-key}")
    private String defaultApiKey;
    // 基于内存存储所有可用的key，减少每次对话对数据库的操作
    private static List<AIApi> apiList = new Vector<>();
    private final AIApiRepository oneApiRepository;

    /**
     * 从数据库中加载api信息到内存中
     */
    @PostConstruct
    @Override
    public void reloadOneApiInfo() {
        List<AIApi> findList = oneApiRepository.findAllByDisableIsFalse();
        // 如果数据库内没有数据，则加载配置的api
        if(findList.isEmpty()){
            long currMillis = System.currentTimeMillis();
            AIApi oneApi = oneApiRepository.save(AIApi.builder()
                    .apiKey(defaultApiKey)
                    .disable(false)
                    .baseUrl(defaultBaseUrl)
                    .createTime(new Date(currMillis))
                    .updateTime(new Date(currMillis))
                    .build());
            apiList.add(oneApi);
        }else{
            apiList.clear();
            apiList.addAll(findList);
        }
    }

    // 随机获取一个可用的api
    @Override
    public AIApi randomGetOne() {
        int size = apiList.size();
        int randIndex = new Random().nextInt(size);
        return apiList.get(randIndex);
    }


    /**
     * 添加api
     * @param request
     * @return
     */
    @Transactional(rollbackOn = Exception.class)
    @Override
    public BaseResponse addOneApi(AddApiDTO request) {
        try {
            if (StrUtil.isBlank(request.apiKey())){
                return ResultUtils.error(ErrorCode.APIKEY_ERROR);
            }
            long currMillis = System.currentTimeMillis();
            AIApi oneApi = AIApi.builder()
                    .apiKey(request.apiKey())
                    .baseUrl(request.baseUrl())
                    .describe(request.describe())
                    .createTime(new Date(currMillis))
                    .updateTime(new Date(currMillis))
                    .disable(false)
                    .build();
            oneApiRepository.save(oneApi);
            reloadOneApiInfo();
            return ResultUtils.success("添加成功");
        }catch (Exception e){
            throw new BusinessException(ErrorCode.OPERATION_ERROR,e.getMessage());
        }
    }

    @Override
    public BaseResponse selectApi(PageRequest pageRequest) {

        List<AIApi> oneApis = oneApiRepository.findAllByDisableIsFalse(pageRequest);
        return ResultUtils.success(oneApis);
    }

    @Override
    public BaseResponse changeApi(Long id) {
        AIApi oneApi= oneApiRepository.getReferenceById(id);
        if (oneApi.getId() == null){
            return ResultUtils.error(ErrorCode.NOT_FOUND_ERROR);
        }
        oneApi.setDisable(!oneApi.getDisable());
        oneApi.setUpdateTime(new Date(System.currentTimeMillis()));
        try{
            oneApiRepository.save(oneApi);
            return ResultUtils.success("更新成功");
        }catch (Exception e){
            throw new BusinessException(ErrorCode.UPDATE_ERROR,e.getMessage());
        }
    }

    @Override
    public BaseResponse selectById(Long id) {
        AIApi oneApi= oneApiRepository.getReferenceById(id);
        return ResultUtils.success(oneApi);
    }

    @Override
    public BaseResponse deleteById(Long id) {
        AIApi oneApi= oneApiRepository.getReferenceById(id);
        if (oneApi.getDisable()){
            return ResultUtils.error(ErrorCode.DELETE_ERROR);
        }
        apiList.remove(oneApi);
        oneApiRepository.deleteById(id);
        return ResultUtils.success("删除成功");
    }

    @Override
    public BaseResponse deleteByIds(List<Long> ids) {
        try{
            List<AIApi> oneApis = oneApiRepository.findAllByIdIn(ids);
            for(AIApi oneApi : oneApis){
                apiList.remove(oneApi);
            }
            oneApiRepository.deleteByIdIn(ids);
            return ResultUtils.success("删除成功");
        } catch (Exception e){
            return ResultUtils.error(ErrorCode.DELETE_ERROR);
        }
    }

    @Override
    public BaseResponse change(OneApiDTO oneApiDTO) {
        AIApi oneApi = oneApiRepository.getReferenceById(oneApiDTO.id());
        oneApi.setApiKey(oneApi.getApiKey());
        oneApi.setBaseUrl(oneApiDTO.baseUrl());
        oneApi.setDisable(oneApiDTO.disable());
        oneApi.setUpdateTime(new Date(System.currentTimeMillis()));
        oneApi.setDescribe(oneApiDTO.describe());
        oneApiRepository.save(oneApi);
        return ResultUtils.success(oneApiDTO);
    }

}
