package com.secondhandauctions.controller;

import com.secondhandauctions.service.ShopService;
import com.secondhandauctions.utils.Criteria;
import com.secondhandauctions.utils.PageDTO;
import com.secondhandauctions.vo.ShopVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class ShopController {

    private static final Logger logger = LoggerFactory.getLogger(ShopController.class);

    @Autowired
    private ShopService shopService;

    @GetMapping(value = "/shop")
    public String shopForm(Criteria criteria, Model model) {
        List<ShopVo> list = new ArrayList<>();

        try {
            list = shopService.getList(criteria);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("error :: " + e);
        }

        model.addAttribute("list", list);
        model.addAttribute("pageMaker", new PageDTO(criteria, 123));

        return "shop/shopList";
    }

    @GetMapping(value = "/shop/detail")
    public String shopDetail() {
        return "shop/shopDetail";
    }

}
