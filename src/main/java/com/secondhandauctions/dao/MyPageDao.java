package com.secondhandauctions.dao;

import com.secondhandauctions.vo.ImageVo;
import com.secondhandauctions.vo.ProductVo;

import java.util.List;
import java.util.Map;

public interface MyPageDao {

    public int checkPassword(Map<String, String> params) throws Exception;

    public int count(String memberId) throws Exception;

    public List<ProductVo> myShopList(Map<String, Object> params) throws Exception;

    public ProductVo myShopDetail(Map<String, Object> info) throws Exception;

    public List<ImageVo> myShopDetailImage(Map<String, Object> info) throws Exception;

    public List<Map<String, Object>> myShopProductQnA(Map<String, Object> params) throws Exception;

    public int deleteProduct(Map<String, Object> info) throws Exception;

    public Map<String, Object> readQuestion(Map<String, Object> info) throws Exception;

    public int registerAnswer(Map<String, Object> info) throws Exception;

    public int updateAnswerOfQuestion(int questionId) throws Exception;

    public int isBidCheck(int productId) throws Exception;

    public int imageDelete(Map<String, Object> params) throws Exception;

    public int modifyProduct(ProductVo productVo) throws Exception;

    public int myBidProductCount(String memberId) throws Exception;

    public List<ProductVo> myBidProductList(Map<String, Object> params) throws Exception;

    public int myBidSuccessCountBySeller(String memberId) throws Exception;

    public int myBidSuccessCountByBidder(String memberId) throws Exception;

    public List<Map<String, Object>> myBidSuccessListByBidder(Map<String, Object> params) throws Exception;

    public List<Map<String, Object>> myBidSuccessListBySeller(Map<String, Object> params) throws Exception;

}
