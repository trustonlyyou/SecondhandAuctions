package com.secondhandauctions.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
public class PointController {

    private final RestTemplate restTemplate = new RestTemplate();

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final String SECRET_KEY = "test_ak_mnRQoOaPz8LwjZD1Oljry47BMw6v";

    @GetMapping(value = "/point/charge/form")
    public String pointForm() {
        return "point/charge";
    }

    @RequestMapping(value = "/point/success")
    public String confirmPoint(@RequestParam String paymentKey, @RequestParam String orderId, @RequestParam Long amount,
                               Model model) throws Exception {
        HttpHeaders headers = new HttpHeaders();

        headers.setBasicAuth(SECRET_KEY, ""); // spring 5.2 이상부터 지원
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> payload = new HashMap<>();
        payload.put("orderId", orderId);
        payload.put("amount", String.valueOf(amount));

        log.info("orderId :: '{}'", orderId);
        log.info("amount :: '{}'", amount);

        HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(payload), headers); // 요청 정보

        ResponseEntity<JsonNode> responseEntity = restTemplate.postForEntity(
                "https://api.tosspayments.com/v1/payments/" + paymentKey, request, JsonNode.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            JsonNode successNode = responseEntity.getBody();
            model.addAttribute("orderId", successNode.get("orderId").asText());
            String secret = successNode.get("secret").asText(); // 가상계좌의 경우 입금 callback 검증을 위해서 secret을 저장하기를 권장함
            return "point/success";
        } else {
            JsonNode failNode = responseEntity.getBody();
            model.addAttribute("message", failNode.get("message").asText());
            model.addAttribute("code", failNode.get("code").asText());
            return "point/fail";
        }

    }

    @RequestMapping("/point/fail")
    public String failPayment(@RequestParam String message, @RequestParam String code, Model model) {
        model.addAttribute("message", message);
        model.addAttribute("code", code);
        return "point/fail";
    }

    @RequestMapping("/virtual-account/callback")
    @ResponseStatus(HttpStatus.OK)
    public void handleVirtualAccountCallback(@RequestBody CallbackPayload payload) {
        if (payload.getStatus().equals("DONE")) {
            // handle deposit result
        }
    }

    // Test
    private static class CallbackPayload {
        public CallbackPayload() {}

        private String secret;
        private String status;
        private String orderId;

        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }
    }


}
