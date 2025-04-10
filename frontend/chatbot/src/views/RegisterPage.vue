<template>
  <div class="container">
    <div class="register-box">
      <h2>회원가입</h2>

      <div class="input-group">
        <label>아이디</label>
        <div class="input-with-button">
          <input v-model="username" placeholder="아이디를 입력하세요." class="input-field" />
          <button class="check-btn" @click="checkUsername">중복확인</button>
        </div>
        <p v-if="usernameMessage" :class="{ 'success-message': isUsernameAvailable, 'error-message': !isUsernameAvailable }">
          {{ usernameMessage }}
        </p>
      </div>

      <div class="input-group">
        <label>이메일</label>
        <div class="email-wrapper">
          <input v-model="emailId" type="text" placeholder="이메일을 입력하세요." class="input-field email-id" />
          <span class="at">@</span>

          <div class="email-domain">

            <div v-if="!isCustomDomain" style="display: flex; align-items: center; gap: 8px;">
              <select
                v-model="emailDomain"
                class="input-field email-select"
                @change="handleDomainChange"
              >
                <option disabled value="">선택</option>
                <option value="gmail.com">gmail.com</option>
                <option value="naver.com">naver.com</option>
                <option value="daum.net">daum.net</option>
                <option value="custom">직접 입력</option>
              </select>
            </div>

            <div v-else style="display: flex; align-items: center; gap: 0px;">
              <input
                v-model="customDomain"
                type="text"
                placeholder="도메인 입력"
                class="input-field email-select"
                style="border-top-right-radius: 0; border-bottom-right-radius: 0;"
              />
              <button class="dropdown-btn" @click="resetToSelect">
                <span class="arrow-down"></span>
              </button>
            </div>
          </div>
        </div>
      </div>

      <div class="input-group">
        <label>전화번호</label>
        <input v-model="phone" type="tel" placeholder="전화번호를 입력하세요." class="input-field" @input="formatPhoneNumber" />
      </div>

      <div class="input-group">
        <label>비밀번호</label>
        <input v-model="password" type="password" placeholder="비밀번호를 입력하세요." class="input-field" />
      </div>

      <div class="input-group">
        <label>비밀번호 확인</label>
        <input v-model="confirmPassword" type="password" placeholder="비밀번호를 다시 입력하세요." class="input-field" />
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
import { ref, watch } from 'vue';
import { useRouter } from 'vue-router';
import { useModalStore } from '@/stores/modal.js'

const username = ref('');
const emailId = ref('');
const emailDomain = ref('');
const customDomain = ref('');
const isCustomDomain = ref(false);
const email = ref('');
const phone = ref('');
const password = ref('');
const confirmPassword = ref('');
const errorMessage = ref('');
const usernameMessage = ref('');
const isUsernameAvailable = ref(false);
const router = useRouter();

const modal = useModalStore();

// 도메인 선택 시
const handleDomainChange = () => {
  if (emailDomain.value === 'custom') {
    isCustomDomain.value = true;
    customDomain.value = '';
  } else {
    isCustomDomain.value = false;
  }
};

// 다시 select로 전환
const resetToSelect = () => {
  isCustomDomain.value = false;
  emailDomain.value = '';
  customDomain.value = '';
};

// 이메일 조합
watch([emailId, emailDomain, customDomain, isCustomDomain], () => {
  const domainToUse = isCustomDomain.value ? customDomain.value : emailDomain.value;
  if (emailId.value && domainToUse) {
    email.value = `${emailId.value}@${domainToUse}`;
  } else {
    email.value = '';
  }
});

// 전화번호 하이픈 처리
const formatPhoneNumber = () => {
  let raw = phone.value.replace(/\D/g, '');
  if (raw.length <= 3) {
    phone.value = raw;
  } else if (raw.length <= 7) {
    phone.value = `${raw.slice(0, 3)}-${raw.slice(3)}`;
  } else {
    phone.value = `${raw.slice(0, 3)}-${raw.slice(3, 7)}-${raw.slice(7, 11)}`;
  }
};

// 아이디 중복 확인
const checkUsername = async () => {
  if (!username.value) {
    usernameMessage.value = '아이디를 입력해주세요.';
    isUsernameAvailable.value = false;
    return;
  }

  try {
    const res = await fetch(`http://localhost:8080/api/auth/check-username?username=${username.value}`);
    const result = await res.json();
    if (res.ok && result.available) {
      usernameMessage.value = '사용 가능한 아이디입니다.';
      isUsernameAvailable.value = true;
    } else {
      usernameMessage.value = '이미 사용 중인 아이디입니다.';
      isUsernameAvailable.value = false;
    }
  } catch (e) {
    usernameMessage.value = '중복 확인 중 오류가 발생했습니다.';
    isUsernameAvailable.value = false;
  }
};

// 회원가입
const register = async () => {
  errorMessage.value = '';

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
  const correctPhone = phone.value.replace(/\D/g, '');
  if (!/^01[0-9]{8,9}$/.test(correctPhone)) {
    errorMessage.value = "유효한 전화번호 형식이 아닙니다. 다시 입력하세요 예) 010-1234-5678";
    return;
  }
  if (!password.value || !confirmPassword.value) {
    errorMessage.value = "비밀번호를 입력해주세요";
    return;
  }
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
      modal.showModal('회원가입 성공!');
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

.success-message {
  color: green;
  font-size: 14px;
  margin-top: 5px;
}

.btn {
  width: 100%;
  padding: 14px;
  background: #007bff;
  color: white;
  font-size: 16px;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: background 0.3s;
  margin-top: 10px;
}

.btn:hover {
  background: #0056b3;
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

.input-with-button {
  display: flex;
  gap: 10px;
  align-items: center;
}

.check-btn {
  padding: 10px;
  font-size: 14px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  white-space: nowrap;
}

.check-btn:hover {
  background-color: #0056b3;
}

.email-wrapper {
  display: flex;
  align-items: center;
  gap: 8px;
  flex-wrap: nowrap;
}

.email-id {
  flex: 1;
}

.email-select {
  width: 160px;
  padding: 12px;
  font-size: 16px;
  border: 1px solid #ddd;
  border-radius: 8px;
  box-sizing: border-box;
  height: 45px;
}

.at {
  font-size: 16px;
  color: #333;
}

.email-domain {
  display: flex;
  flex-direction: column;
}

.dropdown-btn {

  border: 1px solid #ddd;
  border-left: none;
  border-radius: 0 8px 8px 0;
  width: 20px;
  height: 45px;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
}

.dropdown-btn:hover {
  background-color: #e0e0e0;
}

.arrow-down {
  display: inline-block;
  width: 0;
  height: 0;
  border-left: 6px solid transparent;
  border-right: 6px solid transparent;
  border-top: 6px solid #333;
}
</style>
