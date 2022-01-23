package com.secondhandauctions.service;

import com.secondhandauctions.dao.ProductDao;
import com.secondhandauctions.utils.Commons;
import com.secondhandauctions.utils.FileUtils;
import com.secondhandauctions.vo.ImageVo;
import com.secondhandauctions.vo.ProductVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;

@Service
@Slf4j
public class ProductService {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private Commons commons;

    public boolean uploadImage(List<MultipartFile> uploadFiles, int productId) throws Exception {
        List<ImageVo> imageVoList = new ArrayList<>();

        String uploadDir = "";
        File uploadPath = null;

        boolean check = false;

        uploadDir = FileUtils.getUploadPath();
        uploadPath = new File(ProductDao.UPLOAD_PATH, uploadDir);

        if (uploadFiles.isEmpty()) {
            return false;
        }
        if (!uploadPath.exists()) {
            uploadPath.mkdirs();
        }

        for (MultipartFile file : uploadFiles) {
            ImageVo imageVo = new ImageVo();
            String uuidFileName = FileUtils.getUUID();
            String uploadFileName = uuidFileName + "_" + file.getOriginalFilename();

            log.info("uploadPath :: " + uploadDir); // /Users/junghwan/Desktop/upload/2021/10/20
            log.info("originalFileName :: " + file.getOriginalFilename());
            log.info("fileExtension :: " + FilenameUtils.getExtension(file.getOriginalFilename()));
            log.info("uploadFileName :: " + uploadFileName); // b5483f69-3f4e-473c-afb6-2e0d12b9773a_bag.png
            log.info("fileSize :: " + file.getSize());

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
    public boolean registerImage(int productId, List<ImageVo> images) throws Exception {
        if (images.isEmpty()) {
            return false;
        } else {
            try {
                for (ImageVo imageVo : images) {
                    imageVo.setProductId(productId);
                }

                productDao.registerImg(images);
                return true;

            } catch (Exception e) {
                log.error(commons.printStackLog(e));
                return false;
            }
        }
    }

    // 게시물 등록
    public Map<String, Object> setRegisterProduct(ProductVo productVo) throws Exception {
        Map<String, Object> result = new HashMap<>();
        List<String> checkList = new ArrayList<>();
        boolean chk = false;

        int productId = 0;
        String memberId = "";
        String categoryName = "";
        String productTitle = "";
        String productContent = "";
        String startPrice = "";
        Date expireTime = null;

        expireTime = commons.getExpireTime();
        productVo.setExpireTime(expireTime);

        try {
            // 게시물 등록
            productDao.registerProduct(productVo);
            productId = productVo.getProductId();

            result.put("check", true);
            result.put("productId", productId);

            return result;
        } catch (Exception e) {
            log.error(commons.printStackLog(e));
            result.put("check", false);
            return result;
        }
    }

    public int updateBidPrice(Map<String, Object> params) throws Exception {
        int check = 0;

        if (params.isEmpty()) {
            return check;
        }

        check = productDao.updateBidPrice(params);

        return check;
    }
}
