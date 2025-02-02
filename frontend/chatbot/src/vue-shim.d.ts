declare module '*.vue' {
  import { DefineComponent } from 'vue'
  const component: DefineComponent<{}, {}, any>
  export default component
} // TypeScript가 .vue 파일을 모듈로 인식하지 못해서 발생하는 문제를 해결
