package com.chatbot.project.repository;

import com.chatbot.project.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    // receiver가 bot인 메시지 수
    long countByReceiver(String receiver);

    // sender 수
    @Query("SELECT COUNT(DISTINCT m.sender) FROM ChatMessage m")
    long countDistinctSender();

    // 최근 메시지 20개
    List<ChatMessage> findTop20ByOrderByCreatedAtDesc();

    // 전체 메시지 정렬
    List<ChatMessage> findAllByOrderByCreatedAtDesc();

    // 일자별 메시지 수 집계
    @Query(value = """
        SELECT DATE(created_at) AS date, COUNT(*) AS count
        FROM chatbot_message
        GROUP BY DATE(created_at)
        ORDER BY date
    """, nativeQuery = true)
    List<Map<String, Object>> countMessagesGroupedByDate();
}
