package com.secondhandauctions.handelr;

import com.secondhandauctions.vo.ChatMessageVo;
import lombok.extern.slf4j.Slf4j;
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

        roomChk = chatRoomList.containsKey(roomNo);

        if (roomChk == true) { // 방이 있을 때

        } else { // 방이 없을 때

        }


    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
    }

    private String getMemberId(WebSocketSession session) throws Exception {
        Map<String, Object> httpSession = session.getAttributes();
        String memberId = (String) httpSession.get("member");

        log.info("login info :: {}", memberId);
        return memberId;
    }
}
