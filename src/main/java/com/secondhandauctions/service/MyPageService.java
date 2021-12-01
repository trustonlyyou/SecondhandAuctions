package com.secondhandauctions.service;

import com.secondhandauctions.dao.MyPageDao;
import com.secondhandauctions.utils.Criteria;
import com.secondhandauctions.vo.ImageVo;
import com.secondhandauctions.vo.ProductVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;
import java.util.*;

@Service
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

        String memberId = "";
        Integer productId = null;
        String fileName = "";

        memberId = (String) info.get("memberId");
        productId = (Integer) info.get("productId");

        productVo = myPageDao.myShopDetail(info);

        imageList = myPageDao.myShopDetailImage(info);

        if (imageList.isEmpty()) {
            imageList = Collections.emptyList();
        }

        for (ImageVo imageVo : imageList) {
            fileName = Paths.get("/" + imageVo.getUploadPath() + "/" + imageVo.getUploadFileName()).toString();

            fileNames.add(fileName);
        }

        result.put("product", productVo);
        result.put("fileName", fileNames);


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
}
