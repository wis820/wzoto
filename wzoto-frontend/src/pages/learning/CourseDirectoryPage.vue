<template>
  <div class="page course-directory-page">
    <!-- 顶部栏 -->
    <div class="page-header">
      <el-icon class="back-icon" @click="router.back()"><ArrowLeft /></el-icon>
      <h1>课程学习</h1>
      <el-select v-model="selectedChildId" placeholder="选择子女" style="width: 140px" @change="onChildChange">
        <el-option v-for="child in children" :key="child.id" :label="child.name" :value="child.id" />
      </el-select>
    </div>

    <!-- 学科 Tab -->
    <div class="subject-tabs">
      <button
        v-for="s in subjectList"
        :key="s.value"
        class="subject-tab"
        :class="{ active: currentSubject === s.value }"
        @click="switchSubject(s.value)"
      >
        <span class="tab-icon">{{ s.icon }}</span>
        <span>{{ s.label }}</span>
      </button>
    </div>

    <!-- 主体布局：左目录 + 右播放 -->
    <div class="main-layout">
      <!-- 左侧目录树 -->
      <div class="sidebar">
        <div v-if="treeLoading" class="sidebar-loading">
          <el-skeleton :rows="8" animated />
        </div>
        <div v-else-if="courseTree.length === 0" class="sidebar-empty">
          <el-empty description="暂无课程数据" :image-size="80" />
        </div>
        <div v-else class="tree-scroll">
          <div
            v-for="chapter in courseTree"
            :key="chapter.id"
            class="tree-chapter"
          >
            <div class="chapter-title" @click="toggleChapter(chapter.id)">
              <el-icon class="arrow" :class="{ expanded: expandedChapters.has(chapter.id) }">
                <ArrowRight />
              </el-icon>
              <span>{{ chapter.title }}</span>
            </div>
            <transition name="collapse">
              <div v-if="expandedChapters.has(chapter.id)" class="chapter-children">
                <!-- 子章节或资源 -->
                <template v-if="chapter.children && chapter.children.length">
                  <div
                    v-for="sub in chapter.children"
                    :key="sub.id"
                    class="tree-sub"
                  >
                    <div class="sub-title" @click="toggleChapter(sub.id)">
                      <el-icon class="arrow" :class="{ expanded: expandedChapters.has(sub.id) }">
                        <ArrowRight />
                      </el-icon>
                      <span>{{ sub.title }}</span>
                    </div>
                    <transition name="collapse">
                      <div v-if="expandedChapters.has(sub.id)" class="sub-resources">
                        <div
                          v-for="res in sub.resources"
                          :key="res.id"
                          class="resource-item"
                          :class="{
                            active: selectedResource?.id === res.id,
                            locked: res.vipOnly && !isVip,
                            completed: res.completed
                          }"
                          @click="selectResource(res)"
                        >
                          <span class="res-icon">
                            {{ res.completed ? '✅' : (res.vipOnly && !isVip ? '🔒' : '📖') }}
                          </span>
                          <span class="res-title">{{ res.title }}</span>
                          <span v-if="res.progressPercent > 0 && !res.completed" class="res-progress">
                            {{ res.progressPercent }}%
                          </span>
                        </div>
                      </div>
                    </transition>
                  </div>
                </template>
                <!-- 直接挂载资源（无子章节） -->
                <template v-else-if="chapter.resources && chapter.resources.length">
                  <div
                    v-for="res in chapter.resources"
                    :key="res.id"
                    class="resource-item"
                    :class="{
                      active: selectedResource?.id === res.id,
                      locked: res.vipOnly && !isVip,
                      completed: res.completed
                    }"
                    @click="selectResource(res)"
                  >
                    <span class="res-icon">
                      {{ res.completed ? '✅' : (res.vipOnly && !isVip ? '🔒' : '📖') }}
                    </span>
                    <span class="res-title">{{ res.title }}</span>
                    <span v-if="res.progressPercent > 0 && !res.completed" class="res-progress">
                      {{ res.progressPercent }}%
                    </span>
                  </div>
                </template>
              </div>
            </transition>
          </div>
        </div>
      </div>

      <!-- 右侧播放区 -->
      <div class="content-area">
        <div v-if="!selectedResource" class="no-selection">
          <div class="no-selection-inner">
            <div class="big-icon">📚</div>
            <h2>选择一节课开始学习</h2>
            <p>从左侧目录选择要学习的课时</p>
          </div>
        </div>

        <template v-else>
          <!-- 播放器 -->
          <OnionPlayer
            ref="playerRef"
            :src="selectedResource.contentUrl"
            :title="selectedResource.title"
            :poster="selectedResource.coverUrl"
            :subtitle-url="selectedResource.subtitleUrl"
            :quality-levels="selectedResource.qualityLevels"
            :knowledge-markers="selectedResource.knowledgeMarkers"
            :breakpoint-seconds="selectedResource.lastPositionSeconds || 0"
            :resource-id="selectedResource.id"
            :child-id="selectedChildId"
            @progress-report="onProgressReport"
            @ended="onVideoEnded"
          />

          <!-- 课程信息 -->
          <div class="course-info card">
            <h2>{{ selectedResource.title }}</h2>
            <div class="info-meta">
              <el-tag size="small" type="info">{{ gradeLabel }}</el-tag>
              <el-tag size="small">{{ subjectLabelMap[currentSubject] }}</el-tag>
              <el-tag v-if="selectedResource.sourceType" size="small" type="warning">
                {{ sourceTypeLabel(selectedResource.sourceType) }}
              </el-tag>
            </div>
            <p v-if="selectedResource.description" class="info-desc">{{ selectedResource.description }}</p>
          </div>

          <!-- 播放完成提示 -->
          <transition name="fade">
            <div v-if="showCompleteTip" class="complete-tip card">
              <div class="complete-icon">🎉</div>
              <div>
                <h3>本节学习完成！</h3>
                <p>太棒了，继续保持学习势头吧！</p>
              </div>
              <el-button type="primary" @click="goNext">下一课</el-button>
            </div>
          </transition>
        </template>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, reactive } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { ArrowLeft, ArrowRight } from '@element-plus/icons-vue'
