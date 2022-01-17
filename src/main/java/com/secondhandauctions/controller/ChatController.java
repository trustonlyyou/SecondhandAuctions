package com.secondhandauctions.controller;

import com.secondhandauctions.service.ChatService;
import com.secondhandauctions.utils.Commons;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
public class ChatController {

    @Autowired
    private ChatService chatService;

    @Autowired
    private Commons commons;

    @GetMapping(value = "/chat/{roomNo}")
    public String chatForm(@PathVariable int roomNo, HttpServletRequest request, Model model) throws Exception {
        Map<String, String> roomMember = new HashMap<>();
        String memberId = "";
        String bidder = "";
        String seller = "";

        if (roomNo == 0) {
            return "redirect:/myPage/form";
        } else {
            roomMember = chatService.getChatMembers(roomNo);
            memberId = commons.getMemberSession(request);

            bidder = roomMember.get("bidder");
            seller = roomMember.get("seller");

            if (bidder.equals(memberId) || seller.equals(memberId)) {
                model.addAttribute("roomNo", roomNo);
                model.addAttribute("memberId", memberId);
                model.addAttribute("bidder", bidder);
                model.addAttribute("seller", seller);
                model.addAttribute("list", chatService.getChatList(roomNo));

                return "myPage/chatting";
            } else {
                log.info("Permission Denied");
                return "redirect:/myPage/form";
            }
        }
    }
}
