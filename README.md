# 💬 chatbot

STOMP 기반 WebSocket으로 구현한 실시간 챗봇 프로젝트입니다.  
Vue + Spring Boot 환경에서 동작하며, JWT 로그인 기능과 MariaDB 연동을 포함하고 있습니다.

---

## 📦 사용한 스택

| 분류         | 기술                            |
|--------------|----------------------------------|
| 프론트엔드   | Vue 3 + Yarn + Vite + Pinia      |
| 백엔드       | Spring Boot 3.4.2               |
| 실시간 통신  | STOMP over WebSocket + SockJS   |
| 인증         | JWT                             |
| 데이터베이스 | MariaDB                         |
| ORM          | Spring Data JPA                 |

---

## 🔐 주요 기능

- ✅ **JWT 로그인 및 회원가입 기능**  
  - 사용자 정보 입력 시 회원가입 처리 (username, password, email 등)  
  - 로그인 시 JWT 토큰을 발급하고 인증 처리 수행
  - 계정 정보 찾기 및 보안 관리 기능 지원 (아이디/비밀번호 찾기 및 변경)

- 💬 **실시간 채팅 기능**  
  - STOMP WebSocket을 통한 실시간 메시지 전송  
  - 클라이언트 → 서버: `/app/chat` 경로로 메시지 전송  
  - 서버 → 클라이언트: `/topic/messages` 경로 구독을 통해 메시지 수신  
  - 채팅 메시지는 DB에 저장됨 (sender, text, createdAt)  

- 🔁 **자동 재연결 처리**  
  - WebSocket 연결이 끊기면 일정 시간 후 자동 재연결  
  - 메시지 전송 실패 시 경고 출력 및 연결 복구 시도  

- 🌐 **보안 및 CORS 처리**  
  - `@CrossOrigin` 또는 `.setAllowedOrigins("*")` 설정으로 CORS 허용  
  - JWT 기반 인증 시스템 도입

 
