package com.secondhandauctions.dao;

import java.util.List;
import java.util.Map;

public interface AdminDao {

    // 고객 포인트 구매정보
    public List<Map<String, Object>> memberChargeList(String keyword) throws Exception;

    public List<Map<String, Object>> payPointList(String keyword) throws Exception;

    // 고객 포인트 결제정보
}
