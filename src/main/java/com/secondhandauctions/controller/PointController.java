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

    private final RestTemplate restTemplate = new RestTemplate();

    private final ObjectMapper objectMapper = new ObjectMapper();

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

    /**
     * curl --request GET \
     *   --url 'https://api.tosspayments.com/v1/transactions?startDate=2021-07-27&endDate=2021-07-28' \
     *   --header 'Authorization: Basic dGVzdF9za19KUWJnTUdaem9yekRLWmdXbWVrM2w1RTFlbTRkOg=='
     *
     *
     */
    // TODO: 2022/01/03 리스트는 admin 만 접금 가능하다. 환불은 admin 만 가능하다. 처음에 보여줄때 날짜를 정할 수 있게 보여줄 것.
    // TODO: 2022/01/03 DB 작업 마무리하고, 결제 완료 insert 코드 짜고, 환불 계획 세우자
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

    @PostMapping
    @ResponseBody
    public void cancelPoint() throws Exception {
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

    }
}

// TODO: 2022/01/06 결제완료, 결제실패 .jsp 완성 
// TODO: 2022/01/06 Success Bid 연결 된거에서 포인트로 결재 했을 때 포인트 옮겨지는 service