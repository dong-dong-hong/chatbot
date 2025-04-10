package com.chatbot.project.scheduler;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChatScheduler {

    private final EntityManager entityManager;

    // 사용자 탈퇴시 chatbot_message에 있는 탈퇴 아이디 내용 삭제
    @Scheduled(cron = "0 0 1 * * *") // 매일 새벽 1시(실제 운영서비스)
//    @Scheduled(cron = "*/30 * * * * *") // 테스트용
    @Transactional
    public void cleanMessagesOfDeletedUsers() {
        String sql = """
            DELETE FROM chatbot_message m
            WHERE NOT EXISTS (
                SELECT 1 FROM user u
                WHERE u.username COLLATE utf8mb4_general_ci = m.sender COLLATE utf8mb4_general_ci
            )
        """;

        int deletedCount = entityManager
                .createNativeQuery(sql)
                .executeUpdate();

        log.info("🧹 삭제된 메시지 수: {}", deletedCount);
    }
}
