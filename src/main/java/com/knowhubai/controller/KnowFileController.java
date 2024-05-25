package com.knowhubai.controller;

import com.knowhubai.common.ApplicationConstant;
import com.knowhubai.common.BaseResponse;
import com.knowhubai.common.ErrorCode;
import com.knowhubai.common.ResultUtils;
import com.knowhubai.model.dto.QueryFileDTO;
import com.knowhubai.model.dto.UploadFileDTO;
import com.knowhubai.model.entity.User;
import com.knowhubai.repository.UserRepository;
import com.knowhubai.service.KnowFileService;
import com.knowhubai.utils.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;


    @Operation(summary = "文件上传", description = "文件上传")
    @PostMapping(value = "/file/upload", headers = "content-type=multipart/form-data")
    public BaseResponse addPdf(HttpServletRequest request, UploadFileDTO uploadFileDTO) {

        if (!checkJwt(request, uploadFileDTO.userId())) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }

        List<MultipartFile> file = uploadFileDTO.files();

        if (file == null || file.isEmpty()) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "文件为空");
        }
        return knowFileService.filesStore(uploadFileDTO);
    }

    @Operation(summary = "文件查询", description = "文件查询")
    @GetMapping("/contents")
    public BaseResponse queryFiles(HttpServletRequest request, QueryFileDTO queryFileDTO) {
        if (!checkJwt(request, queryFileDTO.userId())) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        if (queryFileDTO.page() == null || queryFileDTO.pageSize() == null) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "page 或 pageSize为空");
        }
        log.info("{}", queryFileDTO);

        return knowFileService.queryPage(queryFileDTO);
    }

    @Operation(summary = "文件删除", description = "文件删除")
    @DeleteMapping("/delete")
    public BaseResponse deleteFiles(@RequestParam List<Long> ids) {
        return knowFileService.deleteFiles(ids);
    }


    /**
     * 检查jwt里的userId 与 指定userId是否相同
     *
     * @param request
     * @param userId  目标Id
     * @return
     */
    private boolean checkJwt(HttpServletRequest request, Long userId) {
        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) return false;
        String jwt = authorization.substring(7);
        String email = jwtUtil.extractUsername(jwt);
        Optional<User> userOptional = userRepository.findByEmail(email);
        if (userOptional.isPresent()) {
            Long id = userOptional.get().getId();
            return Objects.equals(id, userId);
        }

        return false;

    }

}
