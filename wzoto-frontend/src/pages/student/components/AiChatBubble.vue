<template>
  <div class="ai-chat-bubble" :class="{ 'is-user': isUser, 'is-ai': !isUser }">
    <div class="bubble-avatar">
      <span v-if="isUser">👤</span>
      <span v-else>🤖</span>
    </div>
    <div class="bubble-content">
      <div class="bubble-header" v-if="!isUser">
        <span class="ai-name">AI老师</span>
        <span class="step-info" v-if="stepIndex != null">第{{ stepIndex + 1 }}步</span>
      </div>
      <div class="bubble-body">
        <div v-if="!isUser && steps && steps.length > 0" class="step-content">
          <div v-for="(step, i) in visibleSteps" :key="i" class="step-item">
            <span class="step-num">{{ i + 1 }}</span>
            <span class="step-text">{{ step }}</span>
          </div>
          <el-button
            v-if="visibleSteps.length < steps.length"
            text
            type="primary"
            size="small"
            @click="showNextStep"
          >
            下一步 →
          </el-button>
        </div>
        <div v-else class="text-content" v-html="renderedContent" />
      </div>
      <div class="knowledge-tags" v-if="tags && tags.length > 0">
        <el-tag v-for="tag in tags" :key="tag" size="small" type="info">{{ tag }}</el-tag>
      </div>
      <div class="bubble-time">{{ timeLabel }}</div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue'

const props = defineProps({
  isUser: { type: Boolean, default: false },
  content: { type: String, default: '' },
  steps: { type: Array, default: () => [] },
  tags: { type: Array, default: () => [] },
  time: { type: [String, Number], default: '' },
  showAllSteps: { type: Boolean, default: false },
})

const visibleCount = ref(props.showAllSteps ? (props.steps?.length || 0) : 1)

const visibleSteps = computed(() => {
  return (props.steps || []).slice(0, visibleCount.value)
})

const stepIndex = computed(() => {
  if (!props.steps || props.steps.length === 0) return null
  return Math.min(visibleCount.value, props.steps.length) - 1
})

const renderedContent = computed(() => {
  if (!props.content) return ''
  return props.content
    .replace(/\n/g, '<br/>')
    .replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')
    .replace(/`(.*?)`/g, '<code>$1</code>')
})

const timeLabel = computed(() => {
  if (!props.time) return ''
  if (typeof props.time === 'string') return props.time
  const d = new Date(props.time)
  return `${d.getHours().toString().padStart(2, '0')}:${d.getMinutes().toString().padStart(2, '0')}`
})

function showNextStep() {
  if (visibleCount.value < (props.steps?.length || 0)) {
    visibleCount.value++
  }
}

watch(() => props.showAllSteps, (val) => {
  if (val) visibleCount.value = props.steps?.length || 0
})
</script>

<style lang="scss" scoped>
.ai-chat-bubble {
  display: flex;
  gap: 10px;
  margin-bottom: 16px;
  align-items: flex-start;

  &.is-user {
    flex-direction: row-reverse;

    .bubble-content { align-items: flex-end; }
    .bubble-body { background: var(--color-primary); color: #fff; border-radius: 16px 4px 16px 16px; }
    .bubble-time { text-align: right; }
  }

  &.is-ai {
    .bubble-body { background: #fff; border-radius: 4px 16px 16px 16px; }
  }
}

.bubble-avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: var(--color-bg);
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  flex-shrink: 0;
}

.bubble-content {
  max-width: 75%;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.bubble-header {
  display: flex;
  align-items: center;
  gap: 8px;

  .ai-name { font-size: var(--font-size-xs); color: var(--color-text-secondary); }
  .step-info { font-size: var(--font-size-xs); color: var(--color-primary); }
}

.bubble-body {
  padding: 12px 16px;
  box-shadow: var(--color-card-shadow);
  font-size: var(--font-size-md);
  line-height: 1.6;
}

.step-content {
  display: flex;
  flex-direction: column;
  gap: 8px;

  .step-item {
    display: flex;
    gap: 8px;
    align-items: flex-start;

    .step-num {
      width: 22px; height: 22px;
      border-radius: 50%;
      background: var(--color-primary);
      color: #fff;
      display: flex; align-items: center; justify-content: center;
      font-size: var(--font-size-xs); font-weight: 600;
      flex-shrink: 0;
      margin-top: 2px;
    }

    .step-text { flex: 1; line-height: 1.6; }
  }
}

.text-content {
  :deep(code) {
    background: rgba(0, 0, 0, 0.06);
    padding: 1px 4px;
    border-radius: 3px;
    font-size: var(--font-size-sm);
  }
}

.knowledge-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 4px;
}

.bubble-time {
  font-size: var(--font-size-xs);
  color: var(--color-text-placeholder);
  margin-top: 2px;
}
</style>
