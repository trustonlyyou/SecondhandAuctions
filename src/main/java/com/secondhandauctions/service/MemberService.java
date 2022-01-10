package com.secondhandauctions.service;

import com.secondhandauctions.dao.MemberDao;
import com.secondhandauctions.vo.MemberVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class MemberService {

    @Autowired
    private MemberDao memberDao;

    public boolean idCheck(String memberId) throws Exception {
        int chk = 0;

        if (StringUtils.isEmpty(memberId)) {
            return false;
        } else {
            chk = memberDao.idCheck(memberId);

            if (chk == 0) {
                return true;
            } else {
                return false;
            }
        }
    }

    public void setMember(MemberVo memberVo) throws Exception {
        memberDao.join(memberVo);
    }

    public void setMemberIdFlag(String memberId) throws Exception {
        memberDao.memberIdFlag(memberId);
    }

    public boolean setMemberKako(Map<String, String> memberInfo) throws Exception {
        int chk = 0;

        if (memberInfo.isEmpty()) {
            log.error("memberInfo is Empty");
            return false;
        } else {
            chk = memberDao.joinByKakao(memberInfo);

            if (chk == 1) {
                return true;
            } else {
                return false;
            }
        }
    }

    public Map<String, Boolean> getLoginResult(Map<String, Object> loginInfo) throws Exception {
        Map<String, Object> info = new HashMap<>();
        Map<String, Boolean> result = new HashMap<>();
        Map<String, Object> isMember = new HashMap<>();

        int chk = 0;
        int isLock = 0;
        int isKakaoChk = 0;
        int isMemberChk = 0;

        if (loginInfo.isEmpty()) {
            log.error("loginInfo parma isEmpty");
            result.put("isMember", false);
            return result;
        } else {
            isMember = memberDao.isMember((String) loginInfo.get("memberId"));
            isMemberChk = Integer.parseInt(String.valueOf(isMember.get("isMember")));

            try {
                isKakaoChk = (int) isMember.get("isKakao");
            } catch (NullPointerException e) {
                isKakaoChk = 0;
            }

            if (isMemberChk == 0 || isKakaoChk == 1) {
                result.put("isMember", false);
                return result;
            }  else {
                result.put("isMember", true);
                info = memberDao.login(loginInfo);

                chk = Integer.parseInt(String.valueOf(info.get("login")));
                isLock = (int) info.get("isLock");

                log.info("login chk :: " + chk);
                log.info("login isLock :: " + isLock);


                if (isLock == 0) { // non lock
                    result.put("isLock", false);
                    if (chk == 1) { // success
                        result.put("chk", true);

                    } else { // fail
                        result.put("chk", false);
                    }
                    return result;
                } else { // lock
                    result.put("isLock" ,true);
                    result.put("chk", false);
                    return result;
                }
            }

        }
    }

    public boolean lockSolveFromPhone(Map<String, String> memberInfo) throws Exception {
        int chk = 0;

        if (memberInfo.isEmpty()) {
            log.info("info isEmpty");
            return false;
        } else {
            chk = memberDao.isLockMemberPhone(memberInfo);

            if (chk == 1) {
                String memberId = memberInfo.get("memberId");

                memberDao.resetFailCount(memberId); // 실패 카운트 초과
                memberDao.solveLock(memberId); // 락 풀기

                return true;
            } else {
                return false;
            }
        }
    }

    public boolean lockSolveFormEmail(Map<String, String> memberInfo) throws Exception {
        int chk = 0;

        if (memberInfo.isEmpty()) {
            log.info("info isEmpty");

            return false;
        } else {
            chk = memberDao.isLockMemberEmail(memberInfo);

            if (chk == 1) {
                String memberId = memberInfo.get("memberId");

                memberDao.resetFailCount(memberId);
                memberDao.solveLock(memberId);

                return true;
            } else {
                return false;
            }
        }
    }

    public void memberLock(String memberId) throws Exception {
        memberDao.loginLock(memberId);
    }

    public void failCountUp(String memberId) throws Exception {
        memberDao.loginFailCountUp(memberId);
    }

    public void resetFailCount(String memberId) throws Exception {
        memberDao.resetFailCount(memberId);
    }

    public int getLoginFileCount(String memberId) throws Exception {
        return memberDao.loginFailCount(memberId);
    }

    public String getMemberIdByEmail(String memberEmail) throws Exception {
        if (StringUtils.isEmpty(memberEmail)) {
            return null;
        } else {
            return memberDao.memberIdByEmail(memberEmail);
        }
    }

    public MemberVo getMemberInfo(String memberId) throws Exception {
        return memberDao.memberInfo(memberId);
    }

    public boolean isEmail(String memberEmail) throws Exception {
        int result = 0;

        result = memberDao.isMemberEmail(memberEmail);
        log.info("isEmail result :: '{}'", result);

        if (result == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isPhone(Map<String, Object> params) throws Exception {
        int chk = 0;

        if (params.isEmpty()) {
            return false;
        }

        chk = memberDao.isMemberPhone(params);

        if (chk == 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isKakaoMember(String memberId) throws Exception {
        int chk = 0;

        chk = memberDao.isKakaoMember(memberId);

        log.info("isKakaoCheck :: '{}'", chk);

        if (chk == 1) {
            return true;
        } else {
            return false;
        }
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
    public boolean setPassword(Map<String, Object> memberInfo) throws Exception {
        int check = 0;

        if (memberInfo.isEmpty()) {
            return false;
        } else {
            check = memberDao.modifyPassword(memberInfo);

            if (check == 1) {
                return true;
            } else {
                return false;
            }
        }


    }
}
