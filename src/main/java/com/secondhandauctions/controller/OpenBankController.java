package com.secondhandauctions.controller;

import com.secondhandauctions.dao.PointDao;
import com.secondhandauctions.service.MyPageService;
import com.secondhandauctions.service.OpenBankService;
import com.secondhandauctions.service.PointService;
import com.secondhandauctions.utils.Commons;
import com.secondhandauctions.utils.OpenBankCommonsUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@Slf4j
public class OpenBankController {
    @Autowired
    private OpenBankService openBankService;

    @Autowired
    private Commons commons;

    @Autowired
    private MyPageService myPageService;

    @Autowired
    private PointService pointService;

    @Autowired
    private PointDao pointDao;

    @Autowired
    private OpenBankCommonsUtils openBankCommonsUtils;

    @GetMapping("/oauth/callback")
    public String oauthCallback(Model model, String code, HttpServletRequest request) throws Exception {
        Map tokenResult = new HashMap();
        Map<String, Object> info = new HashMap<>();

        try {
            tokenResult = openBankService.callbackOpenBankOauth(code);

            String memberId = commons.getMemberSession(request);
            String access_token = (String) tokenResult.get("access_token");

            log.info("access_token :: '{}'", access_token);
            log.info("memberId :: '{}'", memberId);

            if (StringUtils.isEmpty(access_token) || StringUtils.isEmpty(memberId)) {
                model.addAttribute("rsp_code", tokenResult.get("rsp_code"));
                model.addAttribute("msg", tokenResult.get("rsp_message"));

                return "myPage/openBankError";
            } else {
                info.put("memberId", memberId);
                info.put("token", access_token);

                openBankService.setToken(info);

                return "myPage/registerBank";
            }
        } catch (Exception e) {
            log.error(commons.printStackLog(e));
            model.addAttribute("rsp_code", tokenResult.get("rsp_code"));
            model.addAttribute("msg", tokenResult.get("rsp_message"));

            return "myPage/openBankError";
        }
    }

    @GetMapping(value = "/real/name/form")
    public String form() throws Exception {
        return "myPage/registerBank";
    }

    @PostMapping(value = "/real/name/callback")
    @ResponseBody
    public Map<String, Object> realName(String memberName, String memberBirth, String account_num,
                         String bank_code, HttpServletRequest request) throws Exception {

        Map<String, Object> params = new HashMap<>();
        Map<String, Object> bankInfo = new HashMap<>();
        Map openBankResult = new HashMap();
        Map<String, Object> result = new HashMap<>();

        String bank_tran_id = openBankCommonsUtils.getBankTranId(); // 이용기관코드 + "U"+임의 난수 9자리
        String tran_dtime = openBankCommonsUtils.getTranDtime();

        params.put("bank_tran_id", bank_tran_id);
        params.put("bank_code_std", bank_code);
        params.put("account_num", account_num);
        params.put("account_holder_info", memberBirth);
        params.put("tran_dtime", tran_dtime);

        openBankResult = openBankService.realName(request, params);
        String responseCode = (String) openBankResult.get("rsp_code");

        if ("A0000".equals(responseCode)) {
            bankInfo.put("bankName", openBankResult.get("bank_name"));
            bankInfo.put("accountNum", openBankResult.get("account_num"));
            bankInfo.put("bankCode", openBankResult.get("bank_code_std"));
            bankInfo.put("accountHolder", openBankResult.get("account_holder_name"));
            bankInfo.put("memberId", commons.getMemberSession(request));

            openBankService.setBankInfo(bankInfo);

            result.put("result", true);

            return result;
        } else {
            result.put("result", false);
            result.put("code", responseCode);
            result.put("message", openBankResult.get("rsp_message"));

            return result;
        }
    }


    @GetMapping(value = "/point/exchange/form")
    public String pointExchangeForm(HttpServletRequest request, Model model) throws Exception {
        String memberId = commons.getMemberSession(request);
        int myPoint = myPageService.getMyPoint(memberId);

        model.addAttribute("myPoint", myPoint);

        return "myPage/exchange";
    }

    @PostMapping(value = "/point/exchange")
    @ResponseBody
    public Map<String, Object> pointExchange(HttpServletRequest request, String reqPoint) throws Exception {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> downPointInfo = new HashMap<>();
        Map<String, Object> updateTimeInfo = new HashMap<>();
        Map exchangeResult = new HashMap();
        Map<String, Object> exchangeInfo = new HashMap<>();
        Map res = new HashMap();

        log.info("reqPoint :: " + reqPoint);

        String memberId = commons.getMemberSession(request);
        Map<String, Object> bankInfo = openBankService.getBankInfo(memberId);

        if (bankInfo.isEmpty()) {
            log.info("bankInfo isEmpty");
            result.put("result", false);

            return result;
        } else {
            exchangeResult = openBankService.transferAccountOfPointExchange(bankInfo, memberId, reqPoint);

            log.info(exchangeResult.toString());

            if ("A0000".equals(exchangeResult.get("rsp_code"))) {
                List list = (List) exchangeResult.get("res_list");
                res = (Map) list.get(0);

                exchangeInfo.put("exchangeId", res.get("bank_tran_id"));
                exchangeInfo.put("memberId", memberId);
                exchangeInfo.put("bankName", res.get("bank_name"));
                exchangeInfo.put("bankCode", res.get("bank_code_std"));
                exchangeInfo.put("accountHolder", res.get("account_holder_name"));
                exchangeInfo.put("accountName", res.get("account_num"));
                exchangeInfo.put("amount", res.get("tran_amt"));

                openBankService.setExchangeInfo(exchangeInfo);

                downPointInfo.put("memberId", memberId);
                downPointInfo.put("disChargePoint", res.get("tran_amt"));

                pointService.pointDown(downPointInfo);
                updateTimeInfo.put("memberId", commons.getMemberSession(request));
                updateTimeInfo.put("updateTime", commons.getNowTime());

                pointDao.pointUpdateMemberTime(updateTimeInfo);


                result.put("result", true);

                return result;
            } else {
                result.put("result", "error");
                result.put("code", exchangeResult.get("rsp_code"));
                result.put("msg", exchangeResult.get("rsp_message"));

                return result;
            }
        }
    }
}
