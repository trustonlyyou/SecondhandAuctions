package com.secondhandauctions.controller;

import com.secondhandauctions.dao.ProductDao;
import com.secondhandauctions.service.FileService;
import com.secondhandauctions.service.ProductService;
import com.secondhandauctions.service.RouteService;
import com.secondhandauctions.utils.FileUtils;
import com.secondhandauctions.vo.ImageVo;
import com.secondhandauctions.vo.ProductVo;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private FileService fileService;

    @GetMapping(value = "/product/register/form")
    public String registerForm(HttpServletRequest request, RedirectAttributes redirectAttributes) {
        String memberId = "";

        HttpSession session = request.getSession();
        memberId = (String) session.getAttribute("member");

        if (("".equals(memberId)) || (memberId == null)) {
            logger.error("Member is null ");

            return "member/login";
        }

        logger.info("member :: " + memberId);

        return "product/productRegister";
    }

    @PostMapping(value = "/upload/ajax", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ImageVo>> uploadAjax(MultipartFile[] uploadFile) {

        ResponseEntity<List<ImageVo>> result = null;

        result = productService.uploadAjax(uploadFile);

        // TODO: 2021/10/21 HttpStatus 에 따라 return 처라히면 좋을 듯.

        logger.info("result :: " + result);

        return result;
    }

    @GetMapping("/view")
    public ResponseEntity<byte[]> loadImage(String fileName, HttpServletRequest request) {
        ResponseEntity<byte[]> result = null;
        int status = 0;

        logger.info("Input :: " + request.getRequestURI());

        result = fileService.getImageAjax(fileName);
        status = result.getStatusCodeValue();

        if (status == 501) {
            return null; // error 이미지 보여주자
        }

        return result;
    }

    @PostMapping("/delete/image")
    public ResponseEntity<String> deleteFile(String fileName, HttpServletRequest request) {
        ResponseEntity<String> result = null;

        logger.info("URL :: " + request.getRequestURI());

        result = productService.deleteImage(fileName);

        return result;
    }

    @PostMapping(value = "/register/product/submit")
    public String productRegister(HttpServletRequest request, ProductVo productVo) {
        List<ImageVo> imageVoList = new ArrayList<>();
        Map<String, Integer> result = new HashMap<>();

        int fileCount = 0;
        String uploadPath = "";
        String uploadFileName = "";
        String fileExtension = "";
//        byte fileSize = 0;
        int fileSize = 0; // TODO: 2021/10/26 FileSize data type check;
        int imgResultChk = 0;
        String memberId = "";
        int productId = 0;

        HttpSession session = request.getSession();
        memberId = (String) session.getAttribute("member");

        logger.info("ID :: " + memberId + ", URL :: " + request.getRequestURI());

        if ("".equals(memberId) || (memberId == null)) {
            return "redirect:/member/login/form";
        }

        productVo.setMemberId(memberId);

        fileCount = Integer.parseInt(request.getParameter("fileCount"));


        for (int i = 0; i < fileCount; i++) {
            ImageVo imageVo = new ImageVo();

            try {
                uploadPath = request.getParameter("imageList[" + i + "].uploadPath");
                uploadFileName = request.getParameter("imageList[" + i + "].uploadFileName");
                fileExtension = FilenameUtils.getExtension(uploadFileName);
//            fileSize = Byte.parseByte(request.getParameter("imageList[" + i + "].fileSize"));
                fileSize = Integer.parseInt(request.getParameter("imageList[" + i + "].fileSize"));

            } catch (Exception e) {
                logger.error("Error :: " + e);
                e.printStackTrace();
            }

            if (("".equals(uploadPath) || uploadPath == null) || ("".equals(uploadFileName) || uploadFileName == null)) {
                logger.error("File is null or Empty");
                logger.error("uploadPath :: " + uploadPath);
                logger.error("uploadFileName ::" + uploadFileName);

                return "redirect:/product/register/form";
            }

            imageVo.setUploadPath(uploadPath);
            imageVo.setUploadFileName(uploadFileName);
            imageVo.setFileExtension(fileExtension);
            imageVo.setFileSize(fileSize);

            imageVoList.add(imageVo);

            logger.info("Image Info");
            logger.info(imageVo.toString());
            logger.info("=====================================");
        }

        // Service
        try {
            result = productService.setRegisterProduct(productVo);

            productId = result.get("productId");

            logger.info("productId :: " + productId);

            if (productId == 0) {
                return "redirect:/product/register/form";
            }

//            imgResultChk = productService.setImage(imageVoList);

            imgResultChk = productService.setRegisterImage(productId, imageVoList);

            if (imgResultChk == 0) {
                return "redirect:/product/register/form";
            }

        } catch (Exception e) {
            logger.error("error :: " + e);
            logger.error(RouteService.printStackTrace(e));
        }

        return "redirect:/product/register/success";
    }

    @GetMapping(value = "/product/register/success")
    public String registerSuccess() {
        return "product/productSuccess";
    }

    // 게시물 수정

    // 게시물 삭제

}
