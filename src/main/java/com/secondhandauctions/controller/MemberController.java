package com.secondhandauctions.controller;

import com.secondhandauctions.service.EmailService;
import com.secondhandauctions.service.MemberService;
import com.secondhandauctions.service.SmsService;
import com.secondhandauctions.utils.EncryptionSHA256;
import com.secondhandauctions.vo.MemberVo;
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
@RequestMapping("member")
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

    @RequestMapping(method = RequestMethod.GET, value = "/join/form")
    public String joinForm(HttpServletRequest request, HttpServletResponse response) {

        logger.info("Client request URL :: " + request.getRequestURL());

        return "member/join"; // join_sample
    }

    @PostMapping(value = "/join/idCheck", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public int iDCheck(@RequestBody String memberId, HttpServletRequest request) {
        int isCheck = 0;

        if (memberId == null) {
            logger.info("URL ::" + request.getRequestURL());
            logger.info("input ID is null");
        } else {
            logger.info("아이디 중복 데이터 확인");
            logger.info("ID :: " + memberId);

            try {
                isCheck = memberService.idCheck(memberId);
            } catch (Exception e) {
                logger.info("idCheck Error :: ", e);
            }
        }


        return isCheck;
    }

    @RequestMapping(method = RequestMethod.POST, value = "/sendEmail", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public String emailCheck(@RequestBody String email) throws Exception {
        String toEmail = "";
        String title = "";
        String content = "";

        // 난수 생성
        Random random = new Random();

        // 111111 ~ 999999 범위의 숫자를 얻기 위해서 nextInt(888888) + 111111를 사용하였습니다.
        int checkNum = random.nextInt(888888) + 11111;

        toEmail = email;
        title = "중고 경매의 세계 인증 이메일 입니다.";
        content = "중고 경매의 세계를 이용해 주셔서 감사합니다." +
                "<br><br>" +
                "인증번호는 " + checkNum + " 입니다. <br>" +
                "해당 인증번호를 인증번호 확인란에 기입해 주세요.";

        emailService.sendEmail(toEmail, title, content);

        String num = Integer.toString(checkNum);

        logger.info("인증번호 " + checkNum);

        // TODO: 2021/08/21 이메일 중복 체크해야함.

        return num;
    }

    @RequestMapping(value ="/join/sendSms")
    @ResponseBody
    public String checkPhone(HttpServletRequest request, HttpServletResponse response) {
        String strNum = "";

        // 난수 생성
        Random random = new Random();

        // 111111 ~ 999999 범위의 숫자를 얻기 위해서 nextInt(888888) + 111111를 사용하였습니다.
        int checkNum = random.nextInt(888888) + 11111;

        logger.info("인증번호 " + checkNum);

        strNum = Integer.toString(checkNum);


        return strNum;
    }

    @PostMapping(value = "/join/action")
    public String joinEnd(@ModelAttribute MemberVo memberVo, HttpServletRequest request, HttpServletResponse response) {

        String encryptionPwd = "";

        if (memberVo == null) {
            logger.error("URL :: " + request.getRequestURL());
            logger.error("MemberVo is null");

            request.setAttribute("msg", "0");

            return "member/join";
        } else {
            String inputPassword = memberVo.getMemberPassword();
            encryptionPwd = EncryptionSHA256.encrypt(inputPassword);
            memberVo.setMemberPassword(encryptionPwd);

            try {
                memberService.setMember(memberVo);
            } catch (Exception e) {
                logger.error("error :: " + e);
            }

            logger.info("Success join");
            logger.info("New User is {}", memberVo.getMemberId());

            return "redirect:member/join_result";
        }

    }

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
        MemberVo member = null;

        try {
            memberId = request.getParameter("memberId");
            memberPassword = request.getParameter("memberPassword");
            encryptionPwd = EncryptionSHA256.encrypt(memberPassword);

            memberInfo.put("memberId", memberId);
            memberInfo.put("memberPassword", encryptionPwd);

            result = memberService.getLoginResult(memberInfo);

            if (result == 0) {
                return "member/login_fail";
            }

            member = memberService.getMemberInfo(memberId);

        } catch (Exception e) {
            logger.error("URL :: " + request.getRequestURL());
            logger.error("Login Error ", e);
        }


        // TODO: 2021/08/19 Login Session 처리
        HttpSession session = request.getSession();
        session.setAttribute("member", member);

        return "redirect:/";
    }

    @GetMapping(value = "logout/action")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();

        MemberVo memberVo = (MemberVo) session.getAttribute("member");
        String memberId = memberVo.getMemberId();

        session.invalidate();

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

                return "searchIdResultEmail";
            }

            title = "중고 경매의 세계 아이디 찾기 이메일 입니다.";
            content = "아이디 찾기 서비스를 이용해 해주셔서 감사합니다." +
                    "아이디는 " + id + " 입니다.";

            emailService.sendEmail(toEmail, title, content);
        }catch (Exception e) {
            logger.error("error :: " + e);
        }

        return "member/searchIdResultEmail";
    }

    @GetMapping(value = "/searchIdResult")
    public String IdResultEmail() {
        return "searchIdResultEmail";
    }

    @PostMapping(value = "/check/phone/sendSms")
    @ResponseBody
    public Map<String, Object> searchIdFromPhone(String memberPhone) {
        Map<String, Object> map = new HashMap<>();
        String strNum = "";

        try {
            strNum = smsService.sendSms(memberPhone);
        } catch (Exception e) {
            logger.error("error :: " + e);
        }

        map.put("key", strNum);

        return map;
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
    public String searchPasswordPhone(String memberId, String memberName, String memberPhone, RedirectAttributes redirectAttributes) {
        Map<String, Object> memberInfo = new HashMap<>();
        int check = 0;

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

        redirectAttributes.addFlashAttribute("memberId", memberId);

        return "redirect:/member/modify/form";
    }

    // 이메일로 패스워드 찾기
    @PostMapping(value = "/search/password/email/action")
    public String searchPasswordEmail(String memberIdEmail, String memberEmail, RedirectAttributes redirectAttributes) {
        Map<String, Object> memberInfo = new HashMap<>();
        int check = 0;

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

        redirectAttributes.addFlashAttribute("memberId", memberIdEmail);

        logger.info("Member :: " + memberIdEmail);

        return "redirect:/member/modify/form";
    }

    @GetMapping(value = "/modify/form")
    public String modifyPassword(HttpServletRequest request, Model model) {
        String memberId = "";

        memberId = (String) model.getAttribute("memberId");

        HttpSession session = request.getSession();
        session.setAttribute("memberId", memberId);

        logger.info("memberId :: " + memberId);

        return "member/modifyPassword";
    }

    @PostMapping(value = "/modify/password")
    public String modifyPassword(HttpServletRequest request, String memberPassword, Model model) {
        Map<String, Object> memberInfo = new HashMap<>();
        String memberId = "";
        String encryptionPwd = "";

        HttpSession session = request.getSession();
        memberId = (String) session.getAttribute("memberId");

        logger.info("memberId :: " + memberId);

        if ((memberId == null || "".equals(memberId))) {
            return "redirect:/";
        }

        encryptionPwd = EncryptionSHA256.encrypt(memberPassword);

        memberInfo.put("memberId", memberId);
        memberInfo.put("memberPassword", encryptionPwd);

        try {
            memberService.setPassword(memberInfo);

        } catch (Exception e) {
            logger.error("error :: " + e);
        }

        session.removeAttribute("memberId");

        return "member/modifyPasswordResult";
    }
}
