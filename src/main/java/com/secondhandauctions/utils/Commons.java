package com.secondhandauctions.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Component
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
}
