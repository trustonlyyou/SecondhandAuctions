package com.secondhandauctions.service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.secondhandauctions.utils.Commons;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class KakaoService {

    @Autowired
    private Commons commons;

    private final String CLIENT_INFO_URL = "";

    @Value("${REDIRECT_URI}")
    private String redirectURI;

    @Value("${OAUTH_TOKEN_URL}")
    private String oauthTokenURL;

    @Value("${CLIENT_INFO_URL}")
    private String clientInfoURL;

    /**
     *
     * POST /oauth/token HTTP/1.1
     * Host: kauth.kakao.com
     * Content-type: application/x-www-form-urlencoded;charset=utf-8
     *
     * Access Token	사용자를 인증합니다.
     * Android, iOS : 12시간
     * JavaScript: 2 시간
     * REST API : 6시간
     *
     * Refresh Token	일정 기간 동안 다시 인증 절차를 거치지 않고도 액세스 토큰 발급을 받을 수 있게 해 줍니다.	2달
     * 유효기간 1달 남은 시점부터 갱신 가능
     *
     * ==============<필수>==============
     * grant_type	String	authorization_code로 고정	O
     * client_id	String	앱 REST API 키 [내 애플리케이션] > [앱 키]에서 확인 가능	O
     * redirect_uri	String	인가 코드가 리다이렉트된 URI	O
     * code	String	인가 코드 받기 요청으로 얻은 인가 코드
     *
     */
    public Map getKakaoToken(String code, String client_id) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        ObjectMapper objectMapper = new ObjectMapper();

        Map oAuthToken = new HashMap();

        params.add("grant_type", "authorization_code");
        params.add("client_id", client_id);
        params.add("redirect_uri", redirectURI);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                oauthTokenURL,
                HttpMethod.POST,
                request,
                String.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            oAuthToken = objectMapper.readValue(response.getBody(), Map.class);
            log.info(oAuthToken.toString());

            return oAuthToken;
        } else {
            log.error("status :: '{}'", response.getStatusCode().toString());
            return null;
        }
    }

    /**
     * GET/POST /v2/user/me HTTP/1.1
     * Host: kapi.kakao.com
     * Authorization: Bearer {ACCESS_TOKEN}
     * Content-type: application/x-www-form-urlencoded;charset=utf-8
     *
     * Header
     * Name	Description	Required
     * Authorization	사용자 인증 수단, 액세스 토큰 값
     * Authorization: Bearer {ACCESS_TOKEN}	O
     *
     *
     */
    public Map<String, Object> getKakaoClientInfo(String access_token) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> userInfo = new HashMap<>();
        Map<String, Object> result = new HashMap<>();

        String memberName = "";
        String memberEmail = "";

        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        headers.add("Authorization", "Bearer " + access_token);

        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(
                clientInfoURL,
                HttpMethod.POST,
                kakaoProfileRequest,
                String.class
        );

        if (response.getStatusCode() == HttpStatus.OK) {
            JSONParser parser = new JSONParser();
            JSONObject object = null;

            object = (JSONObject) parser.parse(response.getBody());

            userInfo = objectMapper.readValue(object.toString(), Map.class);

            Map propertiesMap = (Map) userInfo.get("properties");
            Map accountMap = (Map) userInfo.get("kakao_account");

            memberName = (String) propertiesMap.get("nickname");
            memberEmail = (String) accountMap.get("email");
            log.info("memberName :: '{}'", memberName);
            log.info("memberEmail :: '{}'", memberEmail);

            if (StringUtils.isEmpty(memberName) || StringUtils.isEmpty(memberEmail)) {
                log.error("name || email is empty");
                return Collections.emptyMap();
            }

            result.put("memberName", memberName);
            result.put("memberEmail", memberEmail);

            return result;
        } else {
            log.error("Status code :: '{}'", response.getStatusCode().toString());

            return Collections.emptyMap();
        }
    }
}
