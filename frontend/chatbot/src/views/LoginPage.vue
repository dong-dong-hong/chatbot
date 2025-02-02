<template>
  <div class="login-container">
    <h2>Please Sign In</h2>
    <input v-model="username" placeholder="Username" />
    <input v-model="password" type="password" placeholder="Password" />
    <button @click="login">Sign in</button>
  </div>
</template>

<script>
import { ref } from 'vue';
import { useRouter } from 'vue-router';

export default {
  setup() {
    const username = ref('');
    const password = ref('');
    const router = useRouter();

    const login = async () => {
      // 백엔드 로그인 API 호출 (REST 방식)
      const response = await fetch('http://localhost:8080/api/auth/login', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ username: username.value, password: password.value })
      });

      if (response.ok) {
        router.push('/chatbot'); // 로그인 성공 시 챗봇 페이지로 이동
      } else {
        alert('로그인 실패');
      }
    };

    return { username, password, login };
  }
};
</script>

<style scoped>
.login-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
}
</style>
