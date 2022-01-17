package com.secondhandauctions.dao;

import java.util.Map;

public interface PointDao {

    public int successCard(Map<String, Object> info) throws Exception;

    public int successTransferBank(Map<String, Object> info) throws Exception;

    public int pointUpMember(Map<String, Object> info) throws Exception;

    public void pointUpdateMemberTime(Map<String, Object> info) throws Exception;

    public void setCancelCard(Map cancelInfo) throws Exception;

    public void setCancelTransfer(Map cancelInfo) throws Exception;

    public void pointDown(Map<String, Object> info) throws Exception;

    public void cancelPay(String paymentKey) throws Exception;

    public int bidderSeller(Map<String, Object> info) throws Exception;

    public String memberPayPriceAllow(Map<String, Object> info) throws Exception;

    public void closeSuccessBid(int successBidNo) throws Exception;
}
