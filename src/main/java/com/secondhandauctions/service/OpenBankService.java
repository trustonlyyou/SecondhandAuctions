package com.secondhandauctions.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.secondhandauctions.dao.OpenBankDao;
import com.secondhandauctions.utils.Commons;
import com.secondhandauctions.utils.OpenBankCommonsUtils;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Service
@Slf4j
public class OpenBankService {

    @Value("${client_id}")
    private String client_id;

    @Value("${client_secret}")
    private String client_secret;

    @Autowired
    private OpenBankDao openBankDao;

    @Autowired
    private Commons commons;

    @Autowired
    private OpenBankCommonsUtils openBankCommonsUtils;

    public Map<String, Object> isAccount(String memberId) throws Exception {
        Map<String, Object> accountChk = new HashMap<>();
        Map<String, Object> result = new HashMap<>();

        int chk = 0;

        accountChk = openBankDao.accountChk(memberId);
        chk = Integer.parseInt(String.valueOf(accountChk.get("count")));

        if (chk == 1) {
            result.put("accountChk", true);
        } else {
            result.put("accountChk", false);
        }
        result.put("token", accountChk.get("token"));

        return result;
    }

    public Map callbackOpenBankOauth(String code) throws Exception {
        Map oauthResult = new HashMap();
        MultiValueMap params = new LinkedMultiValueMap();
        HttpHeaders headers = new HttpHeaders();
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();

        String requestUri = "https://testapi.openbanking.or.kr/oauth/2.0/token";
        String redirectUri = "http://localhost:8080/oauth/callback";

        headers.add("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");

        params.add("client_id", client_id);
        params.add("client_secret", client_secret);
        params.add("scope", "oob"); // 고정
        params.add("grant_type", "client_credentials"); // 고정

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                requestUri, HttpMethod.POST, request, String.class
        );
        oauthResult = objectMapper.readValue(response.getBody(), Map.class);

        log.info(response.toString());

