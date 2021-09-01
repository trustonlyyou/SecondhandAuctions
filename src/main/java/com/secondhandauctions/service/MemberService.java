package com.secondhandauctions.service;

import com.secondhandauctions.dao.MemberDao;
import com.secondhandauctions.vo.MemberVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
public class MemberService {

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private SmsService smsService;

    public int idCheck(String memberId) throws Exception {
        return memberDao.idCheck(memberId);
    }

    public void setMember(MemberVo memberVo) throws Exception {
        memberDao.join(memberVo);
    }

    public int getLoginResult(Map<String, Object> loginInfo) throws Exception {
        return memberDao.login(loginInfo);
    }

    public MemberVo getMemberInfo(String memberId) throws Exception {
        return memberDao.memberInfo(memberId);
    }

    public String getMemberId(String email) throws Exception {
        return memberDao.searchIdEmail(email);
    }

    @Transactional
    public String getMemberIdFromEmail(String memberEmail) throws Exception {
        int check = 0;
        String memberId = "";

        check = memberDao.isMemberEmail(memberEmail);

        if (check == 0) {
            return memberId;
        }

        memberId = memberDao.searchIdEmail(memberEmail);

        return memberId;
    }

    @Transactional
    public String getMemberIdFromPhone(Map<String, Object> memberInfo) throws Exception {
        int check = 0;
        String memberId = "";

        check = memberDao.isMemberPhone(memberInfo);

        if (check == 0) {
            return memberId;
        }

        memberId = memberDao.searchIdPhone(memberInfo);

        return memberId;
    }

    @Transactional
    public void getMemberPasswordPhone(Map<String, Object> memberInfo) throws Exception {
        int check = 0;

        check = memberDao.checkSearchPwdPhone(memberInfo);

        if (check != 0) {

        }
    }


}
