package com.secondhandauctions.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.secondhandauctions.dao.PointDao;
import com.secondhandauctions.utils.Commons;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
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

    @Autowired
    private PointDao pointDao;

    @Autowired
    private Commons commons;

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

    @Transactional(
            isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED,
            rollbackFor = {Exception.class, RuntimeException.class}, timeout = 10)
    public boolean paySuccess(Map info, HttpServletRequest request) throws Exception {
        Map payInfo = new HashMap();
        Map<String, Object> chargeInfo = new HashMap<>();

        String payMethod = "";
        String memberId = "";

        int chargePoint = 0;
        int payChk = 0;
        boolean pointChk = false;

        memberId = commons.getMemberSession(request);
        payMethod = (String) info.get("method");
        chargePoint = getChargePoint((String) info.get("orderName"));

        if (StringUtils.isEmpty(memberId)) {
            log.error("Do not get memberId");
            return false;
        } else {
            payInfo.put("orderId", info.get("orderId"));
            payInfo.put("paymentKey", info.get("paymentKey"));
            payInfo.put("orderName", info.get("orderName"));
            payInfo.put("method", info.get("method"));
            payInfo.put("totalAmount", info.get("totalAmount"));
            payInfo.put("approvedAt", info.get("approvedAt"));
            payInfo.put("chargePoint", chargePoint);
            payInfo.put("memberId", memberId);

            if ("카드".equals(payMethod) || "카드" == payMethod) {
                Map card = (Map) info.get("card");

                payInfo.put("cardCompany", card.get("company"));

                commons.printLogByMap(payInfo);

                try {
                    payChk = pointDao.successCard(payInfo);
                } catch (Exception e) {
                    commons.printStackLog(e);
                    return false;
                }

                if (payChk == 1) {
                    chargeInfo.put("chargePoint", chargePoint);
                    chargeInfo.put("memberId", memberId);

                    commons.printLogByMap(chargeInfo);
                    pointChk = pointUp(chargeInfo);

                    if (pointChk == true) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }

            } else if ("계좌이체".equals(payMethod) || "계좌이체" == payMethod) {
                Map transfer = (Map) info.get("transfer");

                payInfo.put("transferBank", transfer.get("bank"));

                commons.printLogByMap(payInfo);

                try {
                    payChk = pointDao.successTransferBank(payInfo);
                } catch (Exception e) {
                    commons.printStackLog(e);
                    return false;
                }

                if (payChk == 1) {
                    chargeInfo.put("chargePoint", chargePoint);
                    chargeInfo.put("memberId", memberId);

                    commons.printLogByMap(chargeInfo);
                    pointChk = pointUp(chargeInfo);

                    if (pointChk == true) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                log.info("anothor");
                return false;
            }
        }
    }

    public boolean pointUp (Map<String, Object> info) throws Exception {
        int chk = 0;

        if (info.isEmpty()) {
            return false;
        } else {
            chk = pointDao.pointUpMember(info);

            if (chk == 1) {
                log.info("charge point success");
                log.info("targetId :: '{}'", info.get("memberId"));

                return true;
            } else {
                log.error("charge point fail");
                return false;
            }

        }
    }

    public Map<String, String> payErrorResponse (Map result) {
        Map<String, String> error = new HashMap<>();

        String code = (String) result.get("code");
        String message = (String) result.get("message");

        log.error("ERROR CODE :: '{}'", code);
        log.error("ERROR MESSAGE :: '{}'", message);

        error.put("code", code);
        error.put("message", message);

        return error;
    }
}
