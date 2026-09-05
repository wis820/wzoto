<template>
  <div class="onion-player" ref="playerRoot" :class="{ 'is-fullscreen': isFullscreen }">
    <!-- MP4 播放模式 -->
    <div v-if="mode === 'mp4'" class="video-container" @click="togglePlay" @dblclick="toggleFullscreen">
      <video
        ref="videoEl"
        class="video-element"
        :poster="poster"
        playsinline
        webkit-playsinline
        preload="metadata"
        @play="onPlay"
        @pause="onPause"
        @timeupdate="onTimeUpdate"
        @loadedmetadata="onLoadedMeta"
        @ended="onEnded"
        @error="onError"
        @waiting="buffering = true"
        @canplay="buffering = false"
      >
        <source :src="currentSrc" />
        <track v-if="subtitleUrl && showSubtitle" kind="subtitles" :src="subtitleUrl" default label="中文" />
      </video>

      <!-- 加载动画 -->
      <div v-if="buffering" class="loading-overlay">
        <div class="spinner"></div>
        <span>缓冲中...</span>
      </div>

      <!-- 大播放按钮（暂停时显示） -->
      <div v-if="!isPlaying && !buffering" class="big-play" @click.stop="togglePlay">
        <svg viewBox="0 0 80 80" width="64" height="64"><circle cx="40" cy="40" r="38" fill="rgba(0,0,0,0.5)" stroke="#fff" stroke-width="2"/><polygon points="30,22 30,58 60,40" fill="#fff"/></svg>
      </div>

      <!-- 知识点弹窗 -->
      <transition name="kp-fade">
        <div v-if="activeKnowledge" class="knowledge-popup" @click.stop>
          <div class="kp-header">
            <span class="kp-icon">💡</span>
            <span class="kp-title">{{ activeKnowledge.title }}</span>
            <button class="kp-close" @click="activeKnowledge = null">✕</button>
          </div>
          <p class="kp-content">{{ activeKnowledge.description }}</p>
        </div>
      </transition>

      <!-- 顶部信息栏 -->
      <transition name="ctrl-fade">
        <div v-if="showControls" class="top-bar">
          <span class="video-title">{{ title }}</span>
        </div>
      </transition>

      <!-- 底部控制栏 -->
      <transition name="ctrl-fade">
        <div v-if="showControls" class="control-bar" @click.stop>
          <!-- 进度条 -->
          <div class="progress-wrap" ref="progressWrap" @mousedown="startDrag" @touchstart.prevent="startDrag">
            <div class="progress-track">
              <div class="progress-buffered" :style="{ width: bufferedPercent + '%' }"></div>
              <div class="progress-played" :style="{ width: playedPercent + '%' }">
                <div class="progress-thumb"></div>
              </div>
            </div>
            <!-- 知识点标记点 -->
            <div
              v-for="(kp, idx) in knowledgeMarkers"
              :key="idx"
              class="kp-marker"
              :style="{ left: (kp.time / duration * 100) + '%' }"
              :title="kp.title"
              @click.stop="seekTo(kp.time)"
            ></div>
          </div>

          <div class="controls-row">
            <!-- 左侧 -->
            <div class="ctrl-left">
              <button class="ctrl-btn" @click="togglePlay" :title="isPlaying ? '暂停' : '播放'">
                <svg v-if="!isPlaying" viewBox="0 0 24 24" width="22" height="22"><polygon points="6,3 6,21 20,12" fill="#fff"/></svg>
                <svg v-else viewBox="0 0 24 24" width="22" height="22"><rect x="5" y="3" width="5" height="18" fill="#fff"/><rect x="14" y="3" width="5" height="18" fill="#fff"/></svg>
              </button>
              <span class="time-display">{{ formatTime(currentTime) }} / {{ formatTime(duration) }}</span>
            </div>

            <!-- 右侧 -->
            <div class="ctrl-right">
              <!-- 字幕开关 -->
              <button v-if="subtitleUrl" class="ctrl-btn" :class="{ active: showSubtitle }" @click="showSubtitle = !showSubtitle" title="字幕">
                <svg viewBox="0 0 24 24" width="20" height="20"><rect x="2" y="4" width="20" height="16" rx="2" fill="none" stroke="#fff" stroke-width="1.5"/><text x="12" y="15" text-anchor="middle" fill="#fff" font-size="7" font-weight="bold">CC</text></svg>
              </button>

              <!-- 倍速 -->
              <div class="menu-wrap" @mouseenter="showSpeedMenu = true" @mouseleave="showSpeedMenu = false">
                <button class="ctrl-btn speed-btn">{{ playbackRate }}x</button>
                <transition name="menu-fade">
                  <div v-if="showSpeedMenu" class="popup-menu">
                    <div
                      v-for="s in speedOptions"
                      :key="s"
                      class="menu-item"
                      :class="{ active: playbackRate === s }"
                      @click="setSpeed(s)"
                    >{{ s }}x</div>
                  </div>
                </transition>
              </div>

              <!-- 画质 -->
              <div v-if="qualityOptions.length > 1" class="menu-wrap" @mouseenter="showQualityMenu = true" @mouseleave="showQualityMenu = false">
                <button class="ctrl-btn">{{ currentQualityLabel }}</button>
                <transition name="menu-fade">
                  <div v-if="showQualityMenu" class="popup-menu">
                    <div
                      v-for="q in qualityOptions"
                      :key="q.label"
                      class="menu-item"
                      :class="{ active: currentQuality === q.label }"
                      @click="setQuality(q)"
                    >{{ q.label }}</div>
                  </div>
                </transition>
              </div>

              <!-- 画中画 -->
              <button class="ctrl-btn" @click="togglePiP" title="画中画">
                <svg viewBox="0 0 24 24" width="20" height="20"><rect x="2" y="4" width="20" height="16" rx="2" fill="none" stroke="#fff" stroke-width="1.5"/><rect x="12" y="11" width="8" height="7" rx="1" fill="#fff"/></svg>
              </button>

              <!-- 全屏 -->
              <button class="ctrl-btn" @click="toggleFullscreen" title="全屏">
                <svg v-if="!isFullscreen" viewBox="0 0 24 24" width="20" height="20"><path d="M3 3h6v2H5v4H3V3zm12 0h6v6h-2V5h-4V3zM3 15h2v4h4v2H3v-6zm16 4h-4v2h6v-6h-2v4z" fill="#fff"/></svg>
                <svg v-else viewBox="0 0 24 24" width="20" height="20"><path d="M9 3v2H5v4H3V3h6zm6 0h6v6h-2V5h-4V3zM3 15h2v4h4v2H3v-6zm14 4h4v-6h-2v4h-4v2h2z" fill="#fff"/></svg>
              </button>
            </div>
          </div>
        </div>
      </transition>
    </div>

    <!-- B站 iframe 模式 -->
    <div v-else-if="mode === 'bilibili'" class="iframe-container">
      <iframe :src="bilibiliEmbedUrl" scrolling="no" border="0" frameborder="no" framespacing="0" allowfullscreen referrerpolicy="no-referrer"></iframe>
    </div>

    <!-- 外链跳转模式 -->
    <div v-else-if="mode === 'smartedu'" class="external-container">
      <div class="external-card">
        <div class="external-icon">🏫</div>
        <h3>{{ title || '外部资源' }}</h3>
        <p>该资源来自国家中小学智慧教育平台，点击下方按钮将在新标签页打开官方页面进行学习。</p>
        <button class="external-btn" @click="openExternal">跳转官方平台学习</button>
      </div>
    </div>

    <!-- 空状态 -->
    <div v-else class="empty-container">
      <el-empty description="暂无可播放的视频资源" />
    </div>
  </div>
