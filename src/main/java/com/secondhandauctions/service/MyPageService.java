package com.secondhandauctions.service;

import com.secondhandauctions.dao.MyPageDao;
import com.secondhandauctions.utils.Commons;
import com.secondhandauctions.utils.Criteria;
import com.secondhandauctions.vo.ImageVo;
import com.secondhandauctions.vo.ProductVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Paths;
import java.sql.PreparedStatement;
import java.util.*;

@Service
@Slf4j
public class MyPageService {

    @Autowired
    private MyPageDao myPageDao;

    @Autowired
    private Commons commons;

    public int checkPassword(Map<String, String> info) throws Exception {
        int check = 0;

        if (info.isEmpty()) {
            return check;
        }

        check = myPageDao.checkPassword(info);

        if (check > 1 ) {
            check = 0;
            return check;
        }

        return check;
    }

    public int getMyShopListCount(String memberId) throws Exception {
        int count = 0;

        if (("".equals(memberId)) || (memberId == null)) {
            return count;
        }

        count = myPageDao.count(memberId);

        return count;
    }

    public List<ProductVo> getMyShopList(Map<String, Object> params) throws Exception {
        List<ProductVo> result = new ArrayList<>();
        String memberId = "";

        memberId = (String) params.get("memberId");

        if (("".equals(memberId)) || (memberId == null)) {
            result = Collections.emptyList();
            return result;
        }

        result = myPageDao.myShopList(params);

        return result;
    }

    public Map<String, Object> getMyShopDetail(Map<String, Object> info) throws Exception {
        Map<String, Object> result = new HashMap<>();
        List<ImageVo> imageList = new ArrayList<>();
        List<String> fileNames = new ArrayList<>();
        ProductVo productVo = new ProductVo();
        List<Map<String, Object>> qna = new ArrayList<>();

        String memberId = "";
        Integer productId = null;
        String fileName = "";

        productVo = myPageDao.myShopDetail(info);

        imageList = myPageDao.myShopDetailImage(info);

        if (imageList.isEmpty()) {
            imageList = Collections.emptyList();
        }

        for (ImageVo imageVo : imageList) {
            fileName = Paths.get("/" + imageVo.getUploadPath() + "/" + imageVo.getUploadFileName()).toString();

            fileNames.add(fileName);
        }

        qna = myPageDao.myShopProductQnA(info);

        result.put("product", productVo);
        result.put("fileName", fileNames);
        result.put("qna", qna);

        return result;
    }

    public int deleteProduct(Map<String, Object> params) throws Exception {
        int result = 0;

        if (params.isEmpty()) {
            return result;
        }

        result = myPageDao.deleteProduct(params);

        return result;
    }

    public Map<String, Object> getQuestion(Map<String, Object> info) throws Exception {
        Map<String, Object> result = new HashMap<>();

        if (info.isEmpty()) {
//            return Collections.EMPTY_MAP;
            return Collections.emptyMap(); // 변경 하는 순간 UnsupportedOperationException
        }

        result = myPageDao.readQuestion(info);

        if (result.isEmpty()) {
            return Collections.EMPTY_MAP;
        }

        return result;
    }

    // 한명의 판매자만 답변을 해주는데 multi thread 가 아니지 않나? 동기화 해줄 필요 없지 않나?
    public int setAnswer(Map<String, Object> info, int productId) throws Exception {
        int check = 0;

        if (info.isEmpty()) {
            return check;
        }

        check = myPageDao.registerAnswer(info);


        if (check == 0) {
            log.error("registerAnswer check is {}", check);

            return check;
        }

        check = myPageDao.updateAnswerOfQuestion(productId);


        if (check == 0) {
            log.error("isAnswer update check is {}", check);

            return check;
        }

        return check;
    }

    public int isBidCheck(int productId) throws Exception {
        int check = 0;

        if (productId < 1) {
            log.error("bid check productId < 1");
            check = -1;

            return check;
        }

        check = myPageDao.isBidCheck(productId);

        return check;
    }

    public ProductVo getProductDetail(Map<String, Object> info) throws Exception {
        ProductVo productVo = new ProductVo();

        if (info.isEmpty()) {
            return productVo;
        }

        productVo = myPageDao.myShopDetail(info);

        return productVo;
    }

    public List<ImageVo> getProductImages(Map<String, Object> info) throws Exception {
        List<ImageVo> imageList = new ArrayList<>();

        if (info.isEmpty()) {
            return Collections.emptyList();
        }

        imageList = myPageDao.myShopDetailImage(info);

        return imageList;
    }

    public int deleteImg(Map<String, Object> params) throws Exception {
        int result = 0;

        if (params.isEmpty()) {
            log.info("params isEmpty");
            return result;
        }

        result = myPageDao.imageDelete(params);

        return result;
    }

