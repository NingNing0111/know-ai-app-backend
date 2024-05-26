package com.knowhubai.config;

import com.knowhubai.enums.Role;
import com.knowhubai.filter.JwtAuthenticationFilter;
import com.knowhubai.filter.StreamChatAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @Project: com.knowhubai.config
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/5/18 15:28
 * @Description:
 */
@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final StreamChatAuthenticationFilter streamChatAuthenticationFilter;
    private final AuthenticationProvider authenticationProvider;

    private static final String[] WHITE_LIST_URL = {
            "/api/v1/account/login",
            "/api/v1/account/register",
            "/api/v1/account/verify",
            "/swagger-ui/**",
            "/doc.html",
            "/webjars/**",
            "/v3/**",
            "/api/v1/chat/**", // 该接口是WebFlux实现的，Security不允许同时存在两种网络框架的实现，因此需要对该接口单独写个过滤器
    };

    private static final String[] USER_LIST_URL = {
            "/api/v1/draw/**",
            "/api/v1/know/**",
            "/api/v1/account/logout",
            "/api/v1/account/info",
    };

    private static final String[] ADMIN_LIST_URL = {
            "/api/v1/one-api/**"
    };


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable);


        httpSecurity.authorizeHttpRequests(req -> req
                .requestMatchers(WHITE_LIST_URL)
                .permitAll()

                .requestMatchers(USER_LIST_URL)
                .hasAnyAuthority(Role.USER.name(), Role.ADMIN.name())

                .anyRequest()
                .authenticated()
        );


        httpSecurity.authenticationProvider(authenticationProvider)

                .addFilterBefore(streamChatAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(jwtAuthenticationFilter, StreamChatAuthenticationFilter.class);

        return httpSecurity.build();
    }

}
