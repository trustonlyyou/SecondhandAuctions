package com.secondhandauctions.service;

import com.secondhandauctions.dao.ProductDao;
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
    private FileUtils fileUtil;

    public ResponseEntity<List<ImageVo>> uploadAjax(MultipartFile[] uploadFile) {
        List<ImageVo> imageVoList = new ArrayList<>();
        String uploadDir = "";
        File uploadPath = null;

        uploadDir = FileUtils.getUploadPath(); // 2021/10/21
        uploadPath = new File(ProductDao.UPLOAD_PATH, uploadDir); // /Users/Desktop ~~~

        if (!uploadPath.exists()) {
            uploadPath.mkdirs();
        }

        for (MultipartFile file : uploadFile) {
            ImageVo imageVo = new ImageVo();
            String uuidFileName = FileUtils.getUUID();
            String uploadFileName = uuidFileName + "_" + file.getOriginalFilename();

            logger.info("uploadPath :: " + uploadDir); // /Users/junghwan/Desktop/upload/2021/10/20
            logger.info("originalFileName :: " + file.getOriginalFilename());
            logger.info("uuidFileName :: " + uuidFileName);
            logger.info("fileExtension :: " + FilenameUtils.getExtension(file.getOriginalFilename()));
            logger.info("uploadFileName :: " + uploadFileName); // b5483f69-3f4e-473c-afb6-2e0d12b9773a_bag.png
            logger.info("fileSize :: " + file.getSize());

            File target = new File(uploadPath, uploadFileName);

            logger.info("Save File Path :: " + target.getPath());

            try {
                file.transferTo(target);

            } catch (Exception e) {
                // TODO: 2021/10/21 ResponseEntity<List<ImageVo>> result, Collections.emptyList(); :: I think Type Error
                logger.error("error :: " + e);

                ResponseEntity<List<ImageVo>> result = null;

                result =  new ResponseEntity<>(Collections.emptyList(), HttpStatus.INTERNAL_SERVER_ERROR); // 500 error

                return result;
            }


            imageVo.setUploadPath(uploadDir);
            imageVo.setFileExtension(FilenameUtils.getExtension(file.getOriginalFilename()));
            imageVo.setUploadFileName(uploadFileName);
            imageVo.setFileSize(file.getSize());

            imageVoList.add(imageVo);
        }

        ResponseEntity<List<ImageVo>> result = new ResponseEntity<>(imageVoList, HttpStatus.OK);


        return result;
    }

    // FileService 로 Refactoring 했음
    public ResponseEntity<byte[]> getImageAjax(String fileName) {
        ResponseEntity<byte[]> result = null;

        logger.info("fileName :: " + fileName);

        String uploadFolder = ProductDao.UPLOAD_PATH;

        File file = new File(uploadFolder, fileName);

        logger.info("filePath :: " + file.getPath());

        try {
            HttpHeaders header = new HttpHeaders();
            header.add("Content-type", Files.probeContentType(file.toPath())); // contentType 을 알 수 있다.
            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("error :: " + e);

            result = new ResponseEntity<>(null, null, HttpStatus.NOT_IMPLEMENTED);

            return result;
        }

        return result;
    }

    public ResponseEntity<String> deleteImage(String fileName) {
        File file = null;
        String uploadFolder = ProductDao.UPLOAD_PATH;

        try {
            logger.info("NonDecoder :: " + fileName);
            logger.info("URLDDecoder :: " + URLDecoder.decode(fileName, "UTF-8"));

            file = new File(uploadFolder, URLDecoder.decode(fileName, "UTF-8"));

            logger.info("getAbsolutePath :: " + file.getAbsolutePath());
            logger.info("getPath :: " + file.getPath());

            String target = file.getAbsolutePath();

            file = new File(target);

            Files.delete(file.toPath()); // file.delete ignore

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("error :: " + e);

            return new ResponseEntity<String>("fail", HttpStatus.NOT_IMPLEMENTED);
        }

        return new ResponseEntity<String>("success", HttpStatus.OK);
    }

    // register service refactoring
    public Map<String, Integer> setRegisterProduct(ProductVo productVo) throws Exception {
        Map<String, Integer> result = new HashMap<>();

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

        if (("".equals(memberId) || memberId == null) || ("".equals(categoryName) || categoryName == null)
                || ("".equals(productTitle)) || productTitle == null || ("".equals(productContent) || productContent == null)
                || ("".equals(startPrice) || startPrice == null)) {

            result.put("result", 0);
        }

        // 게시물 등록
        productDao.registerProduct(productVo);

        productId = productVo.getProductId();

        result.put("productId", productId);

        return result;
    }

    public int setRegisterImage(int productId, List<ImageVo> imageList) {
        int result = 0;

        if (imageList.isEmpty()) {
            return result;
        }
        try {

            for (ImageVo imageVo : imageList) {
                imageVo.setProductId(productId);
            }

            productDao.registerImg(imageList);

            result = 1;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

}
