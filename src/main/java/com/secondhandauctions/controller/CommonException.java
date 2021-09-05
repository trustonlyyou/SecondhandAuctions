package com.secondhandauctions.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice // 이 클래스의 객체가 컨트롤러에서 발생하는 Exception 을 전문적으로 처리하는 클래스라는 것을 명시
public class CommonException {

    private static final Logger logger = LoggerFactory.getLogger(CommonException.class);

    @ExceptionHandler(Exception.class)
    private ModelAndView errorModelAndView(Exception ex) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("/error_common");
        modelAndView.addObject("exception", ex);
        return modelAndView;
    }
}
