package com.secondhandauctions.controller;

import com.secondhandauctions.service.ShopService;
import com.secondhandauctions.utils.Commons;
import com.secondhandauctions.utils.Criteria;
import com.secondhandauctions.utils.PageDTO;
import com.secondhandauctions.vo.ShopVo;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@Slf4j
public class RouteController {

    @Autowired
    private ShopService shopService;

    @RequestMapping(value = "/")
    public String home(HttpServletRequest request, @ModelAttribute Criteria criteria,
                       @RequestParam(required = false, defaultValue = "") String status, Model model) {
                                        // 해당 파라미터가 필수 인지
        List<ShopVo> items = new ArrayList<>();
        PageDTO pageDTO = new PageDTO();

        String fileName = "";
        int count = 0;
        String ip = "";

        ip = Commons.getClientIp(request);

        log.info("client ip :: {}", ip);
        log.info("status :: {}", status);

        try {
            switch (status) {
                case "" :
                case "newList" :
                    count = shopService.getTotalCount();
                    items = shopService.getNewProductList(criteria);

                    break;

                case "expireList" :
                    count = shopService.getTotalCount();
                    items = shopService.getExpireTimeProductList(criteria);

                    break;
                }
        } catch (Exception e) {
            log.error("error :: " + e);
        }

        pageDTO.setCriteria(criteria);
        pageDTO.setTotalCount(count);

        model.addAttribute("list", items);
        model.addAttribute("pageMaker", pageDTO);

        if ("".equals(status)) {
            try {
                count = shopService.getTotalCount();
                items = shopService.getNewProductList(criteria);

                pageDTO.setCriteria(criteria);
                pageDTO.setTotalCount(count);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("error :: " + e);
            }

            model.addAttribute("list", items);
            model.addAttribute("pageMaker", pageDTO);
        }


        return "home";
    }


}
