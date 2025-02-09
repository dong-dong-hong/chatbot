<template>
  <div class="container">
    <div class="login-box">
      <h2>로그인</h2>
      <input v-model="username" placeholder="Username" class="input-field" />
      <input v-model="password" type="password" placeholder="Password" class="input-field" />
        <button @click="login" class="btn">로그인</button>

        <router-link to="/register" class="register-link">회원가입</router-link>

        <div class="social-login">
          <button @click="naverLogin" class="social-btn naver">네이버 로그인</button>
        <button @click="kakaoLogin" class="social-btn kakao">카카오 로그인</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const username = ref('');
const password = ref('');
const router = useRouter();

const login = async () => {
  const response = await fetch('http://localhost:8080/api/auth/login', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ username: username.value, password: password.value })
  });

  if (response.ok) {
    router.push('/chatbot');
  } else {
    alert('로그인 실패');
  }
};

const naverLogin = () => {
  window.location.href = 'http://localhost:8080/oauth2/authorization/naver';
};

const kakaoLogin = () => {
  window.location.href = 'http://localhost:8080/oauth2/authorization/kakao';
};


</script>

<style scoped>
.container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f0f2f5;
}

.login-box {
  background: white;
  padding: 30px;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  text-align: center;
  width: 320px;
}

.input-field {
  width: 100%;
  padding: 10px;
  margin: 10px 0;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.btn {
  width: 100%;
  padding: 10px;
  background: #007bff;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  margin-top: 10px;
}

.register-link {
  display: block;
  margin-top: 15px;
  color: #007bff;
  text-decoration: none;
  font-size: 14px;
}

.social-login {
  display: flex;
  justify-content: space-between;
  margin-top: 15px;
}

.social-btn {
  flex: 1;
  padding: 10px;
  border: none;
  cursor: pointer;
  color: white;
  border-radius: 5px;
  margin: 0 5px;
}

.naver {
  background: #2db400;
}

.kakao {
  background: #fee500;
  color: black;
}
</style>
