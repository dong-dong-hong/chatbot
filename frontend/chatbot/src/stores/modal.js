import { defineStore } from 'pinia';

export const useModalStore = defineStore('modal', {
  state: () => ({
    visible: false,
    message: '',
    confirmCallback: null,
    modalType: 'alert',
  }),
  actions: {
    showModal(message) {
      this.message = message;
      this.modalType = 'alert';
      this.visible = true;
      this.confirmCallback = null;
    },
    showConfirm(message, callback) {
      this.message = message;
      this.modalType = 'confirm';
      this.confirmCallback = callback;
      this.visible = true;
    },
    hideModal() {
      this.visible = false;
      this.message = '';
      this.confirmCallback = null;
      this.modalType = 'alert';
    }
  }
});
