package com.secondhandauctions.controller;

import com.secondhandauctions.service.*;
import com.secondhandauctions.utils.*;
import com.secondhandauctions.vo.ImageVo;
import com.secondhandauctions.vo.MemberVo;
import com.secondhandauctions.vo.ProductVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@Slf4j
public class MyPageController {

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

    @Autowired
    private EncryptionSHA256 encryptionSHA256;

    @Autowired
    private OpenBankService openBankService;

    @GetMapping(value = "/authority/form")
    public String authorityForm(HttpServletRequest request, Model model) throws Exception {
        String memberId = commons.getMemberSession(request);
        model.addAttribute("chk", memberService.isKakaoMember(memberId));

        return "myPage/myAuthority";
    }

    @PostMapping(value = "/authority", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Boolean> authorityChk(HttpServletRequest request, @RequestBody String memberPassword) throws Exception {
        Map<String, Boolean> result = new HashMap<>();
        Map<String, String> info = new HashMap<>();
        HttpSession session = request.getSession();

        String memberId = "";
        String encryptionPassword = "";
        boolean chk = false;

        memberId = commons.getMemberSession(request);
        encryptionPassword = encryptionSHA256.encrypt(memberPassword);

        if (commons.isEmptyStrings(memberId, memberPassword)) {
            result.put("chk", false);
            return result;
        } else {
            info.put("memberId", memberId);
            info.put("memberPassword", encryptionPassword);

            chk = myPageService.isAuthority(info);
            log.info("authorityChk result :: '{}'", chk);

            if (chk == false) {
                result.put("chk", false);
                return result;
            } else {
                session.setAttribute("authority", true);
                result.put("chk", true);

                return result;
            }

        }
    }

    @PostMapping(value = "/authority/sendSms",produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> authorityChkSms(@RequestBody String memberPhone) throws Exception {
        Map<String, Object> result = new HashMap<>();

        String key = smsService.authenticationNum(memberPhone);

        log.info(memberPhone);
        log.info(key);

        if (commons.isEmptyStrings(memberPhone, key) == true) {
            log.error("isEmpty");
            result.put("chk", false);
            return result;
        } else {
            result.put("chk", true);
            result.put("key", key);
            return result;
        }
    }

    @GetMapping(value = "/authority/kakao")
    public String authorityKakao(HttpServletRequest request) {
        HttpSession session = request.getSession();
        session.setAttribute("authority", true);

        return "redirect:/myPage/form";
    }

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

        log.info(memberVo.toString());

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

        strNum = smsService.authenticationNum(memberPhone);

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
    public String myShopList(@ModelAttribute Criteria criteria,
                             HttpServletRequest request, Model model) throws Exception {
        List<ProductVo> result = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        PagingUtil pagingUtil = new PagingUtil();
        String memberId = "";
        int count = 0;

        HttpSession session = request.getSession();
        memberId = (String) session.getAttribute("member");

        count = myPageService.getMyShopListCount(memberId);

        params.put("memberId", memberId);
        params.put("criteria", criteria);

        result = myPageService.getMyShopList(params);

        pagingUtil.setCriteria(criteria);
        pagingUtil.setTotalCount(count);

        model.addAttribute("list", result);
        model.addAttribute("pageMaker", pagingUtil);

        return "myPage/myShopList";

    }

    @GetMapping(value = "/myShop/get/{productId}")
    public String myShopDetail(@PathVariable Integer productId, @ModelAttribute("criteria") Criteria criteria,
                               HttpServletRequest request, Model model) throws Exception {
        HttpSession session = request.getSession();

        Map<String, Object> info = new HashMap<>();
        List<String> fileNames = new ArrayList<>();
        ProductVo productVo = new ProductVo();

        Map<String, Object> result = new HashMap<>();
        List<Map<String, Object>> qNa = new ArrayList<>();


        String memberId = "";

        if (productId != 0) {
            info.put("productId", productId);
        }

        memberId = (String) session.getAttribute("member");

        if (StringUtils.isEmpty(memberId)) {
            return "redirect:/member/login/form";
        }

        info.put("memberId", memberId);

        result = myPageService.getMyShopDetail(info);

        productVo = (ProductVo) result.get("product");
        fileNames = (List<String>) result.get("fileName");
        qNa = (List<Map<String, Object>>) result.get("qna");

        model.addAttribute("product", productVo);
        model.addAttribute("fileName", fileNames);
        model.addAttribute("qna", qNa);

        return "myPage/myShopDetail";
    }

    // 수정
    @GetMapping(value = "/myPage/myShop/product/modify/form")
    public String myProductModify(HttpServletRequest request, Model model) throws Exception {
        Map<String, Object> info = new HashMap<>();
        ProductVo productVo = new ProductVo();
        List<ImageVo> imageList = new ArrayList<>();

        String memberId = "";
        int productId = 0;
        int bidCheck = 0;

        productId = Integer.parseInt(request.getParameter("modifyProductId"));

        bidCheck = myPageService.isBidCheck(productId);
        model.addAttribute("bidCheck", bidCheck);

        memberId = commons.getMemberSession(request);

        info.put("productId", productId);
        info.put("memberId", memberId);

        productVo = myPageService.getProductDetail(info);
        imageList = myPageService.getProductImages(info);

        log.info(productVo.toString());
        log.info("================================================");
        log.info(imageList.toString());

        model.addAttribute("product", productVo);
        model.addAttribute("imageList", imageList);

        return "myPage/myProductModify";
    }

    @PostMapping(value = "/myPage/myShop/product/modify/deleteImg")
    @ResponseBody
    public Map<String, Integer> deleteImg(int productId,
                                          @RequestParam("deleteData[]") List<String> deleteData) throws Exception {
        Map<String, Integer> result = new HashMap<>();
        Map<String, Object> params = new HashMap<>();
        int midCheck = 0;
        int resultCheck = 0;

        for (String uploadFileName : deleteData) {
            log.info("target productId :: '{}'", productId);
            log.info("delete image by modify | target FileName :: '{}'", uploadFileName);

            params.put("uploadFileName", uploadFileName);
            params.put("productId", productId);

            midCheck = myPageService.deleteImg(params);

            if (midCheck == 0) {
                resultCheck = -1;
                log.error("fail image delete :: target fileName '{}'", uploadFileName);
            }

            params.clear();
        }

        if (resultCheck == -1) {
            result.put("check", resultCheck);
        } else {
            resultCheck = 1;
            result.put("check", resultCheck);
        }

        log.info("delete image by modify service result :: '{}'", resultCheck);

        return result;
    }

    @PostMapping(value = "/myPage/myShop/product/modify")
    @ResponseBody
    public Map<String, Integer> updateProduct(@RequestParam int productId, ProductVo productVo) throws Exception {
        Map<String, Integer> result = new HashMap<>();
        int check = 0;

        log.info(productVo.toString());
        log.info(String.valueOf(productId));

        check = myPageService.setModifyProduct(productVo);

        log.info("check :: " + check);

        result.put("check", check);
        result.put("productId", productId);


        return result;
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

        log.info("target productId :: '{}'", productId);

        params.put("memberId", memberId);
        params.put("productId", productId);

        check = myPageService.deleteProduct(params);

        log.info("Delete product result :: {}", check);

        result.put("check", check);

        return result;
    }

    @GetMapping(value = "/myShop/product/answer/form")
    public String myProductAnswerOfQuestion(HttpServletRequest request, Model model,
                                            @RequestParam int productId,
                                            @RequestParam int questionId) throws Exception {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> info = new HashMap<>();
        String referer = "";

        referer = request.getHeader("Referer");

        log.info("referer :: '{}'", referer);

        info.put("productId", productId);
        info.put("questionId", questionId);

        result = myPageService.getQuestion(info);

        model.addAttribute("qna", result);
        model.addAttribute("prevPageUrl", referer);

        return "myPage/answerForm";
    }

    @PostMapping(value = "/myShop/product/answer/register")
    @ResponseBody
    public Map<String, Object> registerAnswer(HttpServletRequest request,
                                              String answer, int questionId) throws Exception {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> info = new HashMap<>();

        int check = 0;

        String writer = commons.getMemberSession(request);

        info.put("memberId", writer);
        info.put("questionId", questionId);
        info.put("answer", answer);

        check = myPageService.setAnswer(info, questionId);

        log.info("answer service result :: '{}'", check);

        result.put("check", check);

        return result;
    }

//     나의 입찰 물품
    @GetMapping(value = "/myBid/list")
    public String myBidProduct(@ModelAttribute Criteria criteria,
                               HttpServletRequest request, Model model) throws Exception {

        List<ProductVo> result = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        PagingUtil pagingUtil = new PagingUtil();

        String memberId = "";
        int count = 0;

        memberId = commons.getMemberSession(request);

        count = myPageService.getMyBidProductCount(memberId);

        if (count == 0) {
            model.addAttribute("list", null);
            return "myPage/myBidList";
        }

        params.put("criteria", criteria);
        params.put("memberId", memberId);

        result = myPageService.getMyBidProduct(params);

        if (result.isEmpty()) {
            model.addAttribute("list", null);
            return "myPage/myBidList";
        }

        pagingUtil.setCriteria(criteria);
        pagingUtil.setTotalCount(count);

        model.addAttribute("list", result);
        model.addAttribute("pageMaker", pagingUtil);

        return "myPage/myBidList";
    }

    @GetMapping(value = "/myBid/success/sell")
    public String mySellSuccessProduct(@ModelAttribute Criteria criteria,
                                      HttpServletRequest request, Model model) throws Exception {

        List<Map<String, Object>> bidingSellerList = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        PagingUtil pagingUtil = new PagingUtil();

        String memberId = "";
        int totalCount = 0;

        memberId = commons.getMemberSession(request);
        totalCount = myPageService.countSuccessSell(memberId);

        if (totalCount == 0) {
            model.addAttribute("list", null);
            return "myPage/mySuccessSell";
        }

        params.put("memberId", memberId);
        params.put("criteria", criteria);

        bidingSellerList = myPageService.getSuccessSellList(params);

        if (bidingSellerList.isEmpty()) {
            model.addAttribute("list", null);
            return "myPage/mySuccessSell";
        }

        pagingUtil.setCriteria(criteria);
        pagingUtil.setTotalCount(totalCount);

        model.addAttribute("list", bidingSellerList);
        model.addAttribute("pageMaker", pagingUtil);

        return "myPage/mySuccessSell";
    }

    @GetMapping("/myBid/success/bid")
    public String myBidSuccessProduct(@ModelAttribute Criteria criteria,
                                      HttpServletRequest request, Model model) throws Exception {

        List<Map<String, Object>> bidingList = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        PagingUtil pagingUtil = new PagingUtil();

        String memberId = "";
        int totalCount = 0;

        memberId = commons.getMemberSession(request);

        if (StringUtils.isEmpty(memberId)) {
            return "redirect:/";
        }

        totalCount = myPageService.countSuccessBid(memberId);

        if (totalCount == 0) {
            model.addAttribute("list", null);
            return "myPage/mySuccessBid";
        }

        params.put("memberId", memberId);
        params.put("criteria", criteria);

        bidingList = myPageService.getSuccessBidList(params);

        if (bidingList.isEmpty()) {
            model.addAttribute("list", null);
            return "myPage/mySuccessBid";
        }

        pagingUtil.setCriteria(criteria);
        pagingUtil.setTotalCount(totalCount);

        model.addAttribute("list", bidingList);
        model.addAttribute("pageMaker", pagingUtil);

        return "myPage/mySuccessBid";
    }


    @GetMapping(value = "/myBid/success/sell/detail")
    public String myBidSuccessSellDetail(@ModelAttribute("criteria") Criteria criteria,
                                         @RequestParam int successBidNo,
                                         @RequestParam int productId,
                                         HttpServletRequest request, Model model) throws Exception {

        Map<String, Object> info = new HashMap<>();
        Map<String, Object> result = new HashMap<>();
        List<String> fileNames = new ArrayList<>();
        Map<String, Object> product = new HashMap<>();

        String memberId = "";

        memberId = commons.getMemberSession(request);

        log.info("successBidNo :: '{}'", successBidNo);
        log.info("productId :: '{}'", productId);

        info.put("memberId", memberId);
        info.put("successBidNo", successBidNo);
        info.put("productId", productId);

        result = myPageService.getSuccessBidSellDetail(info);

        if (result.isEmpty()) {
            log.error("result is empty, target SuccessBidNo :: '{}'", successBidNo);
            return "redirect:/myBid/success/sell";
        }

        product = (Map<String, Object>) result.get("successProduct");
        fileNames = (List<String>) result.get("fileNames");

        model.addAttribute("product", product);
        model.addAttribute("fileNames", fileNames);


        return "myPage/mySuccessSellDetail";
    }

    @GetMapping(value = "/myBid/success/bid/detail")
    public String myBidSuccessBidDetail(@ModelAttribute("criteria") Criteria criteria,
                                        @RequestParam int successBidNo,
                                        @RequestParam int productId,
                                        HttpServletRequest request, Model model) throws Exception {

        Map<String, Object> info = new HashMap<>();
        Map<String, Object> result = new HashMap<>();
        List<String> fileNames = new ArrayList<>();
        Map<String, Object> product = new HashMap<>();

        String memberId = commons.getMemberSession(request);

        info.put("memberId", memberId);
        info.put("successBidNo", successBidNo);
        info.put("productId", productId);

        commons.printLogByMap(info);

        result = myPageService.getSuccessBidDetail(info);

        if (result.isEmpty()) {
            log.error("result is empty, target SuccessBidNo :: '{}'", successBidNo);
            return "redirect:/myBid/success/bid";
        }

        product = (Map<String, Object>) result.get("successProduct");
        fileNames = (List<String>) result.get("fileNames");

        model.addAttribute("product", product);
        model.addAttribute("fileNames", fileNames);

        // TODO: 2022/01/17 이미지 안나옴

        return "myPage/mySuccessBidDetail";
    }

    @GetMapping(value = "/myPage/myPoint")
    public String pointList(@ModelAttribute Criteria criteria,
                            HttpServletRequest request, Model model) throws Exception {

        List<Map<String, Object>> pointPayList = new ArrayList<>();
        Map<String, Object> info = new HashMap<>();
        PagingUtil pagingUtil = new PagingUtil();

        String memberId = "";
        int totalCount = 0;
        Map<String, Object> accountInfo = new HashMap<>();

        memberId = commons.getMemberSession(request);
        totalCount = myPageService.getCountChargePoint(memberId);

        info.put("memberId", memberId);
        info.put("criteria", criteria);

        pointPayList = myPageService.getChargePointList(info);
        accountInfo = openBankService.isAccount(memberId);
        model.addAttribute("account", accountInfo);

        if (pointPayList.isEmpty()) {
            log.info("point list is null");

            model.addAttribute("myPoint", myPageService.getMyPoint(memberId));
            model.addAttribute("list", null);
        } else {
            pagingUtil.setCriteria(criteria);
            pagingUtil.setTotalCount(totalCount);

            model.addAttribute("myPoint", myPageService.getMyPoint(memberId));
            model.addAttribute("list", pointPayList);
            model.addAttribute("pageMaker", pagingUtil);
        }
        return "myPage/chargePointList";
    }
}
