package com.secondhandauctions;

import com.secondhandauctions.dao.MyPageDao;
import com.secondhandauctions.dao.ProductDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        locations = {"file:src/main/webapp/WEB-INF/config/database-context.xml"}
)
@WebAppConfiguration
public class BidingTest {

    @Autowired
    private ProductDao productDao;

    @Autowired
    private MyPageDao myPageDao;

    @Test
    public void test1() throws Exception {
        List<Integer> list = new ArrayList<>();

        list.add(1);
        list.add(2);

        productDao.insertSuccessBidInfo(list);
    }

    @Test
    public void test2() throws Exception {
//        List<Map<String, Object>> list = new ArrayList<>();
//        list = myPageDao.myBidSuccessListByBidder("minemusic");
//
//        for (Map<String, Object> map : list) {
//            System.out.println("productTitle :: " + map.get("productTitle"));
//        }
    }

}
