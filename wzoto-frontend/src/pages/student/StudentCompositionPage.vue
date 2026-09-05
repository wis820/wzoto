<template>
  <div class="page student-composition-page">
    <div class="page-header">
      <el-icon class="back-icon" @click="router.back()"><ArrowLeft /></el-icon>
      <h1>AI作文批改</h1>
    </div>

    <div class="child-selector">
      <el-select v-model="selectedChildId" placeholder="选择孩子" style="width: 100%">
        <el-option v-for="c in children" :key="c.id" :label="c.name" :value="c.id" />
      </el-select>
    </div>

    <!-- 作文输入 -->
    <div class="card compose-card" v-if="!showResult">
      <el-input v-model="title" placeholder="作文标题" style="margin-bottom: 12px" />
      <el-input
        v-model="content"
        type="textarea"
        placeholder="请输入作文内容..."
        :rows="10"
        show-word-limit
        :maxlength="2000"
      />
      <div class="compose-actions">
        <span class="word-count">{{ (content || '').length }} 字</span>
        <el-button type="primary" :disabled="!canSubmit" :loading="grading" @click="submitComposition">
          🤖 AI批改
        </el-button>
      </div>
    </div>

    <!-- 批改结果 -->
    <div v-if="showResult" class="result-section">
      <div class="card score-card">
        <div class="score-header">
          <h3>📊 批改结果</h3>
          <div class="score-circle">
            <el-progress type="circle" :percentage="result.score || 0" :width="70" :stroke-width="5"
              :color="result.score >= 80 ? '#52C41A' : result.score >= 60 ? '#FAAD14' : '#FF4D4F'" />
          </div>
        </div>
        <div class="score-breakdown" v-if="result.details">
          <div class="score-item" v-for="(val, key) in result.details" :key="key">
            <span class="score-label">{{ scoreLabels[key] || key }}</span>
            <el-progress :percentage="val" :stroke-width="4" />
          </div>
        </div>
      </div>

      <!-- 错别字 -->
      <div class="card" v-if="result.typoErrors && result.typoErrors.length > 0">
        <h3>✏️ 错别字</h3>
        <div class="error-list">
          <div v-for="(err, i) in result.typoErrors" :key="i" class="error-item">
            <span class="wrong-text">{{ err.wrong }}</span>
            <span class="arrow">→</span>
            <span class="correct-text text-success">{{ err.correct }}</span>
          </div>
        </div>
      </div>

      <!-- 病句 -->
      <div class="card" v-if="result.grammarErrors && result.grammarErrors.length > 0">
        <h3>📝 病句修正</h3>
        <div class="error-list">
          <div v-for="(err, i) in result.grammarErrors" :key="i" class="error-item grammar">
            <p class="wrong-sentence">{{ err.original }}</p>
            <p class="correct-sentence text-success">✓ {{ err.suggestion }}</p>
          </div>
        </div>
      </div>

      <!-- 优化建议 -->
      <div class="card" v-if="result.suggestions">
        <h3>💡 优化建议</h3>
        <div class="suggestion-text" v-html="renderSuggestions(result.suggestions)" />
      </div>

      <div class="result-actions">
        <el-button type="primary" @click="resetCompose">再写一篇</el-button>
        <el-button @click="showResult = false">修改本文</el-button>
      </div>
    </div>

    <!-- 历史记录 -->
    <div class="section-title" v-if="!showResult">📜 历史批改记录</div>
    <div class="history-list" v-if="!showResult">
      <div v-for="h in historyList" :key="h.id" class="card history-card" @click="viewHistory(h)">
        <div class="history-header">
          <span class="history-title">{{ h.title || '无标题' }}</span>
          <el-tag size="small">得分 {{ h.score }}</el-tag>
        </div>
        <p class="history-time">{{ formatTime(h.createdAt) }}</p>
      </div>
      <el-empty v-if="historyList.length === 0" description="暂无批改记录" :image-size="40" />
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ArrowLeft } from '@element-plus/icons-vue'
import { getMyChildren } from '@/api/child'
import { gradeComposition, getGradingHistory } from '@/api/student'
import { showToast } from '@/utils/toast'

const router = useRouter()
const children = ref([])
const selectedChildId = ref(null)
const title = ref('')
const content = ref('')
const grading = ref(false)
const showResult = ref(false)
const result = ref({})
const historyList = ref([])

const canSubmit = computed(() => {
  return title.value.trim() && content.value.trim().length >= 20 && selectedChildId.value
})

