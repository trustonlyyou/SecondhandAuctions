package com.secondhandauctions.controller;

import com.secondhandauctions.dao.ProductDao;
import com.secondhandauctions.service.FileService;
import com.secondhandauctions.service.ShopService;
import com.secondhandauctions.utils.Commons;
import com.secondhandauctions.utils.Criteria;
import com.secondhandauctions.utils.PagingUtil;
import com.secondhandauctions.vo.ProductVo;
import com.secondhandauctions.vo.ShopVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
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
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class ShopController {

    private static final Logger logger = LoggerFactory.getLogger(ShopController.class);

    @Autowired
    private ShopService shopService;

    @Autowired
    private FileService fileService;

    @Autowired
    private Commons commons;

    // TODO: 2021/11/29 categoryName = null 일때는?
    @GetMapping(value = "/shop")
    public String shopList(@ModelAttribute Criteria criteria,
                           @RequestParam(required = false) String categoryName, Model model) throws Exception {
        List<ShopVo> list = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        PagingUtil pagingUtil = new PagingUtil();
        int count = 0;

        if (StringUtils.isEmpty(categoryName)) {
            list = shopService.getNewProductList(criteria);
        } else {
            params.put("categoryName", categoryName);
            params.put("criteria", criteria);

        }

        count = shopService.getTotalCount();
        list = shopService.getListOfCategory(params);

        pagingUtil.setCriteria(criteria);
        pagingUtil.setTotalCount(count);

        model.addAttribute("list", list);
        model.addAttribute("pageMaker", pagingUtil);

        log.info("category :: {}", categoryName);

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
    public ResponseEntity<Resource> fileShow(@RequestParam String uploadPath, @RequestParam String fileName) throws Exception {
        ResponseEntity<Resource> result = null;
        Resource resource = null;
        String path = "";

        path = Paths.get(ProductDao.UPLOAD_PATH + "/" + uploadPath + "/" + fileName).toString();

        // 이건 안쓰고
        // apache ftpservice 사용한다.
        resource = new FileSystemResource(path);

        if (!resource.exists()) {
            log.error("error :: {}", HttpStatus.NOT_FOUND);

            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        HttpHeaders headers = new HttpHeaders();
        Path filePath = null;

        filePath = Paths.get(path);
        headers.add("Content-Type", Files.probeContentType(filePath));

        result = new ResponseEntity<>(resource, headers, HttpStatus.OK);

        log.info("Response ::  {}", result.toString());

        return result;
    }

    // 디테일 페이지
    @GetMapping(value = "/shop/get")
    public String shopDetail(@RequestParam int productId,
                             @ModelAttribute("criteria") Criteria criteria, Model model) throws Exception {

        Map<String, Object> info = new HashMap<>();
        List<String> fileNames = new ArrayList<>();
        ProductVo productVo = new ProductVo();

        log.info("productId :: '{}'", productId);

        info = shopService.getDetail(productId);

        productVo = (ProductVo) info.get("product");

        if (productVo == null) {
            log.error("error :: {}", "product id null");
            model.addAttribute("msg", "productNull");
        }

        fileNames = (List<String>) info.get("fileName");

        if (fileNames.isEmpty()) {
            log.error("error :: {}", "Image is null");
        }

        model.addAttribute("product", productVo);
        model.addAttribute("fileName", fileNames);

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

    // Q&A
    @GetMapping("/shop/question")
    @ResponseBody
    public Map<String, Object> loadQuestionAnswer(HttpServletRequest request) throws Exception {
        Map<String, Object> result = new HashMap<>();
        String memberId = "";

        memberId = commons.getMemberSession(request);

        if (StringUtils.isEmpty(memberId)) {
            result.put("sessionCheck", 0);
        }

        return result;
    }


}
