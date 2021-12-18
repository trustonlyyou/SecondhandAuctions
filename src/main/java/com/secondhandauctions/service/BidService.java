package com.secondhandauctions.service;

import com.secondhandauctions.dao.BidDao;
import com.secondhandauctions.dao.MemberDao;
import com.secondhandauctions.dao.ProductDao;
import com.secondhandauctions.utils.Commons;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    private ProductDao productDao;

    @Autowired
    private ProductService productService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private SmsService smsService;

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
    @Scheduled(cron = "0 0 3 * * *") // 매일 새벽 3시에 실행, return void & Don't have parameter
    @Transactional(
            isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED,
            rollbackFor = Exception.class)
    public void closeBidOfProductAndSuccessBid() throws Exception {
        /**
         * 1. 마감 (입찰 할 때 살아 있는 것만)
         * 2. 입찰하게 있으면 성공으로 넘기기
         *
         */

        List<Integer> targetProductIds = new ArrayList<>(); // 오늘 마감 할 게시물 id
        List<Integer> successBidProductId = new ArrayList<>(); // 오늘 마강 할 게시물 id 중에 입찰에 성공한

        // 마감
        targetProductIds = productDao.closeTargetProducts(); // 오늘 마감할 게시물들
        successBidProductId = productDao.successBidProductIds(); // 오늘 마감할 게시물들 중에 입찰 된 것들

        if (targetProductIds.isEmpty()) {
            log.info("There are no closing products today.");
            return;
        }

        if (successBidProductId.isEmpty()) { // 입찰 된게 없으니까, 모든 게시물 마감
            for (int productId : targetProductIds) {
                log.info("target productId :: '{}'", productId);
            }
            productDao.closeProducts(targetProductIds); // 게시물 마감하기
        }

        productDao.insertSuccessBidInfo(successBidProductId); // 입찰 성공
    }

    @Scheduled(cron = "0 0 7 * * *") // 매일 새벽 7시에 실행, return void & Don't have parameter
    @Transactional(
            isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED,
            rollbackFor = Exception.class)
    public void successBidderSendSms() throws Exception {
        List<Map<String, String>> sellersPhoneList = new ArrayList<>();
        List<Map<String, String>> biddersPhoneList = new ArrayList<>();

        String sellerText = "판매 하신 상품이 낙찰되셨습니다. 자세한 내용은 홈페이지의 마이페이지를 이용해 주세요.";
        String bidderText = "입찰 하신 상품이 낙찰되었습니다. 자세한 내용은 홈페이지의 마이페이지를 이용해 주세요.";

        sellersPhoneList = bidDao.successSellerPhone();
        biddersPhoneList = bidDao.successBiddersPhone();


        for (Map<String, String> info : sellersPhoneList) {
            String memberId = info.get("memberId");
            String memberPhone = info.get("memberPhone");

            // test
            log.info("memberId :: '{}'", memberId);
            log.info("memberPhone :: '{}'", memberPhone);

            smsService.sendSms(memberPhone, sellerText);
        }

        for (Map<String, String> info : biddersPhoneList) {
            String memberId = info.get("memberId");
            String memberPhone = info.get("memberPhone");

            // test
            log.info("memberId :: '{}'", memberId);
            log.info("memberPhone :: '{}'", memberPhone);

            smsService.sendSms(memberPhone, sellerText);

        }

    }
}
