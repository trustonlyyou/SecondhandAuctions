package com.secondhandauctions.dao;

import com.secondhandauctions.vo.MemberVo;

import java.util.Map;

public interface MemberDao {

    public int idCheck(String memberId) throws Exception;

    public void join(MemberVo memberVo) throws Exception;

    public int login(Map<String, Object> memberInfo) throws Exception;

    // todo ::  비밀번호도 체크해야지
    public MemberVo memberInfo(String memberId) throws Exception;

    // 아이디 찾기
    public String searchIdEmail(Map<String, Object> info) throws Exception;

    public String searchIdPhone(Map<String, Object> params) throws Exception;

    public int isMemberEmail(String memberEmail) throws Exception;

    public String getMemberEmail(String memberId) throws Exception;

    public int isMemberPhone(Map<String, Object> params) throws Exception;

    // 비밀번호 찾기
    public int checkSearchPwdPhone(Map<String, Object> params) throws Exception;

    public int checkSearchPwdEmail(Map<String, Object> params) throws Exception;

    public int modifyPassword(Map<String, Object> params) throws Exception;
}

