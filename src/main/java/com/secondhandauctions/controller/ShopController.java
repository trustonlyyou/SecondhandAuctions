package com.secondhandauctions.controller;

import com.secondhandauctions.dao.ProductDao;
import com.secondhandauctions.service.BidService;
import com.secondhandauctions.service.FileService;
import com.secondhandauctions.service.ShopService;
import com.secondhandauctions.utils.Commons;
import com.secondhandauctions.utils.Criteria;
import com.secondhandauctions.utils.PagingUtil;
import com.secondhandauctions.vo.ProductVo;
import com.secondhandauctions.vo.ShopVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Controller
@Slf4j
public class ShopController {

    @Autowired
    private ShopService shopService;

    @Autowired
    private FileService fileService;

    @Autowired
    BidService bidService;

    @Autowired
    private Commons commons;

    @GetMapping(value = "/shop")
    public String shopList(@ModelAttribute Criteria criteria,
                           @RequestParam(required = false) String categoryName,
                           @RequestParam(required = false) String status, Model model) throws Exception {
        List<ShopVo> list = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        PagingUtil pagingUtil = new PagingUtil();
        int count = 0;

        if (StringUtils.isEmpty(categoryName)) {
            if (StringUtils.isEmpty(status)) {
                list = shopService.getNewProductList(criteria);
                count = shopService.getTotalCount();
            } else {
                switch (status) {
                    case "" :
                    case "newList" :
                        count = shopService.getTotalCount();
                        list = shopService.getNewProductList(criteria);

                        break;

                    case "expireList" :
                        count = shopService.getTotalCount();
                        list = shopService.getExpireTimeProductList(criteria);

                        break;
                }
            }
        } else { // 카테고리 있음
            if (StringUtils.isEmpty(status)) {
                params.put("categoryName", categoryName);
                params.put("criteria", criteria);

                count = shopService.getProductCountOfCategory(categoryName);
                list = shopService.getListOfCategory(params);
            } else { // 카테고리 있고 status 있음
                switch (status) {
                    case "" :
                    case "newList" :
                        params.put("categoryName", categoryName);
                        params.put("criteria", criteria);

                        count = shopService.getProductCountOfCategory(categoryName);
                        list = shopService.getNewProductListOfCategory(params);

                        break;

                    case "expireList" :
                        params.put("categoryName", categoryName);
                        params.put("criteria", criteria);

                        count = shopService.getTotalCount();
                        list = shopService.getExpireTimeProductListOfCategory(params);

                        break;
                }
            }

        }


        pagingUtil.setCriteria(criteria);
        pagingUtil.setTotalCount(count);

        model.addAttribute("list", list);
        model.addAttribute("pageMaker", pagingUtil);
        model.addAttribute("category", categoryName);

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
    public String shopDetail(HttpServletRequest request, @RequestParam int productId,
                             @ModelAttribute("criteria") Criteria criteria, Model model) throws Exception {

        Map<String, Object> info = new HashMap<>();
        List<String> fileNames = new ArrayList<>();
        ProductVo productVo = new ProductVo();
        List<Map<String, Object>> question = new ArrayList<>();

        String topBidMember = "";

        log.info("productId :: '{}'", productId);

        info = shopService.getDetail(productId);

        productVo = (ProductVo) info.get("product");

        if (productVo == null) {
            log.error("error :: {}", "product id null");
            model.addAttribute("msg", "productNull");
        }

        fileNames = (List<String>) info.get("fileName");

        if (fileNames.isEmpty()) {
            model.addAttribute("fileName", Collections.EMPTY_MAP);
            log.error("error :: {}", "Image is null");
        }

        question = (List<Map<String, Object>>) info.get("qna");

        topBidMember = bidService.getTopBidMember(productId);

        model.addAttribute("product", productVo);
        model.addAttribute("fileName", fileNames);
        model.addAttribute("qna", question);
        model.addAttribute("topBidMember", topBidMember);

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

    // ============== 질문 등록================
    @GetMapping(value = "/shop/question/form")
    public String questionForm(HttpServletRequest request,
                                   @RequestParam int productId, Model model) {

        String referer = request.getHeader("Referer");

        log.info("prevPageUrl :: {}", referer);
        log.info("target productId :: '{}'", productId);

        model.addAttribute("targetProductId", productId);
        model.addAttribute("prevPageUrl", referer);

        return "shop/questionForm";
    }


    @PostMapping(value = "/shop/question/register")
    @ResponseBody
    public Map<String, Object> questionRegister(String questionTitle, String questionContent, int productId,
                                                HttpServletRequest request, Model model) throws Exception {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> params = new HashMap<>();

        String memberId = "";
        int check = 0;

        memberId = commons.getMemberSession(request);

        if (StringUtils.isEmpty(questionTitle) || StringUtils.isEmpty(questionContent)) {
            result.put("check", check);

            return result;
        }

        params.put("productId", productId);
        params.put("memberId", memberId);
        params.put("questionTitle", questionTitle);
        params.put("questionContent", questionContent);

        check = shopService.setQuestion(params);

        log.info("check :: " + check);

        result.put("check", check);

        return result;
    }
}
