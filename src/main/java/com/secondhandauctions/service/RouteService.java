package com.secondhandauctions.service;

import com.secondhandauctions.dao.RouteDao;
import com.secondhandauctions.utils.Criteria;
import com.secondhandauctions.vo.ShopVo;
import com.sun.corba.se.impl.transport.CorbaResponseWaitingRoomImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
public class RouteService {

    @Autowired
    private RouteDao routeDao;

    public String getClientIp(HttpServletRequest request) {
        String ip = "";

        try {
            request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            ip = request.getHeader("X-FORWARDED-FOR");

            if (ip == null) {
                ip = request.getRemoteAddr();
            }

        } catch (Exception e) {
            log.error("error :: " + e);
        }
        return ip;
    }

    public int getTotalCount() throws Exception {
        return routeDao.countProduct();
    }

    public List<ShopVo> getNewProductList(Criteria criteria) throws Exception {
        List<ShopVo> itemList = new ArrayList<>();

        itemList = routeDao.newProductList(criteria);

        if (itemList.isEmpty()) {
            itemList = Collections.emptyList();
        }

        return itemList;
    }

    public List<ShopVo> getExpireTimeProductList(Criteria criteria) throws Exception {
        List<ShopVo> itemList = new ArrayList<>();

        itemList = routeDao.expireTimeProductList(criteria);

        if (itemList.isEmpty()) {
            itemList = Collections.emptyList();
        }

        return itemList;
    }

}