</template>

<script setup>
import { ref, computed, watch, onMounted, onBeforeUnmount, nextTick } from 'vue'

const props = defineProps({
  /** 视频源 URL */
  src: { type: String, default: '' },
  /** 视频标题 */
  title: { type: String, default: '' },
  /** 封面图 */
  poster: { type: String, default: '' },
  /** 字幕 URL (VTT) */
  subtitleUrl: { type: String, default: '' },
  /** 画质选项 JSON: [{label:'720P', url:'...'},{label:'1080P', url:'...'}] */
  qualityLevels: { type: [String, Array], default: '' },
  /** 知识点标记 JSON: [{time:30, title:'xxx', description:'...'}] */
  knowledgeMarkers: { type: [String, Array], default: '' },
  /** 断点秒数（上次播放位置） */
  breakpointSeconds: { type: Number, default: 0 },
  /** 资源 ID（用于上报） */
  resourceId: { type: [String, Number], default: null },
  /** 孩子 ID */
  childId: { type: [String, Number], default: null }
})

const emit = defineEmits(['play', 'pause', 'ended', 'timeUpdate', 'progressReport', 'loadedMetadata'])

// ============ 播放模式检测 ============
const mode = computed(() => {
  const url = (props.src || '').toLowerCase()
  if (!url) return 'empty'
  if (url.includes('player.bilibili.com') || url.includes('bilibili.com/video')) return 'bilibili'
  if (url.includes('.mp4') || url.includes('.webm') || url.includes('.m3u8') || url.includes('.mov')) return 'mp4'
  if (url.includes('smartedu.cn') || url.startsWith('http')) return 'smartedu'
  return 'empty'
})

