<template>
  <div class="knowledge-radar" ref="chartRef" :style="{ width: '100%', height: height + 'px' }" />
</template>

<script setup>
import { ref, onMounted, watch, onBeforeUnmount, nextTick } from 'vue'
import * as echarts from 'echarts'

const props = defineProps({
  data: { type: Array, default: () => [] },
  height: { type: Number, default: 280 },
  maxScore: { type: Number, default: 100 },
})

const chartRef = ref(null)
let chart = null

function initChart() {
  if (!chartRef.value) return
  chart = echarts.init(chartRef.value)
  updateChart()
}

function updateChart() {
  if (!chart || !props.data || props.data.length === 0) return
  const indicators = props.data.map(item => ({
    name: item.name,
    max: props.maxScore,
  }))
  const values = props.data.map(item => item.value ?? item.score ?? 0)

  chart.setOption({
    radar: {
      indicator: indicators,
      radius: '65%',
      splitNumber: 4,
      axisName: {
        color: '#666',
        fontSize: 11,
      },
      splitArea: {
        areaStyle: { color: ['#f8f9fa', '#fff', '#f8f9fa', '#fff'] },
      },
    },
    series: [{
      type: 'radar',
      data: [{
        value: values,
        areaStyle: { color: 'rgba(79, 110, 247, 0.15)' },
        lineStyle: { color: '#4F6EF7', width: 2 },
        itemStyle: { color: '#4F6EF7' },
      }],
    }],
    tooltip: { trigger: 'item' },
  })
}

function handleResize() {
  chart?.resize()
}

onMounted(() => {
  nextTick(() => initChart())
  window.addEventListener('resize', handleResize)
})

watch(() => props.data, () => {
  updateChart()
}, { deep: true })

onBeforeUnmount(() => {
  window.removeEventListener('resize', handleResize)
  chart?.dispose()
})
</script>

<style lang="scss" scoped>
.knowledge-radar {
  min-height: 200px;
}
</style>
