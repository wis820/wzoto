<template>
  <div class="card exercise-card">
    <div class="exercise-header">
      <el-tag :type="difficultyTagType" size="small">{{ difficultyLabel }}</el-tag>
      <span class="exercise-type">{{ questionTypeLabel }}</span>
      <span class="exercise-index" v-if="index != null">第 {{ index + 1 }} 题</span>
    </div>

    <div class="question-content" v-html="questionContent" />

    <!-- 选择题 -->
    <div v-if="questionType === 'CHOICE'" class="options">
      <div
        v-for="(opt, i) in options"
        :key="i"
        class="option-item"
        :class="{
          selected: selectedAnswer === opt.value,
          correct: submitted && opt.value === correctAnswer,
          wrong: submitted && selectedAnswer === opt.value && opt.value !== correctAnswer,
          disabled: submitted
        }"
        @click="selectOption(opt.value)"
      >
        <span class="option-label">{{ opt.value }}</span>
        <span class="option-text">{{ opt.label }}</span>
      </div>
    </div>

    <!-- 填空题 -->
    <div v-else-if="questionType === 'FILL'" class="fill-input">
      <el-input
        v-model="fillAnswer"
        placeholder="请输入答案"
        :disabled="submitted"
        @keyup.enter="handleSubmit"
      />
    </div>

    <!-- 判断题 -->
    <div v-else-if="questionType === 'JUDGE'" class="judge-options">
      <div
        class="judge-btn"
        :class="{ selected: selectedAnswer === 'TRUE', correct: submitted && correctAnswer === 'TRUE', wrong: submitted && selectedAnswer === 'TRUE' && correctAnswer !== 'TRUE', disabled: submitted }"
        @click="selectOption('TRUE')"
      >✓ 正确</div>
      <div
        class="judge-btn"
        :class="{ selected: selectedAnswer === 'FALSE', correct: submitted && correctAnswer === 'FALSE', wrong: submitted && selectedAnswer === 'FALSE' && correctAnswer !== 'FALSE', disabled: submitted }"
        @click="selectOption('FALSE')"
      >✗ 错误</div>
    </div>

    <!-- 提交 & 结果 -->
    <div class="exercise-footer">
      <el-button v-if="!submitted" type="primary" :disabled="!canSubmit" @click="handleSubmit">
        提交答案
      </el-button>
      <div v-else class="result" :class="isCorrect ? 'correct' : 'wrong'">
        <span class="result-icon">{{ isCorrect ? '✓' : '✗' }}</span>
        <span>{{ isCorrect ? '回答正确' : '回答错误' }}</span>
        <span v-if="!isCorrect && correctAnswer" class="correct-hint">正确答案：{{ correctAnswer }}</span>
      </div>
      <div v-if="explanation && submitted" class="explanation">
        <strong>解析：</strong>{{ explanation }}
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

const props = defineProps({
  index: { type: Number, default: null },
  questionType: { type: String, default: 'CHOICE' },
  questionContent: { type: String, default: '' },
  options: { type: Array, default: () => [] },
  correctAnswer: { type: String, default: '' },
  explanation: { type: String, default: '' },
  difficulty: { type: String, default: 'EASY' },
})

const emit = defineEmits(['submit'])

const selectedAnswer = ref('')
const fillAnswer = ref('')
const submitted = ref(false)
const isCorrect = ref(false)

const difficultyLabel = computed(() => {
  const map = { EASY: '基础', MEDIUM: '提高', HARD: '挑战' }
  return map[props.difficulty] || props.difficulty
})

const difficultyTagType = computed(() => {
  const map = { EASY: 'success', MEDIUM: 'warning', HARD: 'danger' }
  return map[props.difficulty] || ''
})

const questionTypeLabel = computed(() => {
  const map = { CHOICE: '选择题', FILL: '填空题', JUDGE: '判断题' }
  return map[props.questionType] || props.questionType
})

