package com.secondhandauctions;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        locations = {"file:src/main/webapp/WEB-INF/config/*.xml"}
)
@WebAppConfiguration
public class ConvertTest {

    @Test
    public void test() throws Exception {
        String str = "10,000";
        String result = "";

        result = str.replaceAll("," , "");

        System.out.println(result);
    }

    @Test
    public void refererTest() throws Exception {
        String referer = "http://localhost:8080/member/login/form";

//        String[] strArr = referer.split("/");
//
//        for (String str : strArr) {
//            System.out.println(str);
//        }

        System.out.println(referer.contains("/member/login"));
    }
}
