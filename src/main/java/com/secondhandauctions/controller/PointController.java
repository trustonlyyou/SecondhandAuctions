package com.secondhandauctions.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.cj.log.Log;
import com.secondhandauctions.service.PointService;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Controller
@Slf4j
public class PointController {

    private final RestTemplate restTemplate = new RestTemplate();

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${SECRET_KEY}")
    private String SECRET_KEY;

    @Autowired
    private PointService pointService;

    @GetMapping(value = "/point/charge/form")
    public String pointForm() {
        return "point/charge";
    }

    @PostMapping(value = "/get/orderId")
    @ResponseBody
    public Map<String, String> settingOrderId() {
        Map<String, String> result = new HashMap<>();
        String orderId = pointService.getOrderId();

        result.put("orderId", orderId);

        return result;
    }

    /**
     * https://{origin}/success?paymentKey={paymentKey}&orderId={orderId}&amount={amount}
     *
     * HttpRequest request = HttpRequest.newBuilder()
     *     .uri(URI.create("https://api.tosspayments.com/v1/payments/5zJ4xY7m0kODnyRpQWGrN2xqGlNvLrKwv1M9ENjbeoPaZdL6"))
     *     .header("Authorization", "Basic dGVzdF9za196WExrS0V5cE5BcldtbzUwblgzbG1lYXhZRzVSOg==")
     *     .header("Content-Type", "application/json")
     *     .method("POST", HttpRequest.BodyPublishers.ofString("{\"amount\":15000,\"orderId\":\"5vkbgjiS5TWQTdeHPIWXN\"}"))
     *     .build();
     * HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
     * System.out.println(response.body());
     *
     */
    @GetMapping(value = "/success")
    public String confirmPointCharge(@RequestParam String orderId, @RequestParam Long amount,
                                     @RequestParam String paymentKey, Model model) throws Exception {

        Map result = new HashMap();
        JSONParser parser = new JSONParser();
        JSONObject object = null;
        ResponseEntity<String> response = pointService.success(orderId, amount, paymentKey);

        object = (JSONObject) parser.parse(response.getBody());
        result = objectMapper.readValue(object.toString(), Map.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            String orderName = (String) result.get("orderName");
            log.info("orderName :: '{}'", orderName);
            int chargePoint = 0;
            chargePoint = pointService.getChargePoint(orderName);

            log.info(String.valueOf(chargePoint));
            // insert point service

            model.addAttribute("orderName", orderName);

            return "point/success";
        } else {
            String code = (String) result.get("code");
            String message = (String) result.get("message");

            log.error("ERROR CODE :: '{}'", code);
            log.error("ERROR MESSAGE :: '{}'", message);

            model.addAttribute("code", code);
            model.addAttribute("message", message);

            return "point/fail";
        }
    }
    // TODO: 2022/01/03 계좌이체, 핸드폰 마무리 하자
}
