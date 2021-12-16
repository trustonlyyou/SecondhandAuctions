package com.secondhandauctions.dao;

import java.util.Map;

public interface BidDao {

    public int registerBid(Map<String, Object> params) throws Exception;

    public String topBidMember(int productId) throws Exception;
}