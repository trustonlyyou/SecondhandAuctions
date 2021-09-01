package com.secondhandauctions;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        locations = {"file:src/main/webapp/WEB-INF/config/*.xml"}
)
@WebAppConfiguration
public class RandomTest {

    @Test
    public void randomTest() throws Exception {
        Random random = new Random();
        String numStr = String.valueOf(random.nextInt(888888) + 111111);

        System.out.println(numStr);
    }
}
