<template>
  <div class="container">
    <div class="register-box">
      <h2>회원가입</h2>
      <input v-model="username" placeholder="Username" class="input-field" />
      <input v-model="password" type="password" placeholder="Password" class="input-field" />
      <input v-model="confirmPassword" type="password" placeholder="Confirm Password" class="input-field" />
      <button @click="register" class="btn">회원가입</button>

      <router-link to="/login" class="login-link">이미 계정이 있으신가요? 로그인</router-link>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';

const username = ref('');
const password = ref('');
const confirmPassword = ref('');
const router = useRouter();

const register = async () => {
  try {
    const response = await fetch('http://localhost:8080/api/auth/register', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({ username: username.value, password: password.value }),
      mode: 'cors'
    });

    if (response.ok) {
      alert('회원가입 성공!');
      router.push('/login');
    } else {
      console.error('회원가입 실패', await response.text());
      alert('회원가입 실패');
    }
  } catch (error) {
    console.error('Error:', error);
  }
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

.register-box {
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
  background: #28a745;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  margin-top: 10px;
}

.login-link {
  display: block;
  margin-top: 15px;
  color: #007bff;
  text-decoration: none;
  font-size: 14px;
}
</style>
