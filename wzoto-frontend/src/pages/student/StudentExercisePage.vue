<template>
  <div class="page student-exercise-page">
    <div class="page-header">
      <el-icon class="back-icon" @click="router.back()"><ArrowLeft /></el-icon>
      <h1>专项练习</h1>
    </div>

    <!-- 筛选 -->
    <div class="filter-bar" v-if="!currentExercise">
      <el-select v-model="filter.grade" placeholder="年级" @change="loadExercises">
        <el-option v-for="g in 6" :key="g" :label="`${g}年级`" :value="g" />
      </el-select>
      <el-select v-model="filter.subject" placeholder="学科" clearable @change="loadExercises">
        <el-option label="数学" value="MATH" />
        <el-option label="语文" value="CHINESE" />
        <el-option label="英语" value="ENGLISH" />
      </el-select>
      <el-select v-model="filter.difficulty" placeholder="难度" clearable @change="loadExercises">
        <el-option label="基础" value="EASY" />
        <el-option label="提高" value="MEDIUM" />
        <el-option label="挑战" value="HARD" />
      </el-select>
    </div>

    <!-- 做题模式 -->
    <div v-if="currentExercise" class="exercise-mode">
      <div class="exercise-progress">
        <span>{{ currentIndex + 1 }} / {{ exercises.length }}</span>
        <el-progress :percentage="Math.round(((currentIndex) / exercises.length) * 100)" :stroke-width="4" :show-text="false" />
      </div>
      <ExerciseCard
        ref="exerciseCardRef"
        :key="currentExercise.id"
        :index="currentIndex"
        :question-type="currentExercise.questionType || 'CHOICE'"
        :question-content="currentExercise.questionContent"
        :options="parseOptions(currentExercise.optionsJson)"
        :correct-answer="currentExercise.correctAnswer"
        :explanation="currentExercise.explanation"
        :difficulty="currentExercise.difficulty"
        @submit="onAnswerSubmit"
      />
      <div class="exercise-actions">
        <el-button @click="skipQuestion" :disabled="currentIndex >= exercises.length - 1">跳过</el-button>
        <el-button v-if="answered" type="primary" @click="nextQuestion">
          {{ currentIndex >= exercises.length - 1 ? '完成' : '下一题' }}
        </el-button>
      </div>
      <div class="result-summary" v-if="showSummary">
        <h3>📊 练习结果</h3>
        <div class="summary-stats">
          <div class="stat"><span class="num text-success">{{ correctCount }}</span><span>正确</span></div>
          <div class="stat"><span class="num text-danger">{{ wrongCount }}</span><span>错误</span></div>
          <div class="stat"><span class="num text-primary">{{ exercises.length }}</span><span>总题数</span></div>
        </div>
        <el-button type="primary" @click="resetExercise">再来一组</el-button>
        <el-button @click="currentExercise = null">返回</el-button>
      </div>
    </div>

    <!-- 题目列表 -->
    <div v-else class="exercise-list">
      <div v-for="ex in exercises" :key="ex.id" class="card exercise-preview" @click="startExercise(ex)">
        <div class="preview-header">
          <el-tag :type="diffTag(ex.difficulty)" size="small">{{ diffLabel(ex.difficulty) }}</el-tag>
          <span class="question-type">{{ typeLabel(ex.questionType) }}</span>
        </div>
        <p class="preview-content" v-html="truncate(ex.questionContent, 80)" />
        <div class="preview-meta">
          <span>{{ subjectLabel(ex.subject) }}</span>
          <span>已做 {{ ex.useCount || 0 }} 次</span>
        </div>
      </div>
      <el-empty v-if="exercises.length === 0" description="暂无题目，请调整筛选条件" />
    </div>

    <!-- 试卷入口 -->
    <div class="section-title" v-if="!currentExercise">📄 推荐试卷</div>
    <div class="paper-list" v-if="!currentExercise">
      <div v-for="paper in papers" :key="paper.id" class="card paper-card" @click="startPaper(paper)">
        <h4>{{ paper.title }}</h4>
        <p>{{ paperTypeLabel(paper.paperType) }} · 总分 {{ paper.totalScore }}</p>
      </div>
      <el-empty v-if="papers.length === 0" description="暂无试卷" :image-size="40" />
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ArrowLeft } from '@element-plus/icons-vue'
import ExerciseCard from './components/ExerciseCard.vue'
import { listExercises, submitAnswer, listTestPapers, getTestPaper } from '@/api/student'
import { showToast } from '@/utils/toast'
import { subjectLabel, paperTypeLabel } from '@/utils/format'

const router = useRouter()
const exercises = ref([])
const papers = ref([])
const currentExercise = ref(null)
const currentIndex = ref(0)
const answered = ref(false)
const showSummary = ref(false)
const correctCount = ref(0)
const wrongCount = ref(0)
const exerciseCardRef = ref(null)

const filter = reactive({ grade: 1, subject: '', difficulty: '' })

const wrongCount2 = computed(() => currentIndex.value - correctCount.value)

