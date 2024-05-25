package com.knowhubai.config;

import com.knowhubai.common.ErrorCode;
import com.knowhubai.exception.BusinessException;
import com.knowhubai.model.entity.User;
import com.knowhubai.repository.UserRepository;
import com.knowhubai.service.KnowFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Objects;
import java.util.Optional;


/**
 * @Project: com.ningning0111.config
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/4/2 13:52
 * @Description:
 */
@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserRepository userRepository;

    @Value("spring.jpa.hibernate.ddl-auto")
    private String ddlAuto;

    @Bean
    public TokenTextSplitter tokenTextSplitter() {
        return new TokenTextSplitter();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            Optional<User> userByEmail = userRepository.findByEmail(username);

            if (userByEmail.isPresent()) {
                return userByEmail.get();
            }
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "User Not Found");
        };

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public VectorStore vectorStore(KnowFileService service, JdbcTemplate jdbcTemplate) {
        if (Objects.equals(ddlAuto, "create")) {
            jdbcTemplate.execute("drop table if exists vector_store cascade");
        }
        return service.randomGetVectorStore();
    }


}
