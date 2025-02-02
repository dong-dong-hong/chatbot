<template>
  <div class="chat-container">
    <div class="messages">
      <div v-for="msg in messages" :key="msg.id" :class="msg.sender">
        {{ msg.text }}
      </div>
    </div>
    <input v-model="newMessage" @keyup.enter="sendMessage" placeholder="Type a message..." />
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';

export default {
  setup() {
    const messages = ref([]);
    const newMessage = ref('');

    const fetchMessages = async () => {
      const response = await fetch('http://localhost:8080/api/chat/messages');
      messages.value = await response.json();
    };

    const sendMessage = async () => {
      if (newMessage.value.trim()) {
        await fetch('http://localhost:8080/api/chat/send', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify({ text: newMessage.value, sender: 'user' })
        });
        newMessage.value = '';
        fetchMessages();
      }
    };

    onMounted(fetchMessages);

    return { messages, newMessage, sendMessage };
  }
};
</script>

<style scoped>
.chat-container {
  display: flex;
  flex-direction: column;
  align-items: center;
}
.messages {
  width: 300px;
  height: 400px;
  overflow-y: scroll;
  border: 1px solid #ccc;
}
.user {
  text-align: right;
  color: blue;
}
.bot {
  text-align: left;
  color: green;
}
</style>
