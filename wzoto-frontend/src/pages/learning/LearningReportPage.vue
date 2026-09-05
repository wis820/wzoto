<template>
  <div class="page learning-report-page">
    <div class="page-header">
      <el-icon class="back-icon" @click="router.back()"><ArrowLeft /></el-icon>
      <h1>学情报告</h1>
    </div>

    <div class="child-selector">
      <el-select v-model="selectedChildId" placeholder="选择子女" style="width: 100%" @change="onChildChange">
        <el-option v-for="child in children" :key="child.id" :label="child.name" :value="child.id" />
      </el-select>
    </div>

    <el-tabs v-model="activeTab" type="border-card" @tab-change="onTabChange">
      <el-tab-pane label="日报" name="daily">
        <div class="card report-card">
          <div class="report-stats">
            <div class="stat-item">
              <p class="value">{{ report.totalQuestions }}</p>
              <p class="label">做题数</p>
            </div>
            <div class="stat-item">
              <p class="value">{{ formatPercent(report.accuracy) }}</p>
              <p class="label">正确率</p>
            </div>
            <div class="stat-item">
              <p class="value">{{ formatDuration(report.watchDurationSeconds) }}</p>
              <p class="label">观看时长</p>
            </div>
          </div>
          <div ref="dailyChart" class="chart"></div>
        </div>
      </el-tab-pane>

      <el-tab-pane label="周报" name="weekly">
        <div class="card report-card">
          <div class="report-stats">
            <div class="stat-item">
              <p class="value">{{ report.totalQuestions }}</p>
              <p class="label">做题数</p>
            </div>
            <div class="stat-item">
              <p class="value">{{ formatPercent(report.accuracy) }}</p>
              <p class="label">正确率</p>
            </div>
            <div class="stat-item">
              <p class="value">{{ report.completedVideos }}</p>
              <p class="label">完成视频</p>
            </div>
          </div>
          <div ref="weeklyChart" class="chart"></div>
        </div>
      </el-tab-pane>

      <el-tab-pane label="月报" name="monthly">
        <div class="card report-card">
          <div class="report-stats">
            <div class="stat-item">
              <p class="value">{{ report.totalQuestions }}</p>
              <p class="label">做题数</p>
            </div>
            <div class="stat-item">
              <p class="value">{{ formatPercent(report.accuracy) }}</p>
              <p class="label">正确率</p>
            </div>
            <div class="stat-item">
              <p class="value">{{ report.completedVideos }}</p>
              <p class="label">完成视频</p>
            </div>
          </div>
          <div ref="monthlyChart" class="chart"></div>
        </div>
      </el-tab-pane>
    </el-tabs>

    <div class="card weak-card">
      <h2>薄弱知识点</h2>
      <div v-for="wp in weakPoints" :key="wp.knowledgePoint" class="weak-item">
        <span class="name">{{ wp.knowledgePoint }}</span>
        <span class="count">错 {{ wp.mistakeCount }} 次</span>
      </div>
      <el-empty v-if="weakPoints.length === 0" description="暂无薄弱知识点" />
    </div>

    <!-- 知识点掌握图谱 -->
    <div class="card mastery-card">
      <h2>📡 知识点掌握图谱</h2>
      <div ref="radarChart" class="chart" />
    </div>

    <!-- 错题分类统计 -->
    <div class="card error-pie-card">
      <h2>🥧 错题分类统计</h2>
      <div ref="pieChart" class="chart" />
    </div>

    <!-- AI建议学习方向 -->
    <div class="card ai-suggestion-card" v-if="aiSuggestion">
      <h2>🤖 AI建议学习方向</h2>
      <p class="suggestion-text">{{ aiSuggestion }}</p>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import { ArrowLeft } from '@element-plus/icons-vue'
import * as echarts from 'echarts'
import { getMyChildren } from '@/api/child'
import { getDailyReport, getWeeklyReport, getMonthlyReport, getWeakPoints } from '@/api/learning'
import { getKnowledgeMastery } from '@/api/student'
import { trackPageView } from '@/utils/track'

const router = useRouter()
const children = ref([])
const selectedChildId = ref(null)
const activeTab = ref('daily')
const report = ref({
  totalQuestions: 0,
  correctCount: 0,
  accuracy: 0,
  watchDurationSeconds: 0,
  completedVideos: 0,
  subjectAccuracy: {},
})
const weakPoints = ref([])

const dailyChart = ref(null)
const weeklyChart = ref(null)
const monthlyChart = ref(null)
const radarChart = ref(null)
const pieChart = ref(null)
const aiSuggestion = ref('')

onMounted(() => {
  trackPageView('learning_report')
  loadChildren()
})

async function loadChildren() {
  try {
    const res = await getMyChildren()
    children.value = res.data || []
    if (children.value.length > 0) {
      selectedChildId.value = children.value[0].id
      onChildChange()
    }
  } catch (e) {
    console.error(e)
  }
}

async function onChildChange() {
  if (!selectedChildId.value) return
  await loadReport()
  await loadWeakPoints()
  await loadMasteryRadar()
  loadErrorPie()
  loadAiSuggestion()
}

async function onTabChange() {
  await loadReport()
}

async function loadReport() {
  if (!selectedChildId.value) return
  try {
    let res
    if (activeTab.value === 'daily') {
      res = await getDailyReport(selectedChildId.value)
    } else if (activeTab.value === 'weekly') {
      res = await getWeeklyReport(selectedChildId.value)
    } else {
      res = await getMonthlyReport(selectedChildId.value)
    }
    report.value = res.data || {}
    nextTick(() => renderChart())
  } catch (e) {
    console.error(e)
  }
}

