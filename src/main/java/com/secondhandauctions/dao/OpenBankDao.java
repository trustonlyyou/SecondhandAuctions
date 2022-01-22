package com.secondhandauctions.dao;

import java.util.Map;

public interface OpenBankDao {

    public Map<String, Object> accountChk(String memberId) throws Exception;

    public void setToken(Map<String, Object> params) throws Exception;

    public String getToken(String memberId) throws Exception;

    public void setBankInfo(Map<String, Object> info) throws Exception;

    public Map<String, Object> getBankInfo(String memberId) throws Exception;

    public void setExchangeInfo(Map<String, Object> params) throws Exception;
}
