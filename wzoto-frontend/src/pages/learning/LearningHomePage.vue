<template>
  <div class="page learning-home-page">
    <div class="page-header">
      <el-icon class="back-icon" @click="router.back()"><ArrowLeft /></el-icon>
      <h1>小学学习</h1>
    </div>

    <div class="child-selector">
      <el-select v-model="selectedChildId" placeholder="选择子女" style="width: 100%" @change="onChildChange">
        <el-option v-for="child in children" :key="child.id" :label="child.name" :value="child.id" />
      </el-select>
      <el-button text type="primary" @click="router.push('/learning/children')">管理</el-button>
    </div>

    <div v-if="selectedChild" class="card today-card">
      <div class="today-header">
        <div>
          <p class="child-name">{{ selectedChild.name }} 今日学习</p>
          <p class="task-progress">已完成 {{ completedCount }}/{{ todayTasks.length }} 项</p>
        </div>
        <el-progress type="circle" :percentage="todayProgress" :width="60" :stroke-width="6" />
      </div>
      <div class="today-tasks">
        <div v-for="task in todayTasks.slice(0, 3)" :key="task.id" class="task-item">
          <span class="task-icon">{{ taskIcon(task.taskType) }}</span>
          <span class="task-title">{{ task.title }}</span>
          <el-tag :type="task.status === 'COMPLETED' ? 'success' : 'info'" size="small">
            {{ task.statusDesc }}
          </el-tag>
        </div>
        <el-empty v-if="todayTasks.length === 0" description="今日暂无任务" />
      </div>
      <el-button text type="primary" style="margin-top: 8px" @click="router.push('/learning/tasks')">查看全部任务</el-button>
    </div>

    <div class="card quick-entry">
      <h2>快速入口</h2>
      <div class="entry-grid">
        <div class="entry-item" @click="router.push('/learning/plan')">
          <span class="icon">📅</span>
          <span>学习计划</span>
        </div>
        <div class="entry-item" @click="router.push('/learning/report')">
          <span class="icon">📊</span>
          <span>学情报告</span>
        </div>
        <div class="entry-item" @click="router.push('/learning/resources')">
          <span class="icon">📚</span>
          <span>学习资源</span>
        </div>
        <div class="entry-item" @click="router.push('/learning/wrong-questions')">
          <span class="icon">❌</span>
          <span>错题本</span>
        </div>
        <div class="entry-item" @click="router.push('/learning/control')">
          <span class="icon">⏱️</span>
          <span>时长管控</span>
        </div>
        <div class="entry-item" @click="router.push('/learning/achievements')">
          <span class="icon">🏆</span>
          <span>成长激励</span>
        </div>
        <div class="entry-item" @click="router.push('/learning/ai-answer')">
          <span class="icon">🤖</span>
          <span>AI 答疑</span>
        </div>
        <div class="entry-item" @click="router.push('/student/home')">
          <span class="icon">👧</span>
          <span>孩子学习</span>
        </div>
      </div>
    </div>

    <!-- AI提问记录摘要 -->
    <div class="card ai-summary-card">
      <h2>🤖 今日AI提问摘要</h2>
      <div v-if="aiQaSummary.length > 0" class="ai-summary-list">
        <div v-for="q in aiQaSummary" :key="q.id" class="ai-summary-item">
          <el-tag size="small">{{ subjectLabel(q.subject) }}</el-tag>
          <span class="q-text">{{ q.questionText }}</span>
          <span class="q-time">{{ formatTime(q.createdAt) }}</span>
        </div>
      </div>
      <el-empty v-else description="今日暂无AI提问" :image-size="40" />
    </div>

    <!-- AI学习计划预览 -->
    <div class="card ai-plan-card" v-if="aiPlan">
      <h2>🎯 AI学习计划</h2>
      <p class="plan-desc">{{ aiPlanSummary }}</p>
      <el-button text type="primary" size="small" @click="router.push('/learning/plan')">查看详情</el-button>
    </div>

    <!-- 学习时长统计 -->
    <div class="card duration-card">
      <h2>⏱️ 本周学习时长</h2>
      <div ref="durationChart" class="chart" />
    </div>

    <div v-if="weakPoints.length > 0" class="card weak-card">
      <h2>薄弱知识点</h2>
      <div v-for="wp in weakPoints.slice(0, 5)" :key="wp.knowledgePoint" class="weak-item">
        <span class="dot"></span>
        <span class="name">{{ wp.knowledgePoint }}</span>
        <span class="count">错 {{ wp.mistakeCount }} 次</span>
      </div>
      <el-button text type="primary" style="margin-top: 8px" @click="router.push('/learning/report')">查看完整报告</el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import { ArrowLeft } from '@element-plus/icons-vue'
import { getMyChildren } from '@/api/child'
import { listLearningTasks, getWeakPoints } from '@/api/learning'
import { getQaHistory, getTodayAiPlan } from '@/api/student'
import { trackPageView } from '@/utils/track'
import * as echarts from 'echarts'
import { subjectLabel } from '@/utils/format'

const router = useRouter()
const children = ref([])
const selectedChildId = ref(null)
const todayTasks = ref([])
const weakPoints = ref([])
const aiQaSummary = ref([])
const aiPlan = ref(null)
const durationChart = ref(null)

const selectedChild = computed(() => {
  return children.value.find(c => c.id === selectedChildId.value) || null
})

const completedCount = computed(() => {
  return todayTasks.value.filter(t => t.status === 'COMPLETED').length
})

