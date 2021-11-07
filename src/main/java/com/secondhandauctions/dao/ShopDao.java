package com.secondhandauctions.dao;

import com.secondhandauctions.utils.Criteria;
import com.secondhandauctions.vo.ShopVo;

import java.util.List;

public interface ShopDao {

    public int countProduct() throws Exception;

    public List<ShopVo> getListWithPaging(Criteria criteria) throws Exception;

    public ShopVo getDetail(int productId) throws Exception;
}
