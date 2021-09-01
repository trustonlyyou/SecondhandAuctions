package com.secondhandauctions.service;

import net.nurigo.java_sdk.api.Message;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SampleService {
    public void certifiedPhoneNumber(String phoneNumber, String cerNum) {

        String api_key = "NCSHMJOPHZ0OIVTS";
        String api_secret = "YHZAOVPNGWFHPM2MWWNOFDHWPPZ2UVJA";
        Message coolsms = new Message(api_key, api_secret);

        // 4 params(to, from, type, text) are mandatory. must be filled
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("to", phoneNumber);    // 수신전화번호
        params.put("from", "발송할 번호 입력");    // 발신전화번호. 테스트시에는 발신,수신 둘다 본인 번호로 하면 됨
        params.put("type", "SMS");
        params.put("text", "핫띵크 휴대폰인증 테스트 메시지 : 인증번호는" + "["+cerNum+"]" + "입니다.");
        params.put("app_version", "test app 1.2"); // application name and version

        try {
            JSONObject obj = (JSONObject) coolsms.send(params);
            System.out.println(obj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
