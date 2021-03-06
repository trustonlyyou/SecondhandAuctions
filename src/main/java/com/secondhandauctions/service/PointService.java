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
import java.util.*;

@Service
@Slf4j
public class PointService {

    @Value("${SECRET_KEY}")
    private String SECRET_KEY;

    @Autowired
    private PointDao pointDao;

    @Autowired
    private Commons commons;

    public String getMemberName(String memberId) throws Exception {
        String memberName = "";

        if (StringUtils.isEmpty(memberId)) {
            return "";
        } else {
            memberName = pointDao.getCustomerName(memberId);

            if (StringUtils.isEmpty(memberName)) {
                log.error("memberName is Empty");
                return "";
            } else {
                return memberName;
            }
        }
    }

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

    public int getChargeAndCancelPoint(String orderName) {
        String chargePointStr = "";
        int chargePoint = 0;

        String[] splitArr = orderName.split("\\s+");
        chargePointStr = splitArr[0];


        chargePoint = (int) Math.floor(Integer.parseInt(chargePointStr) * 1.08);

        return chargePoint;
    }

    /**
     * HttpRequest request = HttpRequest.newBuilder()
     *     .uri(URI.create("https://api.tosspayments.com/v1/payments/5zJ4xY7m0kODnyRpQWGrN2xqGlNvLrKwv1M9ENjbeoPaZdL6"))
     *     .header("Authorization", "Basic dGVzdF9za19KUWJnTUdaem9yekRLWmdXbWVrM2w1RTFlbTRkOg==")
     *     .header("Content-Type", "application/json")
     *     .method("POST", HttpRequest.BodyPublishers.ofString("{\"amount\":15000,\"orderId\":\"Dvb4l75Ci8J7ESo-QsR0f\"}"))
     *     .build();
     * HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
     * System.out.println(response.body());
     * @param orderId
     * @param amount
     * @param paymentKey
     * @return
     * @throws Exception
     */
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

    // ????????? ??????
    @Transactional(
            isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED,
            rollbackFor = {Exception.class, RuntimeException.class}, timeout = 10)
    public boolean paySuccess(Map info, HttpServletRequest request) throws Exception {
        Map payInfo = new HashMap();
        Map<String, Object> chargeInfo = new HashMap<>();
        Map<String, Object> updateMemberTime = new HashMap<>();

        String payMethod = "";
        String memberId = "";

        int chargePoint = 0;
        int payChk = 0;
        boolean pointChk = false;

        memberId = commons.getMemberSession(request);
        payMethod = (String) info.get("method");
        chargePoint = getChargeAndCancelPoint((String) info.get("orderName"));

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

            if ("??????".equals(payMethod) || "??????" == payMethod) {
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

                    updateMemberTime.put("updateTime", info.get("approvedAt"));
                    updateMemberTime.put("memberId", memberId);

                    commons.printLogByMap(chargeInfo);

                    pointChk = pointUp(chargeInfo);
                    pointDao.pointUpdateMemberTime(updateMemberTime);

                    if (pointChk == true) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }

            } else if ("????????????".equals(payMethod) || "????????????" == payMethod) {
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

                    updateMemberTime.put("updateTime", info.get("approvedAt"));
                    updateMemberTime.put("memberId", memberId);

                    commons.printLogByMap(chargeInfo);

                    pointChk = pointUp(chargeInfo);
                    pointDao.pointUpdateMemberTime(updateMemberTime);

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
        String message = (String) result.get("WEB-INF/messages");

        log.error("ERROR CODE :: '{}'", code);
        log.error("ERROR MESSAGE :: '{}'", message);

        error.put("code", code);
        error.put("message", message);

        return error;
    }

    // ?????? ??????
    public ResponseEntity<String> cancel(String paymentKey, String cancelReason) throws Exception {
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

        return response;
    }

