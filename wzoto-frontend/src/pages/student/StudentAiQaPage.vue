<template>
  <div class="page student-ai-qa-page">
    <div class="page-header">
      <el-icon class="back-icon" @click="router.back()"><ArrowLeft /></el-icon>
      <h1>AI答疑中心</h1>
    </div>

    <!-- 孩子选择 -->
    <div class="child-selector">
      <el-select v-model="selectedChildId" placeholder="选择孩子" style="width: 100%" @change="onChildChange">
        <el-option v-for="c in children" :key="c.id" :label="c.name" :value="c.id" />
      </el-select>
    </div>

    <!-- 对话区 -->
    <div class="chat-area" ref="chatAreaRef">
      <AiChatBubble
        v-for="(msg, i) in messages"
        :key="i"
        :is-user="msg.isUser"
        :content="msg.content"
        :steps="msg.steps"
        :tags="msg.tags"
        :time="msg.time"
      />
      <div v-if="loading" class="typing-indicator">
        <AiChatBubble :is-user="false" content="AI老师正在思考中..." />
      </div>
    </div>

    <!-- 输入区 -->
    <div class="input-area">
      <div class="input-tools">
        <el-upload
          :show-file-list="false"
          accept="image/*"
          :auto-upload="false"
          @change="onImageSelected"
        >
          <el-button text type="primary" size="small">📷 拍照</el-button>
        </el-upload>
        <el-select v-model="selectedSubject" placeholder="学科" size="small" style="width: 90px">
          <el-option label="数学" value="MATH" />
          <el-option label="语文" value="CHINESE" />
          <el-option label="英语" value="ENGLISH" />
        </el-select>
      </div>
      <div class="image-preview" v-if="selectedImage">
        <img :src="selectedImage" />
        <el-icon class="remove-img" @click="selectedImage = null"><Close /></el-icon>
      </div>
      <div class="input-row">
        <el-input
          v-model="questionText"
          placeholder="输入你的问题..."
          :disabled="loading"
          @keyup.enter="askQuestion"
        />
        <el-button type="primary" :disabled="!canAsk || loading" @click="askQuestion">
          提问
        </el-button>
      </div>
    </div>

    <!-- 历史会话 -->
    <div class="section-title">💬 历史提问</div>
    <div class="history-list">
      <div v-for="h in history" :key="h.id" class="card history-item" @click="loadSession(h.conversationId)">
        <div class="history-header">
          <el-tag size="small">{{ subjectLabel(h.subject) }}</el-tag>
          <span class="time">{{ formatTime(h.createdAt) }}</span>
        </div>
        <p class="history-text">{{ h.questionText }}</p>
      </div>
      <el-empty v-if="history.length === 0" description="暂无提问记录" :image-size="40" />
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, nextTick } from 'vue'
import { useRouter } from 'vue-router'
import { ArrowLeft, Close } from '@element-plus/icons-vue'
import AiChatBubble from './components/AiChatBubble.vue'
import { getMyChildren } from '@/api/child'
import { askQuestion as askQuestionApi, followUpQuestion, getQaHistory, getQaSession } from '@/api/student'
import { showToast } from '@/utils/toast'
import { subjectLabel } from '@/utils/format'

const router = useRouter()
const children = ref([])
const selectedChildId = ref(null)
const selectedSubject = ref('MATH')
const questionText = ref('')
const selectedImage = ref(null)
const loading = ref(false)
const messages = ref([])
const history = ref([])
const currentConversationId = ref(null)
const chatAreaRef = ref(null)

const canAsk = computed(() => {
  return questionText.value.trim() || selectedImage.value
})

onMounted(() => { loadChildren() })

async function loadChildren() {
  try {
    const res = await getMyChildren()
    children.value = res.data || []
    if (children.value.length > 0) {
      selectedChildId.value = children.value[0].id
      loadHistory()
    }
  } catch (e) { console.error(e) }
}

function onChildChange() {
  messages.value = []
  currentConversationId.value = null
  loadHistory()
}

async function loadHistory() {
  if (!selectedChildId.value) return
  try {
    const res = await getQaHistory(selectedChildId.value)
    history.value = res.data || []
  } catch { history.value = [] }
}

