package com.secondhandauctions.controller;

import com.secondhandauctions.dao.ProductDao;
import com.secondhandauctions.vo.BoardVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
public class TestController {

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @Resource(name = "uploadPath")
    private String uploadPath;

    @Autowired
    private ProductDao productDao;


    @GetMapping(value = "/uploadForm")
    public String uploadForm() {
        return "test/uploadForm";
    }

    @PostMapping(value = "/upload")
    public String upload(@RequestParam("file") MultipartFile multipartFile, Model model) throws Exception {

        logger.info("OriginalFilename :: " + multipartFile.getOriginalFilename());
        logger.info("Size :: " + multipartFile.getSize());
        logger.info("ContentType :: " + multipartFile.getContentType());

        String savedName = uploadFile(multipartFile.getOriginalFilename(), multipartFile.getBytes());

        model.addAttribute("savedName", savedName);

        return "";
    }

    private String uploadFile(String originalName, byte[] fileData) throws Exception {
        UUID uuid = UUID.randomUUID();

        String savedName = uuid.toString() + "_" + originalName;

        File target = new File(uploadPath, savedName);

        FileCopyUtils.copy(fileData, target);

        return savedName;
    }

    @GetMapping(value = "/uploadAjax")
    public String uploadAjax() throws Exception {
        return "test/uploadAjax";
    }

    @GetMapping(value = "/display")
    public ResponseEntity<byte[]> getImage(String fileName) throws Exception {
        logger.info("getImage~~~");

        final String PATH = Paths.get("/Users", "junghwan", "Desktop", "auctionProductImg").toString();

        File file = new File(PATH, fileName);

        ResponseEntity<byte[]> result = null;

        try {
            HttpHeaders header = new HttpHeaders();

            header.add("Content-type", Files.probeContentType(file.toPath()));

            result = new ResponseEntity<>(FileCopyUtils.copyToByteArray(file), header, HttpStatus.OK);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    @GetMapping(value = "/show")
    @ResponseBody
    public List<String> show() throws Exception {
        List<BoardVo> list = new ArrayList<>();
        List<String> imageList = new ArrayList<>();

        list = productDao.searchImgTest();

        for (BoardVo boardVo : list) {
            String imagePath  = boardVo.getImagePath();
            String s_fileName = boardVo.getS_FileName();
            String fileExtension = boardVo.getFileExtension();

            String fullName = imagePath + "/" +
                    s_fileName +
                    "." +
                    fileExtension;

            imageList.add(fullName);
        }

        return imageList;

        // TODO: 2021/10/16 이미지를 어떻게 ajax 로 보낼 것인가!!
        // TODO: 2021/10/16 a,b 테스트 할 것 \\ 썸네일은 List 뿌리는 것 , 원본 -> 썸네일 변환 뿌리믄 속도
        // TODO: 2021/10/16 이미지 업로드 ajax 처리
    }

    @GetMapping(value = "/file")
    public String fileForm() throws Exception {
        return "test/goodsEnroll";
    }

    @PostMapping(value = "/uploadAjaxAction")
    public void uploadAjaxAction(@RequestParam("uploadFile") MultipartFile[] uploadFile) throws Exception {
        logger.info("upload Ajax Action");

        for (MultipartFile multipartFile : uploadFile) {

            logger.info("파일 이름 : " + multipartFile.getOriginalFilename());
            logger.info("파일 타입 : " + multipartFile.getContentType());
            logger.info("파일 크기 : " + multipartFile.getSize());
        }

    }



}
