package com.secondhandauctions.service;

import com.secondhandauctions.dao.ChatDao;
import com.secondhandauctions.vo.ChatMessageVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class ChatService {

    @Autowired
    private ChatDao chatDao;

    public Map<String, String> getChatMembers(int roomNo) throws Exception {
        if (roomNo == 0) {
            return Collections.emptyMap();
        } else {
            return chatDao.chattingMembers(roomNo);
        }
    }

    public List<ChatMessageVo> getChatList(int roomNo) throws Exception {
        if (roomNo == 0) {
            return Collections.emptyList();
        } else {
            return chatDao.chattingList(roomNo);
        }
    }
}
