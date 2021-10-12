package com.secondhandauctions.controller;

import com.secondhandauctions.dao.ProductDao;
import com.secondhandauctions.service.ProductService;
import com.secondhandauctions.utils.FileUtils;
import com.secondhandauctions.vo.ImageVo;
import com.secondhandauctions.vo.MemberVo;
import com.secondhandauctions.vo.ProductVo;
import org.apache.commons.io.FilenameUtils;
import org.apache.ibatis.ognl.enhance.ExpressionAccessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private FileUtils fileUtils;

    @Autowired
    private ProductDao productDao;

    @GetMapping(value = "/product/register/form")
    public String registerForm(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        MemberVo memberVo = null;
        HttpSession session = request.getSession();

        memberVo = (MemberVo) session.getAttribute("member");

        if (memberVo == null) {
            logger.error("Member is null ");

            redirectAttributes.addFlashAttribute("stance", "product");

            return "redirect:/member/login/form";
        }

        logger.info("member :: " + memberVo.getMemberId());

        return "product/productRegister";
    }

    @PostMapping(value = "/register/product")
    public String registerProduct(HttpServletRequest request, ProductVo productVo, @RequestParam("multiFiles") MultipartFile[] files) {
        List<ImageVo> imageList = new ArrayList<>();
        int productId = 0;
        String memberId = "";
        MemberVo memberVo = null;

        HttpSession session = request.getSession();

        memberVo = (MemberVo) session.getAttribute("member");
        memberId = memberVo.getMemberId();



        logger.info("MemberId :: " + memberId);

        if ((!"".equals(memberId)) && (memberId != null)) {
            productVo.setMemberId(memberId);
        }

        productVo.setImagePath(ProductDao.UPLOAD_PATH);

        try {
//            productDao.registerProduct(productVo); // db에 게시물 저장

            productService.setProduct(productVo);

            productId = productVo.getProductId();

            imageList = fileUtils.getFiles(files, productId);

//            productDao.registerImg(imageList); // db에 이미지 저장
//
//            for (ImageVo imageVo : imageList) {
//                System.out.println(imageVo.getFileName());
//            }

            productService.setImage(imageList);

        } catch (Exception e) {
            e.printStackTrace();
        }

        logger.info(productVo.toString());

        return "redirect:/product/register/success";
    }

    @GetMapping(value = "/product/register/success")
    public String registerSuccess() {
        return "product/productSuccess";
    }

}
