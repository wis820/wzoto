<template>
  <div class="opt-history">
    <div v-if="loading" class="loading">
      <el-skeleton :rows="3" animated />
    </div>
    <div v-else-if="list.length === 0" class="empty">
      <el-empty description="暂无优化记录" />
    </div>
    <div v-else class="history-list">
      <div v-for="item in list" :key="item.id" class="history-card">
        <div class="card-header">
          <el-tag :type="item.optimizationType === 'RESUME' ? 'primary' : 'success'" size="small">
            {{ item.optimizationType === 'RESUME' ? '简历优化' : '定价分析' }}
          </el-tag>
          <span class="time">{{ formatTime(item.createdAt) }}</span>
        </div>
        <div class="card-body">
          <p v-if="item.university">{{ item.university }} · {{ item.major }}</p>
          <p v-if="item.optimizationType === 'PRICING' && item.currentRate">当前时薪：¥{{ item.currentRate }}/小时</p>
          <p class="preview">{{ item.previewContent }}</p>
        </div>
        <div class="card-footer">
          <el-tag v-if="item.paymentStatus === 'FREE'" type="success" size="small">免费</el-tag>
          <el-tag v-else-if="item.paymentStatus === 'PAID'" type="success" size="small">已解锁</el-tag>
          <el-tag v-else type="warning" size="small">待解锁 ¥{{ item.price }}</el-tag>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue'
import { getMyOptimizations } from '@/api/ai'

const props = defineProps({
  refreshKey: { type: Number, default: 0 }
})

const loading = ref(false)
const list = ref([])

watch(() => props.refreshKey, () => {
  fetchList()
}, { immediate: true })

async function fetchList() {
  loading.value = true
  try {
    const res = await getMyOptimizations()
    list.value = res.data || []
  } catch {
    list.value = []
  } finally {
    loading.value = false
  }
}

function formatTime(t) {
  if (!t) return ''
  return new Date(t).toLocaleString('zh-CN', { month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit' })
}
</script>

<style scoped lang="scss">
.opt-history {
  .history-list {
    display: flex;
    flex-direction: column;
    gap: 12px;
  }

  .history-card {
    background: #fff;
    border: 1px solid #ebeef5;
    border-radius: 8px;
    padding: 12px;

    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 8px;

      .time { font-size: 12px; color: #c0c4cc; }
    }

    .card-body {
      p { margin: 4px 0; font-size: 13px; color: #606266; }
      .preview {
        color: #909399;
        font-size: 12px;
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-line-clamp: 2;
        -webkit-box-orient: vertical;
      }
    }

    .card-footer {
      margin-top: 8px;
    }
  }
}
</style>
