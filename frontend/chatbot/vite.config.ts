import { fileURLToPath, URL } from 'node:url'
import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  define: {
    global: 'window'
  }
  // vite.config.js에서 define 옵션을 사용해 global 객체를 명시적으로 설정하지 않았기 때문에 벌어짐.
})
