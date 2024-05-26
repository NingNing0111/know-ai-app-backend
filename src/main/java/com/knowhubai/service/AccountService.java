package com.knowhubai.service;

import com.knowhubai.common.BaseResponse;
import com.knowhubai.model.dto.LoginDTO;
import com.knowhubai.model.dto.RegisterDTO;
import jakarta.servlet.http.HttpServletRequest;

/**
 * @Project: com.knowhubai.service
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/5/21 20:06
 * @Description:
 */
public interface AccountService {
    BaseResponse login(LoginDTO loginDTO);

    BaseResponse register(RegisterDTO registerDTO);

    BaseResponse verify(String token);

    BaseResponse info(HttpServletRequest request);

    BaseResponse refreshToken(HttpServletRequest request);

    BaseResponse logout(HttpServletRequest request);

}
