<template>
  <div class="container">
    <div class="box">
      <h2>아이디 찾기</h2>
      <input v-model="email" placeholder="가입한 이메일을 입력하세요" class="input-field" />
      <button @click="findUsername" class="btn">아이디 찾기</button>
      <router-link to="/" class="link">로그인으로 돌아가기</router-link>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { useModalStore } from '@/stores/modal';
const modal = useModalStore();
const email = ref('');

const findUsername = async () => {
  const response = await fetch(`http://localhost:8080/auth/find-username?email=${email.value.trim()}`);

  console.log('response: ',response);

  if (response.ok) {
    const data = await response.json();
    // alert(`아이디: ${data.username}`);
    modal.showModal(`아이디는 ${data.username} 입니다.`)
  } else {
    // alert('가입된 이메일이 없습니다.');
    modal.showModal('가입된 이메일이 없습니다.');
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
