<template>
  <div v-if="modal.visible" class="modal-backdrop">
    <div class="modal-box">
      <p class="modal-message">{{ modal.message }}</p>
      <button class="modal-button" @click="modal.hideModal">확인</button>
    </div>
  </div>
</template>

<script setup>
import { useModalStore } from '@/stores/modal';
const modal = useModalStore();
</script>

<style scoped>
.modal-backdrop {
  position: fixed;
  top: 0; left: 0;
  width: 100%; height: 100%;
  background: rgba(0,0,0,0.4);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 999;
}

.modal-box {
  background: #fff;
  width: 320px;
  max-width: 90%;
  padding: 24px 20px;
  border-radius: 16px;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.15);
  text-align: center;
  animation: fadeIn 0.3s ease;
}

.modal-message {
  font-size: 16px;
  margin-bottom: 20px;
  color: #333;
  line-height: 1.5;
}

.modal-button {
  background-color: #2196F3;
  color: white;
  border: none;
  padding: 10px 20px;
  border-radius: 8px;
  font-size: 14px;
  cursor: pointer;
  transition: background 0.3s ease;
}

.modal-button:hover {
  background-color: #1976D2;
}

@keyframes fadeIn {
  from {
    transform: scale(0.95);
    opacity: 0;
  }
  to {
    transform: scale(1);
    opacity: 1;
  }
}
</style>
