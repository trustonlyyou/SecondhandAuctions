package com.secondhandauctions.controller;

import antlr.collections.impl.LList;
import com.secondhandauctions.service.FileService;
import com.secondhandauctions.service.ProductService;
import com.secondhandauctions.utils.Commons;
import com.secondhandauctions.vo.ImageVo;
import com.secondhandauctions.vo.ProductVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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
        String memberId = "";
        int productId = 0;

        memberId = commons.getMemberSession(request);

        productVo.setMemberId(memberId);

        Map<String, Integer> product = new HashMap<>();

        product = productService.setRegisterProduct(productVo);

        log.info("Product register result :: '{}'", product.get("check"));

        if (product.get("check") == 0) {
            result.put("check", 0);
        }

        productId = product.get("productId");

        result.put("check", 1);
        result.put("productId", productId);

        return result;
    }

    @PostMapping(value = "/upload/image")
    @ResponseBody
    public Map<String, Object> uploadImage(MultipartHttpServletRequest multipartRequest, int productId) throws Exception {
        List<MultipartFile> uploadFiles = new ArrayList<>();
        Map<String, Object> result = new HashMap<>();

        String filedName = "";
        int check = 0;

        Iterator<String> iterator = multipartRequest.getFileNames();

        while (iterator.hasNext()) {
            filedName = (String) iterator.next();
            uploadFiles.add(multipartRequest.getFile(filedName));
        }

        if (uploadFiles.isEmpty()) {
            check = -1;
            result.put("check", check);

            return result;
        }

        check = productService.uploadImage(uploadFiles, productId);
        result.put("check", check);

        log.info("image upload result :: '{}'", check);

        return result;
    }

    @GetMapping(value = "/product/register/success")
    public String registerSuccess() {
        return "product/productSuccess";
    }

}
