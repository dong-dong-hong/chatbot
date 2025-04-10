<template>
  <div v-if="modal.visible" class="modal-backdrop">
    <div class="modal-box">
      <p class="modal-message">{{ modal.message }}</p>
      <div class="modal-buttons" :class="{ 'centered': modal.modalType === 'alert' }">
        <!-- 취소 버튼 (confirm 모드일 때만) -->
        <button v-if="modal.modalType === 'confirm'" class="modal-button cancel" @click="modal.hideModal">취소</button>
        <!-- 확인 버튼 -->
        <button class="modal-button confirm" @click="handleConfirm">확인</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useModalStore } from '@/stores/modal';
const modal = useModalStore();

const handleConfirm = () => {
  if (modal.modalType === 'confirm' && modal.confirmCallback) {
    modal.confirmCallback();
  }
  modal.hideModal();
};
</script>

<style scoped>
.modal-backdrop {
  position: fixed;
  top: 0; left: 0;
  width: 100vw;
  height: 100vh;
  background: rgba(0, 0, 0, 0.5);
  backdrop-filter: blur(2px);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-box {
  background: #ffffff;
  border-radius: 20px;
  box-shadow: 0 12px 32px rgba(0, 0, 0, 0.2);
  padding: 30px 24px;
  width: 400px;
  max-width: 90%;
  text-align: center;
  animation: fadeIn 0.3s ease;
}

.modal-message {
  font-size: 18px;
  color: #333;
  font-weight: 500;
  margin-bottom: 28px;
  line-height: 1.6;
}

.modal-buttons {
  display: flex;
  justify-content: center;
  gap: 14px;
}

.modal-button {
  font-size: 15px;
  font-weight: 600;
  padding: 12px 28px;
  border-radius: 10px;
  border: none;
  cursor: pointer;
  transition: all 0.25s ease;
}

.modal-button.confirm {
  background-color: #007bff;
  color: white;
}
.modal-button.confirm:hover {
  background-color: #0062cc;
}

.modal-button.cancel {
  background-color: #e74c3c;
  color: white;
}
.modal-button.cancel:hover {
  background-color: #c0392b;
}

@keyframes fadeIn {
  from {
    transform: scale(0.96);
    opacity: 0;
  }
  to {
    transform: scale(1);
    opacity: 1;
  }
}

</style>
