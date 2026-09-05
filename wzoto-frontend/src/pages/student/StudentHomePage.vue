<template>
  <div class="page student-home-page">
    <div class="page-header">
      <h1>📚 学习中心</h1>
      <div class="child-switch" v-if="children.length > 1">
        <el-select v-model="selectedChildId" size="small" @change="onChildChange">
          <el-option v-for="c in children" :key="c.id" :label="c.name" :value="c.id" />
        </el-select>
      </div>
    </div>

    <!-- 今日AI学习计划 -->
    <div class="card plan-card" v-if="todayPlan">
      <div class="plan-header">
        <span class="plan-icon">🎯</span>
        <h3>今日AI学习计划</h3>
      </div>
      <div class="plan-preview">
        <p class="plan-desc">{{ planSummary }}</p>
      </div>
      <el-button text type="primary" size="small" @click="router.push('/student/exercise')">开始学习 →</el-button>
    </div>
    <div class="card plan-card" v-else @click="generatePlan">
      <div class="plan-header">
        <span class="plan-icon">✨</span>
        <h3>生成AI学习计划</h3>
      </div>
      <p class="plan-desc">AI根据学情为你定制今日学习安排</p>
      <el-button type="primary" size="small" :loading="generating">立即生成</el-button>
    </div>

    <!-- 每日任务进度 -->
    <div class="card progress-card">
      <h3>📊 学习进度</h3>
      <div class="progress-ring">
        <el-progress type="circle" :percentage="taskProgress" :width="80" :stroke-width="6" />
        <span class="progress-label">{{ completedTasks }}/{{ totalTasks }} 任务</span>
      </div>
    </div>

    <!-- 快捷入口 -->
    <div class="quick-entries">
      <div class="entry-item" @click="router.push('/student/exercise')">
        <span class="entry-icon">✏️</span>
        <span class="entry-label">专项练习</span>
      </div>
      <div class="entry-item" @click="router.push('/student/ai-qa')">
        <span class="entry-icon">🤖</span>
        <span class="entry-label">AI答疑</span>
      </div>
      <div class="entry-item" @click="router.push('/student/oral')">
        <span class="entry-icon">🎤</span>
        <span class="entry-label">口语跟读</span>
      </div>
      <div class="entry-item" @click="router.push('/student/composition')">
        <span class="entry-icon">📝</span>
        <span class="entry-label">作文批改</span>
      </div>
    </div>

    <!-- 推荐微课 -->
    <div class="section-title">🎬 推荐微课</div>
    <div class="course-scroll">
      <div class="course-item card" v-for="course in recommendCourses" :key="course.id" @click="router.push(`/student/courses?highlight=${course.id}`)">
        <div class="course-cover" :style="{ backgroundImage: `url(${course.coverUrl || ''})` }">
          <span class="duration">{{ formatDuration(course.durationSeconds) }}</span>
        </div>
        <div class="course-info">
          <p class="course-title">{{ course.title }}</p>
          <p class="course-subject">{{ subjectLabel(course.subject) }}</p>
        </div>
        <el-tag v-if="course.vipOnly" type="warning" size="small">VIP</el-tag>
      </div>
      <el-empty v-if="recommendCourses.length === 0" description="暂无推荐" :image-size="60" />
    </div>

    <!-- 薄弱知识点提示 -->
    <div class="section-title" v-if="weakPoints.length > 0">⚡ 薄弱知识点</div>
    <div class="weak-list" v-if="weakPoints.length > 0">
      <div class="weak-item" v-for="wp in weakPoints" :key="wp.name">
        <span class="weak-name">{{ wp.name }}</span>
        <el-progress :percentage="wp.mastery || 0" :stroke-width="4" :show-text="false" status="warning" />
      </div>
    </div>

    <!-- 底部导航 -->
    <div class="bottom-nav">
      <div class="nav-item active" @click="router.push('/student/home')">
        <span class="nav-icon">🏠</span><span>首页</span>
      </div>
      <div class="nav-item" @click="router.push('/student/courses')">
        <span class="nav-icon">📖</span><span>课程</span>
      </div>
      <div class="nav-item" @click="router.push('/student/wrong-book')">
        <span class="nav-icon">📕</span><span>错题本</span>
      </div>
      <div class="nav-item" @click="router.push('/learning')">
        <span class="nav-icon">👨‍👩‍👧</span><span>家长端</span>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { getMyChildren } from '@/api/child'
import { getTodayAiPlan, generateAiPlan, getRecommendedCourses, getKnowledgeMastery } from '@/api/student'
import { listLearningTasks } from '@/api/learning'
import { showToast } from '@/utils/toast'
import { subjectLabel } from '@/utils/format'

const router = useRouter()
const children = ref([])
const selectedChildId = ref(null)
const todayPlan = ref(null)
const generating = ref(false)
const recommendCourses = ref([])
const weakPoints = ref([])
const completedTasks = ref(0)
const totalTasks = ref(0)

const taskProgress = computed(() => {
  if (totalTasks.value === 0) return 0
  return Math.round((completedTasks.value / totalTasks.value) * 100)
})

