package com.secondhandauctions;

import com.secondhandauctions.utils.InfoFormatter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        locations = {"file:src/main/webapp/WEB-INF/config/*.xml"}
)
@WebAppConfiguration
public class FormatterTest {

    @Autowired
    private InfoFormatter formatter;

    @Test
    public void formatterTest() throws Exception {
//        memberId='zkem123456', memberName='오정환',
//        memberPassword='d16140d4b237caff7ff722cfa12ab3640b578e535e737ae82c7376cccbe5bc4d',
//        memberEmail='zkem123456@naver.com', memberPhone='01068300772', regDate=null

        String formatPassword = "";
        String memberPassword = "d16140d4b237caff7ff722cfa12ab3640b578e535e737ae82c7376cccbe5bc4d";
        formatPassword = memberPassword.replace(memberPassword, "******");

        System.out.println("formatPassword :: " + formatPassword);

        String userPhone = "010-6830-0772";
        String formatPhone = userPhone.replace(userPhone.substring(4, 8), "****");

        System.out.println(formatPhone);

        String memberName = "오정환";
        String memberName2 = "바박정민";
        String memberName3 = "투썸";

        if (memberName.length() < 4) {
            memberName = memberName.replace(memberName.charAt(1), '*');
        } else {
            memberName = memberName.replace(memberName.substring(1, 2), "**");

        }
        System.out.println(memberName);

        String memberEmail = "zkem123456@naver.com";
        StringBuilder formatAddress = new StringBuilder();
        String[] arrStr = null;
        String[] arrStr1 = null;
        String[] formatResult = null;
        List<String> resultList = new ArrayList<>();
        String result = "";

        String[] tmp1 = memberEmail.split("@");

        String[] addressSplit = tmp1[1].split("\\.");
        addressSplit[0] = addressSplit[0].replace(addressSplit[0], "****");

        tmp1[0] = tmp1[0].replace(tmp1[0].substring(tmp1[0].length() - 3, tmp1[0].length()), "****");

        System.out.println(tmp1[0]);

        Collections.addAll(resultList, tmp1[0]);
        Collections.addAll(resultList, "@");
        Collections.addAll(resultList, addressSplit[0]);
        Collections.addAll(resultList, ".");
        Collections.addAll(resultList, addressSplit[1]);

        formatResult = resultList.toArray(new String[0]);

        System.out.println(result);

        for (String s : formatResult) {
            formatAddress.append(s);
        }

        System.out.println("formatAddress :: " + formatAddress);

        try {
            StringBuilder stringBuilder = new StringBuilder("zkem123456@naver.com");


        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(formatAddress);

        System.out.println("==================");

        System.out.println(formatter.emailFormat("1234@naver.com"));
        System.out.println(formatter.nameFormat("조권"));
        System.out.println(formatter.passwordFormat("sdofjasoidfjdo"));
    }

    @Test
    public void printName() throws Exception {
        String regex = "(\\d{3})(\\d{3,4})(\\d{4})";
        String userPhone = "0178300772";
        String formatPhone = "";

        formatPhone = userPhone.replaceAll(regex, "$1-****-$3");

        System.out.println(formatPhone);
    }
}
