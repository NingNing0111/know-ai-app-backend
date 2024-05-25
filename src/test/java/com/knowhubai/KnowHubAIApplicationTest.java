package com.knowhubai;

import com.knowhubai.model.entity.User;
import com.knowhubai.repository.UserRepository;
import com.knowhubai.utils.MailUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

/**
 * @Project: com.knowhubai
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/5/25 12:52
 * @Description:
 */
@SpringBootTest
public class KnowHubAIApplicationTest {

    @Autowired
    private MailUtil mailUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsService userDetailsService;

    @Test
    public void mailTest() {
        mailUtil.sendMailMessage("zdncode@gmail.com", "测试", "这是一封测试邮件");
    }

    @Test
    public void mailTokenTest() {
        mailUtil.sendMailMessage("zdncode@gmail.com", "knowhubaiapp");
    }


    @Test
    public void pwdTest() {
        String sPwd = "$2a$10$aQJpRBEsNQEbmZ6xEeGHgOTQQEdFyLoetHcVMSY60e0hsooT4MVFy";
        String oPwd = "zhangdening1205.";
        System.out.println(passwordEncoder.matches(oPwd, sPwd));
    }

    @Test
    public void userTest() {
        Optional<User> byEmail = userRepository.findByEmail("zdncode@gmail.com");
        if (byEmail.isPresent()) {
            System.out.println(byEmail.get());
        }
    }

    @Test
    public void detailService() {
        UserDetails userDetails = userDetailsService.loadUserByUsername("zdncode@gmail.com");
        System.out.println(userDetails);
    }
}

