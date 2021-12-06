package com.secondhandauctions.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Map;

@Controller
public class BidController {

    @PostMapping(value = "/bid")
    public Map<String, Object> bidProduct() throws Exception {
        return null;
    }
}
