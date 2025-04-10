<template>
  <div class="container">
    <div class="box">
      <h3>비밀번호 찾기(임시 패스워드로 변경)</h3>
      <input v-model="username" placeholder="아이디 입력" class="input-field" />
      <input v-model="email" placeholder="가입한 이메일을 입력하세요" class="input-field" />
      <button @click="findPassword" class="btn">비밀번호 찾기</button>
      <router-link to="/" class="link">로그인으로 돌아가기</router-link>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useModalStore } from '@/stores/modal';
import { useRouter } from 'vue-router'
const modal = useModalStore();
const username = ref('');
const email = ref('');
const router = useRouter();

const findPassword = async () => {
  const response = await fetch('http://localhost:8080/auth/find-password', {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({ username: username.value.trim(), email: email.value.trim() })
  });

  if (response.ok) {
    const data = await response.json();
    console.log(data);
    // alert('입력한 이메일로 비밀번호 재설정 링크가 발송되었습니다.');
    modal.showModal(`${data.tempPassword} 입니다.`); // 원래는 운영에서는 이메일에다가 발송해야하지만 임의로 모달에다가 임시 비밀번호 전달
    router.push('/login');
  } else {
    modal.showModal('일치하는 계정을 찾을 수 없습니다.');
    // alert('일치하는 계정을 찾을 수 없습니다.');
  }
};
</script>

<style scoped>
.container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background: linear-gradient(to right, #d7e1ec, #ffffff);
}
.box {
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

.link {
  display: block;
  margin-top: 15px;
  color: #007bff;
  text-decoration: none;
  font-size: 14px;
}

.link:hover {
  color: #0056b3;
  text-decoration: underline;
}
</style>
