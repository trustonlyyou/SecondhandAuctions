package com.secondhandauctions.service;

import com.secondhandauctions.dao.MyPageDao;
import com.secondhandauctions.utils.Commons;
import com.secondhandauctions.vo.ImageVo;
import com.secondhandauctions.vo.ProductVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;
import java.util.*;

@Service
@Slf4j
public class MyPageService {

    @Autowired
    private MyPageDao myPageDao;

    @Autowired
    private FileService fileService;

    @Autowired
    private Commons commons;

    public boolean isAuthority(Map<String, String> memberInfo) throws Exception {
        int chk = 0;

        if (memberInfo.isEmpty()) {
            log.info("member info isEmpty");
            return false;
        } else {
            chk = myPageDao.checkPassword(memberInfo);

            if (chk == 1) {
                return true;
            } else {
                return false;
            }
        }
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
        List<String> fileNames = new ArrayList<>();
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
        fileNames = fileService.getImageFileNames(imageList);

        result.put("successProduct", detail);
        result.put("fileNames", fileNames);

        return result;
    }

    public Map<String, Object> getSuccessBidSellDetail(Map<String, Object> info) throws Exception {
        Map<String, Object> detail = new HashMap<>();
        List<ImageVo> imageList = new ArrayList<>();
        List<String> fileNames = new ArrayList<>();
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
        fileNames = fileService.getImageFileNames(imageList);

        result.put("successProduct", detail);
        result.put("fileNames", fileNames);

        return result;
    }

    public int getCountChargePoint(String memberId) throws Exception {

        if (StringUtils.isEmpty(memberId)) {
            return 0;
        } else {
            return myPageDao.countMyPontChargeList(memberId);
        }
    }

    public List<Map<String, Object>> getChargePointList(Map<String, Object> info) throws Exception {
        List<Map<String, Object>> reuslt = new ArrayList<>();

        if (info.isEmpty()) {
            log.info("info is null");
            return Collections.emptyList();
        } else {

            try {
                reuslt = myPageDao.myPointChargeList(info);
                log.info(reuslt.toString());
            } catch (Exception e) {
                commons.printStackLog(e);
                return Collections.emptyList();
            }

            return reuslt;
        }
    }

    public int getMyPoint(String memberId) throws Exception {
        if (StringUtils.isEmpty(memberId)) {
            log.error("MemberId is Empty");
            return 0;
        } else {
            return myPageDao.myPoint(memberId);
        }
    }
}
