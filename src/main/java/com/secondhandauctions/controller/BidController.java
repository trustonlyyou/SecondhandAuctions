package com.secondhandauctions.controller;

import com.secondhandauctions.service.BidService;
import com.secondhandauctions.utils.Commons;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@Slf4j
public class BidController {

    @Autowired
    private BidService bidService;

    @Autowired
    private Commons commons;

    @PostMapping(value = "/bid")
    @ResponseBody
    public Map<String, Integer> bidProduct(String bidMemberId, String bidPrice, int productId, String pageUrl) throws Exception {
        Map<String, Object> info = new HashMap<>();
        Map<String, Integer> result = new HashMap<>();

        int resultChk = 0;
        int emailChk = 0;

        info.put("bidMemberId", bidMemberId);
        info.put("bidPrice", bidPrice);
        info.put("productId", productId);
        info.put("pageUrl", pageUrl);

        commons.printLogByMap(info);

        resultChk = bidService.setBid(info);
        log.info("bidProduct Result :: '{}'", resultChk);
        result.put("check", resultChk);

        emailChk = bidService.sendEmailToMember(bidMemberId, pageUrl);
        log.info("SendEmail to bid member result :: '{}'", emailChk);

        return result;
    }

}
