package com.secondhandauctions.controller;

import com.secondhandauctions.service.FileService;
import com.secondhandauctions.service.RouteService;
import com.secondhandauctions.service.ShopService;
import com.secondhandauctions.utils.Criteria;
import com.secondhandauctions.utils.PageDTO;
import com.secondhandauctions.vo.ImageVo;
import com.secondhandauctions.vo.ProductVo;
import com.secondhandauctions.vo.ShopVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ShopController {

    private static final Logger logger = LoggerFactory.getLogger(ShopController.class);

    @Autowired
    private ShopService shopService;

    @Autowired
    private FileService fileService;

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

    @GetMapping(value = "/shop/get")
    public String get(@RequestParam int productId, Model model) {
        Map<String, Object> info = new HashMap<>();
        List<ImageVo> imageList = new ArrayList<>();
        List<String> fileNames = new ArrayList<>();

        ProductVo productVo = new ProductVo();

        logger.info("productId :: " + productId);

        try {
            info = shopService.getReadProduct(productId);

            productVo = (ProductVo) info.get("product");

            if (productVo == null) {
                model.addAttribute("msg", "productNull");
            }

            model.addAttribute("product", productVo);

            fileNames = (List<String>) info.get("fileName");

//            이미지 없을 때는 대체 이미지 구해서 집어 넣자.
//            if (fileNames.isEmpty()) {
//                model.addAttribute("fileName", "fileNameNull");
//            }

            model.addAttribute("fileName", fileNames);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("error :: " + e);
        }

        return "shop/shopDetail";
    }

    @GetMapping("/detail/show")
    public ResponseEntity<byte[]> loadImages(@RequestParam String fileName, HttpServletRequest request) {
        ResponseEntity<byte[]> result = null;
        int status = 0;

        result = fileService.getImageAjax(fileName);

        if (result.getStatusCodeValue() == 404) {
            return null;
            // TODO: 2021/11/10 Show no Image
        }

        return result;
    }


}
