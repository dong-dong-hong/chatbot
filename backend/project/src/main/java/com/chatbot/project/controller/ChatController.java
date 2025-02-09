package com.chatbot.project.controller;

import com.chatbot.project.entity.ChatMessage;
import com.chatbot.project.repository.ChatMessageRepository;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;

@Controller
public class ChatController {

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public ChatMessage sendMessage(ChatMessage message) {
        System.out.println(message.getText());
        // DB에 저장
        ChatMessage savedMessage = chatMessageRepository.save(new ChatMessage(message.getText(), message.getSender()));
        return savedMessage;
    }
}
