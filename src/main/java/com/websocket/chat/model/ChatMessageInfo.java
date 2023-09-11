package com.websocket.chat.model;

import lombok.Getter;
import lombok.Setter;

// 채팅 메시지 구현
@Getter
@Setter
public class ChatMessageInfo {
    // 메시지 타입: 입장, 채팅
    public enum MessageType {
        ENTER, TALK
    }
    private MessageType type; // 메시지 타입
    private String roomId; // 방 번호
    private String sender; // 메시지 보낸사람
    private String message; // 메시지
}