async function askQuestion() {
  if (!canAsk.value || !selectedChildId.value) return
  const text = questionText.value.trim()
  loading.value = true

  messages.value.push({
    isUser: true,
    content: text || '(图片提问)',
    time: Date.now(),
  })
  questionText.value = ''

  try {
    const data = {
      childId: selectedChildId.value,
      subject: selectedSubject.value,
      questionText: text,
      questionImageUrl: selectedImage.value || null,
    }

    const isFollowUp = currentConversationId.value != null
    const res = isFollowUp
      ? await followUpQuestion({ ...data, conversationId: currentConversationId.value })
      : await askQuestionApi(data)

    const result = res.data
    currentConversationId.value = result.conversationId

    let steps = []
    let tags = []
    try {
      const parsed = JSON.parse(result.aiResponseJson)
      steps = parsed.steps || []
      tags = parsed.knowledgeTags || result.knowledgeTags ? (result.knowledgeTags || '').split(',').filter(Boolean) : []
    } catch {
      steps = []
    }

    messages.value.push({
      isUser: false,
      content: result.aiResponseJson ? '' : 'AI已为你解答',
      steps: steps.length > 0 ? steps : undefined,
      tags,
      time: Date.now(),
    })

    selectedImage.value = null
    scrollToBottom()
    loadHistory()
  } catch (e) {
    messages.value.push({ isUser: false, content: '抱歉，AI暂时无法回答，请稍后再试', time: Date.now() })
    showToast(e.message || '提问失败')
  } finally {
    loading.value = false
  }
}

async function loadSession(conversationId) {
  try {
    const res = await getQaSession(conversationId)
    const records = res.data || []
    currentConversationId.value = conversationId
    messages.value = records.map(r => {
      let steps = []
      try {
        const parsed = JSON.parse(r.aiResponseJson)
        steps = parsed.steps || []
      } catch {}
      return {
        isUser: false,
        content: steps.length === 0 ? (r.aiResponseJson || '') : '',
        steps: steps.length > 0 ? steps : undefined,
        tags: r.knowledgeTags ? r.knowledgeTags.split(',').filter(Boolean) : [],
        time: r.createdAt,
      }
    })
    // Add user question at top
    if (records.length > 0) {
      messages.value.unshift({ isUser: true, content: records[0].questionText, time: records[0].createdAt })
    }
    scrollToBottom()
  } catch (e) { showToast(e.message || '加载失败') }
}

function onImageSelected(file) {
  const reader = new FileReader()
  reader.onload = (e) => { selectedImage.value = e.target.result }
  reader.readAsDataURL(file.raw || file)
}

function scrollToBottom() {
  nextTick(() => {
    if (chatAreaRef.value) {
      chatAreaRef.value.scrollTop = chatAreaRef.value.scrollHeight
    }
  })
}

function formatTime(t) {
  if (!t) return ''
  const d = new Date(t)
  return `${d.getMonth() + 1}/${d.getDate()} ${d.getHours().toString().padStart(2, '0')}:${d.getMinutes().toString().padStart(2, '0')}`
}
</script>

<style lang="scss" scoped>
.student-ai-qa-page {
  min-height: 100vh;
  background: var(--color-bg);
  padding: 16px;
  padding-bottom: 24px;
}

.page-header {
  display: flex; align-items: center; gap: 12px; margin-bottom: 12px;
  h1 { font-size: var(--font-size-xl); font-weight: 600; }
  .back-icon { font-size: 24px; cursor: pointer; }
}

.child-selector { margin-bottom: 12px; }

.chat-area {
  min-height: 200px;
  max-height: 400px;
  overflow-y: auto;
  padding: 12px;
  background: var(--color-bg);
  border-radius: var(--radius-md);
  margin-bottom: 12px;
}

.typing-indicator {
  opacity: 0.7;
}

.input-area {
  background: #fff;
  border-radius: var(--radius-md);
  padding: 12px;
  margin-bottom: 16px;

  .input-tools {
    display: flex; gap: 8px; margin-bottom: 8px; align-items: center;
  }

  .image-preview {
    position: relative; display: inline-block; margin-bottom: 8px;
    img { max-width: 120px; max-height: 120px; border-radius: var(--radius-sm); }
    .remove-img {
      position: absolute; top: -8px; right: -8px;
      background: var(--color-danger); color: #fff;
      border-radius: 50%; width: 20px; height: 20px;
      display: flex; align-items: center; justify-content: center;
      cursor: pointer; font-size: 14px;
    }
  }

  .input-row {
    display: flex; gap: 8px;
    .el-input { flex: 1; }
  }
}

.history-list {
  display: flex; flex-direction: column; gap: 10px;
  .history-item { cursor: pointer;
    .history-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 6px;
      .time { font-size: var(--font-size-xs); color: var(--color-text-placeholder); }
    }
    .history-text { font-size: var(--font-size-sm); color: var(--color-text); overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
  }
}
</style>