import { getMyChildren } from '@/api/child'
import { getCourseTree, reportPlayProgress } from '@/api/learning'
import { showToast } from '@/utils/toast'
import { trackPageView, trackEvent } from '@/utils/track'
import { subjectLabel as subjectLabelFn } from '@/utils/format'
import OnionPlayer from '@/components/OnionPlayer.vue'

const router = useRouter()
const route = useRoute()

// ============ 子女选择 ============
const children = ref([])
const selectedChildId = ref(null)
const isVip = ref(false)

// ============ 学科 ============
const subjectList = [
  { value: 'MATH', label: '数学', icon: '🔢' },
  { value: 'CHINESE', label: '语文', icon: '📝' },
  { value: 'ENGLISH', label: '英语', icon: '🔤' }
]
const subjectLabelMap = { MATH: '数学', CHINESE: '语文', ENGLISH: '英语' }
const currentSubject = ref('MATH')
const grade = ref('GRADE_4')
const gradeLabel = computed(() => {
  const map = { GRADE_1: '一年级', GRADE_2: '二年级', GRADE_3: '三年级', GRADE_4: '四年级', GRADE_5: '五年级', GRADE_6: '六年级' }
  return map[grade.value] || '四年级'
})

// ============ 课程树 ============
const courseTree = ref([])
const treeLoading = ref(false)
const expandedChapters = ref(new Set())

// ============ 选中资源 ============
const selectedResource = ref(null)
const playerRef = ref(null)
const showCompleteTip = ref(false)

// ============ 从 query 初始化 ============
onMounted(() => {
  trackPageView('course_directory')
  if (route.query.subject) currentSubject.value = route.query.subject
  if (route.query.grade) grade.value = route.query.grade
  loadChildren()
})

