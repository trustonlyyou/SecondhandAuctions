package com.secondhandauctions.controller;

import com.secondhandauctions.service.OpenBankService;
import com.secondhandauctions.utils.Commons;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Controller
@Slf4j
public class OpenBankController {
    @Autowired
    private OpenBankService openBankService;

    @Autowired
    private Commons commons;

    @GetMapping("/oauth/callback")
    public String test(Model model, String code, HttpServletRequest request) throws Exception {
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

                return "test/realName";
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

            // TODO: 2022/01/21 error page
            return "test/realName";
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

        log.info(memberName);
        log.info(memberBirth);
        log.info(account_num);
        log.info(bank_code);

        Map<String, Object> params = new HashMap<>();
        Map<String, Object> bankInfo = new HashMap<>();
        Map openBankResult = new HashMap();
        Map<String, Object> result = new HashMap<>();


        Random random = new Random();
        int num = random.nextInt(888888888) + 111111111;
        String bank_tran_id = "M202200113" + "U" + num; // 이용기관코드 + "U"+임의 난수 9자리
        String tran_dtime = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());

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

//    @GetMapping(value = "/real/name")
//    public String realName(HttpServletRequest request, Model model) throws Exception {
//        Map<String, Object> params = new HashMap<>();
//        Map result = new HashMap();
//
//        Random random = new Random();
//        int num = random.nextInt(888888888) + 111111111;
//
//        String bank_code_std = request.getParameter("bank_code");
//        String account_num = request.getParameter("account_num");
//        String account_holder_info = request.getParameter("memberBirth");
//        String tran_dtime = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
//        String bank_tran_id = "M202200113" + "U" + num; // 이용기관코드 + "U"+임의 난수 9자리
//
//        params.put("bank_tran_id", bank_tran_id);
//        params.put("bank_code_std", bank_code_std);
//        params.put("account_num", account_num);
//        params.put("account_holder_info", account_holder_info);
//        params.put("tran_dtime", tran_dtime);
//
//        log.info("===============controller param info ===============");
//        log.info(params.toString());
//
//        result = openBankService.realName(request, params);
//
//        model.addAttribute("result", result);
//
//        return "test/result";
//    }
}
