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
import java.util.Random;

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
        encryption.memberSetEncryptionPassword(inputPassword, memberVo);

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

    @PostMapping(value = "/login/action")
    public String loginAction(HttpServletRequest request, HttpServletResponse response, Model model) {
        Map<String, Object> memberInfo = new HashMap<>();
        String memberId = "";
        String memberPassword = "";
        String encryptionPwd = "";
        int result = 0;
        String referer = "";


        try {
            memberId = request.getParameter("memberId");
            memberPassword = request.getParameter("memberPassword");
            encryptionPwd = encryption.encrypt(memberPassword);

            memberInfo.put("memberId", memberId);
            memberInfo.put("memberPassword", encryptionPwd);

            result = memberService.getLoginResult(memberInfo);

            if (result == 0) {
                return "member/login_fail";
            }

            HttpSession session = request.getSession();
            session.setAttribute("member", memberId);

        } catch (Exception e) {
            logger.error("URL :: " + request.getRequestURL());
            logger.error("Login Error ", e);
        }

        referer = request.getHeader("referer");

        logger.info("referer :: " + referer);

        if (!referer.contains("/member/login")) {
            return "redirect:" + referer;
        }

        return "redirect:/";
    }

    @GetMapping(value = "logout/action")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        String memberId = "";
        HttpSession session = request.getSession();

        memberId = (String) session.getAttribute("member");

        if ((!"".equals(memberId)) && (memberId != null)) {
            session.invalidate();
        }

        logger.info("'{}' is Logout" , memberId);

        return "redirect:/";
    }

    @GetMapping(value = "/search/id")
    public String searchIdForm() {
        return "member/searchId";
    }

    @PostMapping(value = "/search/id/email/action")
    public String searchIdFromEmail(HttpServletRequest request, Model model) {
        String id = "";
        String toEmail = "";
        String title = "";
        String content = "";

        toEmail = request.getParameter("memberEmail");


        logger.info("Request Email :: " + toEmail);

        try {
            id = memberService.getMemberIdFromEmail(toEmail);

            logger.info("memberId :: " + id);

            if ((id == null) || id.equals("")) {
                model.addAttribute("msg", "fail");

                return "/member/searchIdResultEmail";
            }

            title = "중고 경매의 세계 아이디 찾기 이메일 입니다.";
            content = "아이디 찾기 서비스를 이용해 해주셔서 감사합니다." +
                    "아이디는 " + id + " 입니다.";

            emailService.sendEmail(toEmail, title, content);
        }catch (Exception e) {
            logger.error("error :: " + e);
        }

        return "/member/searchIdResultEmail";
    }

    @GetMapping(value = "/searchIdResult")
    public String IdResultEmail() {
        return "/member/searchIdResultEmail";
    }


    @PostMapping(value = "/search/id/phone/action")
    public String searchIdFromPhone(HttpServletRequest request, String memberName, String memberPhone, RedirectAttributes redirectAttributes) {
        Map<String, Object> memberInfo = new HashMap<>();
        String memberId = "";

        try {
            logger.info("memberName :: " + memberName);
            logger.info("memberPhone :: " + memberPhone);

            memberInfo.put("memberName", memberName);
            memberInfo.put("memberPhone", memberPhone);

            memberId = memberService.getMemberIdFromPhone(memberInfo);
        }catch (Exception e) {
            logger.error("error :: " + e);
        }

        logger.info("ID :: " + memberId);

        redirectAttributes.addFlashAttribute("memberId", memberId);

        return "redirect:/member/searchIdResultPhone";
    }

    @GetMapping(value = "/searchIdResultPhone")
    public String IdResultPhone(Model model) {
        String memberId = "";

        memberId = (String) model.getAttribute("memberId");

        logger.info("memberId :: " + memberId);

        model.addAttribute("memberId", memberId);

        return "member/searchIdResultPhone";
    }


    // 비밀번호 찾기
    @GetMapping(value = "/search/password")
    public String searchPasswordForm() {
        return "member/searchPassword";
    }

    // 핸드폰으로 패스워드 찾기
    @PostMapping(value = "/search/password/phone/action")
    public String searchPasswordPhone(String memberId, String memberName, String memberPhone, HttpServletRequest request) {
        Map<String, Object> memberInfo = new HashMap<>();
        HttpSession session = request.getSession();

        int check = 0;

        logger.info("Request URL :: {}", request.getRequestURI());

        logger.info("memberId :: {}", memberId);
        logger.info("memberName :: {}", memberName);
        logger.info("memberPhone :: {} ", memberPhone);

        try {
            memberInfo.put("memberId", memberId);
            memberInfo.put("memberName", memberName);
            memberInfo.put("memberPhone", memberPhone);

            check = memberService.isMemberPasswordPhone(memberInfo);

        } catch (Exception e) {
            logger.info("error :: " + e);
        }

        if (check == 0) {
            return "member/searchPasswordResult";
        }

        session.setAttribute("member", memberId);

        return "redirect:/member/modify/form";
    }

    // 이메일로 패스워드 찾기
    @PostMapping(value = "/search/password/email/action")
    public String searchPasswordEmail(HttpServletRequest request, String memberIdEmail, String memberEmail) {
        Map<String, Object> memberInfo = new HashMap<>();
        HttpSession session = request.getSession();

        int check = 0;

        logger.info("Request URL :: {}", request.getRequestURI());

        logger.info("memberId :: {}", memberIdEmail);
        logger.info("memberEmail :: {}", memberEmail);

        try {
            memberInfo.put("memberId", memberIdEmail);
            memberInfo.put("memberEmail", memberEmail);

            check = memberService.isMemberPasswordEmail(memberInfo);

            logger.info("check :: " + check);

        } catch (Exception e) {
            logger.error("error :: " + e);
        }

        if (check == 0) {
            return "member/searchPasswordResult";
        }

        session.setAttribute("member", memberIdEmail);

        logger.info("memberId :: " + memberIdEmail);

        return "redirect:/member/modify/form";
    }

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
        encryptionPwd = encryption.getEncryption(memberPassword);

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
