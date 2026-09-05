<template>
  <div class="page wrong-question-page">
    <div class="page-header">
      <el-icon class="back-icon" @click="router.back()"><ArrowLeft /></el-icon>
      <h1>错题本</h1>
    </div>

    <div class="child-selector">
      <el-select v-model="selectedChildId" placeholder="选择子女" style="width: 100%" @change="onChildChange">
        <el-option v-for="child in children" :key="child.id" :label="child.name" :value="child.id" />
      </el-select>
    </div>

    <div class="filter-bar">
      <el-select v-model="filter.subject" placeholder="学科" clearable @change="loadQuestions">
        <el-option label="语文" value="chinese" />
        <el-option label="数学" value="math" />
        <el-option label="英语" value="english" />
      </el-select>
      <el-checkbox v-model="filter.inReviewPlan" @change="loadQuestions">仅看复习计划</el-checkbox>
      <el-button type="primary" size="small" @click="printWrongQuestions">打印/PDF</el-button>
    </div>

    <div class="question-list">
      <div v-for="q in questions" :key="q.id" class="card question-card">
        <div class="question-header">
          <el-tag size="small">{{ subjectLabel(q.subject) }}</el-tag>
          <span class="knowledge">{{ q.knowledgePoint }}</span>
        </div>
        <div class="stats">
          <span>错误 {{ q.mistakeCount }} 次</span>
          <span :style="{ color: q.masteryColor }">{{ q.masteryLevelDesc }}</span>
          <el-tag v-if="q.inReviewPlan" type="warning" size="small">复习中</el-tag>
        </div>
        <div class="actions">
          <el-checkbox v-model="selectedIds" :label="q.id" size="small">加入复习</el-checkbox>
          <el-button v-if="q.masteryLevel !== 'PROFICIENT'" text type="primary" size="small" @click="markMastered(q.id)">已掌握</el-button>
        </div>
      </div>
      <el-empty v-if="questions.length === 0" description="暂无错题" />
    </div>

    <div v-if="selectedIds.length > 0" class="batch-bar">
      <el-button type="primary" style="width: 100%" @click="batchAddToReview">批量加入复习计划（{{ selectedIds.length }}）</el-button>
    </div>

    <div id="print-area" class="print-area">
      <h2>错题打印</h2>
      <div v-for="q in questions" :key="`print-${q.id}`" class="print-item">
        <p><strong>学科：</strong>{{ subjectLabel(q.subject) }} · <strong>知识点：</strong>{{ q.knowledgePoint }}</p>
        <p><strong>错误次数：</strong>{{ q.mistakeCount }} · <strong>掌握度：</strong>{{ q.masteryLevelDesc }}</p>
        <hr />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ArrowLeft } from '@element-plus/icons-vue'
import { getMyChildren } from '@/api/child'
import { listWrongQuestions, batchAddWrongQuestionsToReview, markWrongQuestionMastered } from '@/api/learning'
import { showToast } from '@/utils/toast'
import { trackPageView, trackEvent } from '@/utils/track'
import { subjectLabel } from '@/utils/format'

const router = useRouter()
const children = ref([])
const selectedChildId = ref(null)
const questions = ref([])
const selectedIds = ref([])

const filter = reactive({
  subject: '',
  inReviewPlan: false,
})

onMounted(() => {
  trackPageView('wrong_question')
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

function onChildChange() {
  selectedIds.value = []
  loadQuestions()
}

async function loadQuestions() {
  if (!selectedChildId.value) return
  try {
    const params = {}
    if (filter.subject) params.subject = filter.subject
    if (filter.inReviewPlan) params.inReviewPlan = true
    const res = await listWrongQuestions(selectedChildId.value, params)
    questions.value = res.data || []
  } catch (e) {
    showToast(e.message || '加载失败')
  }
}

async function markMastered(id) {
  try {
    await markWrongQuestionMastered(id)
    trackEvent('wrong_question_mastered', { id })
    showToast('已标记掌握')
    loadQuestions()
  } catch (e) {
    showToast(e.message || '操作失败')
  }
}

async function batchAddToReview() {
  if (!selectedChildId.value || selectedIds.value.length === 0) return
  try {
    await batchAddWrongQuestionsToReview(selectedChildId.value, selectedIds.value)
    trackEvent('wrong_question_batch_review', { count: selectedIds.value.length })
    showToast('已加入复习计划')
    selectedIds.value = []
    loadQuestions()
  } catch (e) {
    showToast(e.message || '操作失败')
  }
}

function printWrongQuestions() {
  trackEvent('wrong_question_print')
  const printContent = document.getElementById('print-area')
  const originalDisplay = printContent.style.display
  printContent.style.display = 'block'
  window.print()
  printContent.style.display = originalDisplay
}
</script>

<style lang="scss" scoped>
.wrong-question-page {
  min-height: 100vh;
  background: var(--color-bg);
  padding: 16px;
  padding-bottom: 80px;
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
  margin-bottom: 12px;
}

.filter-bar {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 16px;
  flex-wrap: wrap;

  .el-select {
    width: 120px;
  }
}

.question-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.question-card {
  .question-header {
    display: flex;
    align-items: center;
    gap: 8px;
    margin-bottom: 8px;

    .knowledge {
      font-size: var(--font-size-sm);
      color: var(--color-text-secondary);
    }
  }

  .stats {
    display: flex;
    align-items: center;
    gap: 12px;
    font-size: var(--font-size-sm);
    color: var(--color-text-secondary);
    margin-bottom: 8px;
  }

  .actions {
    display: flex;
    align-items: center;
    justify-content: space-between;
  }
}

.batch-bar {
  position: fixed;
  bottom: 24px;
  left: 16px;
  right: 16px;
  z-index: 10;
}

.print-area {
  display: none;
}

@media print {
  body * {
    visibility: hidden;
  }

  .print-area, .print-area * {
    visibility: visible;
  }

  .print-area {
    position: absolute;
    left: 0;
    top: 0;
    width: 100%;
    display: block !important;
    padding: 20px;
  }

  .print-item {
    margin-bottom: 16px;
    page-break-inside: avoid;
  }
}
</style>
