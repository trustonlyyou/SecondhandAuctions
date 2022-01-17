package com.secondhandauctions.dao;

import com.secondhandauctions.vo.ChatMessageVo;

import java.util.List;
import java.util.Map;

public interface ChatDao {

    public Map<String, String> getSellerBidder(int successBidNo) throws Exception;

    public void addChatRoom(Map<String, Object> params) throws Exception;

    public void createChatRoom(List<Integer> list) throws Exception;

    public Map<String, String> chattingMembers(int roomNo) throws Exception;

    public List<ChatMessageVo> chattingList(int roomNo) throws Exception;

    public void insertMessage(ChatMessageVo chatMessageVo) throws Exception;

}
