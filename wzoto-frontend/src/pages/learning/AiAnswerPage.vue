<template>
  <div class="page ai-answer-page">
    <div class="page-header">
      <el-icon class="back-icon" @click="router.back()"><ArrowLeft /></el-icon>
      <h1>AI 答疑记录</h1>
    </div>

    <div class="child-selector">
      <el-select v-model="selectedChildId" placeholder="选择子女" style="width: 100%" @change="loadRecords">
        <el-option v-for="child in children" :key="child.id" :label="child.name" :value="child.id" />
      </el-select>
    </div>

    <div class="record-list">
      <!-- 提问频次统计 -->
      <div class="card freq-card">
        <h3>📊 提问频次统计</h3>
        <div class="freq-stats">
          <div class="freq-item"><span class="num">{{ totalQuestions }}</span><span>总提问</span></div>
          <div class="freq-item"><span class="num">{{ todayQuestions }}</span><span>今日</span></div>
          <div class="freq-item"><span class="num">{{ subjectCount }}</span><span>涉及学科</span></div>
        </div>
      </div>

      <div v-for="record in records" :key="record.id" class="card record-card" @click="loadSession(record.conversationId)">
        <div class="record-header">
          <el-tag size="small">{{ subjectLabel(record.subject) || '通用' }}</el-tag>
          <span class="time">{{ formatTime(record.createdAt) }}</span>
          <el-tag v-if="record.isFollowUp" type="warning" size="small">追问</el-tag>
        </div>
        <div class="question">
          <span class="label">问：</span>
          <span>{{ record.questionText || record.question }}</span>
        </div>
        <!-- AI分步回答 -->
        <div class="answer" v-if="record.steps && record.steps.length > 0">
          <span class="label">AI分步解答：</span>
          <div class="steps">
            <div v-for="(step, i) in record.steps" :key="i" class="step-item">
              <span class="step-num">{{ i + 1 }}</span>
              <span>{{ step }}</span>
            </div>
          </div>
        </div>
        <div class="answer" v-else>
          <span class="label">答：</span>
          <span>{{ record.answer || record.aiResponseJson || '' }}</span>
        </div>
        <div class="tags" v-if="record.knowledgeTags">
          <el-tag v-for="tag in record.knowledgeTags.split(',').filter(Boolean)" :key="tag" size="small" type="info">{{ tag }}</el-tag>
        </div>
      </div>
      <el-empty v-if="records.length === 0" description="暂无答疑记录" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ArrowLeft } from '@element-plus/icons-vue'
import { getMyChildren } from '@/api/child'
import { getQaHistory, getQaSession } from '@/api/student'
import { trackPageView } from '@/utils/track'
import { showToast } from '@/utils/toast'
import { subjectLabel } from '@/utils/format'

const router = useRouter()
const children = ref([])
const selectedChildId = ref(null)
const records = ref([])
const totalQuestions = ref(0)
const todayQuestions = ref(0)
const subjectCount = ref(0)

onMounted(() => {
  trackPageView('ai_answer')
  loadChildren()
})

async function loadChildren() {
  try {
    const res = await getMyChildren()
    children.value = res.data || []
    if (children.value.length > 0) {
      selectedChildId.value = children.value[0].id
      loadRecords()
    }
  } catch (e) {
    console.error(e)
  }
}

async function loadRecords() {
  if (!selectedChildId.value) return
  try {
    const res = await getQaHistory(selectedChildId.value)
    const data = res.data || []
    records.value = data.map(r => {
      let steps = []
      try {
        const parsed = JSON.parse(r.aiResponseJson)
        steps = parsed.steps || []
      } catch {}
      return { ...r, steps, answer: '' }
    })
    // 统计
    totalQuestions.value = data.length
    const today = new Date().toISOString().split('T')[0]
    todayQuestions.value = data.filter(r => (r.createdAt || '').startsWith(today)).length
    const subjects = new Set(data.map(r => r.subject))
    subjectCount.value = subjects.size
  } catch (e) {
    showToast(e.message || '加载失败')
  }
}

async function loadSession(conversationId) {
  if (!conversationId) return
  try {
    const res = await getQaSession(conversationId)
    const records2 = res.data || []
    if (records2.length > 0) {
      const idx = records.value.findIndex(r => r.conversationId === conversationId)
      if (idx >= 0) {
        let steps = []
        try { steps = JSON.parse(records2[0].aiResponseJson).steps || [] } catch {}
        records.value[idx].steps = steps
      }
    }
  } catch (e) { console.error(e) }
}

function formatTime(iso) {
  if (!iso) return ''
  const d = new Date(iso)
  return `${d.getMonth() + 1}/${d.getDate()} ${String(d.getHours()).padStart(2, '0')}:${String(d.getMinutes()).padStart(2, '0')}`
}
</script>

<style lang="scss" scoped>
.ai-answer-page {
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

.record-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.freq-card {
  .freq-stats {
    display: flex; justify-content: space-around;
    .freq-item { text-align: center;
      .num { display: block; font-size: 20px; font-weight: 700; color: var(--color-primary); }
      span:last-child { font-size: var(--font-size-xs); color: var(--color-text-secondary); }
    }
  }
}

.record-card {
  .record-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 12px;

    .time {
      font-size: var(--font-size-xs);
      color: var(--color-text-secondary);
    }
  }

  .question, .answer {
    font-size: var(--font-size-sm);
    line-height: 1.6;
    margin-bottom: 8px;

    .label {
      font-weight: 600;
      color: var(--color-primary);
    }
  }

  .steps {
    margin-top: 4px;
    .step-item {
      display: flex; gap: 8px; align-items: flex-start; padding: 4px 0;
      .step-num {
        width: 20px; height: 20px; border-radius: 50%; background: var(--color-primary); color: #fff;
        display: flex; align-items: center; justify-content: center;
        font-size: 11px; flex-shrink: 0; margin-top: 2px;
      }
    }
  }

  .tags { display: flex; flex-wrap: wrap; gap: 4px; margin-top: 4px; }
}
</style>
