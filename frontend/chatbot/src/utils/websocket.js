import { ref } from 'vue';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

const messages = ref([]);
let stompClient = null;
let isConnected = ref(false);
let manualDisconnect = false; // ✅ 사용자가 수동으로 끊었는지 확인

// 🚀 WebSocket 연결 (사용자가 끊지 않는 한 유지)
const connectWebSocket = () => {
  if (isConnected.value) {
    console.log('🔌 WebSocket 이미 연결됨');
    return;
  }

  manualDisconnect = false; // ✅ 수동 종료가 아니면 재연결 허용

  console.log('🚀 WebSocket 연결 시도 중...');
  const socket = new SockJS('http://localhost:8080/chat');

  stompClient = new Client({
    webSocketFactory: () => socket,
    debug: (str) => console.log(str),
    reconnectDelay: 5000, // ✅ 서버에서 강제 종료해도 5초 후 재연결
  });

  stompClient.onConnect = () => {
    console.log('✅ WebSocket (STOMP) 연결됨');
    isConnected.value = true;

    stompClient.subscribe('/topic/messages', (message) => {
      try {
        const receivedMsg = JSON.parse(message.body);
        messages.value.push(receivedMsg);
      } catch (error) {
        console.error('❌ 메시지 파싱 오류:', error);
      }
    });
  };

  stompClient.onStompError = (error) => {
    console.error('🚨 STOMP 프로토콜 에러:', error);
  };

  stompClient.onWebSocketClose = () => {
    console.log('❌ WebSocket 연결 종료됨');
    isConnected.value = false;

    // ✅ 사용자가 끊은 게 아니라면 재연결
    if (!manualDisconnect) {
      console.log('🔄 WebSocket 강제 종료됨. 재연결 시도 중...');
      setTimeout(() => connectWebSocket(), 3000);
    }
  };

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

// ❌ WebSocket 연결 해제 (사용자가 직접 종료)
const disconnectWebSocket = () => {
  if (stompClient && isConnected.value) {
    manualDisconnect = true; // ✅ 사용자가 종료했음을 표시
    stompClient.deactivate();
    console.log('🔌 WebSocket 연결 수동 해제됨');
    isConnected.value = false;
  } else {
    console.warn('⚠ WebSocket이 이미 끊어졌거나 연결되지 않음');
  }
};

export { messages, connectWebSocket, sendMessage, disconnectWebSocket, isConnected };
