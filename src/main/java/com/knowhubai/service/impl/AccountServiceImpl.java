package com.knowhubai.service.impl;

import com.knowhubai.common.BaseResponse;
import com.knowhubai.common.ErrorCode;
import com.knowhubai.common.ResultUtils;
import com.knowhubai.enums.Role;
import com.knowhubai.enums.TokenType;
import com.knowhubai.model.dto.LoginDTO;
import com.knowhubai.model.dto.RegisterDTO;
import com.knowhubai.model.entity.Token;
import com.knowhubai.model.entity.User;
import com.knowhubai.model.vo.AuthenticationVo;
import com.knowhubai.repository.TokenRepository;
import com.knowhubai.repository.UserRepository;
import com.knowhubai.service.AccountService;
import com.knowhubai.utils.JwtUtil;
import com.knowhubai.utils.MailUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    private final MailUtil mailUtil;
    private final AuthenticationManager authenticationManager;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public BaseResponse login(LoginDTO loginDTO) {
        String userEmail = loginDTO.email();
        Optional<User> userByEmail = userRepository.findByEmail(userEmail);
        if (userByEmail.isEmpty()) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "用户不存在");
        }
        // 判断账户是否激活
        if (!userByEmail.get().isEnabled()) {
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "账户未激活");
        }
        // 验证用户名和密码 验证失败时会抛异常
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            userEmail,
                            loginDTO.password()
                    )
            );
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtils.error(ErrorCode.OPERATION_ERROR, "用户名或密码错误");
        }
        String token = jwtUtil.generateToken(userByEmail.get());
        String refreshToken = jwtUtil.generateRefreshToken(userByEmail.get());
        revokeAllUserTokens(userByEmail.get());
        saveUserToken(userByEmail.get(), token);
        AuthenticationVo vo = AuthenticationVo.builder().accessToken(token).refreshToken(refreshToken).build();
        return ResultUtils.success(vo);
    }

    @Override
    public BaseResponse register(RegisterDTO registerDTO) {
        String userEmail = registerDTO.email();
        Optional<User> userByEmail = userRepository.findByEmail(userEmail);
        if (userByEmail.isPresent()) {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "邮箱已注册");
        }
        User user = User.builder()
                .email(registerDTO.email())
                .enabled(false)
                .userRole(Role.USER)
                .password(passwordEncoder.encode(registerDTO.password()))
                .build();
        // 生成token发送注册链接
        User saveUser = userRepository.save(user);
        String token = jwtUtil.generateToken(saveUser);
        mailUtil.sendMailMessage(saveUser.getEmail(), token);
        return ResultUtils.success("账户注册成功，请前往邮箱进行激活");
    }

    @Override
    public BaseResponse verify(String token) {
        String email = jwtUtil.extractUsername(token);
        Optional<User> verifyUser = userRepository.findByEmail(email);
        if (verifyUser.isEmpty()) {
            return ResultUtils.error(ErrorCode.NOT_FOUND_ERROR, "请重新注册");
        }
        User user = verifyUser.get();
        if (jwtUtil.isTokenValid(token, user)) {
            user.setEnabled(true);
            userRepository.save(user);
            return ResultUtils.success("账户激活成功");
        } else {
            return ResultUtils.error(ErrorCode.PARAMS_ERROR, "链接已失效");
        }
    }

    private void saveUserToken(User user, String jwtToken) {
        Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        List<Token> allValidTokenByUser = tokenRepository.findAllValidTokenByUser(user.getId());
        if (allValidTokenByUser.isEmpty()) {
            return;
        }
        allValidTokenByUser.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(allValidTokenByUser);
    }
}
