<template>
  <div class="page video-player-page">
    <div class="page-header">
      <el-icon class="back-icon" @click="router.back()"><ArrowLeft /></el-icon>
      <h1>{{ resource?.title || '视频播放' }}</h1>
    </div>

    <div v-if="loading" class="loading-wrap">
      <el-skeleton :rows="6" animated />
    </div>

    <div v-else-if="error" class="error-wrap">
      <el-empty description="加载资源失败" />
      <p class="error-msg">{{ error }}</p>
      <el-button type="primary" @click="router.back()">返回资源列表</el-button>
    </div>

    <div v-else-if="resource" class="player-wrap">
      <!-- OnionPlayer 播放器 -->
      <OnionPlayer
        ref="playerRef"
        :src="resource.contentUrl"
        :title="resource.title"
        :poster="resource.coverUrl"
        :subtitle-url="resource.subtitleUrl"
        :quality-levels="resource.qualityLevels"
        :knowledge-markers="resource.knowledgeMarkers"
        :breakpoint-seconds="breakpointSeconds"
        :resource-id="resource.id"
        :child-id="childId"
        @progress-report="onProgressReport"
        @ended="onVideoEnded"
        @loaded-metadata="onLoadedMeta"
      />

      <!-- 资源信息 -->
      <div class="resource-info card">
        <h2>{{ resource.title }}</h2>
        <p class="meta">
          <span>{{ resource.gradeDesc }}</span>
          <span>·</span>
          <span>{{ subjectLabel(resource.subject) }}</span>
          <span>·</span>
          <span>{{ resource.resourceTypeDesc }}</span>
          <span v-if="resource.durationSeconds">· {{ formatDuration(resource.durationSeconds) }}</span>
        </p>
        <p v-if="resource.knowledgePoint" class="knowledge">
          知识点：{{ resource.knowledgePoint }}
        </p>
        <p class="source-type">
          播放模式：{{ modeLabel }}
        </p>
      </div>

      <!-- 学习进度 -->
      <div class="progress-section card">
        <div class="progress-header">
          <span>学习进度</span>
          <span class="progress-text">{{ progressPercent }}%</span>
        </div>
        <el-progress :percentage="progressPercent" :show-text="false" />
        <p v-if="progressPercent >= 90" class="finish-tip">🎉 即将完成本节学习</p>
      </div>

      <!-- 播放完成 -->
      <transition name="fade">
        <div v-if="showComplete" class="complete-section card">
          <div class="complete-icon">🎉</div>
          <h3>本节学习完成！</h3>
          <p>太棒了，继续保持学习势头吧！</p>
          <div class="complete-actions">
            <el-button type="primary" @click="router.back()">返回资源列表</el-button>
          </div>
        </div>
      </transition>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ArrowLeft } from '@element-plus/icons-vue'
import { getLearningResourceDetail, getPlayProgress, reportPlayProgress } from '@/api/learning'
import { showToast } from '@/utils/toast'
import { trackPageView, trackEvent } from '@/utils/track'
import { subjectLabel } from '@/utils/format'
import OnionPlayer from '@/components/OnionPlayer.vue'

const router = useRouter()
const route = useRoute()

const loading = ref(true)
const error = ref('')
const resource = ref(null)
const playerRef = ref(null)
const progressPercent = ref(0)
const breakpointSeconds = ref(0)
const showComplete = ref(false)

const resourceId = computed(() => route.query.id)
const childId = computed(() => route.query.childId || null)

const playerMode = computed(() => {
  const url = (resource.value?.contentUrl || '').toLowerCase()
  if (!url) return 'empty'
  if (url.includes('player.bilibili.com') || url.includes('bilibili.com/video')) return 'bilibili'
  if (url.includes('.mp4') || url.includes('.webm') || url.includes('.m3u8') || url.includes('.mov')) return 'mp4'
  if (url.includes('smartedu.cn') || url.includes('basic.smartedu.cn') || url.startsWith('http')) return 'smartedu'
  return 'empty'
})

