package com.secondhandauctions.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
@Slf4j
public class Commons {

    public static String getClientIp(HttpServletRequest request) {
        String ip = "";

        try {
            request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            ip = request.getHeader("X-FORWARDED-FOR");

            if (ip == null) {
                ip = request.getRemoteAddr();
            }
            // 라인 낭비 try - catch

        } catch (Exception e) {
            log.error("error :: " + e);
        }
        return ip;
    }
}
