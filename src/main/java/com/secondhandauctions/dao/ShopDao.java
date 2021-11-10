package com.secondhandauctions.dao;

import com.secondhandauctions.utils.Criteria;
import com.secondhandauctions.vo.ImageVo;
import com.secondhandauctions.vo.ProductVo;
import com.secondhandauctions.vo.ShopVo;

import java.util.List;

public interface ShopDao {

    public int countProduct() throws Exception;

    public List<ShopVo> getListWithPaging(Criteria criteria) throws Exception;

    public ProductVo readProduct(int productId) throws Exception;

    public List<ImageVo> readProductImage(int productId) throws Exception;
}
