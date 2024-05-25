package com.knowhubai.config;

import com.knowhubai.enums.Role;
import com.knowhubai.filter.JwtAuthenticationFilter;
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
    private final AuthenticationProvider authenticationProvider;

    private static final String[] WHITE_LIST_URL = {
            "/api/v1/account/login",
            "/api/v1/account/register",
            "/api/v1/account/logout",
            "/api/v1/account/verify",
            "/api/v1/chat/**",
    };

    private static final String[] USER_LIST_URL = {
            "/api/v1/draw/**",
            "/api/v1/know/**"
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
                .hasAnyAuthority(Role.USER.name())


                .anyRequest()
                .authenticated()
        );


        httpSecurity.authenticationProvider(authenticationProvider).addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

}
