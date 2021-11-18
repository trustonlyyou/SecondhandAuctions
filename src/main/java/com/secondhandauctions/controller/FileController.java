package com.secondhandauctions.controller;

import com.secondhandauctions.dao.ProductDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);

    @GetMapping(value = "/file/show")
    public ResponseEntity<Resource> fileShow(@RequestParam String uploadPath, @RequestParam String fileName) throws IOException {
        Resource resource = null;
        String path = "";
        String type = "";

        logger.info("uploadPath :: " + uploadPath);
        logger.info("fileName :: " + fileName);

//        path = ProductDao.UPLOAD_PATH + "/" + uploadPath + "/" + fileName;
        path = Paths.get(ProductDao.UPLOAD_PATH + "/" + uploadPath + "/" + fileName).toString();
//        path = Paths.get("/Users/junghwan/Desktop/").toString() + "/" + fileName;

        logger.info("Path :: " + path);

        resource = new FileSystemResource(path);

        if (!resource.exists()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        HttpHeaders headers = new HttpHeaders();
        Path filePath = null;

        try {
            filePath = Paths.get(path);
            headers.add("Content-Type", Files.probeContentType(filePath));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }
}
