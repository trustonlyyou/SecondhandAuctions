package com.secondhandauctions.controller;

import com.secondhandauctions.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@Slf4j
public class ChatController {

    @Autowired
    private ChatService chatService;

    @GetMapping("/chat/add")
    public String chatForm() throws Exception {
        return "test/chat";
    }

    @GetMapping("/chat/doAdd")
    @ResponseBody
    public String doAdd(@RequestParam Map<String, Object> param) {
        Map<String, Object> rs = chatService.doAdd(param);

        int id = (int)rs.get("id");


        return id + "채팅방이 생성 되었습니다.";
    }



}
