package com.secondhandauctions.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class ChatInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String url = request.getHeader("REFERER");

        log.info("url :: " + url);

        if ("".equals(url) || url == null) {
            response.sendRedirect(request.getContextPath() + "/success/list");
            return false;
        } else {
            return true;
        }
    }
}
