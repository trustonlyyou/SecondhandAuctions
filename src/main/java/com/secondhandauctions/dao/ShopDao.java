package com.secondhandauctions.dao;

import com.secondhandauctions.utils.Criteria;
import com.secondhandauctions.vo.ImageVo;
import com.secondhandauctions.vo.ProductVo;
import com.secondhandauctions.vo.ShopVo;

import java.util.List;
import java.util.Map;

public interface ShopDao {

    public int countProduct() throws Exception;

    public List<ShopVo> getListOfCategory(Map<String, Object> params) throws Exception;

    public ProductVo readProduct(int productId) throws Exception;

    public List<Map<String, Object>> readProductQnA(int productId) throws Exception;

    public List<ImageVo> readProductImage(int productId) throws Exception;

    public List<ShopVo> newProductList(Criteria criteria) throws Exception;

    public List<ShopVo> expireTimeProductList(Criteria criteria) throws Exception;

    public int registerQuestion(Map<String, Object> params) throws Exception;


}
