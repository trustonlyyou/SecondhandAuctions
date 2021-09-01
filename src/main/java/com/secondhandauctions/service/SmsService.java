package com.secondhandauctions.service;

import net.nurigo.java_sdk.api.Message;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class SmsService {

    @Value("${api.key}")
    private String api_key;

    @Value("${api.secret}")
    private String api_secret;

    @Value("${fromPhoneNumber}")
    private String fromPhoneNumber;

    private static final Logger logger = LoggerFactory.getLogger(SmsService.class);


    public String sendSms(String toPhoneNumber) throws Exception {
        Message message = new Message(api_key, api_secret);
        HashMap<String, String> params = new HashMap<>();

        Random random = new Random();
        String numStr = "";

        numStr = String.valueOf(random.nextInt(888888) + 111111);

//        params.put("to", toPhoneNumber);
//        params.put("from", fromPhoneNumber);
//        params.put("type", "SMS");
//        params.put("text", "중고 경매의 세계입니다. 휴대폰 인증번호는 " + numStr + " 입니다.");
//        params.put("app_version", "test app 1.2");


        try {
//            JSONObject obj = (JSONObject) message.send(params);
//
//            logger.info(obj.toString());
            logger.info("인증번호 :: " + numStr);
        } catch (Exception e) {
            logger.error("error :: " + e);
            e.printStackTrace();
        }

        return numStr;

    }



}
