<template>
  <div class="chat-header">
    <div class="title">
      💬 챗봇
    </div>
    <div class="btn-group">
      <button @click="goToChangePassword" class="change-btn">
        🔒 비밀번호 변경
      </button>
      <button @click="deleteAccount" class="delete-btn">
        🗑️ 회원탈퇴
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
import { useModalStore } from '@/stores/modal.js'

const router = useRouter();
const modal = useModalStore();

// 로그아웃 처리
const logout = () => {
  disconnectWebSocket();
  resetMessages();
  localStorage.removeItem("token");
  router.push("/login");
};

// 비밀번호 변경 페이지로 이동
const goToChangePassword = () => {
  router.push("/change-password");
};

// 회원 탈퇴
const deleteAccount = async () => {
  const confirmDelete = confirm("정말로 회원 탈퇴하시겠습니까?");
  if (!confirmDelete) return;

  try {
    const token = localStorage.getItem("token");
    const res = await fetch("http://localhost:8080/api/auth/delete", {
      method: "DELETE",
      headers: {
        Authorization: `Bearer ${token}`
      }
    });

    if (res.ok) {
      modal.showModal("회원 탈퇴가 완료되었습니다.")
      logout();
    } else {
      const errText = await res.text();
      modal.showModal(`탈퇴 실패: ${errText}`);
    }
  } catch (e) {
    console.error(e);
    modal.showModal("서버 오류로 탈퇴에 실패했습니다.");
  }
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

/* 버튼별 색상 */
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
