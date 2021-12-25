package com.secondhandauctions.service;

import com.secondhandauctions.dao.MemberDao;
import com.secondhandauctions.utils.Commons;
import com.secondhandauctions.vo.MemberVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class MemberService {

    @Autowired
    private MemberDao memberDao;

    public int idCheck(String memberId) throws Exception {
        return memberDao.idCheck(memberId);
    }

    public void setMember(MemberVo memberVo) throws Exception {
        memberDao.join(memberVo);
    }

    public int getLoginResult(Map<String, Object> loginInfo) throws Exception {
        String memberId = "";
        String memberPassword = "";

        if (loginInfo.isEmpty()) {
            log.error("loginInfo parma isEmpty");
            return 0;
        }

        memberId = (String) loginInfo.get("memberId");
        memberPassword = (String) loginInfo.get("memberPassword");

        if (StringUtils.isEmpty(memberId) || StringUtils.isEmpty(memberPassword)) {
            log.error("memberId isEmpty result :: '{}'", StringUtils.isEmpty(memberId));
            log.error("memberPassword isEmpty result :: '{}'", StringUtils.isEmpty(memberPassword));
            return 0;
        }

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

        if (StringUtils.isEmpty(memberId)) {
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

        if (memberInfo.isEmpty()) {
            return check;
        }

        check = memberDao.checkSearchPwdPhone(memberInfo);

        return check;
    }

    // 이메일로 비밀번호 찾기
    public int isSearchPwdFromEmail(Map<String, Object> memberInfo) throws Exception {
        int check = 0;

        if (memberInfo.isEmpty()) {
            return check;
        }

        check = memberDao.checkSearchPwdEmail(memberInfo);

        return check;
    }

    // 비밀 번호 수정
    public int setPassword(Map<String, Object> memberInfo) throws Exception {
        int check = 0;

        if (memberInfo.isEmpty()) {
            return check;
        }

        check = memberDao.modifyPassword(memberInfo);

        return check;
    }
}