    @Transactional(
            isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED,
            rollbackFor = {Exception.class, RuntimeException.class}, timeout = 10)
    public boolean payCancel(Map info, String memberId) throws Exception {
        /**
         * orderId, paymentKey, orderName, method, cancelAmount,
         * approvedAt, cancelPoint, cancelReason, memberId
         *
         * cardCompany, transferBank,
         */
        List<Map> cancels = new ArrayList<>();
        Map cancelInfo = new HashMap();
        Map<String, Object> disChargeInfo = new HashMap<>();
        Map<String, Object> updateMemberTime = new HashMap<>();

        cancels = (List<Map>) info.get("cancels");
        String method = (String) info.get("method");
        String status = (String) info.get("status");
        String cancelDate = "";
        int cancelPoint = getChargeAndCancelPoint((String) info.get("orderName"));

        // ????????? ????????? 1??? ?????? ????????? ????????? ??????
        for (Map map : cancels) {
            log.info("response :: {}", map.toString());

            cancelInfo.put("approvedAt", map.get("canceledAt"));
            cancelInfo.put("cancelReason", map.get("cancelReason"));
            cancelInfo.put("cancelAmount", map.get("cancelAmount"));

            cancelDate = (String) map.get("canceledAt");
        }

        cancelInfo.put("orderId", info.get("orderId"));
        cancelInfo.put("paymentKey", info.get("paymentKey"));
        cancelInfo.put("orderName", info.get("orderName"));
        cancelInfo.put("method" , method);
        cancelInfo.put("cancelPoint", cancelPoint);
        cancelInfo.put("memberId", memberId);

        if ("CANCELED".equals(status) || "CANCELED" == status) {
            if ("??????".equals(method) || "??????" == method) {
                Map card = (Map) info.get("card");
                cancelInfo.put("cardCompany", card.get("company"));

                commons.printLogByMap(cancelInfo);

                disChargeInfo.put("memberId", memberId);
                disChargeInfo.put("disChargePoint", cancelPoint);

                updateMemberTime.put("updateTime", cancelDate);
                updateMemberTime.put("memberId", memberId);

                pointDao.setCancelCard(cancelInfo);
                pointDao.pointDown(disChargeInfo);
                pointDao.pointUpdateMemberTime(updateMemberTime);

                return true;
            } else if ("????????????".equals(method) || "????????????" == method) {
                Map transfer = (Map) info.get("transfer");
                cancelInfo.put("transferBank", transfer.get("bank"));

                commons.printLogByMap(cancelInfo);

                disChargeInfo.put("memberId", memberId);
                disChargeInfo.put("disChargePoint", cancelPoint);

                updateMemberTime.put("updateTime", cancelDate);
                updateMemberTime.put("memberId", memberId);

                pointDao.setCancelTransfer(cancelInfo);
                pointDao.pointDown(disChargeInfo);
                pointDao.pointUpdateMemberTime(updateMemberTime);

                return true;
            } else {
                return false;
            }
        } else {
            log.info("status is not CANCELED");
            return false;
        }
    }


    // point table update CancelReq
    public void setCancelResult(String paymentKey) throws Exception {
        if (StringUtils.isEmpty(paymentKey)) {
            log.info("paymentKey isEmpty");
        } else {
            pointDao.cancelPay(paymentKey);
        }
    }

    public boolean chkBidderSeller(Map<String, Object> info) throws Exception {
        int result = 0;

        if (info.isEmpty()) {
            log.error("info isEmpty");
            return false;
        } else {
            result = pointDao.bidderSeller(info);

            if (result == 1) {
                log.info("bidder and seller match is Ok");
                return true;
            } else {
                log.info("Do not match bidder and seller");
                return false;
            }
        }
    }

    // ???????????? ?????? ??????
    @Transactional(
            isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED,
            rollbackFor = Exception.class)
    public void pointUpDownBySuccessBidProduct(Map<String, Object> info) throws Exception {
        Map<String, Object> upPointInfo = new HashMap<>();
        Map<String, Object> downPointInfo = new HashMap<>();
        Map<String, Object> updateMemberTime = new HashMap<>();

        boolean priceChk = false;

        int targetId = 0;
        String bidder = "";
        String seller = "";
        int price = 0;

        targetId = (int) info.get("successBidNo");
        bidder = (String) info.get("bidder");
        seller = (String) info.get("seller");
        price = (int) info.get("bidPrice");

        if (info.isEmpty()) {
            log.info("info isEmpty");

            return;
        } else {
            downPointInfo.put("memberId", bidder);
            downPointInfo.put("disChargePoint", price);

            upPointInfo.put("memberId", seller);
            upPointInfo.put("chargePoint", price);

            updateMemberTime.put("memberId", bidder);
            updateMemberTime.put("updateTime", commons.getNowTime());

            pointDao.pointDown(downPointInfo);
            pointDao.pointUpMember(upPointInfo);
            pointDao.pointUpdateMemberTime(updateMemberTime);

            pointDao.closeSuccessBid(targetId);
        }
    }

    public boolean memberPriceChk(Map<String, Object> info) throws Exception {
        Map<String, Object> params = new HashMap<>();
        String result = "";

        String memberId = (String) info.get("bidder");
        int price = (int) info.get("bidPrice");

        if (StringUtils.isEmpty(memberId) || price == 0) {
            log.error("info isEmpty");
            return false;
        } else {
            params.put("memberId", memberId);
            params.put("price", price);

            result = pointDao.memberPayPriceAllow(params);

            if ("OK".equals(result)) {
                log.info("Point pay is Ok");
                return true;
            } else {
                log.info("Point pay is No");
                return false;
            }
        }

    }

    public void pointDown(Map<String, Object> info) throws Exception {
        if (info.isEmpty()) {
            log.info("info isEmpty");
            return;
        } else {
            pointDao.pointDown(info);
        }
    }
}