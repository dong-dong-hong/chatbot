<template>

    <button @click="logout" class="logout-btn">🚪 로그아웃</button>

</template>

<script setup>
import { useRouter } from "vue-router";

const router = useRouter();

const logout = () => {
  localStorage.removeItem("token");
  router.push("/login");
};
</script>

<style scoped>

.logout-btn {
  background: #e74c3c;
  border: none;
  padding: 8px 15px;
  color: white;
  font-size: 14px;
  border-radius: 5px;
  cursor: pointer;
  transition: background 0.3s;
}

.logout-btn:hover {
  background: #c0392b;
}
</style>
