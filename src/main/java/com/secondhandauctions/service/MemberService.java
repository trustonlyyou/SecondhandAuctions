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
    private EmailService emailService;

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

    public int isEmail(String memberEmail) throws Exception {
        int result = 0;

        result = memberDao.isMemberEmail(memberEmail);

        return result;
    }

    public int isPhone(Map<String, Object> params) throws Exception {
        int result = 0;

        result = memberDao.isMemberPhone(params);

        return result;
    }

    // 이메일로 아이디 찾기
    public Map<String, Object> getMemberIdFromEmail(Map<String, Object> info) throws Exception {
        Map<String, Object> result = new HashMap<>();

        String memberId = "";

        int check = 0;

        memberId = memberDao.searchIdEmail(info);

        if (("".equals(memberId)) || (memberId == null)) {
            result.put("check", check);

            return result;
        }

        check = 1;

        result.put("check", check);
        result.put("memberId", memberId);

        return result;
    }

    // 핸드폰으로 아이디 찾기
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

    // 핸드폰으로 비밀번호 찾기
    public int isSearchPwdFromPhone(Map<String, Object> memberInfo) throws Exception {
        int check = 0;

        check = memberDao.checkSearchPwdPhone(memberInfo);

        return check;
    }

    // 이메일로 비밀번호 찾기
    public int isSearchPwdFromEmail(Map<String, Object> memberInfo) throws Exception {
        int check = 0;

        check = memberDao.checkSearchPwdEmail(memberInfo);

        return check;
    }

    public void setPassword(Map<String, Object> memberInfo) throws Exception {
        memberDao.modifyPassword(memberInfo);
    }

}
