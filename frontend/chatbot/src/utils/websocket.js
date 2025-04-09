import { ref } from 'vue';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { getAccessToken } from '@/utils/auth.js'

const messages = ref([]);
let stompClient = null;
let isConnected = ref(false);
let reconnectAttempts = 0;
let reconnectTimeout = null; // 재연결 타이머 ID 저장용
let shouldReconnect = true; // 재연결 허용 여부
const MAX_RECONNECT_DELAY = 30000;

// WebSocket 연결 함수
const connectWebSocket = (username) => {
  if (isConnected.value) {
    console.log('🔌 WebSocket 이미 연결됨');
    return;
  }

  // 기존 연결 종료 후 새 연결 생성
  if (stompClient) {
    console.log('🔄 기존 WebSocket 연결 종료 중...');
    stompClient.deactivate();
    stompClient = null;
  }

  const token = getAccessToken();
  if (!token) {
    console.warn('❌ 토큰 없음: WebSocket 연결 중단');
    return;
  }

  const socket = new SockJS(`http://localhost:8080/chat?token=${token}`);
  console.log('socket: ', socket);

  stompClient = new Client({
    webSocketFactory: () => socket,
    connectHeaders: {
      Authorization: `Bearer ${token}`,
      username: username,
    },
    debug: str => console.log("[STOMP]", str),
    reconnectDelay: 0,
    heartbeatIncoming: 10000,
    heartbeatOutgoing: 10000,
  });

  console.log('stompClient 생성 완료:', stompClient);

  stompClient.onConnect = (frame) => {
    console.log('✅ WebSocket (STOMP) 연결됨:', frame);
    isConnected.value = true;
    reconnectAttempts = 0;

    stompClient.subscribe('/topic/messages', (message) => {
      try {
        console.log(`📩 수신된 메시지: ${message.body}`);
        messages.value.push(JSON.parse(message.body));
      } catch (e) {
        console.error('❌ 메시지 파싱 실패:', e);
      }
    });

    stompClient.subscribe('/user/queue/messages', (message) => {
      try {
        const msg = JSON.parse(message.body);
        console.log('전체 메시지:', msg);
        console.log(`🤖 [봇 응답] 수신된 메시지: ${message.body}`);
        messages.value = [...messages.value, msg];
      } catch (e) {
        console.error('❌ 봇 메시지 파싱 실패:', e);
      }
    });
  };

  stompClient.onWebSocketClose = () => {
    console.log('❌ WebSocket 연결 종료됨');
    isConnected.value = false;
    reconnectWebSocket(username);
  };

  stompClient.onWebSocketError = (event) => {
    console.error("❌ WebSocket 에러 발생:", event);
  };

  stompClient.onStompError = (frame) => {
    console.error("🚨 STOMP 프로토콜 에러:", frame);
  };

  stompClient.activate();
};

// 메시지 보내기
const sendMessage = (text, username) => {
  if (!stompClient || !stompClient.connected) {
    console.warn('🚨 WebSocket이 연결되지 않음. 메시지 전송 불가');
    return;
  }

  if (!text.trim()) {
    console.warn('🚨 보낼 메시지가 비어 있음.');
    return;
  }

  const messageObj = {
    text: text,
    sender: username,
  };

  try {
    console.log(`📤 메시지 전송 시도: ${JSON.stringify(messageObj)}`);
    stompClient.publish({
      destination: '/app/chat',
      body: JSON.stringify(messageObj),
      headers: { 'content-type': 'application/json' },
    });
    console.log(`📩 메시지 전송 성공: ${text}`);
  } catch (error) {
    console.error('❌ 메시지 전송 실패:', error);
  }
};

// WebSocket 종료 함수 (로그아웃 등에서 호출)
const disconnectWebSocket = () => {
  shouldReconnect = false;

  if (stompClient && stompClient.connected) {
    console.log("🔌 WebSocket 연결 해제 중...");
    stompClient.deactivate();
    stompClient = null;
  }

  if (reconnectTimeout) {
    clearTimeout(reconnectTimeout);
    reconnectTimeout = null;
  }

  isConnected.value = false;
  messages.value = [];
  console.log('🛑 WebSocket 종료 및 메시지 초기화 완료');
};

// WebSocket 자동 재연결
const reconnectWebSocket = (username) => {
  if (!shouldReconnect) {
    console.log("🚫 재연결 비활성화 상태. 중단됨");
    return;
  }

  if (stompClient) {
    stompClient.deactivate();
    stompClient = null;
  }

  const reconnectDelay = Math.min(5000 * 2 ** reconnectAttempts, MAX_RECONNECT_DELAY);
  reconnectAttempts++;

  console.log(`🔄 WebSocket 강제 종료됨. ${reconnectDelay / 1000}초 후 재연결 시도...`);
  reconnectTimeout = setTimeout(() => {
    if (!isConnected.value) connectWebSocket(username);
  }, reconnectDelay);
};

const resetMessages = () => {
  if (isConnected.value) {
    console.warn("⚠️ 연결된 상태에서는 resetMessages() 사용 지양");
  } else {
    messages.value = [];
  }
};

export { messages, connectWebSocket, sendMessage, isConnected, disconnectWebSocket, resetMessages };
