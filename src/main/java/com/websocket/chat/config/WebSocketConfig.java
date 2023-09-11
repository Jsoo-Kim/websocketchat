package com.websocket.chat.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
@RequiredArgsConstructor
@Configuration // 이 클래스가 Spring의 구성 클래스임을 나타냄. 애플리케이션 시작 시 이 클래스를 찾아 구성 정보를 로드함
@EnableWebSocket
@Slf4j
public class WebSocketConfig implements WebSocketConfigurer {
    private final WebSocketHandler webSocketHandler;

    // WebSocket 핸들러를 등록하는 데 사용
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        System.out.println("Configuration aaaaaaa");
        // /ws/chat 엔드포인트로의 WebSocket 연결 처리를 위해 webSocketHandler를 등록, setAllowedOrigins("*")를 통해 모든 원본(웹사이트)에서의 연결을 허용
        registry.addHandler(webSocketHandler, "/ws/chat").setAllowedOrigins("*");
        // 이제 클라이언트가 ws://localhost:8080/ws/chat으로 커넥션을 연결하고 통신할 수 있음
    }
}
