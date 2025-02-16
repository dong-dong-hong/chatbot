<template>
  <div class="container">
    <div class="register-box">
      <h2>회원가입</h2>

      <div class="input-group">
        <label>아이디</label>
        <input v-model="username" placeholder="아이디를 입력하세요" class="input-field" />
      </div>

      <div class="input-group">
        <label>이메일</label>
        <input v-model="email" type="email" placeholder="이메일을 입력하세요" class="input-field" />
      </div>

      <div class="input-group">
        <label>전화번호</label>
        <input v-model="phone" type="tel" placeholder="전화번호를 입력하세요" class="input-field" />
      </div>

      <div class="input-group">
        <label>비밀번호</label>
        <input v-model="password" type="password" placeholder="비밀번호를 입력하세요" class="input-field" />
      </div>

      <div class="input-group">
        <label>비밀번호 확인</label>
        <input v-model="confirmPassword" type="password" placeholder="비밀번호를 다시 입력하세요" class="input-field" />
      </div>

      <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>

      <button @click="register" class="btn">회원가입</button>

      <p class="login-link">
        이미 계정이 있으신가요? <router-link to="/login">로그인</router-link>
      </p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const username = ref('');
const id = ref('');
const email = ref('');
const phone = ref('');
const password = ref('');
const confirmPassword = ref('');
const errorMessage = ref('');
const router = useRouter();

const register = async () => {
  errorMessage.value = '';

  // 필수 입력 체크
  if (!username.value) {
    errorMessage.value = "아이디를 입력해주세요.";
    return;
  }
  if (!email.value) {
    errorMessage.value = "이메일을 입력해주세요.";
    return;
  }
  if (!phone.value) {
    errorMessage.value = "전화번호를 입력해주세요.";
    return;
  }

  if (!password.value || !confirmPassword.value) {
    errorMessage.value = "비밀번호를 입력해주세요";
    return;
  }
  // 비밀번호 일치 확인
  if (password.value !== confirmPassword.value) {
    errorMessage.value = "비밀번호가 일치하지 않습니다.";
    return;
  }

  try {
    const response = await fetch('http://localhost:8080/api/auth/register', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        username: username.value,
        email: email.value,
        phone: phone.value,
        password: password.value,
        phone_number: phone.value,
        role: "USER"
      }),
      mode: 'cors'
    });

    if (response.ok) {
      alert('회원가입 성공!');
      router.push('/login');
    } else {
      const errorText = await response.text();
      errorMessage.value = errorText || '회원가입 실패';
    }
  } catch (error) {
    console.error('Error:', error);
    errorMessage.value = '서버 오류 발생';
  }
};
</script>

<style scoped>
.container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f4f6f9;
}

.register-box {
  background: white;
  padding: 40px;
  border-radius: 12px;
  box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
  text-align: center;
  width: 380px;
}

.input-group {
  text-align: left;
  margin-bottom: 15px;
}

.input-group label {
  font-size: 14px;
  font-weight: 600;
  color: #333;
  display: block;
  margin-bottom: 5px;
}

.input-field {
  width: 100%;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 16px;
  transition: border-color 0.3s;
}

.input-field:focus {
  border-color: #007bff;
  outline: none;
}

.error-message {
  color: red;
  font-size: 14px;
  margin-bottom: 10px;
}

.btn {
  width: 100%;
  padding: 14px;
  background: #28a745;
  color: white;
  font-size: 16px;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: background 0.3s;
  margin-top: 10px;
}

.btn:hover {
  background: #218838;
}

.login-link {
  margin-top: 15px;
  font-size: 14px;
  color: #555;
}

.login-link a {
  color: #007bff;
  text-decoration: none;
}

.login-link a:hover {
  text-decoration: underline;
}
</style>
