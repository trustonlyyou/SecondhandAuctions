package com.secondhandauctions.controller;

import com.secondhandauctions.service.EmailService;
import com.secondhandauctions.service.MemberService;
import com.secondhandauctions.service.MyPageService;
import com.secondhandauctions.service.SmsService;
import com.secondhandauctions.utils.*;
import com.secondhandauctions.vo.ImageVo;
import com.secondhandauctions.vo.MemberVo;
import com.secondhandauctions.vo.ProductVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class MyPageController {

    private static final Logger logger = LoggerFactory.getLogger(MyPageController.class);

    @Autowired
    private MemberService memberService;

    @Autowired
    private MyPageService myPageService;

    @Autowired
    private InfoFormatter formatter;

    @Autowired
    private Commons commons;

    @Autowired
    private SmsService smsService;

    @Autowired
    private EmailService emailService;


    @GetMapping(value = "/myPage/form")
    public String myPageForm(HttpServletRequest request, Model model) throws Exception {
        MemberVo memberVo = new MemberVo();
        String memberId = "";
        String memberName = "";
        String memberPassword = "";
        String memberEmail = "";
        String memberPhone = "";

        memberId = commons.getMemberSession(request);

        memberVo = memberService.getMemberInfo(memberId);

        memberPassword = formatter.passwordFormat(memberVo.getMemberPassword());
        memberName = formatter.nameFormat(memberVo.getMemberName());
        memberEmail = formatter.emailFormat(memberVo.getMemberEmail());
        memberPhone = formatter.phoneFormat(memberVo.getMemberPhone());

        memberVo.setMemberPassword(memberPassword);
        memberVo.setMemberName(memberName);
        memberVo.setMemberEmail(memberEmail);
        memberVo.setMemberPhone(memberPhone);

        logger.info(memberVo.toString());

        model.addAttribute("memberInfo", memberVo);

        return "myPage/myInfo";
    }

    // 비밀번호 수정 자기 인증
    @GetMapping(value = "/myPage/certification/password")
    public String userCheck() {
        return "myPage/myCertification";
    }

    @PostMapping(value = "/certification/phone", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> checkPhone(@RequestBody String memberPhone) throws Exception {
        Map<String, Object> map = new HashMap<>();
        String strNum = "";

        strNum = smsService.sendSms(memberPhone);

        if (StringUtils.isEmpty(strNum)) {
            log.info("smsService sendSms return is null");
        }

        log.info("인증번호 :: {}", strNum);

        map.put("key", strNum);

        return map;
    }

    @PostMapping(value = "/certification/email", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> checkEmail(@RequestBody String memberEmail) throws Exception {
        Map<String, Object> result = new HashMap<>();

        String num = "";
        int checkNum = 0;
        String title = "";
        String content = "";

        log.info(memberEmail);

        checkNum = emailService.certificationSendEmail(memberEmail);

        if (checkNum != 0) {
            num = Integer.toString(checkNum);
        } else {
            result.put("num", 0);
            log.error("emailService certification return is 0");

            return result;
        }

        result.put("num", num);

        return result;
    }

    @GetMapping(value = "/myShop/list")
    public String myShopList(@ModelAttribute Criteria criteria, HttpServletRequest request, Model model) {
        List<ProductVo> result = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        PagingUtil pagingUtil = new PagingUtil();
        String memberId = "";
        int count = 0;

        try {
            HttpSession session = request.getSession();
            memberId = (String) session.getAttribute("member");

            count = myPageService.getMyShopListCount(memberId);

            params.put("memberId", memberId);
            params.put("criteria", criteria);

            result = myPageService.getMyShopList(params);

        } catch (Exception e) {
            logger.error("error :: " + e);
        }

        pagingUtil.setCriteria(criteria);
        pagingUtil.setTotalCount(count);

        model.addAttribute("list", result);
        model.addAttribute("pageMaker", pagingUtil);

        return "myPage/myShopList";

    }

    @GetMapping(value = "/myShop/get/{productId}")
    public String myShopDetail(@PathVariable Integer productId, @ModelAttribute("criteria") Criteria criteria, HttpServletRequest request, Model model) {
        HttpSession session = request.getSession();

        Map<String, Object> info = new HashMap<>();
        List<ImageVo> imageList = new ArrayList<>();
        List<String> fileNames = new ArrayList<>();
        Map<String, Object> result = new HashMap<>();

        ProductVo productVo = new ProductVo();

        String memberId = "";

        if (productId != 0) {
            info.put("productId", productId);
        }

        memberId = (String) session.getAttribute("member");

        if (("".equals(memberId) || memberId == null)) {
            return "redirect:/member/login/form";
        }

        info.put("memberId", memberId);

        try {
            result = myPageService.getMyShopDetail(info);

            productVo = (ProductVo) result.get("product");

            if (productVo != null) {
                model.addAttribute("product", productVo);
            }

            fileNames = (List<String>) result.get("fileName");

            model.addAttribute("fileName", fileNames);

        } catch (Exception e) {
            logger.error("error :: " + e);
            e.printStackTrace();
        }

        return "myPage/myShopDetail";
    }

    // TODO: 2021/11/16 myshopdetail :: 수정, 삭제 까지
    // 수정
    @GetMapping(value = "/myPage/myShop/product/modify")
    public String myProductModify(HttpServletRequest request, HttpServletResponse response) {
        return "";
    }


    @PostMapping(value = "/myPage/myShop/product/delete")
    @ResponseBody
    public Map<String, Object> deleteMyProduct(HttpServletRequest request, int productId) throws Exception {
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> result = new HashMap<>();

        String memberId = "";
        int check = 0;

        memberId = commons.getMemberSession(request);

        if (StringUtils.isEmpty(memberId)) {
            result.put("check", 0);
            log.error("Member Session is null");
        }

        if (productId == 0) {
            result.put("check", 0);
            log.error("productId is 0");
        }

        log.info("delete productId :: '{}'", productId);

        params.put("memberId", memberId);
        params.put("productId", productId);

        check = myPageService.deleteProduct(params);

        log.info("Delete product result :: {}", check);

        result.put("check", check);

        return result;
    }

    // 삭제 ajax로 수정
//    @PostMapping(value = "/myPage/myShop/product/delete")
//    public String myProductDelete(HttpServletRequest request,
//                                  Model model, RedirectAttributes attributes) throws Exception {
//        Map<String, Object> params = new HashMap<>();
//
//        String memberId = "";
//        int productId = 0;
//        int result = 0;
//
//        memberId = commons.getMemberSession(request);
//        productId = Integer.parseInt(request.getParameter("deleteProductId"));
//
//        logger.info("delete productId :: " + productId);
//        logger.info("Member ID :: " + memberId);
//
//        params.put("memberId", memberId);
//        params.put("productId", productId);
//
//        result = myPageService.deleteProduct(params);
//
//
//        if (result == 0) {
//            attributes.addFlashAttribute("msg", "삭제 요청이 잘못 되었습니다.");
//        } else if (result == 1) {
//            attributes.addFlashAttribute("msg", "해당 게시물이 삭제 되었습니다.");
//        }
//
//        return "redirect:/myPage/myShop/list";
//    }

}
