package com.secondhandauctions.service;

import com.secondhandauctions.dao.ProductDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.nio.file.Files;

@Service
public class FileService {

    private static final Logger logger = LoggerFactory.getLogger(FileService.class);

    public ResponseEntity<byte[]> getImageAjax(String fileName) {
        ResponseEntity<byte[]> result = null;

        logger.info("fileName :: " + fileName);

        String uploadFolder = ProductDao.UPLOAD_PATH;

        File file = new File(uploadFolder, fileName);

        if (!file.exists()) {
            result = new ResponseEntity<>(HttpStatus.NOT_FOUND);

            return result;
        }

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
}
