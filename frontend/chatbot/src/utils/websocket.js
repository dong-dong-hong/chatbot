import { ref } from 'vue';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

const messages = ref([]);
let stompClient = null;
let isConnected = ref(false);
let reconnectAttempts = 0;
const MAX_RECONNECT_DELAY = 30000;

const connectWebSocket = () => {
  if (isConnected.value) {
    console.log('🔌 WebSocket 이미 연결됨');
    return;
  }

  console.log(`🚀 WebSocket 연결 시도 (${reconnectAttempts + 1}번째)`);

  if (stompClient) {
    stompClient.deactivate();
  }

  const socket = new SockJS('http://localhost:8080/chat');
  stompClient = new Client({
    webSocketFactory: () => socket,
    debug: (str) => console.log(str),
    reconnectDelay: 5000, // 기본 재연결 대기 시간

    onConnect: () => {
      console.log('✅ WebSocket (STOMP) 연결됨');
      isConnected.value = true;
      reconnectAttempts = 0;

      stompClient.subscribe('/topic/messages', (message) => {
        messages.value.push(JSON.parse(message.body));
      });
    },

    onWebSocketClose: () => {
      console.log('❌ WebSocket 연결 종료됨');
      isConnected.value = false;

      // 재연결 간격 점진적 증가
      const reconnectDelay = Math.min(5000 * 2 ** reconnectAttempts, MAX_RECONNECT_DELAY);
      reconnectAttempts++;

      console.log(`🔄 WebSocket 강제 종료됨. ${reconnectDelay / 1000}초 후 재연결 시도...`);
      setTimeout(connectWebSocket, reconnectDelay);
    }
  });

  stompClient.activate();
};

// ✉️ 메시지 보내기
const sendMessage = (text) => {
  if (stompClient && stompClient.connected && text.trim()) {
    const messageObj = { text, sender: 'user' };

    stompClient.publish({
      destination: '/app/chat',
      body: JSON.stringify(messageObj),
    });

    messages.value.push(messageObj);
  } else {
    console.warn('🚨 메시지를 보낼 수 없음: WebSocket이 연결되지 않음');
  }
};

// ❌ WebSocket 연결 해제
const disconnectWebSocket = () => {
  if (stompClient && isConnected.value) {
    stompClient.deactivate();
    isConnected.value = false;
    console.log('🔌 WebSocket 연결 수동 해제됨');
  }
};

export { messages, connectWebSocket, sendMessage, disconnectWebSocket, isConnected };
