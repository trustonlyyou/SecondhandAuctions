package com.secondhandauctions.service;

import com.secondhandauctions.dao.ProductDao;
import com.secondhandauctions.utils.Commons;
import com.secondhandauctions.utils.FileUtils;
import com.secondhandauctions.vo.ImageVo;
import com.secondhandauctions.vo.ProductVo;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.util.*;

@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ProductDao productDao;

    @Autowired
    private Commons commons;

    public int uploadImage(List<MultipartFile> uploadFiles, int productId) throws Exception {
        List<ImageVo> imageVoList = new ArrayList<>();

        String uploadDir = "";
        File uploadPath = null;

        int check = 0;

        uploadDir = FileUtils.getUploadPath();
        uploadPath = new File(ProductDao.UPLOAD_PATH, uploadDir);

        if (uploadFiles.isEmpty()) {
            return check;
        }
        if (!uploadPath.exists()) {
            uploadPath.mkdirs();
        }

        for (MultipartFile file : uploadFiles) {
            ImageVo imageVo = new ImageVo();
            String uuidFileName = FileUtils.getUUID();
            String uploadFileName = uuidFileName + "_" + file.getOriginalFilename();

            logger.info("uploadPath :: " + uploadDir); // /Users/junghwan/Desktop/upload/2021/10/20
            logger.info("originalFileName :: " + file.getOriginalFilename());
            logger.info("fileExtension :: " + FilenameUtils.getExtension(file.getOriginalFilename()));
            logger.info("uploadFileName :: " + uploadFileName); // b5483f69-3f4e-473c-afb6-2e0d12b9773a_bag.png
            logger.info("fileSize :: " + file.getSize());

            File target = new File(uploadPath, uploadFileName);

            file.transferTo(target);

            imageVo.setUploadPath(uploadDir);
            imageVo.setFileExtension(FilenameUtils.getExtension(file.getOriginalFilename()));
            imageVo.setUploadFileName(uploadFileName);
            imageVo.setFileSize(file.getSize());

            imageVoList.add(imageVo);
        }

        check  = registerImage(productId, imageVoList);

        return check;
    }

    // 이미지 저장
    public int registerImage(int productId, List<ImageVo> images) throws Exception {
        int result = 0;

        if (images.isEmpty()) {
            return result;
        }

        for (ImageVo imageVo : images) {
            imageVo.setProductId(productId);
        }

        productDao.registerImg(images);

        result = 1;

        return result;
    }

    // 게시물 등록
    public Map<String, Integer> setRegisterProduct(ProductVo productVo) throws Exception {
        Map<String, Integer> result = new HashMap<>();
        List<String> checkList = new ArrayList<>();
        boolean chk = false;

        int productId = 0;
        String memberId = "";
        String categoryName = "";
        String productTitle = "";
        String productContent = "";
        String startPrice = "";

        memberId = productVo.getMemberId();
        categoryName = productVo.getCategoryName();
        productTitle = productVo.getProductTitle();
        productContent = productVo.getProductContent();
        startPrice = productVo.getStartPrice();

        checkList.add(memberId);
        checkList.add(categoryName);
        checkList.add(productTitle);
        checkList.add(productContent);
        checkList.add(startPrice);

        chk = commons.isEmpty(checkList);

        if (chk == true) {
            result.put("check", 0);
            return result;
        }

        // 게시물 등록
        productDao.registerProduct(productVo);

        productId = productVo.getProductId();

        result.put("check", 1);
        result.put("productId", productId);

        return result;
    }
}
