package com.secondhandauctions;

import com.secondhandauctions.dao.MyPageDao;
import com.secondhandauctions.dao.ShopDao;
import com.secondhandauctions.utils.Criteria;
import com.secondhandauctions.utils.PageDTO;
import com.secondhandauctions.vo.ShopVo;
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
        locations = {"file:src/main/webapp/WEB-INF/config/*.xml"}
)
@WebAppConfiguration
public class ShopTest {

    @Autowired
    private ShopDao shopDao;

    @Autowired
    private MyPageDao myPageDao;

    @Test
    public void listTest() throws Exception {
        Criteria criteria = new Criteria();
        PageDTO pageDTO = new PageDTO();
        int count = 0;

        count = shopDao.countProduct();

        pageDTO.setCriteria(criteria);
        pageDTO.setTotalCount(count);

        List<ShopVo> list = new ArrayList<>();

        list = shopDao.getList(criteria);

        for (ShopVo shopVo : list) {
            System.out.println(shopVo.toString());
        }

        System.out.println(pageDTO.toString());
    }

    @Test
    public void deleteTest() throws Exception {
        Map<String, Object> params = new HashMap<>();

        int productId = 2;
        String memberId = "zkem123456";

        int result = 0;

        params.put("productId", productId);
        params.put("memberId", memberId);

        result = myPageDao.deleteProduct(params);

        System.out.println(result);

    }
}
