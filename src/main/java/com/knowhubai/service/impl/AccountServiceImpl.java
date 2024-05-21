package com.knowhubai.service.impl;

import com.knowhubai.common.BaseResponse;
import com.knowhubai.common.ResultUtils;
import com.knowhubai.model.dto.LoginDTO;
import com.knowhubai.model.dto.RegisterDTO;
import com.knowhubai.model.entity.User;
import com.knowhubai.repository.UserRepository;
import com.knowhubai.service.AccountService;
import com.knowhubai.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @Project: com.knowhubai.service.impl
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/5/21 20:15
 * @Description:
 */

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    @Override
    public BaseResponse login(LoginDTO loginDTO) {
        return null;
    }

    @Override
    public BaseResponse register(RegisterDTO registerDTO) {
        User user = User.builder()
                .email(registerDTO.email())
                .password(registerDTO.password())
                .build();
        // TODO: 根据saveUser构建一个唯一的token 并通过邮箱发送注册链接
        User saveUser = userRepository.save(user);

        return ResultUtils.success("注册链接已发送，前往邮箱完成注册");
    }
}
