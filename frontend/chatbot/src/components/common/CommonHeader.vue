<template>
  <div class="chat-header">
    <div class="title">
      💬 챗봇
    </div>
    <div class="btn-group">
      <button @click="goToChangePassword" class="change-btn">
        🔒 비밀번호 변경
      </button>
      <button @click="logout" class="logout-btn">
        🚪 로그아웃
      </button>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from "vue-router";
import { disconnectWebSocket, resetMessages } from '@/utils/websocket.js'

const router = useRouter();

const logout = () => {
  disconnectWebSocket();
  resetMessages();
  localStorage.removeItem("token");
  router.push("/login");
};

const goToChangePassword = () => {
  router.push("/change-password");
};
</script>

<style scoped>
.chat-header {
  width: 100%;
  max-width: 400px;
  background: #4CAF50;
  color: white;
  padding: 12px 15px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-radius: 12px 12px 0 0;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.15);
}

.title {
  font-size: 18px;
  font-weight: bold;
  display: flex;
  align-items: center;
  gap: 8px;
}

.btn-group {
  display: flex;
  gap: 8px;
}

.logout-btn, .change-btn {
  background: #e74c3c;
  border: none;
  padding: 8px 14px;
  border-radius: 6px;
  color: white;
  cursor: pointer;
  font-weight: bold;
  transition: background 0.3s;
}

.change-btn {
  background: #3498db;
}

.change-btn:hover {
  background: #2980b9;
}

.logout-btn:hover {
  background: #c0392b;
}
</style>
