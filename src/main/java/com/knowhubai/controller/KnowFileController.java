package com.knowhubai.controller;

import com.knowhubai.aop.CheckJwtUserId;
import com.knowhubai.common.ApplicationConstant;
import com.knowhubai.common.BaseResponse;
import com.knowhubai.common.ErrorCode;
import com.knowhubai.common.ResultUtils;
import com.knowhubai.model.dto.QueryFileDTO;
import com.knowhubai.model.dto.UploadFileDTO;
import com.knowhubai.service.KnowFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Project: com.ningning0111.controller
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/4/2 18:53
 * @Description:
 */
@Tag(name = "KnowStoreController", description = "知识库文件存储接口")
@RestController
@RequestMapping(ApplicationConstant.API_VERSION + "/know")
@Slf4j
@RequiredArgsConstructor
public class KnowFileController {

    private final KnowFileService knowFileService;

    @Operation(summary = "文件上传", description = "文件上传")
    @PostMapping(value = "/file/upload", headers = "content-type=multipart/form-data")
    @CheckJwtUserId
    public BaseResponse addPdf(UploadFileDTO uploadFileDTO) {

        List<MultipartFile> file = uploadFileDTO.files();

        if (file == null || file.isEmpty()) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "文件为空");
        }
        return knowFileService.filesStore(uploadFileDTO);
    }

    @Operation(summary = "文件查询", description = "文件查询")
    @GetMapping("/contents")
    @CheckJwtUserId
    public BaseResponse queryFiles(QueryFileDTO queryFileDTO) {

        if (queryFileDTO.page() == null || queryFileDTO.pageSize() == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "page 或 pageSize为空");
        }
        log.info("{}", queryFileDTO);

        return knowFileService.queryPage(queryFileDTO);
    }

    @Operation(summary = "文件删除", description = "文件删除")
    @DeleteMapping("/delete")
    public BaseResponse deleteFiles(@RequestParam List<String> ids) {
        return knowFileService.deleteFiles(ids);
    }

}