const canSubmit = computed(() => {
  if (props.questionType === 'FILL') return fillAnswer.value.trim() !== ''
  return selectedAnswer.value !== ''
})

function selectOption(val) {
  if (submitted.value) return
  selectedAnswer.value = val
}

function handleSubmit() {
  if (submitted.value) return
  const answer = props.questionType === 'FILL' ? fillAnswer.value.trim() : selectedAnswer.value
  if (!answer) return
  submitted.value = true
  isCorrect.value = answer === props.correctAnswer
  emit('submit', { answer, correct: isCorrect.value })
}

function reset() {
  selectedAnswer.value = ''
  fillAnswer.value = ''
  submitted.value = false
  isCorrect.value = false
}

defineExpose({ reset })
</script>

<style lang="scss" scoped>
.exercise-card {
  margin-bottom: 16px;
}

.exercise-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 12px;

  .exercise-type { font-size: var(--font-size-sm); color: var(--color-text-secondary); }
  .exercise-index { font-size: var(--font-size-sm); color: var(--color-primary); font-weight: 600; margin-left: auto; }
}

.question-content {
  font-size: var(--font-size-md);
  line-height: 1.6;
  margin-bottom: 16px;
  color: var(--color-text);
}

.options {
  display: flex;
  flex-direction: column;
  gap: 8px;
  margin-bottom: 16px;
}

.option-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 12px;
  border: 1px solid var(--color-border);
  border-radius: var(--radius-sm);
  cursor: pointer;
  transition: all 0.2s;

  &:active:not(.disabled) { background: #f5f5f5; }
  &.selected { border-color: var(--color-primary); background: rgba(79, 110, 247, 0.05); }
  &.correct { border-color: var(--color-success); background: rgba(82, 196, 26, 0.08); }
  &.wrong { border-color: var(--color-danger); background: rgba(255, 77, 79, 0.08); }
  &.disabled { cursor: default; opacity: 0.8; }

  .option-label {
    width: 28px; height: 28px;
    border-radius: 50%;
    background: var(--color-bg);
    display: flex; align-items: center; justify-content: center;
    font-size: var(--font-size-sm); font-weight: 600; color: var(--color-text-secondary);
  }
  .option-text { flex: 1; font-size: var(--font-size-md); }
}

.fill-input {
  margin-bottom: 16px;
}

.judge-options {
  display: flex;
  gap: 16px;
  margin-bottom: 16px;

  .judge-btn {
    flex: 1;
    padding: 16px;
    text-align: center;
    border: 1px solid var(--color-border);
    border-radius: var(--radius-sm);
    font-size: var(--font-size-lg);
    cursor: pointer;
    transition: all 0.2s;

    &.selected { border-color: var(--color-primary); background: rgba(79, 110, 247, 0.05); }
    &.correct { border-color: var(--color-success); background: rgba(82, 196, 26, 0.08); }
    &.wrong { border-color: var(--color-danger); background: rgba(255, 77, 79, 0.08); }
    &.disabled { cursor: default; opacity: 0.8; }
  }
}

.exercise-footer {
  .result {
    display: flex;
    align-items: center;
    gap: 8px;
    padding: 12px;
    border-radius: var(--radius-sm);
    font-size: var(--font-size-md);
    font-weight: 600;

    &.correct { color: var(--color-success); background: rgba(82, 196, 26, 0.08); }
    &.wrong { color: var(--color-danger); background: rgba(255, 77, 79, 0.08); }

    .result-icon { font-size: 20px; }
    .correct-hint { font-weight: 400; font-size: var(--font-size-sm); margin-left: auto; }
  }

  .explanation {
    margin-top: 12px;
    padding: 12px;
    background: #f8f9fa;
    border-radius: var(--radius-sm);
    font-size: var(--font-size-sm);
    line-height: 1.6;
    color: var(--color-text-secondary);
  }
}
</style>
