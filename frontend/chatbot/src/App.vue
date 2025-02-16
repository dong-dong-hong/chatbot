<template>
  <div>
    <CommonHeader v-if="shouldShowHeader" />
    <router-view />
  </div>
</template>

<script setup>
import { computed } from "vue";
import { useRoute } from "vue-router";
import CommonHeader from "@/components/CommonHeader.vue";

const route = useRoute();

const hiddenRoutes = ["/login", "/register", "/find-username", "/find-password"];
const shouldShowHeader = computed(() => !hiddenRoutes.includes(route.path));
</script>

<style scoped>
</style>
