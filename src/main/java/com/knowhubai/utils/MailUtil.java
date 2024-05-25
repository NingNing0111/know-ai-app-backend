package com.knowhubai.utils;

import com.knowhubai.common.ApplicationConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @Project: com.knowhubai.utils
 * @Author: pgthinker
 * @GitHub: https://github.com/ningning0111
 * @Date: 2024/5/25 01:48
 * @Description: 邮件发送工具
 */
@Component
@RequiredArgsConstructor
public class MailUtil {

    @Value("${application.domain}")
    private String domain;


    private final JavaMailSenderImpl javaMailSender;

    @Async
    public void sendMailMessage(String to, String subject, String message) {
        String username = javaMailSender.getUsername();

        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setFrom(username);
        simpleMailMessage.setTo(to);
        simpleMailMessage.setSubject(subject);
        simpleMailMessage.setText(message);

        javaMailSender.send(simpleMailMessage);
    }

    @Async
    public void sendMailMessage(String to, String token) {
        final String VERIFY_LINK_SUBJECT = "账号激活";

        String verifyLink = domain + ApplicationConstant.API_VERSION + "/account/verify?token=" + token;
        String message = String.format("点击链接：%s 即可进行账号激活", verifyLink);
        sendMailMessage(to, VERIFY_LINK_SUBJECT, message);
    }
}