async function loadChildren() {
  try {
    const res = await getMyChildren()
    children.value = res.data || []
    if (children.value.length > 0) {
      selectedChildId.value = children.value[0].id
      loadTree()
    }
  } catch (e) {
    showToast('加载子女信息失败')
  }
}

function onChildChange() {
  loadTree()
}

function switchSubject(subject) {
  currentSubject.value = subject
  selectedResource.value = null
  showCompleteTip.value = false
  loadTree()
}

async function loadTree() {
  if (!selectedChildId.value) return
  treeLoading.value = true
  try {
    const res = await getCourseTree({
      childId: selectedChildId.value,
      grade: grade.value,
      subject: currentSubject.value
    })
    courseTree.value = res.data || []
    // 自动展开第一个章节
    if (courseTree.value.length > 0) {
      expandedChapters.value.clear()
      expandedChapters.value.add(courseTree.value[0].id)
    }
  } catch (e) {
    courseTree.value = []
    console.error('加载课程树失败:', e)
  } finally {
    treeLoading.value = false
  }
}

function toggleChapter(id) {
  const s = new Set(expandedChapters.value)
  if (s.has(id)) s.delete(id)
  else s.add(id)
  expandedChapters.value = s
}

function selectResource(res) {
  if (res.vipOnly && !isVip.value) {
    showToast('该课程需要VIP会员才能学习')
    return
  }
  selectedResource.value = res
  showCompleteTip.value = false
  trackEvent('course_select', { resourceId: res.id, title: res.title })
}

async function onProgressReport(data) {
  try {
    await reportPlayProgress(data)
  } catch (e) {
    // 静默失败
  }
}

function onVideoEnded() {
  showCompleteTip.value = true
  trackEvent('course_complete', { resourceId: selectedResource.value?.id })
}

function goNext() {
  // 找到当前资源的下一个
  const allResources = flattenResources(courseTree.value)
  const idx = allResources.findIndex(r => r.id === selectedResource.value?.id)
  if (idx >= 0 && idx < allResources.length - 1) {
    selectResource(allResources[idx + 1])
  } else {
    showToast('已经是最后一课了')
  }
  showCompleteTip.value = false
}

function flattenResources(tree) {
  const result = []
  for (const node of tree) {
    if (node.resources) result.push(...node.resources)
    if (node.children) result.push(...flattenResources(node.children))
  }
  return result
}

function sourceTypeLabel(type) {
  const map = { MP4: 'MP4', BILIBILI: 'B站', SMARTEDU: '国家平台', UPLOAD: '上传' }
  return map[type] || type
}
</script>

<style lang="scss" scoped>
.course-directory-page {
  min-height: 100vh;
  background: var(--color-bg);
  display: flex;
  flex-direction: column;
}

.page-header {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 16px;
  background: #fff;
  border-bottom: 1px solid #f0f0f0;

  h1 {
    font-size: 18px;
    font-weight: 600;
    flex: 1;
  }

  .back-icon {
    font-size: 22px;
    cursor: pointer;
    color: #666;
  }
}

// ---- Subject Tabs ----
.subject-tabs {
  display: flex;
  gap: 0;
  background: #fff;
  border-bottom: 1px solid #f0f0f0;
  padding: 0 16px;
}

