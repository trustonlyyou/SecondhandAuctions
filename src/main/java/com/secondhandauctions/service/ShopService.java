package com.secondhandauctions.service;

import com.secondhandauctions.dao.ShopDao;
import com.secondhandauctions.utils.Criteria;
import com.secondhandauctions.vo.ShopVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ShopService {

    @Autowired
    private ShopDao shopDao;

    public List<ShopVo> getList(Criteria criteria) throws Exception {
        List<ShopVo> list = new ArrayList<>();

        list = shopDao.getListWithPaging(criteria);

        return list;
    }
}
