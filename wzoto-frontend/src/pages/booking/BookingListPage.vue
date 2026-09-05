<template>
  <div class="booking-list-page">
    <div class="page-header">
      <h2 class="page-title">{{ userStore.isParent ? '我的预约' : '收到的预约' }}</h2>
    </div>

    <div v-if="bookings.length === 0" class="empty-state">
      <p class="empty-icon">📋</p>
      <p class="empty-text">暂无预约记录</p>
    </div>

    <div v-for="item in bookings" :key="item.id" class="card booking-card">
      <div class="booking-header">
        <span class="subject">{{ subjectLabel(item.subject) }}</span>
        <el-tag :type="statusTagType(item.status)" size="small" round>{{ item.statusDesc }}</el-tag>
      </div>

      <div class="booking-info">
        <p>📅 {{ item.bookingDate }} {{ item.startTime?.substring(0,5) }}-{{ item.endTime?.substring(0,5) }}</p>
        <p v-if="item.address">📍 {{ item.address }}</p>
        <p>{{ userStore.isParent ? '教员' : '家长' }}：{{ userStore.isParent ? item.tutorName : item.parentName }}</p>
        <p v-if="item.message" class="message">💬 {{ item.message }}</p>
        <p v-if="item.reply" class="reply">📝 {{ item.reply }}</p>
      </div>

      <div class="booking-actions">
        <!-- 教员操作：待确认 -->
        <template v-if="userStore.isStudent && item.status === 'PENDING'">
          <el-button type="primary" size="small" round @click="handleConfirm(item.id)">接受</el-button>
          <el-button size="small" round @click="handleReject(item.id)">拒绝</el-button>
        </template>
        <!-- 家长操作：待确认/已确认时可取消 -->
        <template v-if="userStore.isParent && (item.status === 'PENDING' || item.status === 'CONFIRMED')">
          <el-button type="danger" size="small" round @click="handleCancel(item.id)">取消预约</el-button>
        </template>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useUserStore } from '@/stores/user'
import { getMyBookings, confirmBooking, rejectBooking, cancelBooking } from '@/api/booking'
import { showToast } from '@/utils/toast'
import { subjectLabel } from '@/utils/format'
import { ElMessageBox } from 'element-plus'

const userStore = useUserStore()
const bookings = ref([])

onMounted(() => { loadBookings() })

async function loadBookings() {
  try {
    const res = await getMyBookings()
    bookings.value = res.data || []
  } catch {}
}

function statusTagType(status) {
  const map = { PENDING: 'warning', CONFIRMED: 'primary', COMPLETED: 'success', CANCELLED: 'info', REJECTED: 'danger' }
  return map[status] || 'info'
}

async function handleConfirm(id) {
  try {
    await confirmBooking(id)
    showToast('已接受预约')
    loadBookings()
  } catch {}
}

async function handleReject(id) {
  try {
    const { value } = await ElMessageBox.prompt('请输入拒绝原因', '拒绝预约', { confirmButtonText: '确认拒绝', cancelButtonText: '取消' })
    await rejectBooking(id, value)
    showToast('已拒绝预约')
    loadBookings()
  } catch {}
}

async function handleCancel(id) {
  try {
    await ElMessageBox.confirm('确定要取消此预约吗？', '取消预约', { type: 'warning' })
    await cancelBooking(id)
    showToast('预约已取消')
    loadBookings()
  } catch {}
}
</script>

<style lang="scss" scoped>
.booking-list-page {
  min-height: 100vh;
  background: var(--color-bg);
  padding: 16px;
}

.page-header {
  margin-bottom: 16px;
  .page-title { font-size: var(--font-size-xxl); font-weight: 700; }
}

.empty-state {
  text-align: center; padding: 60px 0;
  .empty-icon { font-size: 48px; margin-bottom: 12px; }
  .empty-text { color: var(--color-text-placeholder); }
}

.booking-card {
  .booking-header {
    display: flex; justify-content: space-between; align-items: center;
    margin-bottom: 10px;
    .subject { font-size: var(--font-size-lg); font-weight: 600; }
  }

  .booking-info {
    p { font-size: var(--font-size-sm); color: var(--color-text-secondary); line-height: 1.8; }
    .message, .reply { color: var(--color-text-placeholder); font-style: italic; }
  }

  .booking-actions {
    margin-top: 12px;
    display: flex; gap: 8px;
  }
}
</style>