const scoreLabels = {
  content: '内容', structure: '结构', language: '语言', creativity: '创意', grammar: '语法',
}

onMounted(() => { loadChildren(); loadHistory() })

async function loadChildren() {
  try {
    const res = await getMyChildren()
    children.value = res.data || []
    if (children.value.length > 0) {
      selectedChildId.value = children.value[0].id
    }
  } catch (e) { console.error(e) }
}

async function loadHistory() {
  if (!selectedChildId.value) return
  try {
    const res = await getGradingHistory(selectedChildId.value)
    historyList.value = (res.data || []).filter(h => h.type === 'COMPOSITION')
  } catch { historyList.value = [] }
}

async function submitComposition() {
  if (!canSubmit.value) return
  grading.value = true
  try {
    const res = await gradeComposition({
      childId: selectedChildId.value,
      grade: 3,
      title: title.value,
      content: content.value,
    })
    const data = res.data
    try {
      result.value = JSON.parse(data.aiResultJson || '{}')
      result.value.score = data.score || result.value.score || 0
    } catch {
      result.value = { score: data.score || 75, suggestions: '作文整体不错，继续加油！' }
    }
    showResult.value = true
    loadHistory()
  } catch (e) {
    showToast(e.message || '批改失败')
  } finally {
    grading.value = false
  }
}

function resetCompose() {
  title.value = ''
  content.value = ''
  result.value = {}
  showResult.value = false
}

function viewHistory(h) {
  try {
    result.value = JSON.parse(h.aiResultJson || '{}')
    result.value.score = h.score || result.value.score
  } catch {
    result.value = { score: h.score, suggestions: '' }
  }
  showResult.value = true
}

function renderSuggestions(text) {
  if (!text) return ''
  return text.replace(/\n/g, '<br/>').replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')
}

function formatTime(t) {
  if (!t) return ''
  const d = new Date(t)
  return `${d.getFullYear()}-${(d.getMonth() + 1).toString().padStart(2, '0')}-${d.getDate().toString().padStart(2, '0')}`
}
</script>

<style lang="scss" scoped>
.student-composition-page {
  min-height: 100vh; background: var(--color-bg); padding: 16px; padding-bottom: 24px;
}
.page-header {
  display: flex; align-items: center; gap: 12px; margin-bottom: 12px;
  h1 { font-size: var(--font-size-xl); font-weight: 600; }
  .back-icon { font-size: 24px; cursor: pointer; }
}
.child-selector { margin-bottom: 12px; }
.compose-card {
  .compose-actions {
    display: flex; justify-content: space-between; align-items: center; margin-top: 12px;
    .word-count { font-size: var(--font-size-sm); color: var(--color-text-secondary); }
  }
}
.result-section { margin-top: 16px; }
.score-card {
  .score-header {
    display: flex; justify-content: space-between; align-items: center; margin-bottom: 12px;
    h3 { font-size: var(--font-size-md); font-weight: 600; }
  }
  .score-breakdown {
    display: flex; flex-direction: column; gap: 8px;
    .score-item {
      .score-label { font-size: var(--font-size-sm); color: var(--color-text-secondary); display: block; margin-bottom: 4px; }
    }
  }
}
.error-list {
  display: flex; flex-direction: column; gap: 8px; margin-top: 8px;
  .error-item {
    display: flex; align-items: center; gap: 8px;
    padding: 8px 12px; background: #fff5f5; border-radius: var(--radius-sm);
    .wrong-text { color: var(--color-danger); text-decoration: line-through; }
    .arrow { color: var(--color-text-placeholder); }
    .correct-text { font-weight: 600; }
    &.grammar {
      flex-direction: column; align-items: flex-start;
      .wrong-sentence { font-size: var(--font-size-sm); color: var(--color-danger); }
      .correct-sentence { font-size: var(--font-size-sm); }
    }
  }
}
.suggestion-text {
  font-size: var(--font-size-md); line-height: 1.8; color: var(--color-text);
}
.result-actions {
  display: flex; gap: 12px; margin-top: 16px;
  .el-button { flex: 1; }
}
.history-list {
  display: flex; flex-direction: column; gap: 10px;
  .history-card { cursor: pointer;
    .history-header { display: flex; justify-content: space-between; align-items: center;
      .history-title { font-size: var(--font-size-md); font-weight: 500; }
    }
    .history-time { font-size: var(--font-size-xs); color: var(--color-text-placeholder); margin-top: 4px; }
  }
}
</style>
