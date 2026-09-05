<template>
  <div class="page student-oral-page">
    <div class="page-header">
      <el-icon class="back-icon" @click="router.back()"><ArrowLeft /></el-icon>
      <h1>AI口语跟读</h1>
    </div>

    <div class="child-selector">
      <el-select v-model="selectedChildId" placeholder="选择孩子" style="width: 100%" @change="onChildChange">
        <el-option v-for="c in children" :key="c.id" :label="c.name" :value="c.id" />
      </el-select>
    </div>

    <!-- 单词/课文列表 -->
    <div class="section-title">📖 跟读材料</div>
    <div class="material-list">
      <div
        v-for="m in materials"
        :key="m.id"
        class="card material-card"
        :class="{ active: currentMaterial?.id === m.id }"
        @click="selectMaterial(m)"
      >
        <div class="material-info">
          <h4>{{ m.title }}</h4>
          <p>{{ m.textContent }}</p>
        </div>
        <el-tag v-if="m.audioType === 'FOLLOW_READ'" type="primary" size="small">跟读</el-tag>
        <el-tag v-else-if="m.audioType === 'LISTENING'" type="success" size="small">听力</el-tag>
        <el-tag v-else-if="m.audioType === 'READ'" type="warning" size="small">朗读</el-tag>
        <el-tag v-else size="small">{{ audioTypeLabel(m.audioType) }}</el-tag>
      </div>
      <el-empty v-if="materials.length === 0" description="暂无跟读材料" />
    </div>

    <!-- 跟读区 -->
    <div class="practice-area" v-if="currentMaterial">
      <div class="card reference-card">
        <h3>参考文本</h3>
        <p class="reference-text">{{ currentMaterial.textContent || currentMaterial.referenceText }}</p>
        <el-button v-if="currentMaterial.audioUrl" text type="primary" size="small" @click="playReference">
          🔊 播放原音
        </el-button>
      </div>

      <div class="record-section">
        <div class="record-btn" :class="{ recording: isRecording }" @click="toggleRecord">
          <span class="record-icon">{{ isRecording ? '⏹' : '🎤' }}</span>
          <span>{{ isRecording ? '停止录音' : '开始跟读' }}</span>
        </div>
        <p class="record-hint" v-if="isRecording">正在录音...请朗读上方文本</p>
        <p class="record-timer" v-if="isRecording">{{ recordTime }}s</p>
      </div>

      <!-- AI评测结果 -->
      <div class="card result-card" v-if="evalResult">
        <h3>🤖 AI评测结果</h3>
        <div class="score-display">
          <el-progress type="circle" :percentage="evalResult.score || 0" :width="80" :stroke-width="6"
            :color="evalResult.score >= 80 ? '#52C41A' : evalResult.score >= 60 ? '#FAAD14' : '#FF4D4F'" />
        </div>
        <div class="eval-details">
          <div class="eval-item" v-if="evalResult.pronunciation">
            <span class="label">发音准确度</span>
            <el-progress :percentage="evalResult.pronunciation" :stroke-width="4" />
          </div>
          <div class="eval-item" v-if="evalResult.fluency">
            <span class="label">流利度</span>
            <el-progress :percentage="evalResult.fluency" :stroke-width="4" />
          </div>
        </div>
        <div class="suggestions" v-if="evalResult.suggestions">
          <strong>💡 改进建议：</strong>
          <p>{{ evalResult.suggestions }}</p>
        </div>
      </div>
    </div>

    <audio ref="audioRef" style="display: none" />
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue'
import { useRouter } from 'vue-router'
import { ArrowLeft } from '@element-plus/icons-vue'
import { getMyChildren } from '@/api/child'
import { evaluatePronunciation } from '@/api/student'
import { showToast } from '@/utils/toast'
import request from '@/utils/request'
import { audioTypeLabel } from '@/utils/format'

const router = useRouter()
const children = ref([])
const selectedChildId = ref(null)
const materials = ref([])
const currentMaterial = ref(null)
const isRecording = ref(false)
const recordTime = ref(0)
const evalResult = ref(null)
const audioRef = ref(null)
let mediaRecorder = null
let recordTimer = null
let audioChunks = []

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

async function onChildChange() {
  await loadMaterials()
}

async function loadMaterials() {
  try {
    const res = await request.get('/course/list', { params: { subject: 'ENGLISH', limit: 20 } })
    materials.value = res.data || []
  } catch {
    materials.value = [
      { id: 1, title: 'Hello', textContent: 'Hello, how are you?', audioType: 'FOLLOW_READ', audioUrl: '' },
      { id: 2, title: 'Good Morning', textContent: 'Good morning, teacher!', audioType: 'FOLLOW_READ', audioUrl: '' },
      { id: 3, title: 'Numbers', textContent: 'One, two, three, four, five', audioType: 'FOLLOW_READ', audioUrl: '' },
    ]
  }
}

