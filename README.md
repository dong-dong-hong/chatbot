# 💬 chatbot

STOMP 기반 WebSocket으로 구현한 실시간 챗봇 프로젝트입니다.  
Vue + Spring Boot 환경에서 동작하며, JWT 로그인 기능과 MariaDB 연동을 포함하고 있습니다.

---

## 📦 사용한 스택

| 분류         | 기술                                |
|--------------|--------------------------------------|
| 프론트엔드   | Vue 3 + Yarn + Vite + Pinia          |
| 백엔드       | Spring Boot 3.4.2 + Spring Security  |
| 실시간 통신  | STOMP over WebSocket + SockJS       |
| 인증         | JWT                                  |
| 데이터베이스 | MariaDB                              |
| ORM          | Spring Data JPA                      |

---

## 🔐 주요 기능

- ✅ **JWT 로그인 및 회원가입 기능**
  - 사용자 정보 입력 시 회원가입 처리 (username, password, email 등)
  - 로그인 시 JWT 토큰을 발급하고 인증 처리 수행
  - 계정 정보 찾기 및 보안 관리 기능 지원 (아이디/비밀번호 찾기 및 변경)
  - 회원 탈퇴 기능 지원: 로그인한 사용자가 자신의 계정을 삭제할 수 있음

- 💬 **실시간 채팅 기능**
  - STOMP WebSocket을 통한 실시간 메시지 전송
  - 클라이언트 → 서버: `/app/chat` 경로로 메시지 전송
  - 서버 → 클라이언트: `/topic/messages` 경로 구독을 통해 메시지 수신
  - 채팅 메시지는 DB에 저장됨 (sender, text, createdAt)

- 🧹 **스케줄러를 통한 메시지 정리 기능**
  - 매일 새벽 1시, 탈퇴한 사용자(sender)와 관련된 메시지를 정기적으로 삭제
  - `chatbot_message` 테이블에서 존재하지 않는 사용자의 메시지 자동 정리

- 🔁 **자동 재연결 처리**
  - WebSocket 연결이 끊기면 일정 시간 후 자동 재연결
  - 메시지 전송 실패 시 경고 출력 및 연결 복구 시도

- 🌐 **보안 및 CORS 처리**
  - `@CrossOrigin` 또는 `.setAllowedOrigins("*")` 설정으로 CORS 허용
  - JWT 기반 인증 시스템 도입

---

## 🧑‍💼 관리자 기능 (Admin Dashboard)

- 📊 **관리자 전용 통계 대시보드 제공**
  - 전체 메시지 수, 고유 사용자 수, 봇 메시지 수 등 주요 지표 실시간 집계
  - 일자별 메시지 수를 **Chart.js + Element Plus** 기반 막대그래프로 시각화
  - 최근 메시지와 전체 메시지를 **그리드 테이블**로 관리자가 확인 가능
  - `/api/admin/**` 경로로 백엔드 API 제공 (Spring Boot Controller 구성)
  - `admin` 계정으로 로그인한 경우에만 접근 가능
  - 모든 통계는 MariaDB 기반 실시간 데이터 기준