const bilibiliEmbedUrl = computed(() => {
  const url = props.src || ''
  if (url.includes('player.bilibili.com')) return url
  const match = url.match(/BV\w+/)
  const pMatch = url.match(/[?&]p=(\d+)/)
  const p = pMatch ? pMatch[1] : '1'
  if (match) return `https://player.bilibili.com/player.html?bvid=${match[0]}&p=${p}&autoplay=0`
  return url
})

// ============ Refs ============
const playerRoot = ref(null)
const videoEl = ref(null)
const progressWrap = ref(null)

// ============ State ============
const isPlaying = ref(false)
const currentTime = ref(0)
const duration = ref(0)
const bufferedPercent = ref(0)
const playedPercent = ref(0)
const playbackRate = ref(1)
const isFullscreen = ref(false)
const showControls = ref(true)
const showSpeedMenu = ref(false)
const showQualityMenu = ref(false)
const showSubtitle = ref(true)
const buffering = ref(false)
const activeKnowledge = ref(null)
const currentSrc = ref('')
const currentQuality = ref('')

let controlsTimer = null
let progressTimer = null
let isDragging = false

const speedOptions = [0.5, 0.75, 1, 1.25, 1.5, 2]

// ============ Quality Parsing ============
const qualityOptions = computed(() => {
  let ql = props.qualityLevels
  if (!ql) return []
  if (typeof ql === 'string') {
    try { ql = JSON.parse(ql) } catch { return [] }
  }
  return Array.isArray(ql) ? ql : []
})

const currentQualityLabel = computed(() => currentQuality.value || '自动')

// ============ Knowledge Markers Parsing ============
const knowledgeMarkers = computed(() => {
  let km = props.knowledgeMarkers
  if (!km) return []
  if (typeof km === 'string') {
    try { km = JSON.parse(km) } catch { return [] }
  }
  return Array.isArray(km) ? km.filter(k => typeof k.time === 'number') : []
})

// ============ Lifecycle ============
onMounted(() => {
  currentSrc.value = props.src
  if (qualityOptions.value.length > 0) {
    currentQuality.value = qualityOptions.value[0].label
  }

  // 自动隐藏控制栏
  playerRoot.value?.addEventListener('mousemove', onPlayerMouseMove)
  playerRoot.value?.addEventListener('mouseleave', hideControlsDelayed)

  // 键盘快捷键
  document.addEventListener('keydown', onKeyDown)

  // 全屏变化监听
  document.addEventListener('fullscreenchange', onFullscreenChange)
  document.addEventListener('webkitfullscreenchange', onFullscreenChange)
})