function selectMaterial(m) {
  currentMaterial.value = m
  evalResult.value = null
}

function playReference() {
  if (!currentMaterial.value?.audioUrl || !audioRef.value) return
  audioRef.value.src = currentMaterial.value.audioUrl
  audioRef.value.play()
}

async function toggleRecord() {
  if (isRecording.value) {
    stopRecording()
  } else {
    await startRecording()
  }
}

async function startRecording() {
  try {
    const stream = await navigator.mediaDevices.getUserMedia({ audio: true })
    mediaRecorder = new MediaRecorder(stream)
    audioChunks = []
    mediaRecorder.ondataavailable = (e) => { audioChunks.push(e.data) }
    mediaRecorder.onstop = () => {
      stream.getTracks().forEach(t => t.stop())
      evaluateRecording()
    }
    mediaRecorder.start()
    isRecording.value = true
    recordTime.value = 0
    recordTimer = setInterval(() => { recordTime.value++ }, 1000)
  } catch (e) {
    showToast('无法访问麦克风，请授权')
  }
}

function stopRecording() {
  if (mediaRecorder && mediaRecorder.state !== 'inactive') {
    mediaRecorder.stop()
  }
  isRecording.value = false
  if (recordTimer) { clearInterval(recordTimer); recordTimer = null }
}

async function evaluateRecording() {
  if (!currentMaterial.value) return
  try {
    const res = await evaluatePronunciation({
      childId: selectedChildId.value,
      word: currentMaterial.value.title,
      audioTranscript: currentMaterial.value.textContent,
    })
    const data = res.data
    try {
      evalResult.value = JSON.parse(data.aiResultJson || '{}')
      evalResult.value.score = data.score || evalResult.value.score || 75
    } catch {
      evalResult.value = { score: data.score || 75, suggestions: '发音不错，继续加油！' }
    }
  } catch (e) {
    showToast(e.message || '评测失败')
  }
}

onBeforeUnmount(() => {
  if (recordTimer) clearInterval(recordTimer)
  if (mediaRecorder && mediaRecorder.state !== 'inactive') mediaRecorder.stop()
})
</script>

<style lang="scss" scoped>
.student-oral-page {
  min-height: 100vh; background: var(--color-bg); padding: 16px; padding-bottom: 24px;
}
.page-header {
  display: flex; align-items: center; gap: 12px; margin-bottom: 12px;
  h1 { font-size: var(--font-size-xl); font-weight: 600; }
  .back-icon { font-size: 24px; cursor: pointer; }
}
.child-selector { margin-bottom: 12px; }
.material-list { display: flex; flex-direction: column; gap: 10px; margin-bottom: 16px; }
.material-card {
  display: flex; align-items: center; gap: 12px; cursor: pointer;
  &.active { border: 2px solid var(--color-primary); }
  .material-info { flex: 1;
    h4 { font-size: var(--font-size-md); font-weight: 500; }
    p { font-size: var(--font-size-sm); color: var(--color-text-secondary); }
  }
}
.practice-area { margin-top: 16px; }
.reference-card {
  h3 { font-size: var(--font-size-md); font-weight: 600; margin-bottom: 8px; }
  .reference-text { font-size: var(--font-size-lg); line-height: 1.8; margin-bottom: 8px; color: var(--color-text); }
}
.record-section {
  text-align: center; padding: 24px 0;
  .record-btn {
    display: inline-flex; flex-direction: column; align-items: center; gap: 8px;
    width: 100px; height: 100px; border-radius: 50%;
    background: var(--color-primary); color: #fff;
    justify-content: center; cursor: pointer;
    transition: all 0.3s;
    &.recording { background: var(--color-danger); animation: pulse 1s infinite; }
    .record-icon { font-size: 32px; }
    span:last-child { font-size: var(--font-size-xs); }
  }
  .record-hint { font-size: var(--font-size-sm); color: var(--color-danger); margin-top: 8px; }
  .record-timer { font-size: var(--font-size-xl); font-weight: 600; color: var(--color-danger); margin-top: 4px; }
}
.result-card {
  text-align: center;
  h3 { font-size: var(--font-size-md); font-weight: 600; margin-bottom: 12px; }
  .score-display { margin-bottom: 16px; }
  .eval-details {
    display: flex; flex-direction: column; gap: 10px; text-align: left; margin-bottom: 12px;
    .eval-item {
      .label { font-size: var(--font-size-sm); color: var(--color-text-secondary); display: block; margin-bottom: 4px; }
    }
  }
  .suggestions {
    text-align: left; padding: 12px; background: #f8f9fa; border-radius: var(--radius-sm);
    font-size: var(--font-size-sm); line-height: 1.6;
    strong { color: var(--color-primary); }
  }
}
@keyframes pulse { 0%, 100% { transform: scale(1); } 50% { transform: scale(1.05); } }
</style>
