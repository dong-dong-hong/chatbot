import { createRouter, createWebHistory } from 'vue-router'
import LoginPage from '@/views/LoginPage.vue'
import RegisterPage from '@/views/RegisterPage.vue'
import ChatPage from '@/views/ChatPage.vue'
import FindUsername from '@/views/FindUsername.vue';
import FindPassword from '@/views/FindPassword.vue';

const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', component: LoginPage },
  { path: '/register', component: RegisterPage },
  { path: '/chatbot', component: ChatPage },
  { path: '/find-username', component: FindUsername },
  { path: '/find-password', component: FindPassword }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
