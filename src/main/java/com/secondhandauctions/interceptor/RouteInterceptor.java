package com.secondhandauctions.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.ZoneId;
import java.util.TimeZone;

@Component
public class RouteInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of("UTC")));

        return super.preHandle(request, response, handler);
    }
}
