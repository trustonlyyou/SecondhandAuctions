package com.secondhandauctions.controller;

import com.secondhandauctions.service.MemberService;
import com.secondhandauctions.service.MyPageService;
import com.secondhandauctions.utils.Criteria;
import com.secondhandauctions.utils.InfoFormatter;
import com.secondhandauctions.utils.PageDTO;
import com.secondhandauctions.vo.ImageVo;
import com.secondhandauctions.vo.MemberVo;
import com.secondhandauctions.vo.ProductVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class MyPageController {

    private static final Logger logger = LoggerFactory.getLogger(MyPageController.class);

    @Autowired
    private MemberService memberService;

    @Autowired
    private MyPageService myPageService;

    @Autowired
    private InfoFormatter formatter;

    // TODO: 2021/10/28 MyPage 에다가 게시글 보여주기
    @GetMapping(value = "/myPage")
    public String myPageForm(HttpServletRequest request, Model model) {
        MemberVo memberVo = new MemberVo();
        String memberId = "";
        String memberName = "";
        String memberPassword = "";
        String memberEmail = "";
        String memberPhone = "";

        try {
            HttpSession session = request.getSession();
            memberId = (String) session.getAttribute("member");


            if (("".equals(memberId) || memberId == null)) {
                return "redirect:/member/login/form";
            }

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
        } catch (Exception e) {
            logger.error("error :: " + e);
            e.printStackTrace();
        }

        return "member/myPage";
    }

    @GetMapping(value = "/myShop/list")
    public String myShopList(@ModelAttribute Criteria criteria, HttpServletRequest request, Model model) {
        List<ProductVo> result = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        PageDTO pageDTO = new PageDTO();
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

        pageDTO.setCriteria(criteria);
        pageDTO.setTotalCount(count);

        model.addAttribute("list", result);
        model.addAttribute("pageMaker", pageDTO);

        return "member/myShopList";

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

        return "member/myShopDetail";
    }

    // TODO: 2021/11/16 myshopdetail :: 수정, 삭제 까지

    @GetMapping(value = "/myShop/product/modify")
    public String myProductModify(HttpServletRequest request, HttpServletResponse response) {
        return "";
    }

    // 삭제
    @PostMapping(value = "/myShop/product/delete")
    public String myProductDelete(HttpServletRequest request, Model model, RedirectAttributes attributes) {
        Map<String, Object> params = new HashMap<>();

        String memberId = "";
        int productId = 0;
        int result = 0;

        HttpSession session = request.getSession();

        productId = Integer.parseInt(request.getParameter("deleteProductId"));
        memberId = (String) session.getAttribute("member");

        logger.info("delete productId :: " + productId);
        logger.info("Member ID :: " + memberId);

        params.put("memberId", memberId);
        params.put("productId", productId);

        try {
            result = myPageService.deleteProduct(params);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("error :: " + e);
        }

        if (result == 0) {
            attributes.addFlashAttribute("msg", "삭제 요청이 잘못 되었습니다.");
        } else if (result == 1) {
            attributes.addFlashAttribute("msg", "해당 게시물이 삭제 되었습니다.");
        }

        return "redirect:/myShop/list";
    }

}
