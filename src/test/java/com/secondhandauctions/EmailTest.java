package com.secondhandauctions;

import com.secondhandauctions.dao.MemberDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        locations = {"file:src/main/webapp/WEB-INF/config/*.xml"}
)
@WebAppConfiguration
public class EmailTest {

    @Autowired
    private MemberDao memberDao;

    @Test
    public void isEmailTest() throws Exception {
        int result = 0;
        String memberEmail = "zkem123456@naver.com";

        result = memberDao.isMemberEmail(memberEmail);

        System.out.println(result);
    }
}
