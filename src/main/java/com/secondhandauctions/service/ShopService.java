package com.secondhandauctions.service;

import com.secondhandauctions.dao.ShopDao;
import com.secondhandauctions.utils.Criteria;
import com.secondhandauctions.vo.ImageVo;
import com.secondhandauctions.vo.ProductVo;
import com.secondhandauctions.vo.ShopVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.Paths;
import java.util.*;

@Service
public class ShopService {

    private static final Logger logger = LoggerFactory.getLogger(ShopService.class);

    @Autowired
    private ShopDao shopDao;

    public int getTotalCount() throws Exception {
        return shopDao.countProduct();
    }

    public List<ShopVo> getList(Criteria criteria) throws Exception {
        List<ShopVo> list = new ArrayList<>();


        list = shopDao.getList(criteria);

        return list;
    }

    public Map<String, Object> getReadProduct(int productId) throws Exception {
        Map<String, Object> info = new HashMap<>();
        List<ImageVo> imageList = new ArrayList<>();
        List<String> fileNames = new ArrayList<>();
        ProductVo productVo = new ProductVo();

        String fileName = "";

        productVo = shopDao.readProduct(productId);

        if (productVo == null) {
            info.put("product", null);

            logger.info("productVo is null");
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

        return info;
    }

    public List<ShopVo> getNewProductList(Criteria criteria) throws Exception {
        List<ShopVo> itemList = new ArrayList<>();

        itemList = shopDao.newProductList(criteria);

        if (itemList.isEmpty()) {
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
}
