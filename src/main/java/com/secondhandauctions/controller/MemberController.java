package com.secondhandauctions.controller;

import com.secondhandauctions.service.EmailService;
import com.secondhandauctions.service.MemberService;
import com.secondhandauctions.service.SmsService;
import com.secondhandauctions.utils.Commons;
import com.secondhandauctions.utils.EncryptionSHA256;
import com.secondhandauctions.utils.InfoFormatter;
import com.secondhandauctions.vo.MemberVo;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
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

    private static final Logger logger = LoggerFactory.getLogger(MemberController.class);

    @Autowired
    private JavaMailSender javaMailSender;

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

    @GetMapping(value = "/join/form")
    public String joinForm(HttpServletRequest request, HttpServletResponse response) {

        logger.info(Commons.getClientIp(request));

        return "member/join"; // join_sample
    }

    // 비동기 통신시 데이터를 본문에 담아서 보낸다.
    // ResponseBody :: 응답 본문
    // RequestBody :: 요청 본문
    @PostMapping(value = "/join/idCheck", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Integer> iDCheck(@RequestBody String memberId, HttpServletRequest request) throws Exception {
        Map<String, Integer> result = new HashMap<>();
        int isIdCheck = 0;

        isIdCheck = memberService.idCheck(memberId);

        log.info("IdCheck :: {}", isIdCheck);

        result.put("result", isIdCheck);

        return result;
    }

    // 이메일 중복 체크
    @PostMapping(value = "/emailCheck", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Integer> emailCheck(@RequestBody String email) throws Exception {
        Map<String, Integer> result = new HashMap<>();
        int isEmailCheck = 0;

        isEmailCheck = memberService.isEmail(email);

        log.info("emailCheck Result :: " + isEmailCheck);

        result.put("result", isEmailCheck);

        return result;
    }

    @PostMapping(value = "/sendEmail", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> sendEmail(@RequestBody String email) throws Exception {
        Map<String, Object> result = new HashMap<>();
        String num = "";
        int checkNum = 0;

        checkNum = emailService.joinCheckSendEmail(email);

        if (checkNum != 0) {
            num = Integer.toString(checkNum);
        } else {
            log.error("emailService joinCheckSendEmail return is 0");
        }

        result.put("num", num);

        return result;
    }

    // 사용자 핸드폰 check
    @PostMapping("/phoneCheck")
    @ResponseBody
    public int isCheckPhone(String memberName, String memberPhone) throws Exception {
        Map<String, Object> prams = new HashMap<>();
        int result = 0;

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

        strNum = smsService.sendSms(memberPhone);

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
        encryption.setMemberEncryptionPassword(inputPassword, memberVo);

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

        if (("".equals(memberId) || memberId == null) || ("".equals(memberPassword)) || memberPassword == null) {
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

    @GetMapping(value = "/logout/action")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
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
    public Map<String, Object> searchIdFromEmail(String memberName, String memberEmail) throws Exception {
        Map<String, Object> info = new HashMap<>();
        Map<String, Object> map = new HashMap<>();

        String memberId = "";
        String title = "";
        String content = "";
        int check = 0;

        info.put("memberName", memberName);
        info.put("memberEmail", memberEmail);

        map = memberService.getMemberIdFromEmail(info);

        check = (int) map.get("check");

        log.info("Search id from email check {}", check);

        if (check == 1) {
            memberId = (String) map.get("memberId");

            title = "중고 경매의 세계 아이디 찾기 이메일 입니다.";
            content = "아이디 찾기 서비스를 이용해 해주셔서 감사합니다." +
                    "아이디는 " + memberId + " 입니다.";

            emailService.sendEmail(memberEmail, title, content);
        }

        return map;
    }

//    @PostMapping(value = "/search/id/email/action")
//    public String searchIdFromEmail(HttpServletRequest request, Model model) throws Exception {
//        String id = "";
//        String toEmail = "";
//        String title = "";
//        String content = "";
//
//        toEmail = request.getParameter("memberEmail");
//
//        id = memberService.getMemberIdFromEmail(toEmail);
//
//        logger.info("memberId :: " + id);
//
//        if ((id == null) || id.equals("")) {
//            model.addAttribute("msg", "fail");
//
//            return "/member/searchIdResultEmail";
//        }
//
//        title = "중고 경매의 세계 아이디 찾기 이메일 입니다.";
//        content = "아이디 찾기 서비스를 이용해 해주셔서 감사합니다." +
//                "아이디는 " + id + " 입니다.";
//
//        emailService.sendEmail(toEmail, title, content);
//
//        return "/member/searchIdResultEmail";
//    }

    @GetMapping(value = "/searchIdResult")
    public String IdResultEmail() {
        return "/member/searchIdResultEmail";
    }

    // 핸드폰으로 아이디 찾기
    // 뒤로가기 어떻게 할래? 1. 그냥 한다. 2. 핸드폰 번호로 아이디를 보낸다.
//    @PostMapping(value = "/search/id/phone/action")
//    public String searchIdFromPhone(String memberName, String memberPhone,
//                                    RedirectAttributes redirectAttributes) throws Exception{
//        Map<String, Object> memberInfo = new HashMap<>();
//        String memberId = "";
//
//        memberInfo.put("memberName", memberName);
//        memberInfo.put("memberPhone", memberPhone);
//
//        memberId = memberService.getMemberIdFromPhone(memberInfo);
//
//        redirectAttributes.addFlashAttribute("memberId", memberId);
//
//        return "redirect:/member/searchIdResultPhone";
//    }

    @PostMapping(value = "/search/id/phone")
    @ResponseBody
    public Map<String, Object> searchIdFromPhone(String memberName, String memberPhone) throws Exception {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> info = new HashMap<>();

        String memberId = "";

        info.put("memberName", memberName);
        info.put("memberPhone", memberPhone);

        memberId = memberService.getMemberIdFromPhone(info);

        if ("".equals(memberId) || memberId == null) {
            result.put("check", 0);
            result.put("memberId", "");
        }

        // TODO: 2021/11/23 AES256 암호화 해서 넘기기 
        result.put("check", 1);
        result.put("memberId", memberId);

        return result;
    }

    @GetMapping(value = "/searchIdResultPhone")
    public String IdResultPhone(@RequestParam String memberId, Model model) {
//        String memberId = "";
//
//        memberId = (String) model.getAttribute("memberId");
//
//        logger.info("memberId :: " + memberId);
//
//        model.addAttribute("memberId", memberId);

        // TODO: 2021/11/23 AES256 복호화해서 넘기기
        model.addAttribute("memberId", memberId);

        return "member/searchIdResultPhone";
    }

    // 비밀번호 찾기
    @GetMapping(value = "/search/password")
    public String searchPasswordForm() {
        return "member/searchPassword";
    }

    // ajax from phone get password
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

    // 핸드폰으로 패스워드 찾기
//    @PostMapping(value = "/search/password/phone/action")
//    public String searchPasswordPhone(String memberId, String memberName, String memberPhone, HttpServletRequest request) {
//        Map<String, Object> memberInfo = new HashMap<>();
//        HttpSession session = request.getSession();
//
//        int check = 0;
//
//        logger.info("Request URL :: {}", request.getRequestURI());
//
//        logger.info("memberId :: {}", memberId);
//        logger.info("memberName :: {}", memberName);
//        logger.info("memberPhone :: {} ", memberPhone);
//
//        try {
//            memberInfo.put("memberId", memberId);
//            memberInfo.put("memberName", memberName);
//            memberInfo.put("memberPhone", memberPhone);
//
//            check = memberService.isMemberPasswordPhone(memberInfo);
//
//        } catch (Exception e) {
//            logger.info("error :: " + e);
//        }
//
//        if (check == 0) {
//            return "member/searchPasswordResult";
//        }
//
//        session.setAttribute("member", memberId);
//
//        return "redirect:/member/modify/form";
//    }

    // ajax from email get password
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


    // 이메일로 패스워드 찾기
//    @PostMapping(value = "/search/password/email/action")
//    public String searchPasswordEmail(HttpServletRequest request, String memberIdEmail, String memberEmail) {
//        Map<String, Object> memberInfo = new HashMap<>();
//        HttpSession session = request.getSession();
//
//        int check = 0;
//
//        logger.info("Request URL :: {}", request.getRequestURI());
//
//        logger.info("memberId :: {}", memberIdEmail);
//        logger.info("memberEmail :: {}", memberEmail);
//
//        try {
//            memberInfo.put("memberId", memberIdEmail);
//            memberInfo.put("memberEmail", memberEmail);
//
//            check = memberService.isMemberPasswordEmail(memberInfo);
//
//            logger.info("check :: " + check);
//
//        } catch (Exception e) {
//            logger.error("error :: " + e);
//        }
//
//        if (check == 0) {
//            return "member/searchPasswordResult";
//        }
//
//        session.setAttribute("member", memberIdEmail);
//
//        logger.info("memberId :: " + memberIdEmail);
//
//        return "redirect:/member/modify/form";
//    }

    @GetMapping(value = "/modify/form")
    public String modifyPassword(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();
        String memberId = "";

        logger.info("URL :: " + request.getRequestURI());

//        memberId = (String) model.getAttribute("member");
        memberId = (String) session.getAttribute("member");

        logger.info("memberId :: " + memberId);

//        if ((memberId != null) || ("".equals(memberId))) {
//            session.setAttribute("member", memberId);
//        }
//
//        logger.info("Model MemberId :: " + memberId);
//
//        if (("".equals(memberId)) || (memberId == null)) {
//            memberId = (String) session.getAttribute("member");
//        }

        return "member/modifyPassword";
    }

    @PostMapping(value = "/modify/password")
    public String modifyPassword(HttpServletRequest request, String memberPassword, Model model) throws Exception {
        Map<String, Object> memberInfo = new HashMap<>();
        String memberId = "";
        String encryptionPwd = "";

        HttpSession session = request.getSession();
        memberId = (String) session.getAttribute("member");

        logger.info("memberId :: " + memberId);

        if ((memberId == null || "".equals(memberId))) {
            return "redirect:/";
        }

        // 암호화 하는 것
//        encryptionPwd = EncryptionSHA256.encrypt(memberPassword);

        // 수정하자
//        encryptionPwd = encryption.getEncryption(memberPassword);

        // 값을 넣는 것을 따로 구현
        memberInfo.put("memberId", memberId);
        memberInfo.put("memberPassword", encryptionPwd);

        try {
            memberService.setPassword(memberInfo);

        } catch (Exception e) {
            logger.error("error :: " + e);
        }

        session.invalidate();

        return "redirect:/member/modify/password/result";
    }

    @GetMapping(value = "/modify/password/result")
    public String modifyResult() {
        return "member/modifyPasswordResult";
    }
}
