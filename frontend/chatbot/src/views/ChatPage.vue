<template>
  <div class="chat-container">
    <div class="messages">
      <div v-for="(msg, index) in messages" :key="index" :class="['message', msg.sender]">
        <span class="bubble">{{ msg.text }}</span>
      </div>
    </div>
    <div class="input-container">
      <input v-model="newMessage" @keyup.enter="sendMessageHandler" placeholder="메시지를 입력하세요..." />
      <button @click="sendMessageHandler">📩</button>
      <button @click="toggleWebSocket" class="toggle-btn">
        {{ isConnected ? '🔌 연결 해제' : '🚀 연결' }}
      </button>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import { connectWebSocket, sendMessage, messages, disconnectWebSocket, isConnected } from '@/utils/websocket';

export default {
  setup() {
    const newMessage = ref('');

    const sendMessageHandler = () => {
      if (newMessage.value.trim()) {
        sendMessage(newMessage.value);
        newMessage.value = '';
      }
    };

    // ✅ WebSocket 연결/해제 토글 기능
    const toggleWebSocket = () => {
      if (isConnected.value) {
        disconnectWebSocket();
      } else {
        connectWebSocket();
      }
    };

    onMounted(() => {
      connectWebSocket(); // 페이지 로드시 WebSocket 자동 연결
    });

    return { newMessage, sendMessageHandler, messages, toggleWebSocket, isConnected };
  }
};
</script>

<style scoped>
.chat-container {
  width: 350px;
  height: 500px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  background: #f5f5f5;
  border-radius: 15px;
  padding: 10px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
}

.messages {
  flex: 1;
  overflow-y: auto;
  padding: 10px;
  display: flex;
  flex-direction: column;
}

.message {
  display: flex;
  margin: 5px 0;
}

.user {
  justify-content: flex-end;
}

.bot {
  justify-content: flex-start;
}

.bubble {
  padding: 10px 15px;
  border-radius: 15px;
  max-width: 75%;
  font-size: 14px;
  line-height: 1.4;
  word-wrap: break-word;
}

.user .bubble {
  background: #4CAF50;
  color: white;
  border-top-right-radius: 2px;
}

.bot .bubble {
  background: #ddd;
  color: black;
  border-top-left-radius: 2px;
}

.input-container {
  display: flex;
  align-items: center;
  border-top: 1px solid #ccc;
  padding: 8px;
  background: white;
  border-radius: 0 0 15px 15px;
}

.input-container input {
  flex: 1;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 20px;
  outline: none;
  font-size: 14px;
}

.input-container button {
  margin-left: 8px;
  padding: 8px 12px;
  background: #4CAF50;
  color: white;
  border: none;
  border-radius: 50%;
  cursor: pointer;
  font-size: 16px;
}

.input-container .toggle-btn {
  margin-left: 5px;
  padding: 6px 10px;
  background: #ff4d4d;
  color: white;
  border: none;
  border-radius: 50%;
  cursor: pointer;
  font-size: 14px;
}

.input-container .toggle-btn:hover {
  background: #ff0000;
}
</style>
