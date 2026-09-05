<template>
  <div class="video-player" ref="playerRef">
    <div class="video-wrapper">
      <video
        ref="videoRef"
        :src="src"
        :poster="poster"
        @timeupdate="onTimeUpdate"
        @ended="onEnded"
        @loadedmetadata="onLoaded"
        playsinline
        webkit-playsinline
      />
      <div class="video-overlay" v-if="!isPlaying" @click="togglePlay">
        <div class="play-btn">▶</div>
      </div>
    </div>
    <div class="video-controls">
      <span class="time">{{ formatTime(currentTime) }}</span>
      <div class="progress-bar" ref="progressRef" @click="seekTo">
        <div class="progress-fill" :style="{ width: progressPercent + '%' }" />
      </div>
      <span class="time">{{ formatTime(duration) }}</span>
      <select class="speed-select" v-model="playbackRate" @change="changeSpeed">
        <option :value="0.75">0.75x</option>
        <option :value="1">1x</option>
        <option :value="1.25">1.25x</option>
        <option :value="1.5">1.5x</option>
        <option :value="2">2x</option>
      </select>
      <button class="fullscreen-btn" @click="toggleFullscreen">⛶</button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onBeforeUnmount } from 'vue'

const props = defineProps({
  src: { type: String, required: true },
  poster: { type: String, default: '' },
  reportInterval: { type: Number, default: 30 },
})

const emit = defineEmits(['progress', 'ended', 'play', 'pause'])

const videoRef = ref(null)
const playerRef = ref(null)
const progressRef = ref(null)
const isPlaying = ref(false)
const currentTime = ref(0)
const duration = ref(0)
const playbackRate = ref(1)
let reportTimer = null
let lastReportTime = 0

const progressPercent = computed(() => {
  if (duration.value === 0) return 0
  return (currentTime.value / duration.value) * 100
})

function togglePlay() {
  if (!videoRef.value) return
  if (isPlaying.value) {
    videoRef.value.pause()
    isPlaying.value = false
    emit('pause')
    stopReportTimer()
  } else {
    videoRef.value.play()
    isPlaying.value = true
    emit('play')
    startReportTimer()
  }
}

function onTimeUpdate() {
  if (!videoRef.value) return
  currentTime.value = videoRef.value.currentTime
}

function onLoaded() {
  if (!videoRef.value) return
  duration.value = videoRef.value.duration
}

function onEnded() {
  isPlaying.value = false
  emit('ended')
  stopReportTimer()
  reportProgress()
}

function seekTo(e) {
  if (!videoRef.value || !progressRef.value || duration.value === 0) return
  const rect = progressRef.value.getBoundingClientRect()
  const ratio = (e.clientX - rect.left) / rect.width
  videoRef.value.currentTime = ratio * duration.value
}

function changeSpeed() {
  if (videoRef.value) {
    videoRef.value.playbackRate = playbackRate.value
  }
}

function toggleFullscreen() {
  if (!playerRef.value) return
  if (document.fullscreenElement) {
    document.exitFullscreen()
  } else {
    playerRef.value.requestFullscreen()
  }
}

function formatTime(seconds) {
  const m = Math.floor(seconds / 60)
  const s = Math.floor(seconds % 60)
  return `${m.toString().padStart(2, '0')}:${s.toString().padStart(2, '0')}`
}

function startReportTimer() {
  stopReportTimer()
  reportTimer = setInterval(() => {
    reportProgress()
  }, props.reportInterval * 1000)
}

function stopReportTimer() {
  if (reportTimer) {
    clearInterval(reportTimer)
    reportTimer = null
  }
}

function reportProgress() {
  if (!videoRef.value) return
  const now = currentTime.value
  if (now > lastReportTime) {
    emit('progress', { currentTime: now, duration: duration.value, percent: progressPercent.value })
    lastReportTime = now
  }
}

onBeforeUnmount(() => {
  stopReportTimer()
  reportProgress()
})
</script>

<style lang="scss" scoped>
.video-player {
  width: 100%;
  background: #000;
  border-radius: var(--radius-md);
  overflow: hidden;
}

.video-wrapper {
  position: relative;
  width: 100%;
  aspect-ratio: 16/9;

  video {
    width: 100%;
    height: 100%;
    object-fit: contain;
  }

  .video-overlay {
    position: absolute;
    inset: 0;
    display: flex;
    align-items: center;
    justify-content: center;
    background: rgba(0, 0, 0, 0.3);
    cursor: pointer;

    .play-btn {
      width: 56px;
      height: 56px;
      border-radius: 50%;
      background: rgba(255, 255, 255, 0.9);
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 24px;
      color: var(--color-primary);
      padding-left: 4px;
    }
  }
}

.video-controls {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 12px;
  background: #1a1a1a;

  .time {
    font-size: var(--font-size-xs);
    color: #ccc;
    min-width: 40px;
    text-align: center;
  }

  .progress-bar {
    flex: 1;
    height: 4px;
    background: #444;
    border-radius: 2px;
    cursor: pointer;
    position: relative;

    .progress-fill {
      height: 100%;
      background: var(--color-primary);
      border-radius: 2px;
      transition: width 0.1s;
    }
  }

  .speed-select {
    background: transparent;
    color: #ccc;
    border: 1px solid #555;
    border-radius: 4px;
    font-size: var(--font-size-xs);
    padding: 2px 4px;

    option { background: #333; }
  }

  .fullscreen-btn {
    background: transparent;
    border: none;
    color: #ccc;
    font-size: 18px;
    cursor: pointer;
    padding: 4px;
  }
}
</style>