onBeforeUnmount(() => {
  clearInterval(progressTimer)
  document.removeEventListener('keydown', onKeyDown)
  document.removeEventListener('fullscreenchange', onFullscreenChange)
  document.removeEventListener('webkitfullscreenchange', onFullscreenChange)
  document.removeEventListener('mousemove', onDragMove)
  document.removeEventListener('mouseup', onDragEnd)
  document.removeEventListener('touchmove', onDragMove)
  document.removeEventListener('touchend', onDragEnd)
})

// ============ 断点续播 ============
watch(() => videoEl.value, (el) => {
  if (el && props.breakpointSeconds > 0) {
    el.addEventListener('loadedmetadata', () => {
      if (props.breakpointSeconds > 0 && props.breakpointSeconds < el.duration - 5) {
        el.currentTime = props.breakpointSeconds
      }
    }, { once: true })
  }
})

// ============ Playback Controls ============
function togglePlay() {
  if (!videoEl.value) return
  if (videoEl.value.paused) {
    videoEl.value.play()
  } else {
    videoEl.value.pause()
  }
}

function onPlay() {
  isPlaying.value = true
  startProgressReporting()
  emit('play')
}

function onPause() {
  isPlaying.value = false
  stopProgressReporting()
  emit('pause')
}

function onTimeUpdate() {
  if (!videoEl.value) return
  currentTime.value = videoEl.value.currentTime
  if (duration.value > 0) {
    playedPercent.value = (currentTime.value / duration.value) * 100
  }

  // 缓冲进度
  if (videoEl.value.buffered.length > 0) {
    const bufferedEnd = videoEl.value.buffered.end(videoEl.value.buffered.length - 1)
    bufferedPercent.value = (bufferedEnd / duration.value) * 100
  }

  // 知识点弹窗检测
  checkKnowledgeMarkers()

  emit('timeUpdate', { currentTime: currentTime.value, duration: duration.value })
}

function onLoadedMeta() {
  if (!videoEl.value) return
  duration.value = videoEl.value.duration
  emit('loadedMetadata', { duration: duration.value })
}

function onEnded() {
  isPlaying.value = false
  stopProgressReporting()
  emit('ended')
}

function onError() {
  console.error('[OnionPlayer] 视频加载错误')
}

// ============ 进度上报（每5秒）============
function startProgressReporting() {
  stopProgressReporting()
  progressTimer = setInterval(() => {
    if (props.resourceId && videoEl.value) {
      emit('progressReport', {
        resourceId: props.resourceId,
        childId: props.childId,
        currentTime: Math.floor(videoEl.value.currentTime),
        duration: Math.floor(videoEl.value.duration || 0),
        percent: Math.floor((videoEl.value.currentTime / (videoEl.value.duration || 1)) * 100)
      })
    }
  }, 5000)
}

function stopProgressReporting() {
  if (progressTimer) {
    clearInterval(progressTimer)
    progressTimer = null
  }
}

// ============ 知识点标记检测 ============
function checkKnowledgeMarkers() {
  const ct = currentTime.value
  for (const kp of knowledgeMarkers.value) {
    if (Math.abs(ct - kp.time) < 1.5 && activeKnowledge.value?.time !== kp.time) {
      activeKnowledge.value = kp
      break
    }
  }
}

// ============ 进度条拖拽 ============
function startDrag(e) {
  isDragging = true
  document.addEventListener('mousemove', onDragMove)
  document.addEventListener('mouseup', onDragEnd)
  document.addEventListener('touchmove', onDragMove)
  document.addEventListener('touchend', onDragEnd)
  seekFromEvent(e)
}

function onDragMove(e) {
  if (!isDragging) return
  seekFromEvent(e)
}

