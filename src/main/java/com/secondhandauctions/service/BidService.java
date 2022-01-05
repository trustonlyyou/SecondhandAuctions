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
import java.util.LinkedList;
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

    /**
     * solation : 트랜잭션에서 일관성없는 데이터 허용 수준을 설정한다 / DEFAULT : 기본 격리 수준 : 기본이며, DB의 lsolation Level을 따른다.
     *
     * propagation : 트랜잭션 동작 도중 다른 트랜잭션을 호출할 때, 어떻게 할 것인지 지정하는 옵션이다,
     * Propagation.REQUIRED :  이미 진행중인 트랜잭션이 있다면 해당 트랜잭션 속성을 따르고, 진행중이 아니라면 새로운 트랜잭션을 생성한다.
     *
     * rollbackFor : 특정 예외 발생 시 rollback한다.
     *
     *
     * @param params
     * @return
     * @throws Exception
     */
    @Transactional(
            isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED,
            rollbackFor = {Exception.class, RuntimeException.class}, timeout = 10)
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

        List<Integer> notSuccessBidProductId = new LinkedList<>(); // 오늘 마감 할 게시물 id 중 낙찰에 실패한
        List<Integer> successBidProductId = new LinkedList<>(); // 오늘 마강 할 게시물 id 중에 낙찰에 성공한

        // 마감
        notSuccessBidProductId = productDao.notSuccessBidProductIds(); // 오늘 마감할 게시물중 낙철 실패
        successBidProductId = productDao.successBidProductIds(); // 오늘 마감할 게시물들 중에 낙찰 된 것들

        if (!notSuccessBidProductId.isEmpty()) {
            for (int targetId : notSuccessBidProductId) {
                log.info("notSuccessBidProductId :: '{}'", targetId);
            }

            productDao.closeProducts(notSuccessBidProductId);
        }

        if (!successBidProductId.isEmpty()) {
            for (int targetId : successBidProductId) {
                log.info("successBidProductId :: '{}'", successBidProductId);
            }

            productDao.insertSuccessBidInfo(successBidProductId);
            productDao.closeProducts(successBidProductId);
        }
    }

    @Scheduled(cron = "0 0 7 * * *") // 매일 새벽 7시에 실행, return void & Don't have parameter
    @Transactional(
            isolation = Isolation.DEFAULT, propagation = Propagation.REQUIRED,
            rollbackFor = Exception.class)
    public void successBidderSendSms() throws Exception {
        List<Map<String, String>> sellersPhoneList = new ArrayList<>();
        List<Map<String, String>> biddersPhoneList = new ArrayList<>();

        String sellerText = "판매 하신 상품이 낙찰되셨습니다. 판매 승인을 해주셔야 판매가 진행이 됩니다. 자세한 내용은 홈페이지의 마이페이지를 이용해 주세요.";
        String bidderText = "입찰 하신 상품이 낙찰되었습니다. 구매 승인을 해주셔야 구매가 진행이 됩니다. 자세한 내용은 홈페이지의 마이페이지를 이용해 주세요.";

        sellersPhoneList = bidDao.successSellerPhone();
        biddersPhoneList = bidDao.successBiddersPhone();


        for (Map<String, String> info : sellersPhoneList) {
            String memberId = info.get("memberId");
            String memberPhone = info.get("memberPhone");

            smsService.sendSms(memberPhone, sellerText);
        }

        for (Map<String, String> info : biddersPhoneList) {
            String memberId = info.get("memberId");
            String memberPhone = info.get("memberPhone");

            smsService.sendSms(memberPhone, bidderText);
        }
    }
}
