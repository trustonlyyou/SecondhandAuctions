package com.secondhandauctions.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.secondhandauctions.service.PointService;
import com.secondhandauctions.utils.Commons;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@Slf4j
public class PointController {

    @Value("${SECRET_KEY}")
    private String SECRET_KEY;

    @Autowired
    private PointService pointService;

    @Autowired
    private Commons commons;

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
                                     @RequestParam String paymentKey, RedirectAttributes attributes,
                                     HttpServletRequest request) throws Exception {
        Map result = new HashMap();
        Map<String, Object> info = new HashMap<>();
        boolean chk = false;

        ObjectMapper objectMapper = new ObjectMapper();
        JSONParser parser = new JSONParser();
        JSONObject object = null;
        ResponseEntity<String> response = pointService.success(orderId, amount, paymentKey);

        object = (JSONObject) parser.parse(response.getBody());
        result = objectMapper.readValue(object.toString(), Map.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            log.info(response.getBody().toString());

            chk = pointService.paySuccess(result, request);

            if (chk == true) {
                info.put("memberId", commons.getMemberSession(request));
                return "redirect:/point/success";
            } else {
                attributes.addFlashAttribute("tossError", pointService.payErrorResponse(result));
                return "redirect:/point/fail";
            }

        } else {
            attributes.addFlashAttribute("tossError", pointService.payErrorResponse(result));
            return "redirect:/point/fail";
        }
    }

    @GetMapping(value = "/point/success")
    public String pointSuccess() {
        return "point/success";
    }

    @GetMapping(value = "/point/fail")
    public String pointFail() {
        return "point/fail";
    }



    @RequestMapping("/virtual/account/callback")
    @ResponseStatus(HttpStatus.OK)
    public void handleVirtualAccountCallback(@RequestBody String secret, String status, String orderId) {
        if (("DONE").equals(status)) {
            log.info("call back");
            // handle deposit result

            log.info("callback secret :: '{}'", secret);
        }
    }

    @GetMapping(value = "/pay/list")
    public String payList(Model model) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();

        LocalDateTime nowDate = LocalDateTime.now().plusDays(1);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String date = formatter.format(nowDate);


        headers.set("Authorization", "Basic " + Base64.getEncoder().encodeToString((SECRET_KEY + ":").getBytes()));
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                "https://api.tosspayments.com/v1/transactions?startDate=2022-01-01&endDate=2022-01-04",
                HttpMethod.GET, request, String.class
        );

        Map result = new HashMap();
        List<Map> list = new ArrayList<>();
        JSONParser parser = new JSONParser();
        JSONArray jsonArray = null;

        jsonArray = (JSONArray) parser.parse(response.getBody());
        list = objectMapper.readValue(jsonArray.toString(), List.class);

        for (Map map : list) {
            log.info("=======================================================");
            log.info("paymentKey :: '{}'", map.get("paymentKey"));
            log.info("orderId :: '{}'", map.get("orderId"));
            log.info("customerKey :: '{}'", map.get("customerKey"));
            log.info("method :: '{}'", map.get("method"));
            log.info("amount :: '{}'", map.get("amount"));
        }

        return "point/list";
    }

    // 포인트 결제 취소
    @PostMapping("/cancel/point/pay")
    @ResponseBody
    public Map<String, Object> cancelPoint(String paymentKey, String cancelReason, String memberId) throws Exception {
        Map cancelResult = new HashMap();
        Map<String, Object> result = new HashMap<>();

        ObjectMapper objectMapper = new ObjectMapper();
        JSONParser parser = new JSONParser();
        JSONObject object = null;
        ResponseEntity<String> response = pointService.cancel(paymentKey, cancelReason);

        boolean chk = false;


        if (response.getStatusCode() == HttpStatus.OK) {
            object = (JSONObject) parser.parse(response.getBody());
            cancelResult = objectMapper.readValue(object.toString(), Map.class);

            chk = pointService.payCancel(cancelResult, memberId);

            if (chk == true) {
                pointService.setCancelResult(paymentKey);
                result.put("result", true);

                return result;
            } else {
                result.put("result", false);

                return result;
            }
        } else {
            // TODO: 2022/01/11 수정
            log.error("error body :: {}", response.getBody().toString());
            result.put("result", false);

            return result;
        }
    }
// TODO: 2022/01/06 결제완료, 결제실패 .jsp 완성
// TODO: 2022/01/06 Success Bid 연결 된거에서 포인트로 결재 했을 때 포인트 옮겨지는 service

    // 포인트로 결제
    @ResponseBody
    @PostMapping("/point/pay/success/bid")
    public Map<String, Object> pointPayBySuccessBid(int successBidNo, String seller, String bidder,
                                                     String bidPrice, HttpServletRequest request) throws Exception {
        log.info("successBidNo :: " + successBidNo);
        log.info("seller :: " + seller);
        log.info("bidder :: " + bidder);

        Map<String, Object> result = new HashMap<>();
        Map<String, Object> info = new HashMap<>();

        boolean memberChk = false;
        boolean payChk = false;

        String memberId = commons.getMemberSession(request); // bidder

        log.info("memberId :: " + memberId);

        if (!bidder.equals(memberId)) {
            log.error("The memberId and bidder are different.");

            result.put("msg", "MATCH");
            result.put("result", false);
            return result;
        } else {
            info.put("successBidNo", successBidNo);
            info.put("bidder", bidder);
            info.put("seller", seller);
            info.put("bidPrice", commons.convertPrice(bidPrice));

            memberChk = pointService.chkBidderSeller(info);

            if (memberChk == false) {
                result.put("msg", "MATCH");
                result.put("result", false);
                return result;
            } else {
                // 입찰자가 금액이 있는지 확인해야한다.
                payChk = pointService.memberPriceChk(info);

                if (payChk == false) {
                    result.put("msg", "PAY");
                    result.put("result", false);

                    return result;
                } else {
                    pointService.pointUpDownBySuccessBidProduct(info);
                    result.put("result", true);

                    return result;
                }
            }
        }
    }
}

