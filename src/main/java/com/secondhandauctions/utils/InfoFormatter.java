package com.secondhandauctions.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class InfoFormatter {

    public String passwordFormat(String memberPassword) {
        String formatPassword = "";

        formatPassword = memberPassword.replace(memberPassword, "******");

        return formatPassword;
    }

    public String nameFormat(String memberName) throws Exception {
        String formatName = "";

        if (memberName.length() >= 4) {
            formatName = memberName.replace(memberName.substring(1, 2), "**");
        }

        formatName = memberName.replace(memberName.charAt(1), '*');

        return formatName;
    }

    public String emailFormat(String memberEmail) throws Exception{
        String[] formatEmailArray = null;
        List<String> tmpList = new ArrayList<>();
        String[] tmp = null;
        String[] tmpAddress = null;
        StringBuilder result = new StringBuilder();

        tmp = memberEmail.split("@");

        tmpAddress = tmp[1].split("\\.");
        tmpAddress[0] = tmpAddress[0].replace(tmpAddress[0], "****");

        if (tmp[1].length() < 5) {
            tmp[0] = tmp[0].replace(tmp[0].substring(tmp[0].length() - 2, tmp[0].length()), "****");
        }

        tmp[0] = tmp[0].replace(tmp[0].substring(tmp[0].length() - 3, tmp[0].length()), "****");

        Collections.addAll(tmpList, tmp[0]);
        Collections.addAll(tmpList, "@");
        Collections.addAll(tmpList, tmpAddress[0]);
        Collections.addAll(tmpList, ".");
        Collections.addAll(tmpList, tmpAddress[1]);

        formatEmailArray = tmpList.toArray(new String[0]);


        for (String str : formatEmailArray) {
            result.append(str);
        }

        return result.toString();
    }

    public String phoneFormat(String memberPhone) throws Exception {
        String regex = "(\\d{3})(\\d{3,4})(\\d{4})";
        String formatPhone = "";

        formatPhone = memberPhone.replaceAll(regex, "$1-****-$3");

        return formatPhone;
    }
}
