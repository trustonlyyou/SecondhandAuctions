package com.secondhandauctions.controller;

import com.secondhandauctions.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping(value = "/admin/pay/info/list")
    public String payInfo() throws Exception {
        return "admin/memberPointChargeList";
    }

    @PostMapping(value = "/admin/searchId/pay/info/list")
    public String searchPayInfo(@RequestParam String keyword, Model model) throws Exception {
        Map<String, List<Map<String, Object>>> result = new HashMap<>();
        List<Map<String, Object>> chargeList = new ArrayList<>();
        List<Map<String, Object>> payList = new ArrayList<>();

        log.info("keyword :: '{}'", keyword);

        if (StringUtils.isEmpty(keyword)) {
            model.addAttribute("chargeList", null);
            model.addAttribute("pyaList", null);

            return "admin/memberPointChargeList";
        } else {
            result = adminService.getMemberPayAndChargePointList(keyword);

            if (result.isEmpty()) {
                log.info("result isEmpty");
                model.addAttribute("chargeList", null);
                model.addAttribute("pyaList", null);
                return "admin/memberPointChargeList";
            } else {
                chargeList = result.get("chargeList");
                payList = result.get("payList");

                if (chargeList.isEmpty()) {
                    model.addAttribute("chargeList", null);
                } else {
                    model.addAttribute("chargeList", chargeList);
                }

                if (payList.isEmpty()) {
                    model.addAttribute("payList", null);
                } else {
                    model.addAttribute("payList", payList);
                }

                return "admin/memberPointChargeList";
            }
        }
    }
}
