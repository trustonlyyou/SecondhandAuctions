package com.secondhandauctions.controller;

import com.secondhandauctions.service.MemberService;
import com.secondhandauctions.service.MyPageService;
import com.secondhandauctions.utils.Criteria;
import com.secondhandauctions.utils.InfoFormatter;
import com.secondhandauctions.utils.PageDTO;
import com.secondhandauctions.vo.MemberVo;
import com.secondhandauctions.vo.ProductVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
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

        pageDTO.setDisplayPageNum(7);
        pageDTO.setCriteria(criteria);
        pageDTO.setTotalCount(count);

        model.addAttribute("list", result);
        model.addAttribute("pageMaker", pageDTO);

        return "member/myShopList";

        // TODO: 2021/11/15 view 에 뿌리자
    }
}
