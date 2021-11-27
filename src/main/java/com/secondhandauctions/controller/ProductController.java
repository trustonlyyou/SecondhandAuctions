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

    @Autowired
    private FileService fileService;

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

        log.info("product result :: " + product.get("check"));

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

        log.info("productId ::" + productId);

        while (iterator.hasNext()) {
            filedName = (String) iterator.next();
            uploadFiles.add(multipartRequest.getFile(filedName));
        }

        check = productService.uploadImage(uploadFiles, productId);

        log.info("image upload result :: " + check);

        result.put("check", check);

        return result;
    }

    @GetMapping(value = "/product/register/success")
    public String registerSuccess() {
        return "product/productSuccess";
    }


    // ====================================================================================================
//    @GetMapping(value = "/product/register/form")
//    public String registerForm(HttpServletRequest request, RedirectAttributes redirectAttributes) {
//        String memberId = "";
//
//        memberId = commons.getMemberSession(request);
//
//        if (("".equals(memberId)) || (memberId == null)) {
//            logger.error("Member is null ");
////            HttpServletRequestWrapper
//            return "redirect:/member/login/form";
//        }
//
//        return "product/productRegister";
//    }
//
//    // client 가 이미지 등록시 로컬서버에 우선 저장
//    @PostMapping(value = "/upload/ajax", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<List<ImageVo>> uploadAjax(MultipartFile[] uploadFile) {
//
//        ResponseEntity<List<ImageVo>> result = null;
//
//        result = productService.uploadAjax(uploadFile);
//
//        // TODO: 2021/10/21 HttpStatus 에 따라 return 처라히면 좋을 듯.
//
//        logger.info("result :: " + result);
//
//        return result;
//    }
//
//    // 미리보기
//    @GetMapping("/view")
//    public ResponseEntity<byte[]> loadImage(String fileName, HttpServletRequest request) {
//        ResponseEntity<byte[]> result = null;
//        int status = 0;
//
//        logger.info("Input :: " + request.getRequestURI());
//
//        result = fileService.getImageAjax(fileName);
//        status = result.getStatusCodeValue();
//
//        if (status == 501) {
//            return null; // error 이미지 보여주자
//        }
//
//        return result;
//    }
//
//    // 업로드 했던 이미자가 마음에 안들경우 삭제(로컬 서버에서)한다
//    @PostMapping("/delete/image")
//    public ResponseEntity<String> deleteFile(String fileName, HttpServletRequest request) {
//        ResponseEntity<String> result = null;
//
//        logger.info("URL :: " + request.getRequestURI());
//
//        result = productService.deleteImage(fileName);
//
//        return result;
//    }
//
//    @PostMapping(value = "/register/product/submit")
//    public String productRegister(HttpServletRequest request, ProductVo productVo) {
//        List<ImageVo> imageVoList = new ArrayList<>();
//        Map<String, Integer> result = new HashMap<>();
//
//        int fileCount = 0;
//        String uploadPath = "";
//        String uploadFileName = "";
//        String fileExtension = "";
////        byte fileSize = 0;
//        int fileSize = 0; // TODO: 2021/10/26 FileSize data type check;
//        int imgResultChk = 0;
//        String memberId = "";
//        int productId = 0;
//
//        HttpSession session = request.getSession();
//        memberId = (String) session.getAttribute("member");
//
//        logger.info("ID :: " + memberId + ", URL :: " + request.getRequestURI());
//
//        if ("".equals(memberId) || (memberId == null)) {
//            return "redirect:/member/login/form";
//        }
//
//        productVo.setMemberId(memberId);
//
//        fileCount = Integer.parseInt(request.getParameter("fileCount"));
//
//
//        for (int i = 0; i < fileCount; i++) {
//            ImageVo imageVo = new ImageVo();
//
//            try {
//                uploadPath = request.getParameter("imageList[" + i + "].uploadPath");
//                uploadFileName = request.getParameter("imageList[" + i + "].uploadFileName");
//                fileExtension = FilenameUtils.getExtension(uploadFileName);
////            fileSize = Byte.parseByte(request.getParameter("imageList[" + i + "].fileSize"));
//                fileSize = Integer.parseInt(request.getParameter("imageList[" + i + "].fileSize"));
//
//            } catch (Exception e) {
//                logger.error("Error :: " + e);
//                e.printStackTrace();
//            }
//
//            if (("".equals(uploadPath) || uploadPath == null) || ("".equals(uploadFileName) || uploadFileName == null)) {
//                logger.error("File is null or Empty");
//                logger.error("uploadPath :: " + uploadPath);
//                logger.error("uploadFileName ::" + uploadFileName);
//
//                return "redirect:/product/register/form";
//            }
//
//            imageVo.setUploadPath(uploadPath);
//            imageVo.setUploadFileName(uploadFileName);
//            imageVo.setFileExtension(fileExtension);
//            imageVo.setFileSize(fileSize);
//
//            imageVoList.add(imageVo);
//
//            logger.info("Image Info");
//            logger.info(imageVo.toString());
//            logger.info("=====================================");
//        }
//
//        // Service
//        try {
//            result = productService.setRegisterProduct(productVo);
//
//            productId = result.get("productId");
//
//            logger.info("productId :: " + productId);
//
//            if (productId == 0) {
//                return "redirect:/product/register/form";
//            }
//
////            imgResultChk = productService.setImage(imageVoList);
//
//            imgResultChk = productService.setRegisterImage(productId, imageVoList);
//
//            if (imgResultChk == 0) {
//                return "redirect:/product/register/form";
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            logger.error("error :: " + e);
//        }
//
//        return "redirect:/product/register/success";
//    }
}
