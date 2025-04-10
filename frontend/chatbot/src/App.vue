<template>
  <div>
    <CommonHeader v-if="shouldShowHeader" />
    <router-view />
    <ModalComponent />
  </div>
</template>

<script setup>
import { computed, ref, provide } from "vue";
import { useRoute } from "vue-router";
import CommonHeader from "@/components/common/CommonHeader.vue";
import ModalComponent from '@/components/common/CommonModal.vue'

const route = useRoute();
const hiddenRoutes = ["/login", "/register", "/find-username", "/find-password", "/change-password"];

const shouldShowHeader = computed(() => !hiddenRoutes.includes(route.path));

</script>

<style scoped>
</style>
