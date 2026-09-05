<template>
  <div class="page achievement-page">
    <div class="page-header">
      <el-icon class="back-icon" @click="router.back()"><ArrowLeft /></el-icon>
      <h1>成长激励</h1>
    </div>

    <div class="child-selector">
      <el-select v-model="selectedChildId" placeholder="选择子女" style="width: 100%" @change="loadAchievements">
        <el-option v-for="child in children" :key="child.id" :label="child.name" :value="child.id" />
      </el-select>
    </div>

    <div class="stats-row">
      <div class="card stat-card">
        <p class="stat-value">{{ totalPoints }}</p>
        <p class="stat-label">总积分</p>
      </div>
      <div class="card stat-card">
        <p class="stat-value">{{ medalCount }}</p>
        <p class="stat-label">勋章数</p>
      </div>
    </div>

    <div class="card achievement-list">
      <h2>激励记录</h2>
      <div v-for="item in achievements" :key="item.id" class="achievement-item">
        <span class="icon">{{ typeIcon(item.achievementType) }}</span>
        <div class="info">
          <p class="name">{{ item.achievementName }}</p>
          <p class="desc">{{ item.taskDescription || item.achievementCode || '' }}</p>
        </div>
        <span class="points" v-if="item.points">+{{ item.points }}</span>
      </div>
      <el-empty v-if="achievements.length === 0" description="暂无激励记录" />
    </div>

    <div class="card task-card">
      <h2>下发激励任务</h2>
      <el-form :model="taskForm" label-position="top">
        <el-form-item label="任务名称">
          <el-input v-model="taskForm.achievementName" placeholder="例如：完成本周数学练习" />
        </el-form-item>
        <el-form-item label="任务说明">
          <el-input v-model="taskForm.taskDescription" type="textarea" rows="3" placeholder="描述任务内容" />
        </el-form-item>
        <el-form-item label="奖励积分">
          <el-input-number v-model="taskForm.points" :min="0" :max="100" />
        </el-form-item>
      </el-form>
      <el-button type="primary" style="width: 100%; margin-top: 12px" @click="assignTask">下发任务</el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ArrowLeft } from '@element-plus/icons-vue'
import { getMyChildren } from '@/api/child'
import { listAchievements, assignAchievementTask } from '@/api/learning'
import { showToast } from '@/utils/toast'
import { trackPageView, trackEvent } from '@/utils/track'

const router = useRouter()
const children = ref([])
const selectedChildId = ref(null)
const achievements = ref([])

const taskForm = reactive({
  achievementName: '',
  taskDescription: '',
  points: 10,
})

const totalPoints = computed(() => {
  return achievements.value.reduce((sum, item) => sum + (item.points || 0), 0)
})

const medalCount = computed(() => {
  return achievements.value.filter(item => item.achievementType === 'medal').length
})

onMounted(() => {
  trackPageView('achievement')
  loadChildren()
})

async function loadChildren() {
  try {
    const res = await getMyChildren()
    children.value = res.data || []
    if (children.value.length > 0) {
      selectedChildId.value = children.value[0].id
      loadAchievements()
    }
  } catch (e) {
    console.error(e)
  }
}

async function loadAchievements() {
  if (!selectedChildId.value) return
  try {
    const res = await listAchievements(selectedChildId.value)
    achievements.value = res.data || []
  } catch (e) {
    showToast(e.message || '加载失败')
  }
}

function typeIcon(type) {
  const map = {
    medal: '🏅',
    points: '💎',
    skin: '🎨',
    task: '📝',
  }
  return map[type] || '⭐'
}

async function assignTask() {
  if (!selectedChildId.value) {
    showToast('请先选择子女')
    return
  }
  if (!taskForm.achievementName) {
    showToast('请输入任务名称')
    return
  }
  try {
    await assignAchievementTask(selectedChildId.value, { ...taskForm })
    trackEvent('achievement_assign_task', { childId: selectedChildId.value })
    showToast('任务已下发')
    taskForm.achievementName = ''
    taskForm.taskDescription = ''
    taskForm.points = 10
    loadAchievements()
  } catch (e) {
    showToast(e.message || '下发失败')
  }
}
</script>

<style lang="scss" scoped>
.achievement-page {
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
    font-size: var(--font-size-xl);
    font-weight: 600;
  }

  .back-icon {
    font-size: 24px;
    cursor: pointer;
  }
}

.child-selector {
  margin-bottom: 16px;
}

.stats-row {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
  margin-bottom: 16px;
}

.stat-card {
  text-align: center;
  padding: 20px;

  .stat-value {
    font-size: 28px;
    font-weight: 700;
    color: var(--color-primary);
  }

  .stat-label {
    font-size: var(--font-size-sm);
    color: var(--color-text-secondary);
    margin-top: 4px;
  }
}

.achievement-list, .task-card {
  margin-bottom: 16px;

  h2 {
    font-size: var(--font-size-lg);
    font-weight: 600;
    margin-bottom: 16px;
  }
}

.achievement-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 0;
  border-bottom: 1px solid #f0f0f0;

  &:last-child {
    border-bottom: none;
  }

  .icon {
    font-size: 28px;
  }

  .info {
    flex: 1;

    .name {
      font-size: var(--font-size-md);
      font-weight: 600;
    }

    .desc {
      font-size: var(--font-size-sm);
      color: var(--color-text-secondary);
      margin-top: 2px;
    }
  }

  .points {
    font-size: var(--font-size-md);
    color: #faad14;
    font-weight: 600;
  }
}
</style>
