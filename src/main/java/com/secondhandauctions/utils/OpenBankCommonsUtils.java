package com.secondhandauctions.utils;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

@Component
public class OpenBankCommonsUtils {

    public String getTranDtime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String tran_dtime = simpleDateFormat.format(Calendar.getInstance().getTime());

        return tran_dtime;
    }

    public String getBankTranId() {
        StringBuffer stringBuffer = new StringBuffer();
        Random random = new Random();
        int num = random.nextInt(888888888) + 111111111;

        stringBuffer.append("M202200113");
        stringBuffer.append("U");
        stringBuffer.append(num);

        return stringBuffer.toString();
    }
}
