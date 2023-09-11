package com.websocket.chat.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.websocket.chat.model.ChatRoom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

// 채팅방을 생성, 조회하고 하나의 세션에 메시지를 발송하는 서비스
@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {

    private final ObjectMapper objectMapper;

    // 서버에 생성된 모든 채팅방의 정보를 모아둔 구조체. 빠른 구현을 위해 일단 외부 저장소가 아닌 HashMap에 저장하는 것으로 구현
    private Map<String, ChatRoom> chatRooms;

    @PostConstruct // Spring Framework에서 빈(bean) 초기화 시 호출
    private void init() { // 클래스 내부에 정의된 비공개(private) 메서드. 따라서 외부에서 직접 호출되지 않으며, Spring Framework가 빈 초기화 시 자동으로 호출됨
        // (순서가 있는) 새로운 빈 맵 객체 생성(채팅방 정보를 저장하기 위한 구조체)
        chatRooms = new LinkedHashMap<>();
    }

    // 채팅방 조회 - 채팅방 Map(chatRooms)에 담긴 정보를 조회
    public List<ChatRoom> findAllRoom() {
        return new ArrayList<>(chatRooms.values());
    }

    public ChatRoom findRoomById(String roomId) {
        return chatRooms.get(roomId);
    }

    // 채팅방 생성 - Random UUID로 구별ID를 가진 채탕방 객체를 생성하고 채팅방 Map에 추가
    public ChatRoom createRoom(String name) {
        String randomId = UUID.randomUUID().toString();

        ChatRoom chatRoom = ChatRoom.builder()
                .roomId(randomId)
                .name(name)
                .build();
        chatRooms.put(randomId, chatRoom);
        return chatRoom;
    }

    // 메시지 발송 - 지정한 Websocket 세션에 메시지를 발송
    public <T> void sendMessage(WebSocketSession session, T message) {
        try {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
}
