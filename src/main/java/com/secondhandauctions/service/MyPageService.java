package com.secondhandauctions.service;

import com.secondhandauctions.dao.MyPageDao;
import com.secondhandauctions.utils.Criteria;
import com.secondhandauctions.vo.ImageVo;
import com.secondhandauctions.vo.ProductVo;
import lombok.extern.slf4j.Slf4j;
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
}
