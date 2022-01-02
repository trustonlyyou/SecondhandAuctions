package com.secondhandauctions.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
@Slf4j
public class PointService {

    @Value("${SECRET_KEY}")
    private String SECRET_KEY;

    public String getOrderId() {
        StringBuffer orderId = new StringBuffer();
        LocalDateTime dateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        Random random = new Random();

        String randomStr = String.valueOf(random.nextInt(888888) + 111111);
        String date = formatter.format(dateTime);

        orderId.append("SH_");
        orderId.append(date);
        orderId.append(randomStr);

        return orderId.toString();
    }

    public int getChargePoint(String orderName) {
        String chargePointStr = "";
        int chargePoint = 0;

        String[] splitArr = orderName.split("\\s+");
        chargePointStr = splitArr[0];


        chargePoint = (int) Math.floor(Integer.parseInt(chargePointStr) * 1.08);

        return chargePoint;
    }

    public ResponseEntity<String> success(String orderId, Long amount, String paymentKey) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> params = new HashMap<>();
        Map result = new HashMap();

        headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString((SECRET_KEY + ":").getBytes()));
        headers.setContentType(MediaType.APPLICATION_JSON);

        params.put("orderId", orderId);
        params.put("amount", String.valueOf(amount));

        log.info("orderId :: '{}'", orderId);
        log.info("amount :: '{}'", amount);

        HttpEntity<String> request =
                new HttpEntity<>(objectMapper.writeValueAsString(params), headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "https://api.tosspayments.com/v1/payments/" + paymentKey,
                HttpMethod.POST, request, String.class
        );

        log.info("status :: '{}'", response.getStatusCode());

        return response;
    }
}
