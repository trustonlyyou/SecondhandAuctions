package com.secondhandauctions.service;

import com.secondhandauctions.dao.AdminDao;
import com.secondhandauctions.utils.Commons;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class AdminService {

    @Autowired
    private AdminDao adminDao;

    @Autowired
    private Commons commons;

    public Map<String, List<Map<String, Object>>> getMemberPayAndChargePointList(String keyword) throws Exception {
        Map<String, List<Map<String, Object>>> result = new HashMap<>();
        List<Map<String, Object>> chargeList = new ArrayList<>();
        List<Map<String, Object>> payList= new ArrayList<>();

        if (StringUtils.isEmpty(keyword)) {
            log.error("keyword isEmpty");
            return Collections.emptyMap();
        } else {
            try {
                chargeList = adminDao.memberChargeList(keyword);
                payList = adminDao.payPointList(keyword);

                log.info(chargeList.toString());
                log.info(payList.toString());
            } catch (Exception e) {
                commons.printStackLog(e);
                return Collections.emptyMap();
            }

            result.put("chargeList", chargeList);
            result.put("payList", payList);

            return result;
        }
    }

    public Map<String, Object> chkPointDates(String memberId) throws Exception {
        if (StringUtils.isEmpty(memberId)) {
            return Collections.emptyMap();
        } else {
            return adminDao.dateChk(memberId);
        }
    }
}
