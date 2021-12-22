package com.secondhandauctions.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Component
public class MyPageInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        boolean chk = false;

        if (session.getAttribute("authority") != null) {
            chk = (boolean) session.getAttribute("authority");

            if (chk == false) {
                response.sendRedirect(request.getContextPath() + "/myPage/authority/form");
                return false;
            } else {
                return true;
            }

        }
        response.sendRedirect(request.getContextPath() + "/myPage/authority/form");

        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }
}
