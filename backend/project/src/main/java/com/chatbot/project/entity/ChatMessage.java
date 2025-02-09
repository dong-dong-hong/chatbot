package com.chatbot.project.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "chatbot_message") // 실제 테이블명과 매칭
@Getter
@Setter
@NoArgsConstructor
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가 ID
    private Long id;

    @Column(nullable = false) // NULL 방지
    private String text;

    @Column(nullable = false)
    private String sender;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    public ChatMessage(String text, String sender) {
        this.text = text;
        this.sender = sender;
        this.createdAt = LocalDateTime.now();
    }
}