function onDragEnd() {
  isDragging = false
  document.removeEventListener('mousemove', onDragMove)
  document.removeEventListener('mouseup', onDragEnd)
  document.removeEventListener('touchmove', onDragMove)
  document.removeEventListener('touchend', onDragEnd)
}

function seekFromEvent(e) {
  if (!progressWrap.value || !duration.value) return
  const rect = progressWrap.value.getBoundingClientRect()
  const clientX = e.touches ? e.touches[0].clientX : e.clientX
  let ratio = (clientX - rect.left) / rect.width
  ratio = Math.max(0, Math.min(1, ratio))
  seekTo(ratio * duration.value)
}

function seekTo(time) {
  if (!videoEl.value) return
  videoEl.value.currentTime = Math.max(0, Math.min(time, duration.value))
  currentTime.value = videoEl.value.currentTime
  playedPercent.value = (currentTime.value / duration.value) * 100
}

// ============ 倍速 ============
function setSpeed(rate) {
  playbackRate.value = rate
  if (videoEl.value) videoEl.value.playbackRate = rate
  showSpeedMenu.value = false
}

// ============ 画质 ============
function setQuality(q) {
  if (!videoEl.value) return
  const wasPlaying = !videoEl.value.paused
  const ct = videoEl.value.currentTime
  currentSrc.value = q.url || props.src
  currentQuality.value = q.label
  showQualityMenu.value = false
  nextTick(() => {
    if (videoEl.value) {
      videoEl.value.currentTime = ct
      if (wasPlaying) videoEl.value.play()
    }
  })
}

// ============ 全屏 ============
function toggleFullscreen() {
  if (!playerRoot.value) return
  if (!document.fullscreenElement && !document.webkitFullscreenElement) {
    playerRoot.value.requestFullscreen?.() || playerRoot.value.webkitRequestFullscreen?.()
  } else {
    document.exitFullscreen?.() || document.webkitExitFullscreen?.()
  }
}

function onFullscreenChange() {
  isFullscreen.value = !!(document.fullscreenElement || document.webkitFullscreenElement)
}

// ============ 画中画 ============
async function togglePiP() {
  if (!videoEl.value) return
  try {
    if (document.pictureInPictureElement) {
      await document.exitPictureInPicture()
    } else {
      await videoEl.value.requestPictureInPicture()
    }
  } catch (err) {
    console.warn('[OnionPlayer] 画中画不可用:', err.message)
  }
}

// ============ 控制栏显示 ============
function onPlayerMouseMove() {
  showControls.value = true
  hideControlsDelayed()
}

function hideControlsDelayed() {
  clearTimeout(controlsTimer)
  if (isPlaying.value) {
    controlsTimer = setTimeout(() => { showControls.value = false }, 3000)
  }
}

// ============ 键盘快捷键 ============
function onKeyDown(e) {
  if (mode.value !== 'mp4') return
  switch (e.key) {
    case ' ':
    case 'k':
      e.preventDefault(); togglePlay(); break
    case 'ArrowLeft':
      e.preventDefault(); seekTo(currentTime.value - 5); break
    case 'ArrowRight':
      e.preventDefault(); seekTo(currentTime.value + 5); break
    case 'ArrowUp':
      e.preventDefault(); if (videoEl.value) videoEl.value.volume = Math.min(1, videoEl.value.volume + 0.1); break
    case 'ArrowDown':
      e.preventDefault(); if (videoEl.value) videoEl.value.volume = Math.max(0, videoEl.value.volume - 0.1); break
    case 'f':
      e.preventDefault(); toggleFullscreen(); break
    case 'm':
      e.preventDefault(); if (videoEl.value) videoEl.value.muted = !videoEl.value.muted; break
  }
}

// ============ 外部链接 ============
function openExternal() {
  if (props.src) window.open(props.src, '_blank', 'noopener,noreferrer')
}

// ============ 工具 ============
function formatTime(sec) {
  if (!sec || isNaN(sec)) return '00:00'
  const m = Math.floor(sec / 60)
  const s = Math.floor(sec % 60)
  return `${m < 10 ? '0' + m : m}:${s < 10 ? '0' + s : s}`
}

