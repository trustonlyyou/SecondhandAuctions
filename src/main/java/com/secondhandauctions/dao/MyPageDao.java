package com.secondhandauctions.dao;

import com.secondhandauctions.vo.ImageVo;
import com.secondhandauctions.vo.ProductVo;

import java.util.List;
import java.util.Map;

public interface MyPageDao {

    public int count(String memberId) throws Exception;

    public List<ProductVo> myShopList(Map<String, Object> params) throws Exception;

    public ProductVo myShopDetail(Map<String, Object> info) throws Exception;

    public List<ImageVo> myShopDetailImage(Map<String, Object> info) throws Exception;

}
