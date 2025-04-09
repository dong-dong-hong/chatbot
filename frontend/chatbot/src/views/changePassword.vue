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

const currentPassword = ref('')
const newPassword = ref('')
const confirmPassword = ref('')
const message = ref('')
const router = useRouter();

const changePassword = async () => {
  if (newPassword.value !== confirmPassword.value) {
    message.value = '새 비밀번호가 일치하지 않습니다.'
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

    message.value = res.data.message
  } catch (e) {
    message.value = e.response?.data?.error || '오류 발생'
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
  margin: auto;
}

input {
  display: block;
  width: 100%;
  margin-bottom: 12px;
  padding: 10px;
  font-size: 14px;
}

button {
  background: #3498db;
  color: white;
  padding: 10px;
  border: none;
  cursor: pointer;
  width: 100%;
  font-weight: bold;
}

button:hover {
  background: #2980b9;
}

.back-btn {
  margin-top: 10px;
  width: 100%;
  padding: 10px;
  background-color: #e74c3c;
  color: white;
  border: none;
  cursor: pointer;
  font-weight: bold;
  transition: background 0.3s;
}

.back-btn:hover {
  background-color: #c0392b;
}
</style>
