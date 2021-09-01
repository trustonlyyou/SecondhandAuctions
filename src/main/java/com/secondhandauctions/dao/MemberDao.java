package com.secondhandauctions.dao;

import com.secondhandauctions.vo.MemberVo;

import java.util.Map;

public interface MemberDao {

    public int idCheck(String memberId) throws Exception;

    public void join(MemberVo memberVo) throws Exception;

    public int login(Map<String, Object> memberInfo) throws Exception;

    public MemberVo memberInfo(String memberId) throws Exception;

    public String searchIdEmail(String memberEmail) throws Exception;

    public String searchIdPhone(Map<String, Object> params) throws Exception;

    public int isMemberEmail(String memberEmail) throws Exception;

    public int isMemberPhone(Map<String, Object> params) throws Exception;

    public int checkSearchPwdPhone(Map<String, Object> params) throws Exception;

    public int checkSearchPwdEmail(Map<String, Object> params) throws Exception;

    public String getPasswordFromPhone(Map<String, Object> params) throws Exception;

    public String getPasswordFromEmail(Map<String, Object> params) throws Exception;
}