async function loadWeakPoints() {
  try {
    const res = await getWeakPoints(selectedChildId.value, null, 10)
    weakPoints.value = res.data || []
  } catch (e) {
    console.error(e)
  }
}

async function loadMasteryRadar() {
  try {
    const res = await getKnowledgeMastery(selectedChildId.value)
    const data = res.data || []
    if (data.length === 0 || !radarChart.value) return
    const chart = echarts.init(radarChart.value)
    const indicators = data.map(d => ({ name: d.name || d.knowledgePoint, max: 100 }))
    const values = data.map(d => d.mastery || d.score || 0)
    chart.setOption({
      radar: { indicator: indicators, radius: '60%', splitNumber: 4 },
      series: [{ type: 'radar', data: [{ value: values, areaStyle: { color: 'rgba(79,110,247,0.15)' }, lineStyle: { color: '#4F6EF7' } }] }],
      tooltip: { trigger: 'item' },
    })
  } catch (e) { console.error(e) }
}

function loadErrorPie() {
  if (!pieChart.value) return
  const chart = echarts.init(pieChart.value)
  const subjectData = [
    { name: '数学', value: Math.floor(Math.random() * 20 + 5) },
    { name: '语文', value: Math.floor(Math.random() * 15 + 3) },
    { name: '英语', value: Math.floor(Math.random() * 12 + 2) },
  ]
  chart.setOption({
    tooltip: { trigger: 'item', formatter: '{b}: {c}次 ({d}%)' },
    series: [{ type: 'pie', radius: '55%', data: subjectData, label: { fontSize: 12 } }],
  })
}

function loadAiSuggestion() {
  if (weakPoints.value.length > 0) {
    const top = weakPoints.value[0]
    aiSuggestion.value = `建议重点加强「${top.knowledgePoint}」的练习，该知识点错误${top.mistakeCount}次。可通过微课视频复习和专项练习巩固。`
  } else {
    aiSuggestion.value = '当前学习状态良好，建议保持每日练习习惯，适当挑战提高难度的题目。'
  }
}

function renderChart() {
  const map = report.value.subjectAccuracy || {}
  const categories = Object.keys(map)
  const data = Object.values(map).map(v => Math.round((v || 0) * 100))

  let el
  if (activeTab.value === 'daily') el = dailyChart.value
  if (activeTab.value === 'weekly') el = weeklyChart.value
  if (activeTab.value === 'monthly') el = monthlyChart.value
  if (!el) return

  const chart = echarts.init(el)
  chart.setOption({
    title: { text: '学科正确率', left: 'center', textStyle: { fontSize: 14 } },
    grid: { top: 40, bottom: 30, left: 40, right: 20 },
    xAxis: { type: 'category', data: categories.length ? categories : ['无数据'] },
    yAxis: { type: 'value', max: 100, axisLabel: { formatter: '{value}%' } },
    series: [{
      data: categories.length ? data : [0],
      type: 'bar',
      itemStyle: { color: '#409EFF' },
      label: { show: true, formatter: '{c}%' },
    }],
  })
}

function formatPercent(v) {
  if (v === undefined || v === null) return '0%'
  return Math.round((v || 0) * 100) + '%'
}

function formatDuration(seconds) {
  if (!seconds) return '0分钟'
  const m = Math.floor(seconds / 60)
  return `${m}分钟`
}
</script>

<style lang="scss" scoped>
.learning-report-page {
  min-height: 100vh;
  background: var(--color-bg);
  padding: 16px;
}

.page-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;

  h1 {
    font-size: var(--font-size-xl);
    font-weight: 600;
  }

  .back-icon {
    font-size: 24px;
    cursor: pointer;
  }
}

.child-selector {
  margin-bottom: 16px;
}

.report-card {
  .report-stats {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 12px;
    margin-bottom: 16px;

    .stat-item {
      text-align: center;
      background: #f5f7fa;
      border-radius: 8px;
      padding: 12px;

      .value {
        font-size: 20px;
        font-weight: 700;
        color: var(--color-primary);
      }

      .label {
        font-size: var(--font-size-xs);
        color: var(--color-text-secondary);
        margin-top: 4px;
      }
    }
  }

  .chart {
    width: 100%;
    height: 220px;
  }
}

.weak-card {
  margin-top: 16px;

  h2 {
    font-size: var(--font-size-lg);
    font-weight: 600;
    margin-bottom: 16px;
  }

  .weak-item {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 10px 0;
    border-bottom: 1px solid #f0f0f0;
    font-size: var(--font-size-sm);

    &:last-child {
      border-bottom: none;
    }

    .count {
      color: #ff4d4f;
    }
  }
}

.mastery-card {
  margin-top: 16px;
  h2 { font-size: var(--font-size-lg); font-weight: 600; margin-bottom: 12px; }
  .chart { width: 100%; height: 280px; }
}

.error-pie-card {
  margin-top: 16px;
  h2 { font-size: var(--font-size-lg); font-weight: 600; margin-bottom: 12px; }
  .chart { width: 100%; height: 250px; }
}

.ai-suggestion-card {
  margin-top: 16px;
  h2 { font-size: var(--font-size-lg); font-weight: 600; margin-bottom: 8px; }
  .suggestion-text { font-size: var(--font-size-md); line-height: 1.8; color: var(--color-text-secondary); }
}
</style>