// ============ 对外暴露 ============
defineExpose({
  play: () => videoEl.value?.play(),
  pause: () => videoEl.value?.pause(),
  seekTo,
  getCurrentTime: () => currentTime.value,
  getDuration: () => duration.value
})
</script>

<style lang="scss" scoped>
.onion-player {
  position: relative;
  width: 100%;
  background: #000;
  border-radius: 12px;
  overflow: hidden;
  user-select: none;
  -webkit-user-select: none;

  &.is-fullscreen {
    border-radius: 0;
    width: 100vw;
    height: 100vh;

    .video-container {
      height: 100vh;
    }
  }
}

.video-container {
  position: relative;
  width: 100%;
  aspect-ratio: 16 / 9;
  background: #000;
  cursor: pointer;
  overflow: hidden;
}

.video-element {
  width: 100%;
  height: 100%;
  object-fit: contain;
  display: block;
}

// ---- Loading ----
.loading-overlay {
  position: absolute;
  inset: 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 12px;
  background: rgba(0,0,0,0.4);
  color: #fff;
  font-size: 14px;

  .spinner {
    width: 36px;
    height: 36px;
    border: 3px solid rgba(255,255,255,0.3);
    border-top-color: #fff;
    border-radius: 50%;
    animation: spin 0.8s linear infinite;
  }
}

@keyframes spin { to { transform: rotate(360deg) } }

// ---- Big Play ----
.big-play {
  position: absolute;
  inset: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 5;
}