    public int setModifyProduct(ProductVo productVo) throws Exception {
        List<String> checkList = new ArrayList<>();
        int result = 0;
        boolean chk = false;

        String categoryName = "";
        String productTitle = "";
        String productContent = "";
        String startPrice = "";

        // TODO: 2021/12/05 리스트로 묶어서 따로 빈값 비교 메소드 만들자 귀찮다.
        categoryName = productVo.getCategoryName();
        productTitle = productVo.getProductTitle();
        productContent = productVo.getProductContent();
        startPrice = productVo.getStartPrice();

        checkList.add(categoryName);
        checkList.add(productTitle);
        checkList.add(productContent);
        checkList.add(startPrice);

        chk = commons.isEmpty(checkList);

        if (chk == true) {
            return result;
        }

        result = myPageDao.modifyProduct(productVo);

        return result;
    }

    public int getMyBidProductCount(String memberId) throws Exception {
        int count = 0;

        if (StringUtils.isEmpty(memberId)) {
            return count;
        }

        count = myPageDao.myBidProductCount(memberId);

        return count;
    }

    public List<ProductVo> getMyBidProduct(Map<String, Object> params) throws Exception {
        List<ProductVo> result = new ArrayList<>();

        if (params.isEmpty()) {
            return Collections.emptyList();
        }

        result = myPageDao.myBidProductList(params);

        return result;
    }

    public int countSuccessSell(String memberId) throws Exception {
        int count = 0;

        if (StringUtils.isEmpty(memberId)) {
            return count;
        }

        count = myPageDao.myBidSuccessCountBySeller(memberId);

        return count;
    }

    public int countSuccessBid(String memberId) throws Exception {
        int count = 0;

        if (StringUtils.isEmpty(memberId)) {
            return count;
        }

        count = myPageDao.myBidSuccessCountByBidder(memberId);

        return count;
    }

    // 입찰 한 것 중 낙찰에 성공한 것.
    public List<Map<String, Object>> getSuccessBidList(Map<String, Object> info) throws Exception {
        List<Map<String, Object>> result = new ArrayList<>();
        String memberId = "";

        memberId = (String) info.get("memberId");

        if (StringUtils.isEmpty(memberId)) {
            return Collections.emptyList();
        }

        result = myPageDao.myBidSuccessListByBidder(info);

        return result;
    }

    // 판매한 것 중 낙찰에 성공한 것.
    public List<Map<String, Object>> getSuccessSellList(Map<String, Object> info) throws Exception {
        List<Map<String, Object>> result = new ArrayList<>();
        String memberId = "";

        memberId = (String) info.get("memberId");

        if (StringUtils.isEmpty(memberId)) {
            return Collections.emptyList();
        }

        result = myPageDao.myBidSuccessListBySeller(info);

        return result;
    }

    public Map<String, Object> getSuccessBidDetail(Map<String, Object> info) throws Exception {
        Map<String, Object> detail = new HashMap<>();
        List<ImageVo> imageList = new ArrayList<>();
        Map<String, Object> result = new HashMap<>();

        String memberId = "";
        int successBidNo = 0;
        int productId = 0;

        memberId = (String) info.get("memberId");
        successBidNo = (int) info.get("successBidNo");
        productId = (int) info.get("productId");

        if (StringUtils.isEmpty(memberId) || (successBidNo == 0) || (productId == 0)) {
            log.error("memberId :: '{}'", memberId);
            log.error("successBidNo :: '{}'", successBidNo);
            log.error("productId :: '{}'", productId);
            return Collections.emptyMap();
        }

        detail = myPageDao.mySuccessBidDetail(info);

        if (detail.isEmpty()) {
            log.error("Do not search detail, successBidNo :: '{}'", successBidNo);
            return Collections.emptyMap();
        }

        imageList = myPageDao.myShopDetailImage(info);

        result.put("successProduct", detail);
        result.put("imageList", imageList);

        return result;
    }

    public Map<String, Object> getSuccessBidSellDetail(Map<String, Object> info) throws Exception {
        Map<String, Object> detail = new HashMap<>();
        List<ImageVo> imageList = new ArrayList<>();
        Map<String, Object> result = new HashMap<>();

        String memberId = "";
        int successBidNo = 0;
        int productId = 0;

        memberId = (String) info.get("memberId");
        successBidNo = (int) info.get("successBidNo");
        productId = (int) info.get("productId");

        if (StringUtils.isEmpty(memberId) || (successBidNo == 0) || (productId == 0)) {
            log.error("memberId :: '{}'", memberId);
            log.error("successBidNo :: '{}'", successBidNo);
            log.error("productId :: '{}'", productId);
            return Collections.emptyMap();
        }

        detail = myPageDao.mySuccessBidSellDetail(info);

        if (detail.isEmpty()) {
            log.error("Do not search detail, successBidNo :: '{}'", successBidNo);
            return Collections.emptyMap();
        }

        imageList = myPageDao.myShopDetailImage(info);

        result.put("successProduct", detail);
        result.put("imageList", imageList);

        return result;
    }
}