.subject-tab {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 12px 20px;
  border: none;
  background: none;
  font-size: 15px;
  color: #666;
  cursor: pointer;
  border-bottom: 2px solid transparent;
  transition: all 0.2s;

  .tab-icon { font-size: 18px; }

  &:hover { color: #333; }

  &.active {
    color: var(--color-primary, #6366f1);
    border-bottom-color: var(--color-primary, #6366f1);
    font-weight: 600;
  }
}

// ---- Main Layout ----
.main-layout {
  display: flex;
  flex: 1;
  overflow: hidden;

  @media (max-width: 768px) {
    flex-direction: column;
  }
}

// ---- Sidebar ----
.sidebar {
  width: 320px;
  min-width: 280px;
  background: #fff;
  border-right: 1px solid #f0f0f0;
  overflow-y: auto;

  @media (max-width: 768px) {
    width: 100%;
    max-height: 40vh;
    border-right: none;
    border-bottom: 1px solid #f0f0f0;
  }
}

.sidebar-loading, .sidebar-empty {
  padding: 20px;
}

.tree-scroll {
  padding: 8px 0;
}

.tree-chapter {
  margin-bottom: 2px;
}

.chapter-title {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 16px;
  font-size: 14px;
  font-weight: 600;
  color: #333;
  cursor: pointer;
  transition: background 0.15s;

  &:hover { background: #f5f7fa; }

  .arrow {
    font-size: 14px;
    transition: transform 0.2s;
    color: #999;

    &.expanded { transform: rotate(90deg); }
  }
}

.chapter-children {
  padding-left: 12px;
}

.tree-sub {
  margin-bottom: 1px;
}

.sub-title {
  display: flex;
  align-items: center;
  gap: 6px;
  padding: 8px 16px;
  font-size: 13px;
  font-weight: 500;
  color: #555;
  cursor: pointer;
  transition: background 0.15s;

  &:hover { background: #f5f7fa; }

  .arrow {
    font-size: 12px;
    transition: transform 0.2s;
    color: #bbb;

    &.expanded { transform: rotate(90deg); }
  }
}

.sub-resources {
  padding-left: 20px;
}

.resource-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 16px;
  font-size: 13px;
  color: #444;
  cursor: pointer;
  transition: all 0.15s;
  border-radius: 6px;
  margin: 1px 8px;

  &:hover { background: #f0f2ff; }

  &.active {
    background: #eef2ff;
    color: var(--color-primary, #6366f1);
    font-weight: 600;
  }

  &.locked {
    opacity: 0.5;
    cursor: not-allowed;
  }

  &.completed {
    .res-title { text-decoration: line-through; color: #999; }
  }

  .res-icon { font-size: 14px; flex-shrink: 0; }
  .res-title { flex: 1; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
  .res-progress {
    font-size: 11px;
    color: var(--color-primary, #6366f1);
    background: #eef2ff;
    padding: 1px 6px;
    border-radius: 10px;
    flex-shrink: 0;
  }
}

// ---- Content Area ----
.content-area {
  flex: 1;
  overflow-y: auto;
  padding: 16px;
}

.no-selection {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  min-height: 400px;

  .no-selection-inner {
    text-align: center;

    .big-icon { font-size: 72px; margin-bottom: 20px; }
    h2 { font-size: 20px; font-weight: 600; color: #333; margin-bottom: 8px; }
    p { color: #999; font-size: 14px; }
  }
}

.course-info {
  margin-top: 16px;

  h2 {
    font-size: 18px;
    font-weight: 600;
    margin-bottom: 10px;
  }

  .info-meta {
    display: flex;
    gap: 8px;
    flex-wrap: wrap;
    margin-bottom: 10px;
  }

  .info-desc {
    font-size: 14px;
    color: #666;
    line-height: 1.6;
  }
}

.complete-tip {
  margin-top: 16px;
  display: flex;
  align-items: center;
  gap: 16px;
  background: linear-gradient(135deg, #f0fdf4, #ecfdf5);
  border: 1px solid #bbf7d0;

  .complete-icon { font-size: 40px; }

  h3 {
    font-size: 16px;
    font-weight: 600;
    color: #166534;
    margin-bottom: 4px;
  }

  p {
    font-size: 13px;
    color: #4ade80;
  }
}

// ---- Transitions ----
.collapse-enter-active, .collapse-leave-active {
  transition: all 0.2s ease;
  overflow: hidden;
}
.collapse-enter-from, .collapse-leave-to {
  opacity: 0;
  max-height: 0;
}
.collapse-enter-to, .collapse-leave-from {
  max-height: 2000px;
}

.fade-enter-active, .fade-leave-active { transition: opacity 0.3s; }
.fade-enter-from, .fade-leave-to { opacity: 0; }
</style>
