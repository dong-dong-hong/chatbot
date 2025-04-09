<template>
  <div class="chat-header">
    <div class="title">
      💬 챗봇
    </div>
    <button @click="logout" class="logout-btn">
      🚪 로그아웃
    </button>
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
  //margin: 0 auto 10px auto;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.15);
}

.title {
  font-size: 18px;
  font-weight: bold;
  display: flex;
  align-items: center;
  gap: 8px;
}

.logout-btn {
  background: #e74c3c;
  border: none;
  padding: 8px 14px;
  border-radius: 6px;
  color: white;
  cursor: pointer;
  font-weight: bold;
  transition: background 0.3s;
}

.logout-btn:hover {
  background: #c0392b;
}
</style>
