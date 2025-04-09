<template>
  <div class="container">
    <h2>🔒 비밀번호 변경</h2>
    <form @submit.prevent="changePassword">
      <input v-model="currentPassword" type="password" placeholder="현재 비밀번호" required />
      <input v-model="newPassword" type="password" placeholder="새 비밀번호" required />
      <input v-model="confirmPassword" type="password" placeholder="새 비밀번호 확인" required />
      <button type="submit">변경</button>
    </form>
    <p v-if="message">{{ message }}</p>

    <button class="back-btn" @click="goBack">이전으로</button>
  </div>
</template>


<script setup>
import { ref } from 'vue'
import axios from 'axios'
import { useRouter } from "vue-router";
import { useModalStore } from '@/stores/modal.js'
const modal = useModalStore();
const currentPassword = ref('')
const newPassword = ref('')
const confirmPassword = ref('')
const message = ref('')
const router = useRouter();

const changePassword = async () => {
  if (newPassword.value !== confirmPassword.value) {
    modal.showModal('현재 비밀번호나 새 비밀번호가 일치하지 않습니다.');
    // message.value = '새 비밀번호가 일치하지 않습니다.'
    return
  }

  try {
    const token = localStorage.getItem('token')
    const res = await axios.post('http://localhost:8080/auth/change-password', {
      currentPassword: currentPassword.value,
      newPassword: newPassword.value,
    }, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    })

    // message.value = res.data.message
    modal.showModal(res.data.message);
    router.push('/chatbot');
  } catch (e) {
    modal.showModal(e.response?.data?.error || '오류 발생');
    // message.value = e.response?.data?.error || '오류 발생'
  }
}

const goBack = () => {
  router.push('/chatbot');
}
</script>

<style scoped>
.container {
  padding: 20px;
  max-width: 400px;
  margin: 80px auto;
  border: 1px solid #ccc;
  border-radius: 12px;
  box-shadow: none;
  background-color: white;
}

input {
  display: block;
  width: 95%;
  margin-bottom: 12px;
  padding: 10px;
  font-size: 14px;
  border: 1px solid #ccc;
  border-radius: 6px;
  background: transparent;
}

button {
  background: transparent;
  color: #3498db;
  padding: 10px;
  border: 2px solid #3498db;
  border-radius: 6px;
  cursor: pointer;
  width: 100%;
  font-weight: bold;
  transition: all 0.3s;
}

button:hover {
  background: #3498db;
  color: white;
}

.back-btn {
  margin-top: 10px;
  border: 2px solid #e74c3c;
  color: #e74c3c;
}

.back-btn:hover {
  background-color: #e74c3c;
  color: white;
}

</style>
