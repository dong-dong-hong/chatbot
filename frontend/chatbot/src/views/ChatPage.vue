<template>
  <div class="chat-container">
    <div class="messages">
      <div
        v-for="(msg, index) in messages"
        :key="index"
        :class="['message',
        msg.sender === myName
        ? 'user'
        : msg.sender === 'bot'
          ? 'bot'
          : 'other']"
      >
        <span class="bubble">{{ msg.text }}</span>
      </div>
    </div>
    <div class="input-container">
      <input
        v-model="newMessage"
        @keyup.enter="sendMessageHandler"
        placeholder="💬 메시지를 입력하세요..."
        class="input-box"
      />
      <button @click="sendMessageHandler" class="send-btn">📩</button>
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import { messages, connectWebSocket, sendMessage, isConnected } from '@/utils/websocket';
import { clearTokens } from '@/utils/auth.js'

export default {
  setup() {
    const newMessage = ref('');
    const myName = ref('');

    const sendMessageHandler = () => {
      if (newMessage.value.trim()) {
        sendMessage(newMessage.value, myName.value);
        newMessage.value = '';
        console.log("메시지 내용: ",messages.value);
      }
    };

    onMounted(() => {
      const token = localStorage.getItem('token');
      if (token) {
        try {
          const payload = JSON.parse(atob(token.split('.')[1]));
          myName.value = payload.sub || '';
        } catch (e) {
          console.warn('❌ 토큰 파싱 실패:', e);
        }
      }
      console.log('📡 Chat 컴포넌트 마운트됨. WebSocket 연결 시도...');
      connectWebSocket(myName.value);
    });

    return { newMessage, sendMessageHandler, messages, isConnected, myName };
  }
};
</script>

<style scoped>
.chat-container {
  width: 100%;
  max-width: 400px;
  height: 600px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  background: #f5f5f5;
  border-radius: 12px;
  padding: 15px;
  border: 3px solid #ccc;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
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
  padding: 12px 16px;
  border-radius: 20px;
  max-width: 75%;
  font-size: 14px;
  line-height: 1.4;
  word-wrap: break-word;
}

.user .bubble {
  background: #4CAF50;
  color: white;
  border-top-right-radius: 5px;
}

.bot .bubble {
  background: #ddd;
  color: black;
  border-top-left-radius: 5px;
}

.input-container {
  display: flex;
  align-items: center;
  gap: 8px;
  border-top: 1px solid #ccc;
  padding: 12px;
  background: white;
  border-radius: 0 0 12px 12px;
}

.input-box {
  flex: 1;
  padding: 12px;
  border: 2px solid #aaa;
  border-radius: 20px;
  outline: none;
  font-size: 14px;
  background: white;
}

.send-btn {
  background: #4CAF50;
  color: white;
  border: none;
  padding: 12px;
  border-radius: 50%;
  cursor: pointer;
  font-size: 16px;
}

.send-btn:hover {
  background: #388E3C;
}
</style>
