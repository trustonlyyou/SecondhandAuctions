package com.secondhandauctions.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class SmsUtils {

    @Autowired
    private JavaMailSender javaMailSender;

    public boolean joinEmailCheck(String email) throws Exception {
        String toEmail = "";
        String title = "";
        String content = "";
        int checkNum = 0;

        // 난수 생성
        Random random = new Random();

        // 11111 ~ 999999 범위 숫자를 얻음
        checkNum = random.nextInt(888888) + 111111;



        return true;
    }
}
