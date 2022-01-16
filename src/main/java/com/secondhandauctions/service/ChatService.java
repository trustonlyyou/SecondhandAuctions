package com.secondhandauctions.service;

import com.secondhandauctions.dao.ChatDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ChatService {

    @Autowired
    private ChatDao chatDao;

    public Map<String, Object> doAdd(Map<String, Object> param) {

        chatDao.add(param);

        Map<String, Object> rs = new HashMap<>();

        rs.put("resultCode", "S-1");
        rs.put("msg", "채팅방이 생성되었습니다.");
        rs.put("id", param.get("id"));

        return rs;
    }
}
