package com.chatbot.project.controller;

import com.chatbot.project.entity.ChatMessage;
import com.chatbot.project.repository.ChatMessageRepository;
import com.chatbot.project.security.JwtUtil;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;

@Controller
public class ChatController {
    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    private final ChatMessageRepository chatMessageRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final JwtUtil jwtUtil;

    public ChatController(ChatMessageRepository chatMessageRepository,
                          SimpMessagingTemplate messagingTemplate,
                          JwtUtil jwtUtil) {
        this.chatMessageRepository = chatMessageRepository;
        this.messagingTemplate = messagingTemplate;
        this.jwtUtil = jwtUtil;
    }

    @MessageMapping("/chat")
    public void sendMessage(ChatMessage message, Principal principal) {
        logger.info("📥 @MessageMapping 도달!");
        String token = principal.getName();

        if (!jwtUtil.validateToken(token)) {
            logger.warn("❌ 토큰이 유효하지 않음");
            return;
        }

        String username = jwtUtil.getUsernameFromToken(token);
        logger.info("📩 받은 메시지: {} (보낸 사람: {})", message.getText(), username);

        message.setSender(username);
        ChatMessage savedMessage = chatMessageRepository.save(message);
        messagingTemplate.convertAndSend("/topic/messages", savedMessage);
    }
}