package com.knowhubai;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @Project: com.knowhubai
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/5/25 12:52
 * @Description:
 */
//@SpringBootTest
public class KnowHubAIApplicationTest {

    //    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void pwdTest() {
        String sPwd = "$2a$10$aQJpRBEsNQEbmZ6xEeGHgOTQQEdFyLoetHcVMSY60e0hsooT4MVFy";
        String oPwd = "zhangdening1205.";
        System.out.println(passwordEncoder.matches(oPwd, sPwd));
    }


}

