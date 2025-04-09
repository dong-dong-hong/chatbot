import { createRouter, createWebHistory } from 'vue-router'
import LoginPage from '@/views/LoginPage.vue'
import RegisterPage from '@/views/RegisterPage.vue'
import ChatPage from '@/views/chatPage.vue'
import FindUsername from '@/views/FindUsername.vue';
import FindPassword from '@/views/FindPassword.vue';
import ChangePassword from '@/views/ChangePassword.vue'

const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', component: LoginPage },
  { path: '/register', component: RegisterPage },
  { path: '/chatbot', component: ChatPage },
  { path: '/change-password', component: ChangePassword },
  { path: '/find-username', component: FindUsername },
  { path: '/find-password', component: FindPassword }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
