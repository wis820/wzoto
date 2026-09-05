<template>
  <div class="page student-course-page">
    <div class="page-header">
      <el-icon class="back-icon" @click="router.back()"><ArrowLeft /></el-icon>
      <h1>课程专区</h1>
    </div>

    <div class="filter-bar">
      <el-select v-model="filter.grade" placeholder="年级" @change="loadCourses">
        <el-option v-for="g in 6" :key="g" :label="`${g}年级`" :value="g" />
      </el-select>
      <el-select v-model="filter.subject" placeholder="学科" clearable @change="loadCourses">
        <el-option label="数学" value="MATH" />
        <el-option label="语文" value="CHINESE" />
        <el-option label="英语" value="ENGLISH" />
      </el-select>
    </div>

    <!-- 视频播放区 -->
    <div v-if="playingCourse" class="player-section">
      <VideoPlayer
        :src="playingCourse.videoUrl"
        :poster="playingCourse.coverUrl"
        @progress="onProgress"
        @ended="onVideoEnded"
      />
      <div class="playing-info">
        <h3>{{ playingCourse.title }}</h3>
        <p>{{ subjectLabel(playingCourse.subject) }} · {{ playingCourse.description }}</p>
      </div>
    </div>

    <!-- 课程列表 -->
    <div class="course-list">
      <div
        v-for="course in courses"
        :key="course.id"
        class="card course-card"
        :class="{ active: playingCourse?.id === course.id }"
        @click="playCourse(course)"
      >
        <div class="course-cover" :style="{ backgroundImage: `url(${course.coverUrl || ''})` }">
          <div class="play-overlay">▶</div>
          <span class="duration">{{ formatDuration(course.durationSeconds) }}</span>
          <el-tag v-if="course.vipOnly" class="vip-tag" type="warning" size="small">VIP</el-tag>
        </div>
        <div class="course-info">
          <h4>{{ course.title }}</h4>
          <p class="course-meta">{{ subjectLabel(course.subject) }} · 播放 {{ course.playCount || 0 }}</p>
        </div>
      </div>
      <el-empty v-if="courses.length === 0" description="暂无课程" />
    </div>

    <!-- VIP弹窗 -->
    <MemberUpgradePopup
      :visible="showVipPopup"
      feature-name="VIP课程"
      :features="['全部微课视频', '名师讲解', '知识点全覆盖']"
      :is-parent="true"
      @close="showVipPopup = false"
    />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ArrowLeft } from '@element-plus/icons-vue'
import VideoPlayer from './components/VideoPlayer.vue'
import MemberUpgradePopup from '@/components/MemberUpgradePopup.vue'
import { listCourses, getCourseDetail, reportCourseProgress } from '@/api/student'
import { showToast } from '@/utils/toast'
import { subjectLabel } from '@/utils/format'

const router = useRouter()
const route = useRoute()
const courses = ref([])
const playingCourse = ref(null)
const showVipPopup = ref(false)

const filter = reactive({ grade: 1, subject: '' })

onMounted(() => { loadCourses() })

async function loadCourses() {
  try {
    const params = {}
    if (filter.grade) params.grade = filter.grade
    if (filter.subject) params.subject = filter.subject
    const res = await listCourses(params)
    courses.value = res.data || []
    if (route.query.highlight && courses.value.length > 0) {
      const target = courses.value.find(c => c.id == route.query.highlight)
      if (target) playCourse(target)
    }
  } catch (e) {
    showToast(e.message || '加载失败')
  }
}

async function playCourse(course) {
  if (course.vipOnly) {
    showVipPopup.value = true
    return
  }
  try {
    const res = await getCourseDetail(course.id)
    playingCourse.value = res.data || course
  } catch {
    playingCourse.value = course
  }
}

function onProgress(data) {
  if (!playingCourse.value) return
  reportCourseProgress({
    courseId: playingCourse.value.id,
    currentTime: Math.round(data.currentTime),
    duration: Math.round(data.duration),
    percent: Math.round(data.percent),
  }).catch(() => {})
}

function onVideoEnded() {
  showToast('课程观看完成')
}

function formatDuration(sec) {
  if (!sec) return ''
  const m = Math.floor(sec / 60)
  const s = sec % 60
  return `${m}:${s.toString().padStart(2, '0')}`
}
</script>

<style lang="scss" scoped>
.student-course-page {
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
  display: flex; gap: 8px; margin-bottom: 16px;
  .el-select { flex: 1; }
}

.player-section {
  margin-bottom: 16px;
  .playing-info {
    padding: 12px 0;
    h3 { font-size: var(--font-size-md); font-weight: 600; }
    p { font-size: var(--font-size-sm); color: var(--color-text-secondary); }
  }
}

.course-list {
  display: flex; flex-direction: column; gap: 12px;
}

.course-card {
  display: flex; gap: 12px; cursor: pointer;
  &.active { border: 2px solid var(--color-primary); }
  .course-cover {
    width: 120px; height: 80px; border-radius: var(--radius-sm);
    background: #eee center/cover no-repeat; position: relative; flex-shrink: 0;
    .play-overlay {
      position: absolute; inset: 0; display: flex; align-items: center; justify-content: center;
      background: rgba(0,0,0,0.2); color: #fff; font-size: 24px; opacity: 0;
      transition: opacity 0.2s;
    }
    &:hover .play-overlay { opacity: 1; }
    .duration {
      position: absolute; bottom: 4px; right: 4px;
      font-size: 10px; color: #fff; background: rgba(0,0,0,0.6);
      padding: 1px 4px; border-radius: 3px;
    }
    .vip-tag { position: absolute; top: 4px; left: 4px; }
  }
  .course-info {
    flex: 1; min-width: 0;
    h4 { font-size: var(--font-size-md); font-weight: 500; margin-bottom: 4px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
    .course-meta { font-size: var(--font-size-xs); color: var(--color-text-secondary); }
  }
}
</style>
