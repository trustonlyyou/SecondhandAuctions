package com.secondhandauctions.controller;

import com.secondhandauctions.service.ProductService;
import com.secondhandauctions.utils.Commons;
import com.secondhandauctions.vo.ProductVo;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Controller
@Slf4j
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private Commons commons;

    @GetMapping(value = "/register/product/form")
    public String register(HttpServletRequest request) {
        return "product/registerProduct";
    }

    @PostMapping(value = "/product/submit")
    @ResponseBody
    public Map<String, Object> uploadProduct(HttpServletRequest request, ProductVo productVo) throws Exception {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> product = new HashMap<>();

        String memberId = "";
        int productId = 0;
        boolean chk = false;

        memberId = commons.getMemberSession(request);
        productVo.setMemberId(memberId);

        product = productService.setRegisterProduct(productVo);
        chk = (boolean) product.get("check");
        log.info("Product register result :: '{}'", chk);


        if (chk == false) {
            result.put("check", false);
            return result;
        }

        productId = (int) product.get("productId");

        result.put("check", true);
        result.put("productId", productId);

        return result;
    }

    @PostMapping(value = "/upload/image")
    @ResponseBody
    public Map<String, Object> uploadImage(MultipartHttpServletRequest multipartRequest, int productId) throws Exception {
        List<MultipartFile> uploadFiles = new ArrayList<>();
        Map<String, Object> result = new HashMap<>();

        String filedName = "";
        boolean check = false;

        Iterator<String> iterator = multipartRequest.getFileNames();

        while (iterator.hasNext()) {
            filedName = (String) iterator.next();
            uploadFiles.add(multipartRequest.getFile(filedName));
        }

        if (uploadFiles.isEmpty()) {
            result.put("check", false);

            return result;
        } else {

            check = productService.uploadImage(uploadFiles, productId);
            result.put("check", check);

            log.info("image upload result :: '{}'", check);

            return result;
        }

    }

    @GetMapping(value = "/product/register/success")
    public String registerSuccess() {
        return "product/productSuccess";
    }

}
