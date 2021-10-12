package com.secondhandauctions;

import com.secondhandauctions.dao.MemberDao;
import com.secondhandauctions.dao.ProductDao;
import com.secondhandauctions.vo.ImageVo;
import com.secondhandauctions.vo.TestVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        locations = {"file:src/main/webapp/WEB-INF/config/*.xml"}
)
@WebAppConfiguration
public class InsertTest {

    @Autowired
    private ProductDao productDao;

//    @Test
//    public void insertTest() throws Exception {
//        TestVo testVo = new TestVo();
//        testVo.setCategoryName("의류");
//
//        productDao.registerCategory(testVo);
//
//        System.out.println(testVo.getCategoryId());
//    }

    @Test
    public void insertListTest() throws Exception {
        List<TestVo> list = new ArrayList<>();

        list.add(new TestVo("오정환", 27));
        list.add(new TestVo("쌍퉁바가지", 26));

        System.out.println(list.size());

        productDao.setListTet(list);

    }

    @Test
    public void insertTest() throws Exception {
        List<ImageVo> list = new ArrayList<>();

        list.add(new ImageVo(1, "test1", 10));
        list.add(new ImageVo(1, "test2", 10));
        list.add(new ImageVo(1, "test3", 10));

        productDao.registerImg(list);

    }

    @Autowired
    private DataSource dataSource;

    @Test
    public void testConnectionTest() throws Exception {
        try {
            Connection connection = dataSource.getConnection();
            System.out.println(connection.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
