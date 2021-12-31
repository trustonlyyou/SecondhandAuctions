package com.secondhandauctions.dao;

import com.secondhandauctions.vo.MemberVo;

import java.util.Map;

public interface MemberDao {

    public int idCheck(String memberId) throws Exception;

    public void join(MemberVo memberVo) throws Exception;

    public int joinByKakao(Map<String, String> memberInfo) throws Exception;

    public void memberIdFlag(String memberId) throws Exception;

    public Map<String, Object> isMember(String memberId) throws Exception;

    public void loginFailCountUp(String memberId) throws Exception;

    public void resetFailCount(String memberId) throws Exception;

    public void solveLock(String memberId) throws Exception;

    public int loginFailCount(String memberId) throws Exception;

    public void loginLock(String memberId) throws Exception;

    public Map<String, Object> login(Map<String, Object> memberInfo) throws Exception;

    public int isLockMemberPhone(Map<String, String> memberInfo) throws Exception;

    public int isLockMemberEmail(Map<String, String> memberInfo) throws Exception;

    public String memberIdByEmail(String memberEmail) throws Exception;

    public MemberVo memberInfo(String memberId) throws Exception;

    // 아이디 찾기
    public String searchIdEmail(Map<String, Object> info) throws Exception;

    public String searchIdPhone(Map<String, Object> params) throws Exception;

    public int isMemberEmail(String memberEmail) throws Exception;

    public String getMemberEmail(String memberId) throws Exception;

    public int isMemberPhone(Map<String, Object> params) throws Exception;

    public Integer isKakaoMember(String memberId) throws Exception;

    // 비밀번호 찾기
    public int checkSearchPwdPhone(Map<String, Object> params) throws Exception;

    public int checkSearchPwdEmail(Map<String, Object> params) throws Exception;

    public int modifyPassword(Map<String, Object> params) throws Exception;
}

