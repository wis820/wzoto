<template>
  <div class="feature-page">
    <div class="page-header">
      <h2>增值功能</h2>
      <p>提升竞争力，加速接单</p>
    </div>

    <!-- 功能卡片列表 -->
    <div class="feature-list">
      <!-- 简历置顶 -->
      <div class="feature-card pin-card">
        <div class="card-header">
          <span class="card-icon">📌</span>
          <div class="card-info">
            <h3>简历置顶</h3>
            <p>7天内排序优先展示，曝光量提升300%</p>
          </div>
        </div>
        <div class="card-body">
          <div class="price">
            <span class="amount">¥9.90</span>
            <span class="unit">/7天</span>
          </div>
          <div v-if="isPinned" class="status-badge active">
            已置顶 · 到期 {{ formatDate(pinnedUntil) }}
          </div>
          <el-button
            v-else
            type="primary"
            :loading="pinLoading"
            @click="handleResumePin"
          >
            立即购买
          </el-button>
        </div>
      </div>

      <!-- 加急审核 -->
      <div class="feature-card expedite-card">
        <div class="card-header">
          <span class="card-icon">⚡</span>
          <div class="card-info">
            <h3>加急审核</h3>
            <p>认证审核优先处理，快速通过</p>
          </div>
        </div>
        <div class="card-body">
          <div class="price">
            <span class="amount">¥9.90</span>
            <span class="unit">/次</span>
          </div>
          <div v-if="verifyExpedited" class="status-badge active">
            已加急 · 审核优先处理中
          </div>
          <el-button
            v-else
            type="warning"
            :loading="expediteLoading"
            @click="handleExpediteVerify"
          >
            立即购买
          </el-button>
        </div>
      </div>
    </div>

    <!-- 订单记录 -->
    <div class="order-section">
      <h3>购买记录</h3>
      <div v-if="orders.length === 0" class="empty-tip">暂无购买记录</div>
      <div v-else class="order-list">
        <div v-for="order in orders" :key="order.id" class="order-item">
          <div class="order-info">
            <span class="order-type">{{ order.featureTypeDesc }}</span>
            <span class="order-time">{{ formatDate(order.createdAt) }}</span>
          </div>
          <div class="order-right">
            <span class="order-amount">¥{{ order.amount }}</span>
            <el-tag :type="order.paymentStatus === 'PAID' ? 'success' : 'warning'" size="small">
              {{ order.paymentStatusDesc }}
            </el-tag>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { createResumePinOrder, createExpediteVerifyOrder, payFeatureOrder, getMyFeatureOrders } from '@/api/feature'
import { getMyTutorProfile } from '@/api/tutor'
import { getVerifyStatus } from '@/api/verify'

const pinLoading = ref(false)
const expediteLoading = ref(false)
const orders = ref([])
const verifyExpedited = ref(false)
const isPinned = ref(false)
const pinnedUntil = ref(null)

onMounted(async () => {
  await loadOrders()
  await loadProfile()
})

async function loadOrders() {
  try {
    const res = await getMyFeatureOrders()
    if (res.code === 200) {
      orders.value = res.data || []
    }
  } catch (e) {
    console.error('加载订单失败', e)
  }
}

async function loadProfile() {
  try {
    // 加载教员档案置顶状态
    const profileRes = await getMyTutorProfile()
    if (profileRes.code === 200 && profileRes.data) {
      isPinned.value = profileRes.data.isPinned || false
      pinnedUntil.value = profileRes.data.pinnedUntil || null
    }
    // 加载认证加急状态
    const verifyRes = await getVerifyStatus()
    if (verifyRes.code === 200 && verifyRes.data) {
      verifyExpedited.value = verifyRes.data.expedited || false
    }
  } catch (e) {
    console.error('加载档案失败', e)
  }
}

