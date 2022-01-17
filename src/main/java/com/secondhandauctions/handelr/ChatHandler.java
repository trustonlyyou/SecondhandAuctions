package com.secondhandauctions.handelr;

import com.secondhandauctions.dao.ChatDao;
import com.secondhandauctions.utils.Commons;
import com.secondhandauctions.vo.ChatMessageVo;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
public class ChatHandler extends TextWebSocketHandler {

    @Autowired
    private Commons commons;

    @Autowired
    private ChatDao chatDao;

    //           roomNo
    private Map<Integer, List<Map<String, WebSocketSession>>> chatRoomList = new ConcurrentHashMap<>();

    //          roomNo             채팅
    private Map<Integer, Queue<ChatMessageVo>> chatMessage = new ConcurrentHashMap<>();


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Map<String, WebSocketSession> memberSession = new HashMap<>();
        List<Map<String, WebSocketSession>> members = new LinkedList<>();

        int roomNo = 0;
        String memberId = "";
        boolean roomChk = false;
        boolean memberChk = false;

        roomNo = Integer.parseInt(session.getUri().toString().split("/")[4]);
        memberId = getMemberId(session);

        log.info("roomNo :: '{}'", roomNo);
        log.info("memberId :: '{}'", memberId);

        synchronized (this) {
            roomChk = chatRoomList.containsKey(roomNo);

            if (roomChk == true) { // 방이 있을 때
                members = chatRoomList.get(roomNo);

                for (int i = 0; i < members.size(); i++) {
                    memberChk = members.get(i).containsKey(memberId);
                }

                log.info("memberChk :: '{}'", memberChk);

                if (memberChk == false) {
                    memberSession.put(memberId, session);
                    members.add(memberSession);
                    chatRoomList.put(roomNo, members);
                }

            } else { // 방이 없을 때
                memberSession.put(memberId, session);
                members.add(memberSession);

                chatRoomList.put(roomNo, members);

                Queue<ChatMessageVo> empty = new LinkedList<>();
                chatMessage.put(roomNo, empty);
            }
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String msg = message.getPayload();
        JSONObject msgObject = jsonParser(msg);

        if (msgObject.isEmpty()) {
            log.info("msgObject isEmpty");
            return;
        } else {
            int roomNo = Integer.parseInt((String) msgObject.get("roomNo"));
            String bidder = (String) msgObject.get("bidder");
            String seller = (String) msgObject.get("seller");

            synchronized (this) {
                List<Map<String, WebSocketSession>> members = chatRoomList.get(roomNo);

                for (int i = 0; i < members.size(); i++) {
                    Map<String, WebSocketSession> target = members.get(i);

                    if (target.containsKey(bidder)) {
                        WebSocketSession webSocketSession = target.get(bidder);
                        webSocketSession.sendMessage(new TextMessage(msgObject.toJSONString()));
                    } else if (target.containsKey(seller)) {
                        WebSocketSession webSocketSession = target.get(seller);
                        webSocketSession.sendMessage(new TextMessage(msgObject.toJSONString()));
                    }
                }

                ChatMessageVo messageVo = new ChatMessageVo();
                messageVo.setRoomNo(roomNo);
                messageVo.setMemberId((String) msgObject.get("memberId"));
                messageVo.setMsg((String) msgObject.get("msg"));

                chatMessage.get(roomNo).add(messageVo);

                log.info("send message :: " + messageVo.toString());
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        String memberId = "";
        int roomNo = 0;

        roomNo = Integer.parseInt(session.getUri().toString().split("/")[4]);
        memberId = getMemberId(session);

        log.info("roomNo :: '{}'", roomNo);
        log.info("memberId :: '{}'", memberId);

        List<Map<String, WebSocketSession>> members = chatRoomList.get(roomNo);

        for (int i = 0; i < members.size(); i++) {
            members.get(i).remove(memberId);
        }

        Queue<ChatMessageVo> messages = chatMessage.get(roomNo);

        if (!messages.isEmpty()) {
            for (ChatMessageVo message : messages) {

                log.info("chat Message :: " + message.toString());
                // insert Dao
                chatDao.insertMessage(message);
            }
            chatMessage.get(roomNo).clear();
        }

        log.info("close connection!!");
    }

    private String getMemberId(WebSocketSession session) throws Exception {
        Map<String, Object> httpSession = session.getAttributes();
        String memberId = (String) httpSession.get("member");

        log.info("login info :: {}", memberId);
        return memberId;
    }

    private JSONObject jsonParser(String msg) throws Exception {
        JSONParser parser = new JSONParser();
        JSONObject obj = null;

        try {
            obj = (JSONObject) parser.parse(msg);
        } catch (Exception e) {
            commons.printStackLog(e);
            obj = new JSONObject();
            return obj;
        }
        return obj;
    }
}
