package com.secondhandauctions.service;

import com.secondhandauctions.dao.MemberDao;
import com.secondhandauctions.vo.MemberVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
public class MemberService {

    private static final Logger logger = LoggerFactory.getLogger(MemberService.class);

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

        logger.info("check :: " + check);

        if (check == 0) {
            return memberId;
        }

        memberId = memberDao.searchIdEmail(memberEmail);

        logger.info("memberId :: " + memberId);

        return memberId;
    }

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

    public int isMemberPasswordPhone(Map<String, Object> memberInfo) throws Exception {
        int check = 0;
        String memberPassword = "";

        check = memberDao.checkSearchPwdPhone(memberInfo);

        return check;
    }

    public int isMemberPasswordEmail(Map<String, Object> memberInfo) throws Exception {
        int check = 0;

        check = memberDao.checkSearchPwdEmail(memberInfo);

        if (check == 0) {
            logger.info("Member ID :: " + memberInfo.get("memberId"));
            logger.info("Don't register Data");
        }

        return check;
    }


}
