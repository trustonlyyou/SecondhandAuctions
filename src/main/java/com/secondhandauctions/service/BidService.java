package com.secondhandauctions.service;

import com.secondhandauctions.dao.BidDao;
import com.secondhandauctions.dao.MemberDao;
import com.secondhandauctions.dao.ProductDao;
import com.secondhandauctions.utils.Commons;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
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

            result = 1;
        }

        return result;
    }

    @Transactional(
            isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED,
            rollbackFor = Exception.class, timeout = 10)
    public int biding(Map<String, Object> params) throws Exception {
        int insertChk = 0;
        int updateChk = 0;
        int resultChk = 0;

        log.info((String) params.get("bidMemberId"));

        if (params.isEmpty()) {
            return resultChk;
        }

        try {
            insertChk = bidDao.registerBid(params);
            updateChk = productService.updateBidPrice(params);

            if (insertChk == 0 || updateChk == 0) {
                log.info("insert or update is result '{}'", resultChk);
                return resultChk;
            }

            resultChk = 1;
        } catch (Exception e) {
            log.error("Exception :: '{}'", commons.printStackLog(e));
            return 0;
        }

        return resultChk;
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

    /**
     *
     * cron 은 CronTab 에서의 설정과 같이 cron="0/10 * * * * ?" 과 같은 설정이 가능하고
     * fixedDelay 은 이전에 실행된 Task 의 종료시간으로 부터 정의된 시간만큼 지난 후 Task 를 실행한다.(밀리세컨드 단위)
     * fixedRate 은 이전에 실행된 Task 의 시작시간으로 부터 정의된 시간만큼 지난 후 Task 를 실행한다.(밀리세컨드 단위)
     * @throws Exception
     */
    @Scheduled(cron = "0 0 3 * * *") // 매일 새벽 3시에 실행
    public void successfulBid() throws Exception {
        /**
         * 1. 마감 (입찰 할 때 살아 있는 것만)
         * 2. 입찰하게 있으면 성공으로 넘기기
         *
         */
    }
}
