package com.secondhandauctions.dao;

import java.util.Map;

public interface PointDao {

    public int successCard(Map<String, Object> info) throws Exception;

    public int successTransferBank(Map<String, Object> info) throws Exception;

    public int pointUpMember(Map<String, Object> info) throws Exception;
}
