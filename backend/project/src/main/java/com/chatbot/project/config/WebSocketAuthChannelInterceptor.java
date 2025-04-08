package com.chatbot.project.config;

import com.chatbot.project.security.JwtUtil;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

@Component
public class WebSocketAuthChannelInterceptor implements ChannelInterceptor {

    private final JwtUtil jwtUtil;

    public WebSocketAuthChannelInterceptor(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        System.out.println("📩 [Interceptor] preSend 호출됨"); // ✅ 1단계: 무조건 찍혀야 함

        StompHeaderAccessor accessor =
                MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null) {
            System.out.println("📌 STOMP Command: " + accessor.getCommand()); // ✅ 2단계: CONNECT 확인

            if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                String authHeader = accessor.getFirstNativeHeader("Authorization");
                System.out.println("📥 받은 Authorization 헤더: " + authHeader); // ✅ 3단계: 토큰 확인

                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    String token = authHeader.substring(7);
                    boolean isValid = jwtUtil.validateToken(token);
                    System.out.println("🔍 JWT 유효성 검사 결과: " + isValid); // ✅ 4단계: 토큰 검증 결과

                    if (isValid) {
                        String username = jwtUtil.getUsernameFromToken(token);
                        accessor.setUser(new StompPrincipal(username));
                        System.out.println("✅ 사용자 인증 성공: " + username); // ✅ 5단계: 성공 시
                    } else {
                        System.out.println("❌ JWT 토큰 검증 실패");
                    }
                } else {
                    System.out.println("❌ Authorization 헤더 누락 또는 형식 오류");
                }
            }
        } else {
            System.out.println("⚠️ StompHeaderAccessor가 null입니다.");
        }

        return message;
    }

}