// ---- Knowledge Popup ----
.knowledge-popup {
  position: absolute;
  top: 16px;
  right: 16px;
  width: 260px;
  background: rgba(255,255,255,0.95);
  border-radius: 10px;
  padding: 12px 14px;
  z-index: 10;
  box-shadow: 0 4px 20px rgba(0,0,0,0.3);

  .kp-header {
    display: flex;
    align-items: center;
    gap: 6px;
    margin-bottom: 6px;

    .kp-icon { font-size: 18px; }
    .kp-title { font-weight: 600; font-size: 14px; flex: 1; color: #333; }
    .kp-close {
      background: none;
      border: none;
      font-size: 16px;
      cursor: pointer;
      color: #999;
      padding: 2px;
      &:hover { color: #333; }
    }
  }

  .kp-content {
    font-size: 13px;
    color: #555;
    line-height: 1.5;
    margin: 0;
  }
}

// ---- Top Bar ----
.top-bar {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  padding: 12px 16px;
  background: linear-gradient(180deg, rgba(0,0,0,0.6) 0%, transparent 100%);
  z-index: 8;

  .video-title {
    color: #fff;
    font-size: 14px;
    font-weight: 500;
    text-shadow: 0 1px 3px rgba(0,0,0,0.5);
  }
}

// ---- Control Bar ----
.control-bar {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 0 12px 10px;
  background: linear-gradient(0deg, rgba(0,0,0,0.7) 0%, transparent 100%);
  z-index: 8;
}

.progress-wrap {
  position: relative;
  height: 16px;
  display: flex;
  align-items: center;
  cursor: pointer;
  padding: 4px 0;
}

.progress-track {
  width: 100%;
  height: 4px;
  background: rgba(255,255,255,0.2);
  border-radius: 2px;
  position: relative;
  transition: height 0.15s;

  .progress-wrap:hover & { height: 6px; }
}

.progress-buffered {
  position: absolute;
  top: 0;
  left: 0;
  height: 100%;
  background: rgba(255,255,255,0.3);
  border-radius: 2px;
}

.progress-played {
  position: absolute;
  top: 0;
  left: 0;
  height: 100%;
  background: linear-gradient(90deg, #6366f1, #a855f7);
  border-radius: 2px;
  display: flex;
  align-items: center;
  justify-content: flex-end;
}

.progress-thumb {
  width: 14px;
  height: 14px;
  background: #fff;
  border-radius: 50%;
  box-shadow: 0 0 6px rgba(0,0,0,0.4);
  transform: scale(0);
  transition: transform 0.15s;

  .progress-wrap:hover & { transform: scale(1); }
}

.kp-marker {
  position: absolute;
  top: 50%;
  transform: translate(-50%, -50%);
  width: 8px;
  height: 8px;
  background: #fbbf24;
  border-radius: 50%;
  border: 1.5px solid #fff;
  z-index: 2;
  cursor: pointer;
  transition: transform 0.15s;

  &:hover { transform: translate(-50%, -50%) scale(1.4); }
}

.controls-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 4px;
}

.ctrl-left, .ctrl-right {
  display: flex;
  align-items: center;
  gap: 4px;
}

.ctrl-btn {
  background: none;
  border: none;
  color: #fff;
  cursor: pointer;
  padding: 6px;
  border-radius: 4px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 13px;
  transition: background 0.15s;

  &:hover { background: rgba(255,255,255,0.15); }
  &.active { color: #a855f7; }
}

.speed-btn {
  font-weight: 600;
  min-width: 40px;
  font-size: 12px;
}

.time-display {
  color: rgba(255,255,255,0.85);
  font-size: 12px;
  font-variant-numeric: tabular-nums;
  white-space: nowrap;
  margin-left: 4px;
}

// ---- Popup Menu ----
.menu-wrap {
  position: relative;
}

.popup-menu {
  position: absolute;
  bottom: 100%;
  left: 50%;
  transform: translateX(-50%);
  background: rgba(30,30,30,0.95);
  border-radius: 8px;
  padding: 6px 0;
  margin-bottom: 8px;
  min-width: 80px;
  backdrop-filter: blur(10px);

  .menu-item {
    padding: 6px 16px;
    color: #fff;
    font-size: 13px;
    text-align: center;
    cursor: pointer;
    transition: background 0.15s;

    &:hover { background: rgba(255,255,255,0.1); }
    &.active { color: #a855f7; font-weight: 600; }
  }
}

// ---- iframe / external / empty ----
.iframe-container {
  width: 100%;
  aspect-ratio: 16 / 9;
  border-radius: 12px;
  overflow: hidden;

  iframe {
    width: 100%;
    height: 100%;
    border: none;
  }
}

.external-container {
  padding: 40px 20px;
  display: flex;
  justify-content: center;
}

.external-card {
  text-align: center;
  max-width: 400px;

  .external-icon { font-size: 56px; margin-bottom: 16px; }

  h3 { font-size: 18px; font-weight: 600; margin-bottom: 12px; color: #333; }

  p { font-size: 14px; color: #666; line-height: 1.6; margin-bottom: 24px; }

  .external-btn {
    background: linear-gradient(135deg, #6366f1, #a855f7);
    color: #fff;
    border: none;
    padding: 12px 32px;
    border-radius: 24px;
    font-size: 16px;
    font-weight: 500;
    cursor: pointer;
    transition: transform 0.2s, box-shadow 0.2s;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 6px 20px rgba(99,102,241,0.4);
    }
  }
}

.empty-container {
  padding: 40px 20px;
  background: #fff;
  border-radius: 12px;
}

// ---- Transitions ----
.ctrl-fade-enter-active, .ctrl-fade-leave-active { transition: opacity 0.3s; }
.ctrl-fade-enter-from, .ctrl-fade-leave-to { opacity: 0; }

.kp-fade-enter-active, .kp-fade-leave-active { transition: all 0.3s ease; }
.kp-fade-enter-from { opacity: 0; transform: translateY(-10px); }
.kp-fade-leave-to { opacity: 0; transform: translateY(-10px); }

.menu-fade-enter-active, .menu-fade-leave-active { transition: all 0.15s; }
.menu-fade-enter-from, .menu-fade-leave-to { opacity: 0; transform: translateX(-50%) translateY(4px); }
</style>
