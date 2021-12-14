package com.secondhandauctions.service;

import com.secondhandauctions.utils.Commons;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private Commons commons;

    public int certificationSendEmail(String toEmail) throws Exception {

        if (StringUtils.isEmpty(toEmail)) {
            return 0;
        }

        // 난수 생성
        Random random = new Random();

        // 111111 ~ 999999 범위의 숫자를 얻기 위해서 nextInt(888888) + 111111를 사용하였습니다.
        int checkNum = random.nextInt(888888) + 11111;

        String setFrom = "SecondhandAuctions";
        String title = "중고 경매의 세계 인증 이메일 입니다.";
        String content = "중고 경매의 세계를 이용해 주셔서 감사합니다." +
                "<br><br>" +
                "인증번호는 " + checkNum + " 입니다. <br>" +
                "해당 인증번호를 인증번호 확인란에 기입해 주세요.";

        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");

        helper.setFrom(setFrom);
        helper.setTo(toEmail);
        helper.setSubject(title);
        helper.setText(content);

        javaMailSender.send(message);

        log.info("인증번호 :: {}", checkNum);

        return checkNum;
    }

    public int sendEmailToBidMember(String toEmail, String pageUrl) throws Exception {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");

        String setFrom = "";
        String title = "";
        String content = "";

        int check = 0;

        if (StringUtils.isEmpty(toEmail)) {
            log.info("to email is null");
            return check;
        }

        setFrom = "SecondhandAuctions";
        title = "중고 경매의 세계 입찰 메시지 입니다.";
        content = "해당 상품의 입찰을 완료하였습니다." +
                "<br> 물품 보기 : " + pageUrl;

        helper.setFrom(setFrom);
        helper.setTo(toEmail);
        helper.setSubject(title);
        helper.setText(content);

        try {
            javaMailSender.send(message);
        } catch (Exception e) {
            log.error(commons.printStackLog(e));
            return check;
        }

        check = 1;

        return check;
    }

    public void sendEmailToBidMembers(List<String> memberEmailList) throws Exception {
        String setFrom = "SecondhandAuctions";
        String title = "중고 경매의 세계 입찰 메시지 입니다.";
        String content = "";
    }

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
