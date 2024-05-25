package com.knowhubai.controller;

import com.knowhubai.common.ApplicationConstant;
import com.knowhubai.common.BaseResponse;
import com.knowhubai.common.ErrorCode;
import com.knowhubai.common.ResultUtils;
import com.knowhubai.model.dto.LoginDTO;
import com.knowhubai.model.dto.RegisterDTO;
import com.knowhubai.service.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

/**
 * @Project: com.knowhubai.controller
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/5/21 19:58
 * @Description:
 */
@RequiredArgsConstructor
@RequestMapping(ApplicationConstant.API_VERSION + "/account")
@RestController
@Slf4j
public class AccountController {

    private final AccountService accountService;

    @PostMapping("/register")
    public BaseResponse register(@RequestBody RegisterDTO registerDTO) {
        if (!registerDTO.password().equals(registerDTO.confirmPassword())) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "密码不一致");
        }
        return accountService.register(registerDTO);
    }

    @PostMapping("/login")
    public BaseResponse login(@RequestBody LoginDTO loginDTO) {
        return accountService.login(loginDTO);
    }

    @PostMapping("/logout")
    public BaseResponse logout() {

        return null;
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {

    }

    @GetMapping("/verify")
    public BaseResponse verify(@RequestParam String token) {
        return accountService.verify(token);
    }
}
