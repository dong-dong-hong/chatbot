import { defineStore } from 'pinia';

export const useModalStore = defineStore('modal', {
  state: () => ({
    visible: false,
    message: '',
  }),
  actions: {
    showModal(message) {
      this.message = message;
      this.visible = true;
    },
    hideModal() {
      this.visible = false;
      this.message = '';
    }
  },
});
