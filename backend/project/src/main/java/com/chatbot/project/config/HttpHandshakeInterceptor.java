package com.chatbot.project.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

public class HttpHandshakeInterceptor implements HandshakeInterceptor {

    @Bean
    public HttpHandshakeInterceptor httpHandshakeInterceptor() {
        return new HttpHandshakeInterceptor();
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, org.springframework.http.server.ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {

        System.out.println("✅ [Interceptor] beforeHandshake 진입함!");
        if (request instanceof ServletServerHttpRequest servletRequest) {
            HttpServletRequest httpRequest = servletRequest.getServletRequest();
            String token = httpRequest.getParameter("token");
            System.out.println("토큰토큰토큰" + token);
            if (token != null && !token.isBlank()) {
                // 검증 로직 직접 추가하거나 SecurityContext에 넣을 수도 있음
                attributes.put("token", token);
                System.out.println("✅ WebSocket 연결 시 받은 토큰: " + token);
            } else {
                System.out.println("❌ 토큰이 없음. 연결 거절할 수도 있음");
            }
        }
        return true; // false로 하면 연결 거절
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, org.springframework.http.server.ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {

    }
}
