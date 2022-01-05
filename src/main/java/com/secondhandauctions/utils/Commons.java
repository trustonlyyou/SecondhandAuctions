package com.secondhandauctions.utils;

import com.oracle.tools.packager.mac.MacAppBundler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
@Slf4j
public class Commons {

    public static String getClientIp(HttpServletRequest request) {
        String ip = "";

        request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        ip = request.getHeader("X-FORWARDED-FOR");

        if (ip == null) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }

    public void setMemberSession(HttpServletRequest request, String memberId) {
        HttpSession session = request.getSession();
        session.setAttribute("member", memberId);
    }

    public void setKakaoToken(HttpServletRequest request, String token) {
        HttpSession session = request.getSession();
        session.setAttribute("token", token);
    }

    public String getMemberSession(HttpServletRequest request) {
        String memberId = "";

        HttpSession session = request.getSession();
        memberId = (String) session.getAttribute("member");

        return memberId;
    }

    public boolean isEmptyStrings(String... strings) {
        for (String str : strings) {
            if (StringUtils.isEmpty(str)) {
                return true;
            }
        }

        return false;
    }

    // TODO: 2021/12/28 삭제
    public boolean isEmpty(List<String> list) {
        for (String str : list) {
            if ("".equals(str) || str == null) {
                return true;
            }
        }
        return false;
    }

    public String printStackLog(Exception e) {
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));

        return stringWriter.toString();
    }

    public Date getExpireTime() throws Exception {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 3);
        calendar.add(Calendar.DATE, +7);

        date = sdf.parse(sdf.format(calendar.getTime()));

        return date;
    }

    public void printLogByMap(Map params) {
        Iterator<String> iterator = params.keySet().iterator();

        while (iterator.hasNext()) {
            String key = iterator.next();

            log.info("{} :: '{}'", key, params.get(key));
        }
    }
}
