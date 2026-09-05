<template>
  <div class="page student-wrong-book-page">
    <div class="page-header">
      <el-icon class="back-icon" @click="router.back()"><ArrowLeft /></el-icon>
      <h1>错题本</h1>
    </div>

    <div class="child-selector">
      <el-select v-model="selectedChildId" placeholder="选择孩子" style="width: 100%" @change="onChildChange">
        <el-option v-for="c in children" :key="c.id" :label="c.name" :value="c.id" />
      </el-select>
    </div>

    <div class="filter-bar">
      <el-select v-model="filter.subject" placeholder="学科" clearable @change="loadWrongQuestions">
        <el-option label="数学" value="MATH" />
        <el-option label="语文" value="CHINESE" />
        <el-option label="英语" value="ENGLISH" />
      </el-select>
    </div>

    <!-- AI薄弱点分析 -->
    <div class="card ai-analysis" v-if="masteryData.length > 0">
      <h3>🤖 AI薄弱点分析</h3>
      <KnowledgeRadar :data="masteryData" :height="220" />
      <el-button type="primary" size="small" @click="pushReviewCourse" style="width: 100%; margin-top: 8px">
        📺 一键推送复习微课
      </el-button>
    </div>

    <!-- 错题列表 -->
    <div class="wrong-list">
      <div v-for="q in questions" :key="q.id" class="card wrong-card">
        <div class="wrong-header">
          <el-tag size="small">{{ subjectLabel(q.subject) }}</el-tag>
          <span class="knowledge">{{ q.knowledgePoint }}</span>
          <el-tag :type="masteryTag(q.masteryLevel)" size="small">{{ masteryLabel(q.masteryLevel) }}</el-tag>
        </div>
        <p class="wrong-content" v-html="q.questionContent || ''" />
        <div class="wrong-meta">
          <span>错误 {{ q.mistakeCount }} 次</span>
        </div>
        <div class="wrong-actions">
          <el-button text type="primary" size="small" @click="redoQuestion(q)">重新做题</el-button>
          <el-button v-if="q.masteryLevel !== 'PROFICIENT'" text type="success" size="small" @click="markMastered(q.id)">标记已掌握</el-button>
        </div>
      </div>
      <el-empty v-if="questions.length === 0" description="暂无错题，继续保持！" />
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ArrowLeft } from '@element-plus/icons-vue'
import KnowledgeRadar from './components/KnowledgeRadar.vue'
import { getMyChildren } from '@/api/child'
import { listWrongQuestions, markWrongQuestionMastered } from '@/api/learning'
import { getKnowledgeMastery } from '@/api/student'
import { showToast } from '@/utils/toast'
import { subjectLabel } from '@/utils/format'

const router = useRouter()
const children = ref([])
const selectedChildId = ref(null)
const questions = ref([])
const masteryData = ref([])
const filter = reactive({ subject: '' })

onMounted(() => { loadChildren() })

async function loadChildren() {
  try {
    const res = await getMyChildren()
    children.value = res.data || []
    if (children.value.length > 0) {
      selectedChildId.value = children.value[0].id
      onChildChange()
    }
  } catch (e) { console.error(e) }
}

function onChildChange() {
  loadWrongQuestions()
  loadMastery()
}

async function loadWrongQuestions() {
  if (!selectedChildId.value) return
  try {
    const params = {}
    if (filter.subject) params.subject = filter.subject
    const res = await listWrongQuestions(selectedChildId.value, params)
    questions.value = res.data || []
  } catch (e) { showToast(e.message || '加载失败') }
}

async function loadMastery() {
  if (!selectedChildId.value) return
  try {
    const res = await getKnowledgeMastery(selectedChildId.value)
    const data = res.data || []
    masteryData.value = data.map(d => ({ name: d.name || d.knowledgePoint, value: d.mastery || d.score || 0 }))
  } catch { masteryData.value = [] }
}

async function markMastered(id) {
  try {
    await markWrongQuestionMastered(id)
    showToast('已标记掌握')
    loadWrongQuestions()
    loadMastery()
  } catch (e) { showToast(e.message || '操作失败') }
}

function redoQuestion(q) {
  router.push({ path: '/student/exercise', query: { questionId: q.id } })
}

function pushReviewCourse() {
  router.push('/student/courses')
}

function masteryLabel(l) { return { PROFICIENT: '已掌握', FAMILIAR: '熟悉', WEAK: '薄弱', UNKNOWN: '未知' }[l] || '未知' }
function masteryTag(l) { return { PROFICIENT: 'success', FAMILIAR: '', WEAK: 'danger', UNKNOWN: 'info' }[l] || 'info' }
</script>

<style lang="scss" scoped>
.student-wrong-book-page {
  min-height: 100vh; background: var(--color-bg); padding: 16px; padding-bottom: 24px;
}
.page-header {
  display: flex; align-items: center; gap: 12px; margin-bottom: 12px;
  h1 { font-size: var(--font-size-xl); font-weight: 600; }
  .back-icon { font-size: 24px; cursor: pointer; }
}
.child-selector { margin-bottom: 12px; }
.filter-bar { display: flex; gap: 8px; margin-bottom: 12px; }
.ai-analysis {
  h3 { font-size: var(--font-size-md); font-weight: 600; margin-bottom: 8px; }
}
.wrong-list { display: flex; flex-direction: column; gap: 10px; }
.wrong-card {
  .wrong-header { display: flex; align-items: center; gap: 8px; margin-bottom: 8px;
    .knowledge { font-size: var(--font-size-sm); color: var(--color-text-secondary); flex: 1; }
  }
  .wrong-content { font-size: var(--font-size-md); margin-bottom: 8px; line-height: 1.5; }
  .wrong-meta { font-size: var(--font-size-xs); color: var(--color-text-placeholder); margin-bottom: 8px; }
  .wrong-actions { display: flex; gap: 12px; }
}
</style>
