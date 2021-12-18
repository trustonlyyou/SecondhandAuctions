package com.secondhandauctions.dao;

import java.util.List;
import java.util.Map;

public interface BidDao {

    public int registerBid(Map<String, Object> params) throws Exception;

    public String topBidMember(int productId) throws Exception;

    public List<Map<String, String>> successBiddersPhone() throws Exception;

    public List<Map<String, String>> successSellerPhone() throws Exception;
}
