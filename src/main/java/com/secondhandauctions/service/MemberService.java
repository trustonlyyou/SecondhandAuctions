package com.secondhandauctions.service;

import com.secondhandauctions.dao.MemberDao;
import com.secondhandauctions.utils.Commons;
import com.secondhandauctions.vo.MemberVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class MemberService {

    private static final Logger logger = LoggerFactory.getLogger(MemberService.class);

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private EmailService emailService;

    @Autowired
    private SmsService smsService;


    public int idCheck(String memberId) throws Exception {
        return memberDao.idCheck(memberId);
    }

    public void setMember(MemberVo memberVo) throws Exception {
        memberDao.join(memberVo);
    }

    public int getLoginResult(Map<String, Object> loginInfo) throws Exception {
        String memberId = "";
        String memberPassword = "";

        if (loginInfo.isEmpty()) {
            log.error("loginInfo parma isEmpty");
            return 0;
        }

        memberId = (String) loginInfo.get("memberId");
        memberPassword = (String) loginInfo.get("memberPassword");

        if (StringUtils.isEmpty(memberId) || StringUtils.isEmpty(memberPassword)) {
            log.error("memberId isEmpty result :: '{}'", StringUtils.isEmpty(memberId));
            log.error("memberPassword isEmpty result :: '{}'", StringUtils.isEmpty(memberPassword));
            return 0;
        }

        return memberDao.login(loginInfo);
    }

    public MemberVo getMemberInfo(String memberId) throws Exception {
        return memberDao.memberInfo(memberId);
    }

    public int isEmail(String memberEmail) throws Exception {
        int result = 0;

        result = memberDao.isMemberEmail(memberEmail);

        return result;
    }

    public int isPhone(Map<String, Object> params) throws Exception {
        int result = 0;

        result = memberDao.isMemberPhone(params);

        return result;
    }

    // 이메일로 아이디 찾기
    public Map<String, Object> getMemberIdFromEmail(Map<String, Object> info) throws Exception {
        Map<String, Object> result = new HashMap<>();

        String memberId = "";

        int check = 0;

        memberId = memberDao.searchIdEmail(info);

        if (("".equals(memberId)) || (memberId == null)) {
            result.put("check", check);

            return result;
        }

        check = 1;

        result.put("check", check);
        result.put("memberId", memberId);

        return result;
    }

    // 핸드폰으로 아이디 찾기
    public String getMemberIdFromPhone(Map<String, Object> memberInfo) throws Exception {
        int check = 0;
        String memberId = "";

        check = memberDao.isMemberPhone(memberInfo);

        if (check == 0) {
            return memberId;
        }

        memberId = memberDao.searchIdPhone(memberInfo);

        return memberId;

    }

    // 핸드폰으로 비밀번호 찾기
    public int isSearchPwdFromPhone(Map<String, Object> memberInfo) throws Exception {
        int check = 0;

        check = memberDao.checkSearchPwdPhone(memberInfo);

        return check;
    }

    // 이메일로 비밀번호 찾기
    public int isSearchPwdFromEmail(Map<String, Object> memberInfo) throws Exception {
        int check = 0;

        check = memberDao.checkSearchPwdEmail(memberInfo);

        return check;
    }

    // 비밀 번호 수정
    public int setPassword(Map<String, Object> memberInfo) throws Exception {
        int check = 0;

        check = memberDao.modifyPassword(memberInfo);

        return check;
    }

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

//    public String getKakaoToken(String code, String clientId) throws Exception {
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//
//        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("grant_type", "authorization_code");
//        params.add("client_id", clientId);
//        params.add("redirect_uri", "http://localhost:8080/member/kakao/login/callback");
//        params.add("code", code);
//
//        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(params, headers);
//
//        ResponseEntity<String> response = restTemplate.exchange(
//                "https://kauth.kakao.com/oauth/token",
//                HttpMethod.POST, tokenRequest, String.class
//        );
//
//        return "";
//
//    }

}
