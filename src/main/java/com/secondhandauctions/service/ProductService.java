package com.secondhandauctions.service;

import com.secondhandauctions.dao.ProductDao;
import com.secondhandauctions.utils.FileUtils;
import com.secondhandauctions.vo.ImageVo;
import com.secondhandauctions.vo.ProductVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);

    @Autowired
    private ProductDao productDao;

    @Autowired
    private FileUtils fileUtil;

    public void setProduct(ProductVo productVo) throws Exception {
        productDao.registerProduct(productVo);
    }

    public void setImage(List<ImageVo> list) throws Exception {

        if (list.isEmpty()) {
            list = Collections.emptyList();
        }

        for (ImageVo imageVo : list) {
            logger.info("FileName :: " + imageVo.getFileName());
        }

        productDao.registerImg(list);
    }

    public void registerProduct(ProductVo productVo, MultipartFile[] files) throws Exception {
        // TODO: 2021/09/30 Util 가져오고, Foreach MyBatis
        List<ImageVo> imageList = new ArrayList<>();
        int productId = 0;

        // 게시물 등록
        productDao.registerProduct(productVo);

        productId = productVo.getProductId();



        // image list save
//        imageList = fileUtil.uploadFiles(files, productId);

        // db register
        productDao.registerImg(imageList);

    }

}
