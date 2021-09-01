package com.secondhandauctions;

import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        locations = {"file:src/main/webapp/WEB-INF/config/*.xml"}
)
@WebAppConfiguration
public class SmsTest {

    @Test
    public void sendSms() {
        String api_key = "NCSHMJOPHZ0OIVTS";
        String api_secret = "YHZAOVPNGWFHPM2MWWNOFDHWPPZ2UVJA";
        Message coolsms = new Message(api_key, api_secret);
        HashMap<String, String> params = new HashMap<String, String>();

        params.put("to", "010168300772");
        params.put("from", "01068300772");
        params.put("type", "SMS");
        params.put("text", "Hello, JungMin haha!!! It's Spring sms Testing");
        params.put("app_version", "test app 1.2");

        try {
            JSONObject obj = (JSONObject) coolsms.send(params);
            System.out.println(obj.toString());
        } catch (CoolsmsException e) {
            e.printStackTrace();
        };
    }
}
    // 4 params(to, from, type, text) are mandatory. must be filled
//    HashMap<String, String> params = new HashMap<String, String>();
//        params.put("to", phoneNumber);    // 수신전화번호
//                params.put("from", "발송할 번호 입력");    // 발신전화번호. 테스트시에는 발신,수신 둘다 본인 번호로 하면 됨
//                params.put("type", "SMS");
//                params.put("text", "핫띵크 휴대폰인증 테스트 메시지 : 인증번호는" + "["+cerNum+"]" + "입니다.");
//                params.put("app_version", "test app 1.2"); // application name and version


//        Message coolsms = new Message(api_key, api_secret);
//        HashMap<String, String> params = new HashMap<String, String>();
//
//        params.put("to", "등록한 발신자 번호");
//        params.put("from", "수신자 번호");
//        params.put("type", "SMS");
//        params.put("text", "문자 내용");
//        params.put("app_version", "test app 1.2");
//
//        try {
//            JSONObject obj = (JSONObject) coolsms.send(params);
//            System.out.println(obj.toString());
//        } catch (CoolsmsException e) {
//            System.out.println(e.getMessage());
//            System.out.println(e.getCode());
//        }
//    }
