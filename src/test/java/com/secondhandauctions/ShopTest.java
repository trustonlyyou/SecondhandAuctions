package com.secondhandauctions;

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
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        locations = {"file:src/main/webapp/WEB-INF/config/*.xml"}
)
@WebAppConfiguration
public class ShopTest {

    @Autowired
    private ShopDao shopDao;

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
}