onMounted(() => {
  loadExercises()
  loadPapers()
})

async function loadExercises() {
  try {
    const params = { limit: 20 }
    if (filter.grade) params.grade = filter.grade
    if (filter.subject) params.subject = filter.subject
    if (filter.difficulty) params.difficulty = filter.difficulty
    const res = await listExercises(params)
    exercises.value = res.data || []
  } catch (e) { showToast(e.message || '加载失败') }
}

async function loadPapers() {
  try {
    const params = {}
    if (filter.grade) params.grade = filter.grade
    if (filter.subject) params.subject = filter.subject
    const res = await listTestPapers(params)
    papers.value = res.data || []
  } catch { papers.value = [] }
}

function startExercise(exercise) {
  const idx = exercises.value.findIndex(e => e.id === exercise.id)
  currentIndex.value = idx >= 0 ? idx : 0
  currentExercise.value = exercise
  answered.value = false
  showSummary.value = false
  correctCount.value = 0
  wrongCount.value = 0
}

function startPaper(paper) {
  getTestPaper(paper.id).then(res => {
    showToast('试卷功能开发中')
  }).catch(e => showToast(e.message || '加载失败'))
}

function parseOptions(json) {
  if (!json) return []
  try { return JSON.parse(json) } catch { return [] }
}

async function onAnswerSubmit(result) {
  answered.value = true
  if (result.correct) correctCount.value++
  else wrongCount.value++
  try {
    await submitAnswer({ exerciseId: currentExercise.value.id, studentAnswer: result.answer })
  } catch {}
}

function skipQuestion() {
  wrongCount.value++
  nextQuestion()
}

function nextQuestion() {
  if (currentIndex.value >= exercises.value.length - 1) {
    showSummary.value = true
    return
  }
  currentIndex.value++
  currentExercise.value = exercises.value[currentIndex.value]
  answered.value = false
  exerciseCardRef.value?.reset()
}

function resetExercise() {
  currentExercise.value = null
  showSummary.value = false
  currentIndex.value = 0
  correctCount.value = 0
  wrongCount.value = 0
  loadExercises()
}

function diffLabel(d) { return { EASY: '基础', MEDIUM: '提高', HARD: '挑战' }[d] || d }
function diffTag(d) { return { EASY: 'success', MEDIUM: 'warning', HARD: 'danger' }[d] || '' }
function typeLabel(t) { return { CHOICE: '选择题', FILL: '填空题', JUDGE: '判断题' }[t] || t }
function truncate(html, len) {
  const text = (html || '').replace(/<[^>]+>/g, '')
  return text.length > len ? text.slice(0, len) + '...' : text
}
</script>

<style lang="scss" scoped>
.student-exercise-page {
  min-height: 100vh;
  background: var(--color-bg);
  padding: 16px;
  padding-bottom: 24px;
}

.page-header {
  display: flex; align-items: center; gap: 12px; margin-bottom: 16px;
  h1 { font-size: var(--font-size-xl); font-weight: 600; }
  .back-icon { font-size: 24px; cursor: pointer; }
}

.filter-bar {
  display: flex; gap: 8px; margin-bottom: 16px; flex-wrap: wrap;
  .el-select { flex: 1; min-width: 90px; }
}

.exercise-mode {
  .exercise-progress {
    display: flex; align-items: center; gap: 12px; margin-bottom: 16px;
    span { font-size: var(--font-size-sm); color: var(--color-text-secondary); white-space: nowrap; }
    .el-progress { flex: 1; }
  }
  .exercise-actions {
    display: flex; gap: 12px; margin-top: 16px;
    .el-button { flex: 1; }
  }
  .result-summary {
    text-align: center; padding: 24px 0;
    h3 { font-size: var(--font-size-lg); font-weight: 600; margin-bottom: 16px; }
    .summary-stats {
      display: flex; justify-content: center; gap: 32px; margin-bottom: 24px;
      .stat { display: flex; flex-direction: column; align-items: center; gap: 4px;
        .num { font-size: var(--font-size-xxl); font-weight: 700; }
        span:last-child { font-size: var(--font-size-sm); color: var(--color-text-secondary); }
      }
    }
  }
}

.exercise-list {
  display: flex; flex-direction: column; gap: 12px; margin-bottom: 16px;
}

.exercise-preview {
  cursor: pointer;
  .preview-header { display: flex; gap: 8px; align-items: center; margin-bottom: 8px;
    .question-type { font-size: var(--font-size-xs); color: var(--color-text-secondary); }
  }
  .preview-content { font-size: var(--font-size-md); line-height: 1.5; margin-bottom: 8px; }
  .preview-meta { display: flex; gap: 12px; font-size: var(--font-size-xs); color: var(--color-text-placeholder); }
}

.paper-list {
  display: flex; flex-direction: column; gap: 10px;
  .paper-card { cursor: pointer;
    h4 { font-size: var(--font-size-md); font-weight: 500; }
    p { font-size: var(--font-size-sm); color: var(--color-text-secondary); }
  }
}
</style>
