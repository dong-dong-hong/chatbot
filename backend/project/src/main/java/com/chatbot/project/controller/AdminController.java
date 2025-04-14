package com.chatbot.project.controller;

import com.chatbot.project.entity.ChatMessage;
import com.chatbot.project.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ChatMessageRepository messageRepository;

    @GetMapping("/stats")
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalMessages", messageRepository.count());
        stats.put("uniqueUsers", messageRepository.countDistinctSender());
        stats.put("botMessages", messageRepository.countByReceiver("bot"));
        return stats;
    }

    @GetMapping("/messages/daily-count")
    public List<Map<String, Object>> getDailyCount() {
        return messageRepository.countMessagesGroupedByDate();
    }

    @GetMapping("/messages/recent")
    public List<ChatMessage> getRecentMessages() {
        return messageRepository.findTop20ByOrderByCreatedAtDesc();
    }

    @GetMapping("/messages")
    public List<ChatMessage> getAllMessages() {
        return messageRepository.findAllByOrderByCreatedAtDesc();
    }
}

