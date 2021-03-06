package com.secondhandauctions;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.sql.DataSource;
import java.sql.Connection;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        locations = {"file:src/main/webapp/WEB-INF/config/database-context.xml"}
)
@WebAppConfiguration
public class DataSourceTest {

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
