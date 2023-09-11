package com.websocket.chat.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.websocket.chat.model.ChatMessageInfo;
import com.websocket.chat.model.ChatRoom;
import com.websocket.chat.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebSocketChatHandler extends TextWebSocketHandler {
    private final ObjectMapper objectMapper;
    private final ChatService chatService;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 수신된 텍스트 메시지의 내용을 가져와 payload 변수에 저장. 메시지 객체에서 payload란 일반적으로 메시지의 본문 내용을 나타냄
        String payload = message.getPayload();
        log.info("payload {}", payload);
//        TextMessage textMessage = new TextMessage("Welcome chatting server~^^");
//        // 생성한 환영 메시지를 현재 세션을 통해 클라이언트에게 전송
//        session.sendMessage(textMessage);

        // 웹소켓 클라이언트로부터 채팅 메시지를 전달 받아 채팅 메시지 객체로 변환
        ChatMessageInfo chatMessageInfo = objectMapper.readValue(payload, ChatMessageInfo.class);
        // 전달 받은 메시지에 담긴 채팅방 Id로 발송 대상 채팅방 정보를 조회
        ChatRoom room = chatService.findRoomById(chatMessageInfo.getRoomId());
        // 해당 채팅방에 입장해 있는 모든 클라이언트들(Websocket session)에게 타입에 따른 메시지 발송
        room.handleActions(session, chatMessageInfo, chatService);
    }
}
