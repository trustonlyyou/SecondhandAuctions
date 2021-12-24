package com.secondhandauctions.service;

import com.secondhandauctions.dao.ProductDao;
import com.secondhandauctions.utils.Commons;
import com.secondhandauctions.vo.ImageVo;
import lombok.extern.slf4j.Slf4j;
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
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class FileService {

    @Autowired
    private Commons commons;

    public ResponseEntity<byte[]> getImageAjax(String fileName) {
        ResponseEntity<byte[]> result = null;

        log.info("fileName :: " + fileName);

        String uploadFolder = ProductDao.UPLOAD_PATH;

        File file = new File(uploadFolder, fileName);

        if (!file.exists()) {
            result = new ResponseEntity<>(HttpStatus.NOT_FOUND);

            return result;
        }

        log.info("filePath :: " + file.getPath());

        try {
            HttpHeaders header = new HttpHeaders();
            header.add("Content-type", Files.probeContentType(file.toPath())); // contentType 을 알 수 있다.
            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);
        } catch (Exception e) {
            log.error(commons.printStackLog(e));

            result = new ResponseEntity<>(null, null, HttpStatus.NOT_IMPLEMENTED);

            return result;
        }

        return result;
    }

    public List<String> getImageFileNames(List<ImageVo> imageList) {
        List<String> fileNames = new ArrayList<>();
        String fileName = "";

        if (imageList.isEmpty()) {
            return Collections.emptyList();
        } else {
            for (ImageVo imageVo : imageList) {
                fileName = Paths.get("/" + imageVo.getUploadPath() + "/" + imageVo.getUploadFileName()).toString();

                fileNames.add(fileName);
            }

            return fileNames;
        }
    }
}
