package com.secondhandauctions.controller;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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

    @Value("${JOIN_REDIRECT_URI}")
    private String joinRedirectURI;

    @Value("${LOGIN_REDIRECT_URI}")
    private String loginRedirectURI;

    @Autowired
    private KakaoService kakaoService;

    // =============== 회원가입 ===============

    @GetMapping(value = "/join/list")
    public String joinList() {
        return "member/joinList";
    }

    @GetMapping(value = "/join/form")
    public String joinForm(HttpServletRequest request, HttpServletResponse response) {
        // 엑세스 로그 봐야한다.

        return "member/join";
    }

    // TODO: 2021/12/28 joinform 에서 아이디 check ajax 수
    @PostMapping(value = "/join/idCheck", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Boolean> idCheck(@RequestBody String memberId) throws Exception {
        Map<String, Boolean> result = new HashMap<>();
        boolean chk = false;

        if (StringUtils.isEmpty(memberId)) {
            log.error("Input memberId is null");
            result.put("result", false);
            return result;
        }

        chk = memberService.idCheck(memberId);
        result.put("result", chk);

        log.info("IdCheck :: {}", chk);

        return result;
    }

    // 이메일 중복 체크
    @PostMapping(value = "/emailCheck", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Boolean> emailCheck(@RequestBody String email) throws Exception {
        Map<String, Boolean> result = new HashMap<>();
        boolean isEmailCheck = false;

        if (StringUtils.isEmpty(email)) {
            log.error("Input email is null");
            result.put("result", false);
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
    public Map<String, Boolean> isCheckPhone(String memberName, String memberPhone) throws Exception {
        Map<String, Object> prams = new HashMap<>();
        Map<String, Boolean> result = new HashMap<>();
        boolean chk = false;

        if (StringUtils.isEmpty(memberName) || StringUtils.isEmpty(memberPhone)) {
            log.error("memberName or memberPhone is null");
            result.put("result", chk);
            return result;
        }

        prams.put("memberName", memberName);
        prams.put("memberPhone", memberPhone);

        chk = memberService.isPhone(prams);
        result.put("result", chk);

        log.info("phone check :: '{}'", chk);

        return result;
    }

    // 핸드폰 인정번호 전송
    @PostMapping(value = "/check/phone/sendSms")
    @ResponseBody
    public Map<String, Object> searchIdFromPhone(String memberPhone) throws Exception {
        Map<String, Object> map = new HashMap<>();
        String strNum = "";

        strNum = smsService.authenticationNum(memberPhone);

        if (StringUtils.isEmpty(strNum)) {
            log.info("smsService sendSms return is null");
        }

        map.put("key", strNum);

        return map;
    }

    @PostMapping(value = "/join/action")
    public String joinAction(MemberVo memberVo, RedirectAttributes redirectAttributes) throws Exception {
        String encryptionPwd = "";
        String inputPassword = "";

        inputPassword = memberVo.getMemberPassword();
        encryption.setMemberVoEncryptionPassword(inputPassword, memberVo);

        memberService.setMember(memberVo);
        memberService.setMemberIdFlag(memberVo.getMemberId());

        redirectAttributes.addFlashAttribute("memberId", memberVo.getMemberId());

        log.info("join success");

        return "redirect:/member/join/result";
    }

    @GetMapping(value = "join/result")
    public String join_result() {
        return "member/join_result";
    }

    @GetMapping(value = "/kakao/join/init")
    public String initkakaoJoin() {
        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append("https://kauth.kakao.com/oauth/authorize?client_id=");
        stringBuffer.append(KAKAO_KEY);
        stringBuffer.append("&redirect_uri=");
        stringBuffer.append(joinRedirectURI);
        stringBuffer.append("&response_type=code");

        return "redirect:"+stringBuffer.toString();
    }

    @GetMapping(value = "/kakao/join/callback")
    public String kakaoJoinCallback(@RequestParam String code, Model model) throws Exception {
        Map<String, Object> memberInfo = new HashMap<>();
        Map oAuthToken = new HashMap();
        String accessToken = "";
        String memberEmail = "";
        String memberName = "";
        boolean isMemberChk = false;

        oAuthToken = kakaoService.getOauthToken(code, KAKAO_KEY, joinRedirectURI);
        accessToken = (String) oAuthToken.get("access_token");
        memberInfo = kakaoService.getKakaoClientInfo(accessToken);
        memberEmail = (String) memberInfo.get("memberEmail");
        memberName = (String) memberInfo.get("memberName");

        isMemberChk = memberService.isEmail(memberEmail);

        if (isMemberChk == false) {
            log.info("kakao join result :: '{}'", isMemberChk);
            model.addAttribute("msg", "이미 가입된 회원입니다.");
            return "member/kakaoJoinResult";
        } else { // 추가 기입을 받자
            log.info("kakao join result :: '{}'", isMemberChk);
            model.addAttribute("memberName", memberName);
            model.addAttribute("memberEmail", memberEmail);
            return "member/kakaoJoin";
        }
    }

    @PostMapping(value = "/kakao/join/submit")
    @ResponseBody
    public Map<String, Boolean> kakaoJoinSubmit(String memberId, String memberPhone, String memberName, String memberEmail) throws Exception {
        Map<String, String> memberInfo = new HashMap<>();
        Map<String, Boolean> result = new HashMap<>();

        boolean itemChk = false;
        boolean chk = false;

        itemChk = commons.isEmptyStrings(memberId, memberPhone, memberName, memberEmail);

        if (itemChk) {
            log.error("memberId || memberPhone isEmpty");
            result.put("result", false);

            return result;
        } else {
            memberInfo.put("memberId", memberId);
            memberInfo.put("memberPhone", memberPhone);
            memberInfo.put("memberName", memberName);
            memberInfo.put("memberEmail", memberEmail);

            chk = memberService.setMemberKako(memberInfo);
            log.info("kakao join submit result :: '{}'", chk);
            result.put("result", chk);

            return result;
        }
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
        Map<String, Boolean> loginResult = new HashMap<>();

        String encryptionPassword = "";
        int failCount = 0;
        int tmpFailCount = 0;
        boolean login = false;
        boolean isLock = false;
        boolean isMember = false;

        if (commons.isEmptyStrings(memberId, memberPassword) == true) {
            result.put("result", false);
        }

        encryptionPassword = encryption.encrypt(memberPassword);

        info.put("memberId", memberId);
        info.put("memberPassword", encryptionPassword);

        loginResult = memberService.getLoginResult(info);
        isMember = loginResult.get("isMember");

        if (isMember == false) {
            result.put("isMember", false);
            return result;
        }

        login = loginResult.get("chk");
        isLock = loginResult.get("isLock");
        failCount = memberService.getLoginFileCount(memberId);
        tmpFailCount = failCount + 1;

        log.info("login is Ok '{}'", login);
        log.info("login lock is '{}'", isLock);

        if (isMember == false) {
            result.put("login", false);
            result.put("isLock", false);
            result.put("isMember" ,false);
            return result;
        } else {
            result.put("isMember", true);
            if (isLock == true) { // login lock
                result.put("memberId", memberId);
                result.put("login", false);
                result.put("isLock", true);

                return result;
            } else { // non login lock
                if (login == false) {// 로그인 실패
                    if (tmpFailCount >= 5) { // client fail login count 5

                        if (tmpFailCount > 5) {
                            memberService.memberLock(memberId);
                        } else {
                            memberService.failCountUp(memberId);
                            memberService.memberLock(memberId);
                        }

                        result.put("login", false);
                        result.put("failCount", tmpFailCount);
                        result.put("isLock", true);

                        return result;
                    } else {
                        memberService.failCountUp(memberId);

                        result.put("login", false);
                        result.put("failCount", tmpFailCount);
                        result.put("isLock", false);

                        return result;
                    }
                } else { // 로그인 성공
                    result.put("login", true);
                    result.put("isLock", false);
                    // 로그인 성공시 failCount reset
                    memberService.resetFailCount(memberId);
                    commons.setMemberSession(request, memberId);
                    return  result;
                }
            }
        }
   }
    // TODO: 2021/12/30 getLoginResult Service usages MyPageController
    // TODO: 2021/12/30 로그인 할 때 inner join 해서 isLock check 해야한다. 수정하자. 쿼리부터 시작하면 된다.

    @GetMapping(value = "/lock/solve")
    public String memberLockSolve() {
        return "member/solveLock";
    }

    @PostMapping(value = "/lock/solve/phone")
    @ResponseBody
    public Map<String, Object> memberLockSolveFromPhone(String memberId, String memberName, String memberPhone,
                                                        HttpServletRequest request) throws Exception {
        Map<String, String> info = new HashMap<>();
        Map<String, Object> result = new HashMap<>();

        boolean chk = false;

        if (commons.isEmptyStrings(memberId, memberName, memberPhone) == true) {
            log.info("info isEmpty");
            result.put("result", false);

            return result;
        } else {
            info.put("memberId", memberId);
            info.put("memberName", memberName);
            info.put("memberPhone", memberPhone);

            chk = memberService.lockSolveFromPhone(info);

            log.info("lock solve result :: '{}'", chk);

            if (chk == false) {
                result.put("result", false);

                return result;
            } else {
                commons.setMemberSession(request, memberId);
                result.put("result", true);

                return result;
            }
        }
    }

    @PostMapping(value = "/lock/solve/email")
    @ResponseBody
    public Map<String, Object> memberLockSolveFromEmail(String memberIdEmail, String memberEmail,
                                                        HttpServletRequest request) throws Exception {

        Map<String, String> info = new HashMap<>();
        Map<String, Object> result = new HashMap<>();

        boolean chk = false;

        if (commons.isEmptyStrings(memberIdEmail, memberEmail) == true) {
            log.info("info isEmpty");
            result.put("result", false);

            return result;
        } else {
            info.put("memberId", memberIdEmail);
            info.put("memberEmail", memberEmail);

            chk = memberService.lockSolveFormEmail(info);

            log.info("lock solve result :: '{}'", chk);

            if (chk == false) {
                result.put("result", false);

                return result;
            } else {
                commons.setMemberSession(request, memberIdEmail);
                result.put("result", true);

                return result;
            }
        }
    }


    // ================= 카카오 로그인 =================
    @GetMapping(value = "/kakao/login/init")
    public String initKakao() {
        StringBuffer stringBuffer = new StringBuffer();

        stringBuffer.append("https://kauth.kakao.com/oauth/authorize?client_id=");
        stringBuffer.append(KAKAO_KEY);
        stringBuffer.append("&redirect_uri=");
        stringBuffer.append(loginRedirectURI);
        stringBuffer.append("&response_type=code");

        return "redirect:"+stringBuffer.toString();
    }

    // Kakao login
    @GetMapping(value = "/kakao/login/callback")
    public String kakaoCallback(@RequestParam String code, HttpServletRequest request, Model model) throws Exception {
        Map<String, Object> memberInfo = new HashMap<>();
        Map oAuthToken = new HashMap();
        String accessToken = "";
        String memberEmail = "";
        String memberId = "";
        String memberNmae = "";
        boolean isMemberChk = false;

        try {
            oAuthToken = kakaoService.getOauthToken(code, KAKAO_KEY, loginRedirectURI);
            accessToken = (String) oAuthToken.get("access_token");

            if (StringUtils.isEmpty(accessToken)) {
                log.error("accessToken isEmpty");
                model.addAttribute("msg", "카카오에서 회원의 정보를 받아 올 수 없습니다.");
                return "member/kakaoLoginResult";
            }

        } catch (Exception e) {
            log.error(commons.printStackLog(e));
            model.addAttribute("msg", "카카오에서 회원의 정보를 받아 올 수 없습니다.");
            return "member/kakaoLoginResult";
        }

        memberInfo = kakaoService.getKakaoClientInfo(accessToken);
        memberEmail = (String) memberInfo.get("memberEmail");

        isMemberChk = memberService.isEmail(memberEmail); // 이메일을 이용하여 회원 check

        if (isMemberChk == true) {
            log.info("No members were found.");
            model.addAttribute("msg", "가입된 회원의 정보가 없습니다.");
            return "member/kakaoLoginResult";
        } else {
            memberId = memberService.getMemberIdByEmail(memberEmail);

            if (StringUtils.isEmpty(memberId)) {
                log.error("memberId isEmpty");
                model.addAttribute("msg", "가입된 회원의 정보가 없습니다.");
                return "member/kakaoLoginResult";
            } else {
                // 아이디 가져와서 세션에 저장하고
                commons.setMemberSession(request, memberId);
                commons.setKakaoToken(request, accessToken);
                return "redirect:/";
            }
        }
    }

    @GetMapping(value = "/logout/action")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();

        session.invalidate();

        return "redirect:/";
    }

    @GetMapping(value = "/kakao/logout")
    public String logoutKakao(HttpServletRequest request) throws Exception {
        HttpSession session = request.getSession();
        String token = (String) session.getAttribute("token");
        kakaoService.logoutService(token);
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
        boolean check = false;

        if (commons.isEmptyStrings(memberName, memberEmail) == true) {
            log.error("Request URI :: '{}'", request.getRequestURI());
            log.error("memberName or memberEmail is null");

            result.put("check" ,false);

            return result;
        }

        info.put("memberName", memberName);
        info.put("memberEmail", memberEmail);

        result = memberService.getMemberIdFromEmail(info);

        check = (boolean) result.get("check");

        log.info("Search id from email check {}", check);

        if (check == true) {
            memberId = (String) result.get("memberId");

            title = "중고 경매의 세계 아이디 찾기 이메일 입니다.";
            content = "아이디 찾기 서비스를 이용해 해주셔서 감사합니다." +
                    "아이디는 " + memberId + " 입니다.";

            emailService.sendEmail(memberEmail, title, content);
        } else {
            result.put("check", false);
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

        if (commons.isEmptyStrings(memberName, memberPhone) == true) {
            result.put("check", false);
            result.put("memberId", "");
            return result;
        } else {
            info.put("memberName", memberName);
            info.put("memberPhone", memberPhone);

            memberId = memberService.getMemberIdFromPhone(info);

            if (StringUtils.isEmpty(memberId)) {
                result.put("check", false);
                result.put("memberId", "");
                return result;
            } else {
                result.put("check", true);
                result.put("memberId", memberId);

                return result;
            }
        }
    }

    @GetMapping(value = "/searchIdResultPhone")
    public String IdResultPhone(@RequestParam(required = false) String memberId, Model model) {
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
    public Map<String, Boolean> searchPasswordFromPhone(HttpServletRequest request, String memberId,
                                                        String memberName, String memberPhone) throws Exception {
        Map<String, Object> info = new HashMap<>();
        Map<String, Boolean> result = new HashMap<>();

        boolean check = false;

        info.put("memberId", memberId);
        info.put("memberName", memberName);
        info.put("memberPhone", memberPhone);

        check = memberService.isSearchPwdFromPhone(info);

        log.info("Check search password from phone :: {}", check);

        result.put("check", check);

        if (check == true) {
            commons.setMemberSession(request, memberId);
        }

        return result;
    }

    @PostMapping(value = "/search/password/email")
    @ResponseBody
    public Map<String, Object> searchPasswordEmail(HttpServletRequest request, String memberIdEmail, String memberEmail) throws Exception {
        Map<String, Object> info = new HashMap<>();
        Map<String, Object> result = new HashMap<>();

        boolean check = false;

        info.put("memberId", memberIdEmail);
        info.put("memberEmail", memberEmail);

        check = memberService.isSearchPwdFromEmail(info);

        log.info("Check search password from email :: {}", check);

        result.put("check", check);

        if (check == true) {
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
    public Map<String, Boolean> modifyPassword(HttpServletRequest request, String memberPassword) throws Exception {
        Map<String, Object> info = new HashMap<>();
        Map<String, Boolean> result = new HashMap<>();

        String memberId = "";
        String encryptionPassword = "";
        boolean check = false;

        memberId = commons.getMemberSession(request);
        log.info("target id :: '{}'", memberId);
        log.info("memberPassword :: " + memberPassword);

        if (commons.isEmptyStrings(memberId, memberPassword) == true) {
            log.info("info isEmpty");
            result.put("check", false);

            return result;
        }

        encryptionPassword = encryption.encrypt(memberPassword);

        info.put("memberId", memberId);
        info.put("memberPassword", encryptionPassword);

        check = memberService.setPassword(info);
        log.info("modify password result {}" , check);

        if (check == false) {
            result.put("check", false);

            return result;
        } else {
            request.getSession().invalidate();
            result.put("check", true);

            return result;
        }
    }

    @GetMapping(value = "/modify/password/result")
    public String modifyResult() {
        return "member/modifyPasswordResult";
    }
}
