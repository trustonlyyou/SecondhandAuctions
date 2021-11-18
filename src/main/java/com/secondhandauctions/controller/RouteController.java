package com.secondhandauctions.controller;

import com.secondhandauctions.service.RouteService;
import com.secondhandauctions.service.ShopService;
import com.secondhandauctions.utils.Criteria;
import com.secondhandauctions.utils.PageDTO;
import com.secondhandauctions.vo.ShopVo;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private RouteService routeService;

    @RequestMapping(value = "/")
    public String home(HttpServletRequest request, @ModelAttribute Criteria criteria,
                       @RequestParam(required = false, defaultValue = "") String status, Model model) {
                                        // 해당 파라미터가 필수 인지
        List<ShopVo> items = new ArrayList<>();
        PageDTO pageDTO = new PageDTO();

        String fileName = "";
        int count = 0;
        String ip = "";

        ip = routeService.getClientIp(request);

        log.info("client ip :: {}", ip);
        log.info("status :: {}", status);

        try {
            switch (status) {
                case "" :
                case "newList" :
                    count = routeService.getTotalCount();
                    items = routeService.getNewProductList(criteria);

                    break;

                case "expireList" :
                    count = routeService.getTotalCount();
                    items = routeService.getExpireTimeProductList(criteria);

                    break;
                }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("error :: " + e);
        }

        pageDTO.setCriteria(criteria);
        pageDTO.setTotalCount(count);

        model.addAttribute("list", items);
        model.addAttribute("pageMaker", pageDTO);

//        if ("".equals(status)) {
//            try {
//                count = routeService.getTotalCount();
//                items = routeService.getNewProductList(criteria);
//
//                pageDTO.setCriteria(criteria);
//                pageDTO.setTotalCount(count);
//            } catch (Exception e) {
//                e.printStackTrace();
//                log.error("error :: " + e);
//            }
//
//            model.addAttribute("list", items);
//            model.addAttribute("pageMaker", pageDTO);
//        }


        return "home";
    }


}