        return oauthResult;
    }

    public void setToken(Map<String, Object> info) throws Exception {
        if (info.isEmpty()) {
            log.error("token info isEmpty");
            return;
        } else {
            openBankDao.setToken(info);
        }
    }

    public Map realName(HttpServletRequest servletRequest, Map<String, Object> info) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> params = new HashMap<>();
        HttpSession session = servletRequest.getSession();
        Map result = new HashMap();

        String requestUri = "https://testapi.openbanking.or.kr/v2.0/inquiry/real_name";
        String token = openBankDao.getToken(commons.getMemberSession(servletRequest));

        if (StringUtils.isEmpty(token)) {
            log.error("token is null");
            return Collections.emptyMap();
        }

        headers.set("Authorization", "Bearer " + token);
        headers.set("Content-Type", "application/json; charset=UTF-8");

        log.info("====================header info====================");
        log.info(headers.toString());

        params.put("bank_tran_id", info.get("bank_tran_id")); // 은행 고유 번호 bank_tran_id
        params.put("bank_code_std", info.get("bank_code_std")); // 개설기관 bank_code_std
        params.put("account_num", info.get("account_num")); // account_num
        params.put("account_holder_info_type", "");
        params.put("account_holder_info", info.get("account_holder_info")); // account_holder_info
        params.put("tran_dtime", info.get("tran_dtime")); // tran_dtime

        log.info("====================service params====================");
        log.info(params.toString());

        HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(params), headers);

        ResponseEntity<String> response = restTemplate.exchange(
                requestUri, HttpMethod.POST, request, String.class
        );

        JSONParser parser = new JSONParser();
        JSONObject object = new JSONObject();

        object = (JSONObject) parser.parse(response.getBody());
        result = objectMapper.readValue(object.toString(), Map.class);

        log.info(result.toString());

        return result;
    }

    public void setBankInfo(Map<String, Object> info) throws Exception {
        if (info.isEmpty()) {
            log.error("bank info isEmpty");
            return;
        } else {
            openBankDao.setBankInfo(info);
        }
    }

    public Map<String, Object> getBankInfo(String memberId) throws Exception {
        if (StringUtils.isEmpty(memberId)) {
            return Collections.emptyMap();
        } else {
            return openBankDao.getBankInfo(memberId);
        }
    }

    /**
     *
     * // request
     *
     * {
     *   "cntr_account_type": "N",
     *   "cntr_account_num": "200000000001",
     *   "wd_pass_phrase": "NONE",
     *   "wd_print_content": "출금",
     *   "name_check_option": "off",
     *   "tran_dtime": "20220119084046",
     *   "req_cnt": "1",
     *   "req_list": [
     *     {
     *       "tran_no": "1",
     *       "bank_tran_id": "M202200113U314878129",
     *       "bank_code_std": "088",
     *       "account_num": "110409877530",
     *       "account_holder_name": "오정환",
     *       "print_content": "환불금액",
     *       "tran_amt": "10000",
     *       "req_client_name": "오정환",
     *       "req_client_bank_code": "088",
     *       "req_client_account_num": "110409877530",
     *       "req_client_num": "MINEMUSIC",
     *       "transfer_purpose": "TR"
     *     }
     *   ]
     * }
     */

    // 포인트 환전
    public Map transferAccountOfPointExchange(Map<String, Object> bankInfo, String memberId, String reqPoint) throws Exception {
        HttpHeaders headers = new HttpHeaders();
        RestTemplate restTemplate = new RestTemplate();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> reqListParams = new HashMap<>();
        Map result = new HashMap();

        String requestUri = "https://testapi.openbanking.or.kr/v2.0/transfer/deposit/acnt_num";
        int reqPointOfInt = commons.convertPrice(reqPoint);
        String token = (String) bankInfo.get("token");
        String bankName = (String) bankInfo.get("bankName");
        String bankCode = (String) bankInfo.get("bankCode");
        String accountNum = (String) bankInfo.get("accountNum");
        String accountHolder = (String) bankInfo.get("accountHolder");
        String tran_dtime = openBankCommonsUtils.getTranDtime();
        String bank_tran_id = openBankCommonsUtils.getBankTranId();

        headers.set("Authorization", "Bearer " + token);
        headers.set("Content-Type", "application/json; charset=UTF-8");
        headers.set("scope", "oob");

        reqListParams.put("tran_no", "1"); // 거래순번 1 고정
        reqListParams.put("bank_tran_id", bank_tran_id);
        reqListParams.put("bank_code_std", bankCode);
        reqListParams.put("account_num", accountNum);
        reqListParams.put("account_holder_name", accountHolder);
        reqListParams.put("print_content", "포인트환전");
        reqListParams.put("tran_amt", reqPointOfInt);
        reqListParams.put("req_client_name", accountHolder);
        reqListParams.put("req_client_bank_code", bankCode);
        reqListParams.put("req_client_account_num", accountNum);
        reqListParams.put("req_client_num", memberId.toUpperCase());
        reqListParams.put("transfer_purpose", "TR");

        List paramList = new ArrayList();
        paramList.add(reqListParams);

        params.put("cntr_account_type", "N"); // 약정계좌 = 'N'
        params.put("cntr_account_num", "200000000001"); // 약정계좌
        params.put("wd_pass_phrase", "NONE"); // Test 환경에서는 NONE 고정
        params.put("wd_print_content", "포인트환전");
        params.put("name_check_option", "off"); // 수치인 검증 => 이미 계좌등록에서 확인함
        params.put("tran_dtime", tran_dtime);
        params.put("req_cnt", 1); // 요청건수 1 고정 => 과거에는 여러건 제공함 현재 제공X
        params.put("req_list", paramList);

        HttpEntity<String> request = new HttpEntity<>(objectMapper.writeValueAsString(params), headers);

        log.info("===========================request===========================");
        log.info(request.toString());

        ResponseEntity<String> response = restTemplate.exchange(
            requestUri, HttpMethod.POST, request, String.class
        );

        log.info("response :: " + response.toString());

        JSONParser parser = new JSONParser();
        JSONObject object = new JSONObject();

        object = (JSONObject) parser.parse(response.getBody());
        result = objectMapper.readValue(object.toString(), Map.class);

        log.info(result.toString());

        return result;
    }

    public void setExchangeInfo(Map<String, Object> info) throws Exception {
        if (info.isEmpty()) {
            log.info("info isEmpty");
            return;
        } else {
            openBankDao.setExchangeInfo(info);
        }
    }
}
