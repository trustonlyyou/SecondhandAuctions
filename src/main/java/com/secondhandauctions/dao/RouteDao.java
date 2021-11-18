package com.secondhandauctions.dao;

import com.secondhandauctions.utils.Criteria;
import com.secondhandauctions.vo.ShopVo;

import java.util.List;

public interface RouteDao {

    public int countProduct() throws Exception;
    public List<ShopVo> newProductList(Criteria criteria) throws Exception;

    public List<ShopVo> expireTimeProductList(Criteria criteria) throws Exception;
}
