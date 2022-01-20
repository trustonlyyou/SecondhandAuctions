package com.secondhandauctions.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.secondhandauctions.dao.OpenBankDao;
import com.secondhandauctions.utils.Commons;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

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
//        client_id = [cliend_id 값]
//        client_secret = [client_secret 값]
//        scope = oob(고정값)
//        grant_type = client_credentials(고정 값)

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
//        params.add("redirect_uri", redirectUri);

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
}
