package com.secondhandauctions.controller;

import com.secondhandauctions.dao.ProductDao;
import com.secondhandauctions.service.FileService;
import com.secondhandauctions.service.RouteService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
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
    public String shopForm(@ModelAttribute Criteria criteria, Model model) {
        List<ShopVo> list = new ArrayList<>();
        PageDTO pageDTO = new PageDTO();
        String fileName = "";
        int count = 0;

        try {
            count = shopService.getCount();
//            count = 250;
            list = shopService.getList(criteria);

            pageDTO.setCriteria(criteria);
            pageDTO.setTotalCount(count);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("error :: " + e);
        }

        for (ShopVo shopVo : list) {
            logger.info("===========================================");
            logger.info(shopVo.toString());
            logger.info("===========================================");
        }

        model.addAttribute("list", list);
        model.addAttribute("pageMaker", pageDTO);

        return "shop/shopList";
    }

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


    @GetMapping(value = "/shop/get")
    public String get(@RequestParam int productId, @ModelAttribute("criteria") Criteria criteria, Model model) {
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

//            이미지 없을 때는 대체 이미지 구해서 집어 넣자.
//            if (fileNames.isEmpty()) {
//                model.addAttribute("fileName", "fileNameNull");
//            }

            model.addAttribute("fileName", fileNames);

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("error :: " + e);
        }

        return "shop/shopDetail";
    }

    @GetMapping("/detail/show")
    public ResponseEntity<byte[]> loadImages(@RequestParam String fileName, HttpServletRequest request) {
        ResponseEntity<byte[]> result = null;
        int status = 0;

        result = fileService.getImageAjax(fileName);

        if (result.getStatusCodeValue() == 404) {
            return null;
            // TODO: 2021/11/10 Show no Image
        }

        return result;
    }


}
