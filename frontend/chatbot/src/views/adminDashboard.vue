<template>
  <div class="admin-dashboard">
    <el-row :gutter="20" class="stat-cards">
      <el-col :span="8"><el-card shadow="hover">총 메시지 수: {{ stats.totalMessages }}</el-card></el-col>
      <el-col :span="8"><el-card shadow="hover">사용자 수: {{ stats.uniqueUsers }}</el-card></el-col>
      <el-col :span="8"><el-card shadow="hover">봇 메시지 수: {{ stats.botMessages }}</el-card></el-col>
    </el-row>

    <el-divider content-position="left">일자별 메시지 수</el-divider>
    <BarChart :data="messageCountByDate" />

    <el-divider content-position="left">최근 메시지</el-divider>
    <el-table :data="recentMessages" stripe border style="width: 100%">
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="text" label="내용" />
      <el-table-column prop="sender" label="보낸 아이디" width="120" />
<!--      <el-table-column prop="receiver" label="받은 챗봇" width="120" />-->
      <el-table-column prop="createdAt" label="보낸 시간" width="180" :formatter="formatDate" />
    </el-table>

    <el-divider content-position="left">전체 메시지 목록</el-divider>
    <el-table :data="allMessages" stripe border height="400" style="width: 100%">
      <el-table-column prop="id" label="ID" width="60" />
      <el-table-column prop="text" label="내용" />
      <el-table-column prop="sender" label="보낸 아이디" width="120" />
<!--      <el-table-column prop="receiver" label="받은 챗봇" width="120" />-->
      <el-table-column prop="createdAt" label="보낸 시간" width="180" :formatter="formatDate" />
    </el-table>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import BarChart from '../barChart.vue';

const stats = ref({});
const messageCountByDate = ref([]);
const recentMessages = ref([]);
const allMessages = ref([]);

const formatDate = (row, column, cellValue) => {
  if (!cellValue) return '-';
  return new Date(cellValue).toLocaleString();
};

onMounted(async () => {
  stats.value = await (await fetch('/api/admin/stats')).json();
  console.log("stats.value: ",stats.value);
  messageCountByDate.value = await (await fetch('/api/admin/messages/daily-count')).json();
  console.log("messageCountByDate.value: ",messageCountByDate.value);
  recentMessages.value = await (await fetch('/api/admin/messages/recent')).json();
  console.log("messageCountByDate.value: ",messageCountByDate.value);
  allMessages.value = await (await fetch('/api/admin/messages')).json();
  console.log("allMessages.value: ",allMessages.value);
});
</script>

<style scoped>
.admin-dashboard {
  padding: 30px;
}
.stat-cards {
  margin-bottom: 30px;
}
</style>
