package com.secondhandauctions;

import com.secondhandauctions.dao.MemberDao;
import com.secondhandauctions.service.MemberService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
        locations = {"file:src/main/webapp/WEB-INF/config/*.xml"}
)
public class MemberTest {

    @Autowired
    private MemberService memberService;

    @Test
    public void testId() throws Exception {
        String memberId = "zkem123456";

        System.out.println(memberService.idCheck(memberId));
    }
}
