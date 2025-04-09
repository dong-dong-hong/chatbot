<template>
  <div class="container">
    <div class="login-box">
      <h2>로그인</h2>
      <input v-model="username" placeholder="Username" class="input-field" @keyup.enter="login" />
      <input v-model="password" type="password" placeholder="Password" class="input-field" @keyup.enter="login" />

      <button @click="login" class="btn login-btn">로그인</button>

      <div class="link-container">
        <router-link to="/register" class="link">회원가입</router-link>
        <router-link to="/find-username" class="link">아이디 찾기</router-link>
        <router-link to="/find-password" class="link">비밀번호 찾기</router-link>
      </div>

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
  if (!username.value.trim()) {
    alert('아이디를 입력하세요.');
    return;
  }

  if (!password.value.trim()) {
    alert('비밀번호를 입력하세요.');
    return;
  }

  try {
    const response = await fetch('http://localhost:8080/api/auth/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username: username.value, password: password.value })
    });

    const data = await response.json();

    console.log('🔹 로그인 응답:', data);

    if (response.ok && data.token) {
      console.log('✅ 받은 토큰:', data.token);
      localStorage.setItem('token', data.token);
      router.push('/chatbot');
    } else {
      alert(`로그인 실패: ${data.error || '아이디 또는 비밀번호를 확인하세요.'}`);
    }
  } catch (error) {
    console.error('❌ 로그인 오류:', error);
    alert('서버 오류로 인해 로그인할 수 없습니다.');
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
  background: linear-gradient(to right, #eef2f3, #8e9eab);
}

.login-box {
  background: white;
  padding: 40px;
  border-radius: 12px;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
  text-align: center;
  width: 350px;
}

.input-field {
  width: 100%;
  padding: 12px;
  margin: 10px 0;
  border: 1px solid #ccc;
  border-radius: 8px;
  font-size: 14px;
  outline: none;
  transition: border 0.3s;
}

.input-field:focus {
  border: 1px solid #007bff;
}

.btn {
  width: 100%;
  padding: 12px;
  background: #007bff;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-size: 16px;
  font-weight: bold;
  transition: background 0.3s;
}

.btn:hover {
  background: #0056b3;
}

.link-container {
  display: flex;
  justify-content: space-between;
  margin-top: 15px;
  font-size: 14px;
}

.link {
  color: #007bff;
  text-decoration: none;
  transition: color 0.3s;
}

.link:hover {
  color: #0056b3;
  text-decoration: underline;
}

.social-login {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
}

.social-btn {
  flex: 1;
  padding: 12px;
  border: none;
  cursor: pointer;
  color: white;
  border-radius: 8px;
  margin: 0 5px;
  font-size: 14px;
  font-weight: bold;
  transition: opacity 0.3s;
}

.social-btn:hover {
  opacity: 0.8;
}

.naver {
  background: #2db400;
}

.kakao {
  background: #fee500;
  color: black;
}
</style>
