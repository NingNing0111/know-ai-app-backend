package com.knowhubai.controller;

import com.knowhubai.common.ApplicationConstant;
import com.knowhubai.common.BaseResponse;
import com.knowhubai.model.dto.LoginDTO;
import com.knowhubai.model.dto.RegisterDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/register")
    public BaseResponse register(RegisterDTO registerDTO) {

        return null;
    }

    @PostMapping("/login")
    public BaseResponse login(LoginDTO loginDTO) {

        return null;
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
}
