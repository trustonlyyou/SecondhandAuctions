package com.secondhandauctions.service;

import com.secondhandauctions.dao.ShopDao;
import com.secondhandauctions.utils.Criteria;
import com.secondhandauctions.vo.ImageVo;
import com.secondhandauctions.vo.ProductVo;
import com.secondhandauctions.vo.ShopVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;
import java.util.*;

@Service
@Slf4j
public class ShopService {

    @Autowired
    private ShopDao shopDao;

    public int getTotalCount() throws Exception {
        return shopDao.countProduct();
    }

    public int getProductCountOfCategory(String categoryName) throws Exception {
        return shopDao.countProductOfCategory(categoryName);
    }

    public List<ShopVo> getListOfCategory(Map<String, Object> params) throws Exception {
        List<ShopVo> list = new ArrayList<>();

        list = shopDao.getListOfCategory(params);

        if (list.isEmpty()) {
            list = Collections.emptyList();

            return list;
        }

        return list;
    }

    public Map<String, Object> getDetail(int productId) throws Exception {
        Map<String, Object> info = new HashMap<>();
        List<ImageVo> imageList = new ArrayList<>();
        List<String> fileNames = new ArrayList<>();
        ProductVo productVo = new ProductVo();
        List<Map<String, Object>> question = new ArrayList<>();

        String fileName = "";

        productVo = shopDao.readProduct(productId);

        if (productVo == null) {
            info.put("product", null);
        }

        info.put("product", productVo);

        imageList = shopDao.readProductImage(productId);

        if (imageList.isEmpty()) {
            imageList = Collections.emptyList();
        }

        for (ImageVo imageVo : imageList) {
            fileName = Paths.get("/" + imageVo.getUploadPath() + "/" + imageVo.getUploadFileName()).toString();

            fileNames.add(fileName);
        }

        info.put("fileName", fileNames);

        question = shopDao.readProductQnA(productId);

        info.put("qna", question);

        return info;
    }

    public List<ShopVo> getNewProductList(Criteria criteria) throws Exception {
        List<ShopVo> itemList = new ArrayList<>();

        itemList = shopDao.newProductList(criteria);

        if (itemList.isEmpty()) {
            log.info("itemList is empty");
            itemList = Collections.emptyList();
        }
        return itemList;
    }

    public List<ShopVo> getExpireTimeProductList(Criteria criteria) throws Exception {
        List<ShopVo> itemList = new ArrayList<>();

        itemList = shopDao.expireTimeProductList(criteria);

        if (itemList.isEmpty()) {
            itemList = Collections.emptyList();
        }

        return itemList;
    }

    public List<ShopVo> getNewProductListOfCategory(Map<String, Object> params) throws Exception {
        List<ShopVo> itemList = new ArrayList<>();

        if (params.isEmpty()) {
            return Collections.emptyList();
        }

        itemList = shopDao.newProductListOfCategory(params);

        return itemList;
    }

    public List<ShopVo> getExpireTimeProductListOfCategory(Map<String, Object> params) throws Exception {
        List<ShopVo> itemList = new ArrayList<>();

        if (params.isEmpty()) {
            return Collections.emptyList();
        }

        itemList = shopDao.expireTimeProductListOfCategory(params);

        return itemList;
    }

    public int setQuestion(Map<String, Object> params) throws Exception {
        int check = 0;

        if (params.isEmpty()) {
            return check;
        }
        check = shopDao.registerQuestion(params);

        return check;
    }
}
