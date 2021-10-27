package com.secondhandauctions.utils;

import com.secondhandauctions.dao.ProductDao;
import com.secondhandauctions.vo.ImageVo;
import net.coobird.thumbnailator.Thumbnailator;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Component
public class FileUtils {

    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

    private final String UPLOAD_PATH = Paths.get("/Users", "junghwan", "Desktop", "auctionProductImg").toString();
    private final String UPLOAD_DAY = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));


    public static String getUploadPath() {
        String uploadPath = "";

        String date = ProductDao.UPLOAD_DAY.replace("-", File.separator);

        File file = new File(date);

        uploadPath = file.getPath();

        return uploadPath;
    }

    public static String getUUID() {
        String uploadFileName = "";

        uploadFileName = UUID.randomUUID().toString();

        return uploadFileName;
    }

    public static String getFileNameToServer() {
        String convertFileName = "";

        convertFileName = UUID.randomUUID().toString().replaceAll("-", "");

        return convertFileName;
    }

    // TODO: 2021/10/26 수정(submit)
    public List<ImageVo> getFiles(MultipartFile[] files, int productId) throws Exception {
        List<ImageVo> list = new ArrayList<>();

        String originalFileName = "";
        String originalFileExtension = "";
        String convertFileName = "";
        String saveFileName = "";

        File dir = new File(ProductDao.UPLOAD_PATH);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        if (files[0].getSize() < 1) {
            return Collections.emptyList();
        }

        for (MultipartFile multipartFile : files) {
            if (!multipartFile.isEmpty()) {
                originalFileName = multipartFile.getOriginalFilename();
                originalFileExtension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
                convertFileName = getFileNameToServer();

//                saveFileName = convertFileName + "." + originalFileExtension;
                saveFileName = convertFileName;

                logger.info("originalFileName :: " + originalFileName);
                logger.info("originalFileExtension :: " + originalFileExtension);
                logger.info("saveFileName :: " + saveFileName);

                // save to Server
                File target = new File(ProductDao.UPLOAD_PATH, saveFileName);

                multipartFile.transferTo(target);

                // save Thumbnails to Server
                File thumbnailFile = new File(ProductDao.UPLOAD_PATH, "s_" + saveFileName);

                Thumbnails.of(target).size(160,160).toFile(thumbnailFile);

                ImageVo imageVo = new ImageVo();

                imageVo.setProductId(productId);
//                imageVo.setOriginalFileName(multipartFile.getOriginalFilename());
//                imageVo.setFileName(saveFileName);
//                imageVo.setS_FileName("s_" + saveFileName);
                imageVo.setFileExtension(originalFileExtension);
                imageVo.setFileSize(multipartFile.getSize());

                list.add(imageVo);

            }
        }

        return list;
    }

    /**
     *
     * @param files ::  업로드할 파일들
     * @param productId :: 참조할 PK (productId(1) : image(N))
     * @return
     */
//    public List<ImageVo> uploadFiles(MultipartFile[] files, int productId) {
//        String originalFileName = "";
//        String originalFileExtension = "";
//        String convertFileName = "";
//        String saveFileName = "";
//
//        List<ImageVo> imageList = new ArrayList<>();
//        ImageVo imageVo = new ImageVo();
//        File dir = new File(ProductDao.UPLOAD_PATH);
//
//        if (!dir.exists()) {
//            dir.mkdirs();
//        }
//
//        // 받은 파일이 없으면 비어 있는 리스트 반환
//        if (files[0].getSize() < 1) {
//            return Collections.emptyList();
//        }
//
//        for (MultipartFile file : files) {
//            if (!file.isEmpty()) {
//                originalFileName = file.getOriginalFilename();
//                originalFileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
//                convertFileName = getFileNameToServer(); // server 한글 안됨, UUID 통해서 저장하자
//
//                saveFileName = convertFileName + "." + originalFileExtension;
//
//                logger.info("originalFileName :: " + originalFileName);
//                logger.info("originalFileExtension :: " + originalFileExtension);
//                logger.info("saveFileName :: " + saveFileName);
//
//                // creat file
//                File target = new File(ProductDao.UPLOAD_PATH, saveFileName);
//
//                // file save
//                imageVo.setProductId(productId);
//                imageVo.setFileName(saveFileName);
//                imageVo.setFileSize(file.getSize());
//
//                // file add
//                imageList.add(imageVo);
//            }
//        }
//
//        return imageList;
//    }


}
