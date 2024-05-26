package com.knowhubai.controller;

import com.knowhubai.common.ApplicationConstant;
import com.knowhubai.common.BaseResponse;
import com.knowhubai.common.ErrorCode;
import com.knowhubai.common.ResultUtils;
import com.knowhubai.model.dto.LoginDTO;
import com.knowhubai.model.dto.RegisterDTO;
import com.knowhubai.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @Project: com.knowhubai.controller
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/5/21 19:58
 * @Description:
 */
@Tag(name = "AccountController", description = "用户认证授权接口")
@RequiredArgsConstructor
@RequestMapping(ApplicationConstant.API_VERSION + "/account")
@RestController
@Slf4j
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/register")
    @Operation(summary = "账户注册", description = "账户注册")
    public BaseResponse register(@RequestBody RegisterDTO registerDTO) {
        if (!registerDTO.password().equals(registerDTO.confirmPassword())) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "密码不一致");
        }
        return accountService.register(registerDTO);
    }

    @PostMapping("/login")
    @Operation(summary = "账户登录", description = "账户登录")
    public BaseResponse login(@RequestBody LoginDTO loginDTO) {
        return accountService.login(loginDTO);
    }

    @PostMapping("/logout")
    @Operation(summary = "用户退出", description = "退出接口")
    public BaseResponse logout(HttpServletRequest request) {

        return accountService.logout(request);
    }

    @PostMapping("/refresh-token")
    @Operation(summary = "JWT刷新", description = "JWT无感刷新接口，当accessToken过期时，使用refreshToken请求该接口即可获取新的accessToken")
    public BaseResponse refreshToken(
            HttpServletRequest request
    ) {
        return accountService.refreshToken(request);
    }

    @GetMapping("/verify")
    @Operation(summary = "邮箱验证", description = "邮箱验证")
    public BaseResponse verify(@RequestParam String token) {
        return accountService.verify(token);
    }

    @GetMapping("/info")
    @Operation(summary = "用户详情", description = "获取用户详细详细")
    public BaseResponse info(HttpServletRequest request) {
        return accountService.info(request);
    }
}
