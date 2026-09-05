<template>
  <div class="page learning-task-page">
    <div class="page-header">
      <el-icon class="back-icon" @click="router.back()"><ArrowLeft /></el-icon>
      <h1>学习任务</h1>
    </div>

    <div class="child-selector">
      <el-select v-model="selectedChildId" placeholder="选择子女" style="width: 100%" @change="loadTasks">
        <el-option v-for="child in children" :key="child.id" :label="child.name" :value="child.id" />
      </el-select>
    </div>

    <div class="date-picker">
      <el-date-picker v-model="selectedDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" style="width: 100%" @change="loadTasks" />
    </div>

    <div class="task-list">
      <div v-for="task in tasks" :key="task.id" class="card task-card">
        <div class="task-main">
          <span class="task-icon">{{ taskIcon(task.taskType) }}</span>
          <div class="info">
            <p class="title">{{ task.title }}</p>
            <p class="meta">{{ task.taskTypeDesc }} · {{ subjectLabel(task.subject) }}</p>
          </div>
        </div>
        <el-button v-if="task.status !== 'COMPLETED'" type="primary" size="small" @click="completeTask(task.id)">完成</el-button>
        <el-tag v-else type="success" size="small">已完成</el-tag>
      </div>
      <el-empty v-if="tasks.length === 0" description="暂无任务" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ArrowLeft } from '@element-plus/icons-vue'
import { getMyChildren } from '@/api/child'
import { listLearningTasks, completeLearningTask } from '@/api/learning'
import { showToast } from '@/utils/toast'
import { trackPageView, trackEvent } from '@/utils/track'
import { subjectLabel } from '@/utils/format'

const router = useRouter()
const children = ref([])
const selectedChildId = ref(null)
const selectedDate = ref('')
const tasks = ref([])

onMounted(() => {
  trackPageView('learning_task')
  selectedDate.value = new Date().toISOString().split('T')[0]
  loadChildren()
})

async function loadChildren() {
  try {
    const res = await getMyChildren()
    children.value = res.data || []
    if (children.value.length > 0) {
      selectedChildId.value = children.value[0].id
      loadTasks()
    }
  } catch (e) {
    console.error(e)
  }
}

async function loadTasks() {
  if (!selectedChildId.value) return
  try {
    const res = await listLearningTasks(selectedChildId.value, selectedDate.value)
    tasks.value = res.data || []
  } catch (e) {
    showToast(e.message || '加载失败')
  }
}

async function completeTask(taskId) {
  try {
    await completeLearningTask(taskId)
    trackEvent('task_complete', { taskId })
    showToast('任务已完成')
    loadTasks()
  } catch (e) {
    showToast(e.message || '操作失败')
  }
}

function taskIcon(type) {
  if (type?.includes('VIDEO')) return '📹'
  if (type?.includes('EXERCISE')) return '✏️'
  if (type?.includes('REVIEW')) return '🔄'
  return '📝'
}
</script>

<style lang="scss" scoped>
.learning-task-page {
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
  margin-bottom: 12px;
}

.date-picker {
  margin-bottom: 16px;
}

.task-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.task-card {
  display: flex;
  align-items: center;
  justify-content: space-between;

  .task-main {
    display: flex;
    align-items: center;
    gap: 12px;

    .task-icon {
      font-size: 28px;
    }

    .title {
      font-size: var(--font-size-md);
      font-weight: 600;
    }

    .meta {
      font-size: var(--font-size-xs);
      color: var(--color-text-secondary);
      margin-top: 2px;
    }
  }
}
</style>
