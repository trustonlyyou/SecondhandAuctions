package com.secondhandauctions.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class TestController {

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @GetMapping("/test/main")
    public String testMain(RedirectAttributes attributes) throws Exception {
        attributes.addFlashAttribute("test", "test");

        return "redirect:/test/show";
    }

    @GetMapping("/test/show")
    public String testShow(Model model) throws Exception {
        String stance = "";
        stance = (String) model.getAttribute("test");

        logger.info(stance);

        model.addAttribute("stance", stance);

        return "test/show";
    }
}
