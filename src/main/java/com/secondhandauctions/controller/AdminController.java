package com.secondhandauctions.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.secondhandauctions.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Controller
@Slf4j
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Value("${SECRET_KEY}")
    String SECRET_KEY;

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

    @PostMapping("/admin/cancel/point/pay")
    @ResponseBody
    public Map<String, Object> cancelPay(String paymentKey, String cancelReason) throws Exception {
        /**
         * HttpRequest request = HttpRequest.newBuilder()
         *     .uri(URI.create("https://api.tosspayments.com/v1/payments/{paymentKey}/cancel"))
         *     .header("Authorization", "Basic dGVzdF9za19KUWJnTUdaem9yekRLWmdXbWVrM2w1RTFlbTRkOg==")
         *     .header("Content-Type", "application/json")
         *     .method("POST", HttpRequest.BodyPublishers.ofString("{\"cancelReason\":\"고객이 취소를 원함\"}"))
         *     .build();
         * HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
         * System.out.println(response.body());
         */

        HttpHeaders headers = new HttpHeaders();
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> result = new HashMap<>();

        boolean cancelChk = false;

        log.info("paymentKey :: '{}'", paymentKey);
        log.info("cancel reason :: '{}'", cancelReason);

        headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString((SECRET_KEY + ":").getBytes()));
        headers.setContentType(MediaType.APPLICATION_JSON);

        params.put("cancelReason", cancelReason);

        HttpEntity<String> request =
                new HttpEntity<>(objectMapper.writeValueAsString(params), headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "https://api.tosspayments.com/v1/payments/" + paymentKey + "/cancel",
                HttpMethod.POST, request, String.class
        );

        log.info("status :: " + response.getStatusCode());

        if (response.getStatusCode() == HttpStatus.OK) {
            log.info("body :: " + response.getBody().toString());

            Map map = new HashMap();
            JSONParser parser = new JSONParser();
            JSONObject object = null;

            object = (JSONObject) parser.parse(response.getBody());
            map = objectMapper.readValue(object.toString(), Map.class);

            List<Map> cancels = new ArrayList<>();

            cancels = (List<Map>) map.get("cancels");

            for (Map map1 : cancels) {
                log.info(map1.toString());
                log.info("time :: " + map1.get("canceledAt"));
                log.info("reason :: " + map1.get("cancelReason"));
            }


//            cancelChk = adminService.setCancelResult(paymentKey);
//
//            if (cancelChk == true) {
//                result.put("result", true);
//
//                return result;
//            } else {
//                result.put("result", false);
//            }
            return null;
        } else {
            log.info("body :: " + response.getBody().toString());
            result.put("result", false);

            return null;
        }
    }
    // TODO: 2022/01/11 타겟 아이디 포인트 차감, CancelReq 수정, pointUpdateTime 수정

    @PostMapping(value = "/admin/test")
    @ResponseBody
    public void test(String paymentKey, String cancelReason) throws Exception {
        log.info("paymentKey :: " + paymentKey);
        log.info("cancelReason :: " + cancelReason);

    }
}
