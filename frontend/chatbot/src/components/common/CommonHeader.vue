<template>
  <div
    class="chat-header"
    :style="{ maxWidth: isAdmin ? '1480px' : '400px' }"
  >
    <div class="title">
      {{ isAdmin ? '💬 챗봇 admin' : '💬 챗봇' }}
    </div>
    <div class="btn-group">
      <button v-if="!isAdmin" @click="goToChangePassword" class="change-btn">
        🔒 비밀번호 변경
      </button>
      <button v-if="!isAdmin" @click="deleteAccount" class="delete-btn">
        🗑️ 회원탈퇴
      </button>
      <!-- 로그아웃은 항상 표시 -->
      <button @click="logout" class="logout-btn">
        🚪 로그아웃
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { disconnectWebSocket, resetMessages } from '@/utils/websocket.js';
import { useModalStore } from '@/stores/modal.js';

const router = useRouter();
const modal = useModalStore();

const isAdmin = ref(false);

// JWT에서 username 파싱
const parseUsernameFromToken = () => {
  const token = localStorage.getItem('token');
  if (!token) return null;
2
  try {
    const payloadBase64 = token.split('.')[1];
    const decodedPayload = JSON.parse(atob(payloadBase64));
//    return decodedPayload.username;
    return decodedPayload.sub;
  } catch (e) {
    console.error('JWT 파싱 오류:', e);
    return null;
  }
};

const username = parseUsernameFromToken();
console.log('username:',username)
if (username === 'admin') {
  isAdmin.value = true;
}

// 로그아웃
const logout = () => {
  disconnectWebSocket();
  resetMessages();
  localStorage.removeItem('token');
  router.push('/login');
};

// 비밀번호 변경 페이지 이동
const goToChangePassword = () => {
  router.push('/change-password');
};

// 회원 탈퇴
const deleteAccount = () => {
  modal.showConfirm('정말로 회원 탈퇴하시겠습니까?', async () => {
    try {
      const token = localStorage.getItem('token');
      const res = await fetch('http://localhost:8080/api/auth/delete', {
        method: 'DELETE',
        headers: {
          Authorization: `Bearer ${token}`
        }
      });

      if (res.ok) {
        modal.showModal('회원 탈퇴가 완료되었습니다.');
        logout();
      } else {
        const errText = await res.text();
        modal.showModal(`탈퇴 실패: ${errText}`);
      }
    } catch (e) {
      console.error(e);
      modal.showModal('서버 오류로 탈퇴에 실패했습니다.');
    }
  });
};
</script>

<style scoped>
.chat-header {
  width: 100%;
  background: #4CAF50;
  color: white;
  padding: 12px 15px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-radius: 12px 12px 0 0;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.15);
  transition: max-width 0.3s ease;
}

.title {
  font-size: 18px;
  font-weight: bold;
  display: flex;
  align-items: center;
  gap: 8px;
  white-space: nowrap;
}

.btn-group {
  display: flex;
  gap: 6px;
}

.logout-btn,
.change-btn,
.delete-btn {
  border: none;
  padding: 6px 10px;
  border-radius: 6px;
  color: white;
  cursor: pointer;
  font-weight: bold;
  transition: background 0.3s;
  font-size: 13px;
  white-space: nowrap;
  height: 34px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.change-btn {
  background: #3498db;
}
.change-btn:hover {
  background: #2980b9;
}

.logout-btn {
  background: #e74c3c;
}
.logout-btn:hover {
  background: #c0392b;
}

.delete-btn {
  background: #9b59b6;
}
.delete-btn:hover {
  background: #8e44ad;
}
</style>