const planSummary = computed(() => {
  if (!todayPlan.value) return ''
  try {
    const content = JSON.parse(todayPlan.value.planContentJson)
    return content.summary || '已为你生成今日学习计划'
  } catch {
    return '已为你生成今日学习计划'
  }
})

onMounted(() => {
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
  await Promise.all([loadTodayPlan(), loadRecommend(), loadWeakPoints(), loadTasks()])
}

async function loadTodayPlan() {
  try {
    const res = await getTodayAiPlan(selectedChildId.value)
    todayPlan.value = res.data
  } catch { todayPlan.value = null }
}

async function generatePlan() {
  generating.value = true
  try {
    const res = await generateAiPlan(selectedChildId.value)
    todayPlan.value = res.data
    showToast('AI学习计划已生成')
  } catch (e) {
    showToast(e.message || '生成失败')
  } finally {
    generating.value = false
  }
}

async function loadRecommend() {
  try {
    const res = await getRecommendedCourses(selectedChildId.value)
    recommendCourses.value = res.data || []
  } catch { recommendCourses.value = [] }
}

async function loadWeakPoints() {
  try {
    const res = await getKnowledgeMastery(selectedChildId.value)
    const data = res.data || []
    weakPoints.value = data.filter(d => (d.mastery || d.score || 0) < 60).slice(0, 5)
  } catch { weakPoints.value = [] }
}

async function loadTasks() {
  try {
    const res = await listLearningTasks(selectedChildId.value)
    const tasks = res.data || []
    totalTasks.value = tasks.length
    completedTasks.value = tasks.filter(t => t.status === 'COMPLETED').length
  } catch { totalTasks.value = 0; completedTasks.value = 0 }
}

function formatDuration(sec) {
  if (!sec) return ''
  const m = Math.floor(sec / 60)
  const s = sec % 60
  return `${m}:${s.toString().padStart(2, '0')}`
}
</script>

<style lang="scss" scoped>
.student-home-page {
  min-height: 100vh;
  background: var(--color-bg);
  padding: 16px;
  padding-bottom: 80px;
}

.page-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16px;
  h1 { font-size: var(--font-size-xl); font-weight: 600; }
}

.plan-card {
  .plan-header {
    display: flex; align-items: center; gap: 8px; margin-bottom: 8px;
    .plan-icon { font-size: 20px; }
    h3 { font-size: var(--font-size-md); font-weight: 600; }
  }
  .plan-desc { font-size: var(--font-size-sm); color: var(--color-text-secondary); margin-bottom: 8px; }
}

.progress-card {
  text-align: center;
  h3 { font-size: var(--font-size-md); font-weight: 600; margin-bottom: 12px; }
  .progress-ring {
    display: flex; flex-direction: column; align-items: center; gap: 8px;
    .progress-label { font-size: var(--font-size-sm); color: var(--color-text-secondary); }
  }
}

.quick-entries {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 12px;
  margin: 16px 0;
  .entry-item {
    display: flex; flex-direction: column; align-items: center; gap: 6px;
    padding: 12px 8px;
    background: #fff;
    border-radius: var(--radius-md);
    box-shadow: var(--color-card-shadow);
    cursor: pointer;
    .entry-icon { font-size: 28px; }
    .entry-label { font-size: var(--font-size-xs); color: var(--color-text); }
  }
}

.course-scroll {
  display: flex;
  flex-direction: column;
  gap: 10px;
  margin-bottom: 16px;
  .course-item {
    display: flex; align-items: center; gap: 12px; cursor: pointer;
    .course-cover {
      width: 80px; height: 56px; border-radius: var(--radius-sm);
      background: #eee center/cover no-repeat; position: relative; flex-shrink: 0;
      .duration {
        position: absolute; bottom: 2px; right: 4px;
        font-size: 10px; color: #fff; background: rgba(0,0,0,0.5);
        padding: 1px 4px; border-radius: 3px;
      }
    }
    .course-info { flex: 1; min-width: 0;
      .course-title { font-size: var(--font-size-md); font-weight: 500; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
      .course-subject { font-size: var(--font-size-xs); color: var(--color-text-secondary); }
    }
  }
}

.weak-list {
  display: flex; flex-direction: column; gap: 8px; margin-bottom: 16px;
  .weak-item {
    display: flex; align-items: center; gap: 12px;
    padding: 10px 12px; background: #fff; border-radius: var(--radius-sm);
    .weak-name { font-size: var(--font-size-sm); min-width: 60px; }
    .el-progress { flex: 1; }
  }
}

.bottom-nav {
  position: fixed; bottom: 0; left: 0; right: 0;
  display: flex; background: #fff; border-top: 1px solid var(--color-border);
  padding: 8px 0 env(safe-area-inset-bottom, 8px);
  max-width: 750px; margin: 0 auto;
  .nav-item {
    flex: 1; display: flex; flex-direction: column; align-items: center; gap: 2px;
    font-size: var(--font-size-xs); color: var(--color-text-placeholder); cursor: pointer;
    .nav-icon { font-size: 22px; }
    &.active { color: var(--color-primary); }
  }
}
</style>
