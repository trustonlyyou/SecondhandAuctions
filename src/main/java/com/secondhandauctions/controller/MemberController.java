package com.secondhandauctions.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.secondhandauctions.service.EmailService;
import com.secondhandauctions.service.KakaoService;
import com.secondhandauctions.service.MemberService;
import com.secondhandauctions.service.SmsService;
import com.secondhandauctions.utils.Commons;
import com.secondhandauctions.utils.EncryptionSHA256;
import com.secondhandauctions.utils.InfoFormatter;
import com.secondhandauctions.vo.MemberVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/member")
@Slf4j
public class MemberController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private SmsService smsService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private EncryptionSHA256 encryption;

    @Autowired
    private Commons commons;

    @Autowired
    private InfoFormatter formatter;

    @Value("${KAKAO_JS}")
    private String KAKAO_KEY;

    @Autowired
    private KakaoService kakaoService;

    // =============== 회원가입 ===============

    @GetMapping(value = "/join/list")
    public String joinList() {
        return "member/joinList";
    }

    @GetMapping(value = "/join/form")
    public String joinForm(HttpServletRequest request, HttpServletResponse response) {

        log.info(Commons.getClientIp(request));

        return "member/join";
    }

    @PostMapping(value = "/join/idCheck", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Integer> idCheck(@RequestBody String memberId) throws Exception {
        Map<String, Integer> result = new HashMap<>();
        int isIdCheck = 0;

        if (StringUtils.isEmpty(memberId)) {
            log.error("Input memberId is null");
            result.put("result", -1);
            return result;
        }

        isIdCheck = memberService.idCheck(memberId);
        result.put("result", isIdCheck);

        log.info("IdCheck :: {}", isIdCheck);

        return result;
    }

    // 이메일 중복 체크
    @PostMapping(value = "/emailCheck", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Integer> emailCheck(@RequestBody String email) throws Exception {
        Map<String, Integer> result = new HashMap<>();
        int isEmailCheck = 0;

        if (StringUtils.isEmpty(email)) {
            log.error("Input email is null");
            result.put("result", -1);
        }

        isEmailCheck = memberService.isEmail(email);
        result.put("result", isEmailCheck);

        log.info("emailCheck Result :: " + isEmailCheck);

        return result;
    }

    @PostMapping(value = "/sendEmail", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> sendEmail(@RequestBody String email) throws Exception {
        Map<String, Object> result = new HashMap<>();
        String num = "";
        int checkNum = 0;

        checkNum = emailService.certificationSendEmail(email);

        if (checkNum != 0) {
            num = Integer.toString(checkNum);
            result.put("num", num);

            return  result;
        } else {
            log.error("emailService joinCheckSendEmail return is 0");
            result.put("num", 0);

            return result;
        }
    }

    // 사용자 핸드폰 check
    @PostMapping("/phoneCheck")
    @ResponseBody
    public int isCheckPhone(String memberName, String memberPhone) throws Exception {
        Map<String, Object> prams = new HashMap<>();
        int result = 0;

        if (StringUtils.isEmpty(memberName) || StringUtils.isEmpty(memberPhone)) {
            log.error("memberName or memberPhone is null");
            result = -1;

            return result;
        }

        prams.put("memberName", memberName);
        prams.put("memberPhone", memberPhone);

        result = memberService.isPhone(prams);

        return result;
    }

    // 핸드폰 인정번호 전송
    @PostMapping(value = "/check/phone/sendSms")
    @ResponseBody
    public Map<String, Object> searchIdFromPhone(String memberPhone) throws Exception {
        Map<String, Object> map = new HashMap<>();
        String strNum = "";

        strNum = smsService.authenticationNum(memberPhone);

        if (("".equals(strNum)) || (strNum == null)) {
            log.info("smsService sendSms return is null");
        }

        map.put("key", strNum);

        return map;
    }

    @PostMapping(value = "/join/action")
    public String joinAction(MemberVo memberVo, HttpServletRequest request,
                             HttpServletResponse response, RedirectAttributes redirectAttributes) throws Exception {
        String encryptionPwd = "";
        String inputPassword = "";

        inputPassword = memberVo.getMemberPassword();
        encryption.setMemberVoEncryptionPassword(inputPassword, memberVo);

        memberService.setMember(memberVo);

        redirectAttributes.addFlashAttribute("memberId", memberVo.getMemberId());

        log.info("join success");

        return "redirect:/member/join/result";
    }

    @GetMapping(value = "join/result")
    public String join_result() {
        return "member/join_result";
    }

    // ======================== 회원 가입 끝 ==============================



    // ======================== 로그인 ============================
    @GetMapping(value = "/login/list")
    public String loginList() {
        return "member/loginList";
    }

    @GetMapping(value = "/login/form")
    public String loginForm() {
        return "member/login";
    }

    @PostMapping(value = "/login/submit")
    @ResponseBody
    public Map<String, Object> loginSubmit(String memberId, String memberPassword, HttpServletRequest request) throws Exception {
        Map<String, Object> info = new HashMap<>();
        Map<String, Object> result = new HashMap<>();
        String encryptionPassword = "";
        int loginResult = 0;

        if (StringUtils.isEmpty(memberId) || StringUtils.isEmpty(memberPassword)) {
            result.put("result", 0);
        }

        encryptionPassword = encryption.encrypt(memberPassword);

        info.put("memberId", memberId);
        info.put("memberPassword", encryptionPassword);

        loginResult = memberService.getLoginResult(info);

        log.info("loginResult :: {}", loginResult);

        if (loginResult == 1) {
            commons.setMemberSession(request, memberId);
        }

        result.put("result", loginResult);

        return result;
    }

    // ================= 카카오 로그인 =================
    // TODO: 2021/12/24 카카오 로그인 (기존 회원 check, 추가 내용 받기(핸드폰 번호)
    /**
     * GET /oauth/authorize?client_id={REST_API_KEY}&redirect_uri={REDIRECT_URI}&response_type=code HTTP/1.1
     * Host: kauth.kakao.com
     */
    @GetMapping("/kakao/login/init")
    public String initKakao() {
        StringBuffer stringBuffer = new StringBuffer();
        String redirectURI = "http://localhost:8080/member/kakao/login/callback";

        stringBuffer.append("https://kauth.kakao.com/oauth/authorize?client_id=");
        stringBuffer.append(KAKAO_KEY);
        stringBuffer.append("&redirect_uri=");
        stringBuffer.append(redirectURI);
        stringBuffer.append("&response_type=code");

        return "redirect:"+stringBuffer.toString();
    }

    // Kakao login
    @GetMapping(value = "/kakao/login/callback")
    public String kakaoCallback(@RequestParam String code, Model model) throws Exception {

//        RestTemplate restTemplate = new RestTemplate();
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
//
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("grant_type", "authorization_code");
//        params.add("client_id", KAKAO_KEY);
//        params.add("redirect_uri", "http://localhost:8080/member/kakao/login/callback");
//        params.add("code", code);
//
//        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(params, headers);
//
//        // 요청 - 응답
//        ResponseEntity<String> response = restTemplate.exchange(
//                "https://kauth.kakao.com/oauth/token",
//                HttpMethod.POST, tokenRequest, String.class
//        );
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        OAuthToken oAuthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);
//
//        log.info(oAuthToken.getAccess_token());
//
//        ResponseEntity<String> userInfo = kakaoService.getKakaoClientInfo(oAuthToken.getAccess_token());
//
//        log.info(userInfo.getBody());
//
//        return userInfo.getBody();


        Map<String, Object> memberInfo = new HashMap<>();
        Map oAuthToken = new HashMap();
        String accessToken = "";

        oAuthToken = kakaoService.getKakaoToken(code, KAKAO_KEY);

        if (oAuthToken.isEmpty()) {
            log.error("token isEmpty");
            return "";
        }

        accessToken = (String) oAuthToken.get("access_token");

        memberInfo = kakaoService.getKakaoClientInfo(accessToken);

        if (memberInfo.isEmpty()) {
            log.error("memberInfo is empty");
            return "";
        }

        model.addAttribute("memberName", memberInfo.get("memberName"));
        model.addAttribute("memberEmail", memberInfo.get("memberEmail"));

        return "member/kakaoLoginResult";
    }

    @GetMapping(value = "/logout/action")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();

        session.invalidate();

        return "redirect:/";
    }


    // ================ 아이디 비밀번호 찾기 ================

    @GetMapping(value = "/search/id")
    public String searchIdForm() {
        return "member/searchId";
    }

    // 이메일로 아이디 찾기
    @PostMapping(value = "/search/id/email")
    @ResponseBody
    public Map<String, Object> searchIdFromEmail(HttpServletRequest request, String memberName, String memberEmail) throws Exception {
        Map<String, Object> info = new HashMap<>();
        Map<String, Object> result = new HashMap<>();

        String memberId = "";
        String title = "";
        String content = "";
        int check = 0;

        if (StringUtils.isEmpty(memberName) || StringUtils.isEmpty(memberEmail)) {
            log.error("Request URI :: '{}'", request.getRequestURI());
            log.error("memberName or memberEmail is null");

            result.put("check", -1);

            return result;
        }

        info.put("memberName", memberName);
        info.put("memberEmail", memberEmail);

        result = memberService.getMemberIdFromEmail(info);

        check = (int) result.get("check");

        log.info("Search id from email check {}", check);

        if (check == 1) {
            memberId = (String) result.get("memberId");

            title = "중고 경매의 세계 아이디 찾기 이메일 입니다.";
            content = "아이디 찾기 서비스를 이용해 해주셔서 감사합니다." +
                    "아이디는 " + memberId + " 입니다.";

            emailService.sendEmail(memberEmail, title, content);
        }

        return result;
    }

    @GetMapping(value = "/searchIdResult")
    public String IdResultEmail() {
        return "/member/searchIdResultEmail";
    }


    @PostMapping(value = "/search/id/phone")
    @ResponseBody
    public Map<String, Object> searchIdFromPhone(String memberName, String memberPhone) throws Exception {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> info = new HashMap<>();

        String memberId = "";

        info.put("memberName", memberName);
        info.put("memberPhone", memberPhone);

        memberId = memberService.getMemberIdFromPhone(info);

        if (StringUtils.isEmpty(memberId)) {
            result.put("check", 0);
            result.put("memberId", "");
        }

        result.put("check", 1);
        result.put("memberId", memberId);

        return result;
    }

    @GetMapping(value = "/searchIdResultPhone")
    public String IdResultPhone(@RequestParam String memberId, Model model) {
        model.addAttribute("memberId", memberId);

        return "member/searchIdResultPhone";
    }

    // 비밀번호 찾기
    @GetMapping(value = "/search/password")
    public String searchPasswordForm() {
        return "member/searchPassword";
    }

    @PostMapping(value = "/search/password/phone")
    @ResponseBody
    public Map<String, Integer> searchPasswordFromPhone(HttpServletRequest request, String memberId,
                                                        String memberName, String memberPhone) throws Exception {
        Map<String, Object> info = new HashMap<>();
        Map<String, Integer> result = new HashMap<>();

        int check = 0;

        info.put("memberId", memberId);
        info.put("memberName", memberName);
        info.put("memberPhone", memberPhone);

        check = memberService.isSearchPwdFromPhone(info);

        log.info("Check search password from phone :: {}", check);

        result.put("check", check);

        if (check == 1) {
            commons.setMemberSession(request, memberId);
        }

        return result;
    }

    @PostMapping(value = "/search/password/email")
    @ResponseBody
    public Map<String, Object> searchPasswordEmail(HttpServletRequest request, String memberIdEmail, String memberEmail) throws Exception {
        Map<String, Object> info = new HashMap<>();
        Map<String, Object> result = new HashMap<>();

        int check = 0;

        log.info("memberId :: " + memberIdEmail);
        log.info("memberEmail :: " + memberEmail);

        info.put("memberId", memberIdEmail);
        info.put("memberEmail", memberEmail);

        check = memberService.isSearchPwdFromEmail(info);

        log.info("Check search password from email :: {}", check);

        result.put("check", check);

        if (check == 1) {
            commons.setMemberSession(request, memberIdEmail);
        }

        return result;
    }

    @GetMapping(value = "/password/modify/form")
    public String modifyPassword(HttpServletRequest request) {

        return "member/modifyPassword";
    }

    @PostMapping(value = "/modify/password")
    @ResponseBody
    public Map<String, Integer> modifyPassword(HttpServletRequest request, String memberPassword) throws Exception {
        Map<String, Object> info = new HashMap<>();
        Map<String, Integer> result = new HashMap<>();

        String memberId = "";
        String encryptionPassword = "";
        int check = 0;

        if (!(StringUtils.isEmpty(memberId) || StringUtils.isEmpty(memberPassword))) {
            log.info("memberId || memberPassword isEmpty");
            result.put("check", 0);

            return result;
        }

        memberId = commons.getMemberSession(request);
        encryptionPassword = encryption.encrypt(memberPassword);

        info.put("memberId", memberId);
        info.put("memberPassword", encryptionPassword);

        check = memberService.setPassword(info);

        log.info("modify password result {}" , check);

        if (check == 1) {
            request.getSession().invalidate();
        }

        result.put("check", check);

        return result;
    }

    @GetMapping(value = "/modify/password/result")
    public String modifyResult() {
        return "member/modifyPasswordResult";
    }
}
