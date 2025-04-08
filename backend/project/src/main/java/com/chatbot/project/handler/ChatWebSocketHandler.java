//package com.chatbot.project.handler;
//
//import org.springframework.web.socket.*;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import java.util.*;
//
//public class ChatWebSocketHandler extends TextWebSocketHandler {
//    private static final Set<WebSocketSession> sessions = new HashSet<>();
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        sessions.add(session);
//    }
//
//    @Override
//    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        String payload = message.getPayload();
//        ChatMessage chatMessage = objectMapper.readValue(payload, ChatMessage.class);
//
//        // 봇의 응답 추가
//        ChatMessage botResponse = new ChatMessage("bot", generateBotResponse(chatMessage.text));
//
//        // 모든 클라이언트에게 전송
//        for (WebSocketSession webSocketSession : sessions) {
//            webSocketSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(chatMessage)));
//            webSocketSession.sendMessage(new TextMessage(objectMapper.writeValueAsString(botResponse)));
//        }
//    }
//
//    @Override
//    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//        sessions.remove(session);
//    }
//
//    // 봇 응답 생성 로직
//    private String generateBotResponse(String userMessage) {
//        if (userMessage.contains("안녕")) {
//            return "안녕하세요! 무엇을 도와드릴까요?";
//        } else if (userMessage.contains("시간")) {
//            return "현재 시간은 " + new Date().toString() + " 입니다.";
//        } else {
//            return "죄송해요, 이해하지 못했어요.";
//        }
//    }
//
//    private static class ChatMessage {
//        public String sender;
//        public String text;
//
//        public ChatMessage() {}
//
//        public ChatMessage(String sender, String text) {
//            this.sender = sender;
//            this.text = text;
//        }
//    }
//}
//
