package com.secondhandauctions.controller;

import com.secondhandauctions.dao.ProductDao;
import com.secondhandauctions.service.FileService;
import com.secondhandauctions.service.ShopService;
import com.secondhandauctions.utils.Criteria;
import com.secondhandauctions.utils.PageDTO;
import com.secondhandauctions.vo.ImageVo;
import com.secondhandauctions.vo.ProductVo;
import com.secondhandauctions.vo.ShopVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ShopController {

    private static final Logger logger = LoggerFactory.getLogger(ShopController.class);

    @Autowired
    private ShopService shopService;

    @Autowired
    private FileService fileService;

    @GetMapping(value = "/shop")
    public String shopList(@ModelAttribute Criteria criteria, Model model) {
        List<ShopVo> list = new ArrayList<>();
        PageDTO pageDTO = new PageDTO();
        String fileName = "";
        int count = 0;

        try {
            count = shopService.getTotalCount();
            list = shopService.getList(criteria);

            pageDTO.setCriteria(criteria);
            pageDTO.setTotalCount(count);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("error :: " + e);
        }

        model.addAttribute("list", list);
        model.addAttribute("pageMaker", pageDTO);

        return "shop/shopList";
    }

    /**
     *
     * @param uploadPath : 파일 경로
     * @param fileName 파일 이름
     * @return : 응답 본문
     *
     * shopList.jsp
     */
    @GetMapping(value = "/file/show")
    public ResponseEntity<Resource> fileShow(@RequestParam String uploadPath, @RequestParam String fileName) {
        Resource resource = null;
        String path = "";
        String type = "";

        logger.info("uploadPath :: " + uploadPath);
        logger.info("fileName :: " + fileName);

        path = Paths.get(ProductDao.UPLOAD_PATH + "/" + uploadPath + "/" + fileName).toString();

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

    // 디테일 페이지
    @GetMapping(value = "/shop/get")
    public String shopDetail(@RequestParam int productId, @ModelAttribute("criteria") Criteria criteria, Model model) {
        Map<String, Object> info = new HashMap<>();
        List<ImageVo> imageList = new ArrayList<>();
        List<String> fileNames = new ArrayList<>();

        ProductVo productVo = new ProductVo();

        logger.info("productId :: " + productId);

        try {
            info = shopService.getReadProduct(productId);

            productVo = (ProductVo) info.get("product");

            if (productVo == null) {
                model.addAttribute("msg", "productNull");
            }

            model.addAttribute("product", productVo);

            fileNames = (List<String>) info.get("fileName");

            model.addAttribute("fileName", fileNames);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("error :: " + e);
        }

        return "shop/shopDetail";
    }

    // 상세 페이지에 이미지를 보여준다. myDetail.jsp, myShopDetail.jsp
    @GetMapping("/detail/show")
    public ResponseEntity<byte[]> loadImages(@RequestParam String fileName, HttpServletRequest request) {
        ResponseEntity<byte[]> result = null;
        int status = 0;

        result = fileService.getImageAjax(fileName);

        if (result.getStatusCodeValue() == 404) {
            return null;
        }

        return result;
    }


}