const todayProgress = computed(() => {
  if (todayTasks.value.length === 0) return 0
  return Math.round((completedCount.value / todayTasks.value.length) * 100)
})

onMounted(() => {
  trackPageView('learning_home')
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

const aiPlanSummary = computed(() => {
  if (!aiPlan.value) return ''
  try {
    const content = JSON.parse(aiPlan.value.planContentJson)
    return content.summary || '已生成今日学习计划'
  } catch {
    return '已生成今日学习计划'
  }
})

async function onChildChange() {
  if (!selectedChildId.value) return
  loadTodayTasks()
  loadWeakPoints()
  loadAiQaSummary()
  loadAiPlan()
  loadDurationChart()
}

async function loadTodayTasks() {
  try {
    const today = new Date().toISOString().split('T')[0]
    const res = await listLearningTasks(selectedChildId.value, today)
    todayTasks.value = res.data || []
  } catch (e) {
    console.error(e)
  }
}

async function loadWeakPoints() {
  try {
    const res = await getWeakPoints(selectedChildId.value, null, 5)
    weakPoints.value = res.data || []
  } catch (e) {
    console.error(e)
  }
}

async function loadAiQaSummary() {
  try {
    const res = await getQaHistory(selectedChildId.value)
    const records = res.data || []
    const today = new Date().toISOString().split('T')[0]
    aiQaSummary.value = records.filter(r => (r.createdAt || '').startsWith(today)).slice(0, 3)
  } catch { aiQaSummary.value = [] }
}

async function loadAiPlan() {
  try {
    const res = await getTodayAiPlan(selectedChildId.value)
    aiPlan.value = res.data
  } catch { aiPlan.value = null }
}

function loadDurationChart() {
  nextTick(() => {
    if (!durationChart.value) return
    const chart = echarts.init(durationChart.value)
    const days = ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
    const mockData = days.map(() => Math.floor(Math.random() * 60 + 10))
    chart.setOption({
      grid: { top: 10, bottom: 30, left: 40, right: 10 },
      xAxis: { type: 'category', data: days },
      yAxis: { type: 'value', axisLabel: { formatter: '{value}分' } },
      series: [{ data: mockData, type: 'bar', itemStyle: { color: '#4F6EF7' } }],
    })
  })
}

function formatTime(t) {
  if (!t) return ''
  const d = new Date(t)
  return `${d.getHours().toString().padStart(2, '0')}:${d.getMinutes().toString().padStart(2, '0')}`
}

function taskIcon(type) {
  if (type?.includes('VIDEO')) return '📹'
  if (type?.includes('EXERCISE')) return '✏️'
  if (type?.includes('REVIEW')) return '🔄'
  return '📝'
}
</script>

<style lang="scss" scoped>
.learning-home-page {
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
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 16px;

  .el-select {
    flex: 1;
  }
}

.today-card {
  margin-bottom: 16px;

  .today-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 16px;

    .child-name {
      font-size: var(--font-size-lg);
      font-weight: 600;
    }

    .task-progress {
      font-size: var(--font-size-sm);
      color: var(--color-text-secondary);
      margin-top: 4px;
    }
  }

  .task-item {
    display: flex;
    align-items: center;
    gap: 12px;
    padding: 10px 0;
    border-bottom: 1px solid #f0f0f0;

    &:last-child {
      border-bottom: none;
    }

    .task-icon {
      font-size: 20px;
    }

    .task-title {
      flex: 1;
      font-size: var(--font-size-sm);
    }
  }
}

.quick-entry {
  margin-bottom: 16px;

  h2 {
    font-size: var(--font-size-lg);
    font-weight: 600;
    margin-bottom: 16px;
  }

  .entry-grid {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 12px;
  }

  .entry-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 6px;
    font-size: var(--font-size-xs);
    cursor: pointer;

    &:active {
      opacity: 0.85;
    }

    .icon {
      font-size: 28px;
    }
  }
}

.ai-summary-card {
  margin-bottom: 16px;
  h2 { font-size: var(--font-size-lg); font-weight: 600; margin-bottom: 12px; }
  .ai-summary-item {
    display: flex; align-items: center; gap: 8px;
    padding: 8px 0; border-bottom: 1px solid #f0f0f0;
    &:last-child { border-bottom: none; }
    .q-text { flex: 1; font-size: var(--font-size-sm); overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
    .q-time { font-size: var(--font-size-xs); color: var(--color-text-placeholder); }
  }
}

.ai-plan-card {
  margin-bottom: 16px;
  h2 { font-size: var(--font-size-lg); font-weight: 600; margin-bottom: 8px; }
  .plan-desc { font-size: var(--font-size-sm); color: var(--color-text-secondary); margin-bottom: 8px; }
}

.duration-card {
  margin-bottom: 16px;
  h2 { font-size: var(--font-size-lg); font-weight: 600; margin-bottom: 12px; }
  .chart { width: 100%; height: 180px; }
}

.weak-card {
  h2 {
    font-size: var(--font-size-lg);
    font-weight: 600;
    margin-bottom: 16px;
  }

  .weak-item {
    display: flex;
    align-items: center;
    gap: 8px;
    padding: 8px 0;
    font-size: var(--font-size-sm);

    .dot {
      width: 8px;
      height: 8px;
      border-radius: 50%;
      background: #ff4d4f;
    }

    .name {
      flex: 1;
    }

    .count {
      color: var(--color-text-secondary);
    }
  }
}
</style>
