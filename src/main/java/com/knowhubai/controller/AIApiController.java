package com.knowhubai.controller;

import com.knowhubai.common.ApplicationConstant;
import com.knowhubai.common.BaseResponse;
import com.knowhubai.common.ErrorCode;
import com.knowhubai.common.ResultUtils;
import com.knowhubai.model.dto.AddApiDTO;
import com.knowhubai.model.dto.OneApiDTO;
import com.knowhubai.model.dto.QueryApiDTO;
import com.knowhubai.service.AIApiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Project: com.ningning0111.controller
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/4/2 17:42
 * @Description:
 */
@Tag(name="OneApiController",description = "API管理接口")
@RestController
@RequestMapping(ApplicationConstant.API_VERSION + "/one-api")
@RequiredArgsConstructor
@Slf4j
public class AIApiController {
    private final AIApiService oneApiService;

    @Operation(summary = "add",description = "添加一个apikey")
    @PostMapping("/add")
    BaseResponse addOneApi(@RequestBody AddApiDTO request){
        log.info("传参数据：{}",request);
        String apiKey = request.apiKey();
        if(apiKey == null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR,"key is empty");
        }
        return oneApiService.addOneApi(request);
    }

    /**
     * 查询所有未禁止的key
     * @param queryApiRequest
     * @return
     */
    @Operation(summary = "select",description = "查询所有未禁止的key")
    @GetMapping("/select")
    BaseResponse selectApi(QueryApiDTO queryApiRequest){
        if (queryApiRequest.pageSize() == null || queryApiRequest.page() == null){
            return ResultUtils.error(ErrorCode.PARAMS_ERROR,"page 或 pageSize为空");
        }
        return oneApiService.selectApi(PageRequest.of(queryApiRequest.page()-1, queryApiRequest.pageSize()));
    }

    /**
     * 禁止或解封
     * @param id
     * @return
     */
    @Operation(summary = "change",description = "禁止或解封")
    @PutMapping("/change/{id}")
    BaseResponse changeApi(@PathVariable("id") Long id){

        return oneApiService.changeApi(id);
    }

    /**
     * 根据id查找
     * @param id
     * @return
     */
    @Operation(summary = "selectById",description = "根据id查找")
    @GetMapping("/select/{id}")
    BaseResponse selectById(@PathVariable("id") Long id){
        return oneApiService.selectById(id);
    }

    /**
     * 通过id删除
     * @param id
     * @return
     */
    @Operation(summary = "deleteById",description = "通过id删除")
    @DeleteMapping("/delete/{id}")
    BaseResponse deleteById(@PathVariable("id") Long id){
        return oneApiService.deleteById(id);
    }

    /**
     * 通过id批量删除
     * @param ids
     * @return
     */
    @Operation(summary = "deleteByIds",description = "通过id批量删除")
    @DeleteMapping("/delete")
    BaseResponse deleteById(@RequestParam("ids") List<Long> ids){
        return oneApiService.deleteByIds(ids);
    }

    /**
     * 修改
     * @param oneApiDTO
     * @return
     */
    @Operation(summary = "change",description = "修改")
    @PostMapping("/change")
    BaseResponse change(@RequestBody OneApiDTO oneApiDTO){
        if (oneApiDTO.disable() != null){
            return oneApiService.change(oneApiDTO);
        }else {
            return ResultUtils.error(ErrorCode.DATABASE_ERROR);
        }
    }

}
