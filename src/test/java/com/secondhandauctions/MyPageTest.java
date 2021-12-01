package com.secondhandauctions;

import com.secondhandauctions.dao.MyPageDao;
import com.secondhandauctions.utils.Criteria;
import com.secondhandauctions.vo.ProductVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        locations = {"file:src/main/webapp/WEB-INF/config/*.xml"}
)
@WebAppConfiguration
public class MyPageTest {

    @Autowired
    private MyPageDao myPageDao;

    @Test
    public void test() throws Exception {
        Criteria criteria = new Criteria();
        String memberId = "zkem123456";

        Map<String, Object> params = new HashMap<>();

        params.put("memberId", memberId);
        params.put("criteria", criteria);

        List<ProductVo> result = new ArrayList<>();

        result = myPageDao.myShopList(params);

        System.out.println(result.toString());
    }

    @Test
    public void test1() throws Exception {
        List<String> list = new ArrayList<>();

        list = Collections.emptyList();

        System.out.println(list.isEmpty());
    }

    @Test
    public void test2() throws Exception {
        String memberId = null;

        if ((memberId != null) || ("".equals(memberId))) {
            System.out.println("fuck");
        }

        if (("".equals(memberId)) || (memberId == null)) {
            System.out.println("hello");
        } else {
            System.out.println("fuck");
        }
    }

}
