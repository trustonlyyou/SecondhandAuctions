package com.secondhandauctions.service;

import com.secondhandauctions.dao.MyPageDao;
import com.secondhandauctions.utils.Criteria;
import com.secondhandauctions.vo.ProductVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class MyPageService {

    @Autowired
    private MyPageDao myPageDao;

    public int getMyShopListCount(String memberId) throws Exception {
        int count = 0;

        if (("".equals(memberId)) || (memberId == null)) {
            return count;
        }

        count = myPageDao.count(memberId);

        return count;
    }

    public List<ProductVo> getMyShopList(Map<String, Object> params) throws Exception {
        List<ProductVo> result = new ArrayList<>();
        String memberId = "";

        memberId = (String) params.get("memberId");

        if (("".equals(memberId)) || (memberId == null)) {
            result = Collections.emptyList();
            return result;
        }

        result = myPageDao.myShopList(params);

        return result;
    }
}
