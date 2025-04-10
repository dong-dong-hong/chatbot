package com.chatbot.project.controller;

import com.chatbot.project.config.StompPrincipal;
import com.chatbot.project.entity.ChatMessage;
import com.chatbot.project.repository.ChatMessageRepository;
import com.chatbot.project.security.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;
import java.time.LocalDateTime;

@Controller
public class ChatController {
    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    private final ChatMessageRepository chatMessageRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper;

    public ChatController(ChatMessageRepository chatMessageRepository,
                          SimpMessagingTemplate messagingTemplate,
                          JwtUtil jwtUtil, ObjectMapper objectMapper) {
        this.chatMessageRepository = chatMessageRepository;
        this.messagingTemplate = messagingTemplate;
        this.jwtUtil = jwtUtil;
        this.objectMapper = objectMapper;
    }

    @MessageMapping("/chat")
    public void sendMessage(ChatMessage message, Principal principal) throws JsonProcessingException {
        logger.info("principal: {}", principal);

        if (!(principal instanceof StompPrincipal stompPrincipal)) {
            logger.warn("❌ Principal이 StompPrincipal이 아님");
            return;
        }

        String token = stompPrincipal.getToken();
        logger.info("🔐 토큰: {}", token);

        if (!jwtUtil.validateToken(token)) {
            logger.warn("❌ 토큰이 유효하지 않음");
            return;
        }

        String username = jwtUtil.getUsernameFromToken(token);
        logger.info("📩 받은 메시지: {} (보낸 사람: {})", message.getText(), username);

        // 사용자 메시지 설정 및 저장
        message.setSender(username);
        message.setReceiver("bot");
        ChatMessage savedMessage = chatMessageRepository.save(message);
        messagingTemplate.convertAndSendToUser(username, "/queue/messages", savedMessage);

        // 챗봇 응답 생성 (저장 X)
        ChatMessage botReply = new ChatMessage();
        botReply.setSender("bot");
        botReply.setReceiver(username);
        botReply.setText(generateBotReply(message.getText()));
        botReply.setCreatedAt(LocalDateTime.now()); // 저장하지 않기 때문에 수동 설정

//        ObjectMapper om = new ObjectMapper(); // 테스트
        System.out.println("💬 botReply JSON: " + objectMapper.writeValueAsString(botReply));
        messagingTemplate.convertAndSendToUser(username, "/queue/messages", botReply);
    }


    private String generateBotReply(String text) {
        if (text.contains("테스트")) {
            return "테스트입니다.";
        } else if (text.contains("손동홍")) {
            return "손동홍입니다.";
        }else if (text.contains("야구")) {
            return "현재 한국프로야구에는 10개팀이 있습니다."
;        }
        return "죄송해요, 무슨 말씀이신지 잘 모르겠어요.";
    }

}