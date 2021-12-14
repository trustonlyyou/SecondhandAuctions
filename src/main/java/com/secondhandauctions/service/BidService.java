package com.secondhandauctions.service;

import com.secondhandauctions.dao.BidDao;
import com.secondhandauctions.dao.MemberDao;
import com.secondhandauctions.dao.ProductDao;
import com.secondhandauctions.utils.Commons;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class BidService {

    @Autowired
    private BidDao bidDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private ProductService productService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private Commons commons;

    public int setBid(Map<String, Object> params) throws Exception {
        List<String> checkList = new ArrayList<>();

        int productId = 0;
        String bidMemberId = "";
        String bidPrice = "";

        boolean strCheck = false;
        int bidChk = 0;
        int updateChk = 0;
        int result = 0;

        if (params.isEmpty()) {
            log.error("params isEmpty is true");
            return result;
        }

        productId = (int) params.get("productId");
        bidMemberId = (String) params.get("bidMemberId");
        bidPrice = (String) params.get("bidPrice");

        checkList.add(bidMemberId);
        checkList.add(bidPrice);

        strCheck = commons.isEmpty(checkList);

        if (strCheck == true) {
            log.info("null check is true");
            return result;
        }

        synchronized (this) {
            bidChk = biding(params);

            log.info("bidChk :: '{}'", bidChk);

            if (bidChk != 1) {
                log.error("bidChk result :: '{}'", bidChk);
                return result;
            }

            updateChk = productService.updateBidPrice(params);

            if (updateChk != 1) {
                log.error("updateChk result :: '{}'", updateChk);
                return result;
            }

            result = 1;
        }

        return result;
    }

    @Transactional(
            rollbackFor = {Exception.class, NullPointerException.class }
    )
    public int biding(Map<String, Object> params) throws Exception {
        int chk = 0;

        log.info((String) params.get("bidMemberId"));

        if (params.isEmpty()) {
            return chk;
        }

        try {
            chk = bidDao.registerBid(params);
        } catch (Exception e) {
            log.error("Exception :: {}", commons.printStackLog(e));
            return 0;
        }

        return chk;
    }

    public int sendEmailToMember(String memberId, String pageUrl) throws Exception {
        String memberEmail = "";
        int check = 0;

        memberEmail = memberDao.getMemberEmail(memberId);

        if (StringUtils.isEmpty(memberEmail)) {
            return check;
        }

        check = emailService.sendEmailToBidMember(memberEmail, pageUrl);

        return check;
    }

    public String getTopBidMember(int productId) throws Exception {
        String memberId = "";

        if (productId == 0) {
            return "";
        }

        memberId = bidDao.topBidMember(productId);

        return memberId;
    }
}