async function handleResumePin() {
  try {
    await ElMessageBox.confirm(
      '购买后简历将置顶展示7天，确认购买？',
      '简历置顶',
      { confirmButtonText: '确认支付 ¥9.90', cancelButtonText: '取消' }
    )
    pinLoading.value = true
    // 创建订单
    const createRes = await createResumePinOrder()
    if (createRes.code === 200) {
      // 模拟支付
      const payRes = await payFeatureOrder(createRes.data.id)
      if (payRes.code === 200) {
        ElMessage.success('置顶成功！简历将在7天内优先展示')
        await loadOrders()
        await loadProfile()
      }
    }
  } catch (e) {
    if (e !== 'cancel') {
      ElMessage.error(e.response?.data?.msg || e.message || '操作失败')
    }
  } finally {
    pinLoading.value = false
  }
}

async function handleExpediteVerify() {
  try {
    await ElMessageBox.confirm(
      '购买后认证审核将优先处理，确认购买？',
      '加急审核',
      { confirmButtonText: '确认支付 ¥9.90', cancelButtonText: '取消' }
    )
    expediteLoading.value = true
    // 创建订单
    const createRes = await createExpediteVerifyOrder()
    if (createRes.code === 200) {
      // 模拟支付
      const payRes = await payFeatureOrder(createRes.data.id)
      if (payRes.code === 200) {
        ElMessage.success('加急成功！认证审核将优先处理')
        await loadOrders()
        verifyExpedited.value = true
      }
    }
  } catch (e) {
    if (e !== 'cancel') {
      ElMessage.error(e.response?.data?.msg || e.message || '操作失败')
    }
  } finally {
    expediteLoading.value = false
  }
}

function formatDate(dateStr) {
  if (!dateStr) return '-'
  const d = new Date(dateStr)
  return `${d.getMonth() + 1}/${d.getDate()} ${d.getHours()}:${String(d.getMinutes()).padStart(2, '0')}`
}
</script>

<style scoped lang="scss">
.feature-page {
  max-width: 600px;
  margin: 0 auto;
  padding: 16px;

  .page-header {
    text-align: center;
    margin-bottom: 20px;

    h2 { margin: 0 0 8px; font-size: 20px; color: #303133; }
    p { margin: 0; font-size: 13px; color: #909399; }
  }
}

.feature-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
  margin-bottom: 24px;
}

.feature-card {
  border-radius: 12px;
  padding: 16px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);

  .card-header {
    display: flex;
    align-items: center;
    gap: 12px;
    margin-bottom: 12px;

    .card-icon { font-size: 32px; }
    .card-info {
      h3 { margin: 0; font-size: 16px; }
      p { margin: 4px 0 0; font-size: 12px; color: #909399; }
    }
  }

  .card-body {
    display: flex;
    align-items: center;
    justify-content: space-between;

    .price {
      .amount { font-size: 24px; font-weight: 700; color: #e74c3c; }
      .unit { font-size: 12px; color: #909399; margin-left: 2px; }
    }

    .status-badge {
      padding: 4px 12px;
      border-radius: 12px;
      font-size: 12px;
      &.active {
        background: #f0f9eb;
        color: #67c23a;
      }
    }
  }
}

.pin-card {
  border-left: 4px solid #409eff;
  background: linear-gradient(135deg, #ecf5ff, #ffffff);
}

.expedite-card {
  border-left: 4px solid #e6a23c;
  background: linear-gradient(135deg, #fdf6ec, #ffffff);
}

.order-section {
  h3 {
    font-size: 16px;
    margin: 0 0 12px;
    color: #303133;
  }

  .empty-tip {
    text-align: center;
    color: #909399;
    padding: 24px;
    font-size: 14px;
  }

  .order-list {
    .order-item {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 12px 0;
      border-bottom: 1px solid #f0f0f0;

      &:last-child { border-bottom: none; }

      .order-info {
        .order-type { font-size: 14px; font-weight: 500; display: block; }
        .order-time { font-size: 12px; color: #909399; }
      }

      .order-right {
        display: flex;
        align-items: center;
        gap: 8px;
        .order-amount { font-size: 14px; font-weight: 600; color: #e74c3c; }
      }
    }
  }
}
</style>
