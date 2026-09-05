<template>
  <div class="page learning-resource-page">
    <div class="page-header">
      <el-icon class="back-icon" @click="router.back()"><ArrowLeft /></el-icon>
      <h1>学习资源</h1>
    </div>

    <div class="child-selector">
      <el-select v-model="selectedChildId" placeholder="选择子女" style="width: 100%" @change="onChildChange">
        <el-option v-for="child in children" :key="child.id" :label="child.name" :value="child.id" />
      </el-select>
    </div>

    <div class="filter-bar">
      <el-select v-model="filter.subject" placeholder="学科" clearable @change="loadResources">
        <el-option label="语文" value="CHINESE" />
        <el-option label="数学" value="MATH" />
        <el-option label="英语" value="ENGLISH" />
      </el-select>
      <el-select v-model="filter.resourceType" placeholder="类型" clearable @change="loadResources">
        <el-option label="动画微课" value="VIDEO" />
        <el-option label="互动练习" value="EXERCISE" />
        <el-option label="PDF资料" value="PDF" />
      </el-select>
      <el-checkbox v-model="filter.includeVip" @change="loadResources">含会员</el-checkbox>
    </div>

    <div class="resource-list">
      <div v-for="res in resources" :key="res.id" class="card resource-card" @click="openResource(res)">
        <div class="cover">
          <img v-if="res.coverUrl" :src="res.coverUrl" />
          <span v-else class="cover-placeholder">📚</span>
          <span v-if="res.vipOnly" class="vip-badge">VIP</span>
        </div>
        <div class="info">
          <p class="title">{{ res.title }}</p>
          <p class="meta">{{ res.gradeDesc }} · {{ subjectLabel(res.subject) }} · {{ res.resourceTypeDesc }}</p>
          <p class="knowledge">{{ res.knowledgePoint }}</p>
        </div>
      </div>
      <el-empty v-if="resources.length === 0" description="暂无资源" />
    </div>

    <el-dialog v-model="detailVisible" title="资源详情" width="90%" class="mobile-dialog">
      <div v-if="selectedResource" class="resource-detail">
        <h2>{{ selectedResource.title }}</h2>
        <p class="meta">{{ selectedResource.gradeDesc }} · {{ subjectLabel(selectedResource.subject) }} · {{ selectedResource.resourceTypeDesc }}</p>
        <p v-if="selectedResource.knowledgePoint" class="knowledge">知识点：{{ selectedResource.knowledgePoint }}</p>
        <div v-if="selectedResource.vipOnly" class="vip-tip">
          <p>该资源为 VIP 专属</p>
          <el-button type="primary" @click="handleUnlock">开通学习会员解锁</el-button>
        </div>
        <div v-else class="actions">
          <el-button type="primary" @click="startLearn">开始学习</el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ArrowLeft } from '@element-plus/icons-vue'
import { getMyChildren } from '@/api/child'
import { listLearningResources, unlockLearningResource } from '@/api/learning'
import { showToast } from '@/utils/toast'
import { trackPageView, trackEvent } from '@/utils/track'
import { subjectLabel } from '@/utils/format'

const router = useRouter()
const children = ref([])
const selectedChildId = ref(null)
const resources = ref([])
const detailVisible = ref(false)
const selectedResource = ref(null)

const filter = reactive({
  subject: '',
  resourceType: '',
  includeVip: false,
})

onMounted(() => {
  trackPageView('learning_resource')
  loadChildren()
})

async function loadChildren() {
  try {
    const res = await getMyChildren()
    children.value = res.data || []
    if (children.value.length > 0) {
      selectedChildId.value = children.value[0].id
      loadResources()
    }
  } catch (e) {
    console.error(e)
  }
}

function onChildChange() {
  loadResources()
}

async function loadResources() {
  if (!selectedChildId.value) return
  try {
    const params = {
      childId: selectedChildId.value,
      includeVip: filter.includeVip,
    }
    if (filter.subject) params.subject = filter.subject
    if (filter.resourceType) params.resourceType = filter.resourceType
    const res = await listLearningResources(params)
    resources.value = res.data || []
  } catch (e) {
    showToast(e.message || '加载失败')
  }
}

function openResource(res) {
  selectedResource.value = res
  detailVisible.value = true
  trackEvent('resource_click', { resourceId: res.id })
}

async function handleUnlock() {
  if (!selectedResource.value) return
  try {
    const res = await unlockLearningResource(selectedResource.value.id)
    trackEvent('resource_unlock_click', { resourceId: selectedResource.value.id })
    if (res.data) {
      showToast('解锁订单已创建，请前往支付')
      detailVisible.value = false
    } else {
      showToast('已解锁')
      loadResources()
      detailVisible.value = false
    }
  } catch (e) {
    showToast(e.message || '解锁失败')
  }
}

function startLearn() {
  if (!selectedResource.value) return
  detailVisible.value = false
  router.push({
    path: '/learning/video-player',
    query: { id: selectedResource.value.id, childId: selectedChildId.value }
  })
}
</script>

<style lang="scss" scoped>
.learning-resource-page {
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

.filter-bar {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 16px;
  flex-wrap: wrap;

  .el-select {
    width: 120px;
  }
}

.resource-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.resource-card {
  display: flex;
  gap: 12px;
  cursor: pointer;

  &:active {
    opacity: 0.85;
  }

  .cover {
    width: 100px;
    height: 75px;
    border-radius: 8px;
    background: #f5f7fa;
    display: flex;
    align-items: center;
    justify-content: center;
    position: relative;
    overflow: hidden;

    img {
      width: 100%;
      height: 100%;
      object-fit: cover;
    }

    .cover-placeholder {
      font-size: 36px;
    }

    .vip-badge {
      position: absolute;
      top: 4px;
      right: 4px;
      background: #ffd700;
      color: #8b4513;
      font-size: 10px;
      padding: 2px 6px;
      border-radius: 4px;
      font-weight: 600;
    }
  }

  .info {
    flex: 1;

    .title {
      font-size: var(--font-size-md);
      font-weight: 600;
      margin-bottom: 4px;
    }

    .meta {
      font-size: var(--font-size-xs);
      color: var(--color-text-secondary);
      margin-bottom: 4px;
    }

    .knowledge {
      font-size: var(--font-size-xs);
      color: var(--color-primary);
    }
  }
}

.resource-detail {
  text-align: center;

  h2 {
    font-size: var(--font-size-lg);
    font-weight: 600;
    margin-bottom: 8px;
  }

  .meta {
    font-size: var(--font-size-sm);
    color: var(--color-text-secondary);
    margin-bottom: 8px;
  }

  .knowledge {
    font-size: var(--font-size-sm);
    margin-bottom: 16px;
  }

  .vip-tip {
    background: #fffbe6;
    border: 1px solid #ffe58f;
    border-radius: 8px;
    padding: 16px;
    margin-bottom: 16px;

    p {
      margin-bottom: 12px;
      color: #8b4513;
    }
  }
}
</style>
