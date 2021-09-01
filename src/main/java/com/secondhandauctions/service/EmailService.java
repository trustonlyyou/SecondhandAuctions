package com.secondhandauctions.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendEmail(String toEmail, String title, String content) throws Exception {
        String setFrom = "SecondhandAuctions";
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");

        helper.setFrom(setFrom);
        helper.setTo(toEmail);
        helper.setSubject(title);
        helper.setText(content);

        javaMailSender.send(message);
    }
}