const modeLabel = computed(() => {
  const map = {
    mp4: 'MP4 直接播放',
    bilibili: 'B站 iframe 嵌入',
    smartedu: '国家中小学智慧教育平台外链',
    empty: '未配置'
  }
  return map[playerMode.value] || '未知'
})

onMounted(() => {
  trackPageView('video_player')
  loadResource()
})

async function loadResource() {
  if (!resourceId.value) {
    error.value = '缺少资源ID'
    loading.value = false
    return
  }
  try {
    const res = await getLearningResourceDetail(resourceId.value)
    resource.value = res.data
    trackEvent('video_load', { resourceId: resourceId.value, mode: playerMode.value })

    // 获取断点续播位置
    if (childId.value) {
      try {
        const progressRes = await getPlayProgress(resourceId.value, childId.value)
        const progress = progressRes.data
        if (progress && progress.lastPositionSeconds > 0) {
          breakpointSeconds.value = progress.lastPositionSeconds
          progressPercent.value = progress.progressPercent || 0
        }
      } catch (e) {
        // 无历史记录，从0开始
      }
    }
  } catch (e) {
    error.value = e.message || '加载失败'
    showToast(error.value)
  } finally {
    loading.value = false
  }
}

async function onProgressReport(data) {
  progressPercent.value = data.percent || 0
  try {
    await reportPlayProgress(data)
  } catch (e) {
    // 静默失败
  }
}

function onLoadedMeta(meta) {
  // 可用于更新 duration
}

function onVideoEnded() {
  progressPercent.value = 100
  showComplete.value = true
  trackEvent('video_finish', { resourceId: resourceId.value })
  showToast('本节学习完成')
}

function formatDuration(seconds) {
  if (!seconds) return ''
  const m = Math.floor(seconds / 60)
  const s = seconds % 60
  return `${m}分${s < 10 ? '0' : ''}${s}秒`
}
</script>

<style lang="scss" scoped>
.video-player-page {
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
    font-size: var(--font-size-lg);
    font-weight: 600;
    flex: 1;
    overflow: hidden;
    text-overflow: ellipsis;
    white-space: nowrap;
  }

  .back-icon {
    font-size: 24px;
    cursor: pointer;
  }
}

.loading-wrap,
.error-wrap {
  background: #fff;
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 16px;
}

.error-wrap {
  text-align: center;

  .error-msg {
    color: #999;
    margin: 12px 0 24px;
    font-size: var(--font-size-sm);
  }
}

.resource-info {
  margin-top: 16px;
  margin-bottom: 16px;

  h2 {
    font-size: var(--font-size-lg);
    font-weight: 600;
    margin-bottom: 8px;
  }

  .meta {
    font-size: var(--font-size-sm);
    color: var(--color-text-secondary);
    margin-bottom: 8px;

    span {
      margin-right: 6px;
    }
  }

  .knowledge {
    font-size: var(--font-size-sm);
    color: var(--color-primary);
    margin-bottom: 8px;
  }

  .source-type {
    font-size: var(--font-size-xs);
    color: #999;
    background: #f5f7fa;
    padding: 6px 10px;
    border-radius: 6px;
    display: inline-block;
  }
}

.progress-section {
  .progress-header {
    display: flex;
    justify-content: space-between;
    margin-bottom: 8px;
    font-size: var(--font-size-sm);

    .progress-text {
      color: var(--color-primary);
      font-weight: 600;
    }
  }

  .finish-tip {
    margin-top: 10px;
    color: #52c41a;
    font-size: var(--font-size-sm);
    text-align: center;
  }
}

.complete-section {
  text-align: center;
  padding: 24px;

  .complete-icon {
    font-size: 56px;
    margin-bottom: 12px;
  }

  h3 {
    font-size: 20px;
    font-weight: 600;
    margin-bottom: 8px;
  }

  p {
    color: #666;
    margin-bottom: 20px;
  }

  .complete-actions {
    display: flex;
    justify-content: center;
    gap: 12px;
  }
}

.fade-enter-active, .fade-leave-active { transition: opacity 0.3s; }
.fade-enter-from, .fade-leave-to { opacity: 0; }
</style>
