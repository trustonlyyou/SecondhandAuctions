package com.secondhandauctions.controller;

import com.secondhandauctions.dao.MemberDao;
import com.secondhandauctions.service.MemberService;
import com.secondhandauctions.service.SampleService;
import com.secondhandauctions.service.SmsService;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Random;

@Controller
public class SampleController {

    Logger logger = LoggerFactory.getLogger(SampleController.class);

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    private SampleService service;

    @Autowired
    private SmsService smsService;

    @Autowired
    MemberDao memberDao;

    @RequestMapping(method = RequestMethod.GET, value = "/join")
    public String joinForm(HttpServletRequest request, HttpServletResponse response) {
        logger.info("url :: joinForm");

        return "member/join_form_sample";
    }

    @ResponseBody
    @RequestMapping(value = "/mailCheck", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public String mailCheckGET(@RequestBody String email) throws Exception {

        if (email != null) {
            logger.info("이메일 데이터 전송 확인");
            logger.info("to Email :: " + email);
        } else {
            logger.error("email value is null");
        }

        // 난수 생성
        Random random = new Random();

        // 111111 ~ 999999 범위의 숫자를 얻기 위해서 nextInt(888888) + 111111를 사용하였습니다.
        int checkNum = random.nextInt(888888) + 11111;

        logger.info("인증번호 " + checkNum);

//        String setFrom = "SecondhandAuctions";
//        String toEmail = email;
//        String title = "회원가입 인증 이메일 입니다.";
//        String content = "회원가입을 해주셔서 감사합니다." +
//                "<br><br>" +
//                "인증번호는 " + checkNum + " 입니다. <br>" +
//                "해당 인증번호를 인증번호 확인란에 기입해 주세요.";
//
//        try {
//            MimeMessage message = javaMailSender.createMimeMessage();
//            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
//            helper.setFrom(setFrom);
//            helper.setTo(toEmail);
//            helper.setSubject(title);
//            helper.setText(content, true);
//            javaMailSender.send(message);
//        } catch (Exception e) {
//            logger.error("error :: " + e);
//        }

        String num = Integer.toString(checkNum);

        return num;
    }

    @GetMapping("/sample/phone")
    public String sampleSms() {
        return "sample/sms";
    }

    @GetMapping(value = "/sendSms")
    @ResponseBody
    public String sendSms(String memberPhone) {
        Random rand = new Random();
        String numStr = "";

        for(int i=0; i<4; i++) {
            String ran = Integer.toString(rand.nextInt(10));
            numStr += ran;
        }

        System.out.println("수신자 번호 : " + memberPhone);
        System.out.println("인증번호 : " + numStr);
        service.certifiedPhoneNumber(memberPhone,numStr);
        return numStr;
    }

    @GetMapping("/sendTest")
    public String sendTest(String memberPhone) {
        String api_key = "NCSHMJOPHZ0OIVTS";
        String api_secret = "YHZAOVPNGWFHPM2MWWNOFDHWPPZ2UVJA";
        Message coolsms = new Message(api_key, api_secret);
        HashMap<String, String> params = new HashMap<String, String>();

        Random rand = new Random();
        String numStr = "";

        for(int i=0; i<4; i++) {
            String ran = Integer.toString(rand.nextInt(10));
            numStr += ran;
        }

        params.put("to", "01082226904");
        params.put("from", "01068300772"); // 내 번호
        params.put("type", "SMS");
        params.put("text", "안녕하세요. 빵떡민씨 Web에서 보내는 메시지 입니다. " + numStr);
        params.put("app_version", "test app 1.2");

        try {
            JSONObject obj = (JSONObject) coolsms.send(params);
            System.out.println(obj.toString());
        } catch (CoolsmsException e) {
            e.printStackTrace();
        };


//        try {
//            smsService.smsService(memberPhone);
//
//            logger.info("No error");
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "redirect:/sample/phone";
//        }


        return "sample/result";
    }

    @GetMapping(value = "/email")
    public String form() {
        return "sample/isEmail";
    }


//    https://kimvampa.tistory.com/90
//    @PostMapping(value = "/check/email")
//    @ResponseBody
//    public String check(@RequestBody String memberEmail) throws Exception {
//        int check = memberDao.isMemberEmail(memberEmail);
//
//        if (check == 1) { // 사용 불가
//            return "fail";
//        } else { // 사용 가능
//            return "success";
//        }
//    }

}
