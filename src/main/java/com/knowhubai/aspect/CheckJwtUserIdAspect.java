package com.knowhubai.aspect;

import com.alibaba.fastjson2.JSON;
import com.knowhubai.aop.CheckJwtUserId;
import com.knowhubai.common.ErrorCode;
import com.knowhubai.exception.BusinessException;
import com.knowhubai.model.entity.User;
import com.knowhubai.repository.TokenRepository;
import com.knowhubai.repository.UserRepository;
import com.knowhubai.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * @Project: com.knowhubai.aspect
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/5/26 15:42
 * @Description: aop切面操作 用于判断jwt中的userId与targetUserId是否一致
 */
@Aspect
@Component
@RequiredArgsConstructor
public class CheckJwtUserIdAspect {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;

    @Around("@annotation(checkJwtUserId)")
    public Object checkJwtUserId(ProceedingJoinPoint joinPoint, CheckJwtUserId checkJwtUserId) throws Throwable {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        System.out.println(JSON.toJSONString(request.getParameterMap()));
        long targetUserId = Long.parseLong(request.getParameter("userId"));
        System.out.println("获取到的请求体中的userId：" + targetUserId);

        // 从request中提取jwt信息
        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        String jwt = authorization.substring(7);
        String email = jwtUtil.extractUsername(jwt);
        User userDetails = userRepository.findByEmail(email).orElseThrow();
        Boolean isValid = tokenRepository.findByToken(jwt).map(e -> !e.isExpired() && !e.isRevoked()).orElse(false);
        if (jwtUtil.isTokenValid(jwt, userDetails) && isValid && targetUserId == userDetails.getId()) {
            return joinPoint.proceed();

        }

        throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
    }

}
