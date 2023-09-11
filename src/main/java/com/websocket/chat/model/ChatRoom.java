package com.websocket.chat.model;

import com.websocket.chat.service.ChatService;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.socket.WebSocketSession;

import java.util.HashSet;
import java.util.Set;

// 채팅방 구현
@Getter
public class ChatRoom {
    private String roomId;
    private String name;
    // 채팅방은 입장한 클라이언트들의 정보를 가지고 있어야 하므로 WebsocketSession 정보 리스트를 멤버 변수로 갖는다.
    private Set<WebSocketSession> sessions = new HashSet<>();

    @Builder
    public ChatRoom(String roomId, String name) {
        this.roomId = roomId;
        this.name = name;
    }

    // 채팅방에는 입장, 대화하기의 기능이 있으므로 handleActions를 통해 분기 처리
    public void handleActions(WebSocketSession session, ChatMessageInfo chatMessageInfo, ChatService chatService) {
        // 입장 시에는 클라이언트의 sesseion 정보를 채팅방의 session리스트에 추가해 놓았다가 채팅방에 메시지가 도착할 경우 채팅방의 모든 session에 메시지 발송
        if (chatMessageInfo.getType().equals(ChatMessageInfo.MessageType.ENTER)) {
            sessions.add(session);
            chatMessageInfo.setMessage(chatMessageInfo.getSender() + "님이 입장하셨습니다.");
        }
        sendMessageToAllSessions(chatMessageInfo, chatService);
    }

    // 제네릭 메서드를 사용하여 채팅 메시지를 여러 WebSocket 세션에 전송
    public <T> void sendMessageToAllSessions(T message, ChatService chatService) {
        sessions.parallelStream().forEach(session -> chatService.sendMessage(session, message));
    }

    // parallelStream(): 세션들을 병렬로 처리하기 위해 스트림을 생성. 병렬 처리를 활용하여 여러 세션에 동시에 메시지 전송
    // forEach(session -> chatService.sendMessage(session, message)): 각 세션에 대해서 chatService.sendMessage(session, message) 메서드를 호출하여 메시지를 전송
    // chatService.sendMessage(session, message)는 특정 세션에 메시지를 보내는 작업을 수행함
}
