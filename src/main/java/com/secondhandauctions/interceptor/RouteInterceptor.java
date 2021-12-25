package com.secondhandauctions.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.ZoneId;
import java.util.TimeZone;

@Component
@Slf4j
public class RouteInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of("UTC")));
        log.info("Request URI :: '{}'", request.getRequestURI());
        log.info("Method :: '{}'", request.getMethod());

        return super.preHandle(request, response, handler);
    }
}
