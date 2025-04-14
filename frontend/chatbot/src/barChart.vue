<template>
  <canvas ref="chartRef"></canvas>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue';
import { Chart } from 'chart.js/auto';

const props = defineProps({ data: Array });
const chartRef = ref(null);
let chartInstance = null;

const renderChart = () => {
  const labels = props.data.map(item => item.date);
  const counts = props.data.map(item => item.count);

  if (chartInstance) chartInstance.destroy();

  chartInstance = new Chart(chartRef.value, {
    type: 'bar',
    data: {
      labels,
      datasets: [{
        label: '메시지 수',
        data: counts,
        backgroundColor: '#409EFF',
      }]
    },
    options: {
      responsive: true,
      scales: {
        y: { beginAtZero: true }
      }
    }
  });
};

onMounted(renderChart);
watch(() => props.data, renderChart);
</script>
