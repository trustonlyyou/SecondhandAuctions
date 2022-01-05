package com.secondhandauctions.dao;

import java.util.Map;

public interface PointDao {

    public void successCard(Map<String, Object> info) throws Exception;

    public void successTransferBank(Map<String, Object> info) throws Exception;

    public void pointUpMember(Map<String, Object> info) throws Exception;
}
