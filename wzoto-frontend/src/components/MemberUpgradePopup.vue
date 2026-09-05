<template>
  <div class="member-upgrade-popup" v-if="visible" @click.self="handleClose">
    <div class="popup-content">
      <div class="popup-header">
        <span class="lock-icon">🔒</span>
        <h3 class="popup-title">解锁{{ featureName }}</h3>
        <p class="popup-subtitle">开通会员即可使用</p>
      </div>
      <div class="popup-features">
        <div class="feature-item" v-for="(feat, idx) in features" :key="idx">
          <span class="check-icon">✅</span>
          <span>{{ feat }}</span>
        </div>
      </div>
      <div class="popup-price" v-if="isParent">
        💎 月卡 ¥39/月 | 年卡 ¥299/年（省¥169）
      </div>
      <div class="popup-price" v-else>
        💎 月卡 ¥29/月
      </div>
      <!-- AI报告特殊：双选择弹窗 -->
      <div v-if="showAiOptions" class="ai-options">
        <div class="ai-option-card" @click="handleBuySingle">
          <p class="option-label">单次购买</p>
          <p class="option-price">¥19.9 / 次</p>
          <el-button type="warning" plain size="small">单次购买</el-button>
        </div>
        <div class="ai-option-card ai-option-vip" @click="goVip">
          <p class="option-label">开通会员</p>
          <p class="option-price">💎 月卡¥39 | 年卡¥299</p>
          <p class="option-desc">每月2份免费 + 全部会员权益</p>
          <el-button type="warning" size="small">开通会员</el-button>
        </div>
      </div>
      <div v-else class="popup-actions">
        <el-button @click="handleClose">取消</el-button>
        <el-button type="warning" @click="goVip">去开通会员</el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const props = defineProps({
  visible: { type: Boolean, default: false },
  featureName: { type: String, default: '会员功能' },
  features: { type: Array, default: () => [] },
  isParent: { type: Boolean, default: true },
  showAiOptions: { type: Boolean, default: false },
})

const emit = defineEmits(['close', 'buy-single', 'go-vip'])
const router = useRouter()

function handleClose() {
  emit('close')
}

function goVip() {
  emit('go-vip')
  handleClose()
  // TODO: 跳转会员开通页
}

function handleBuySingle() {
  emit('buy-single')
  handleClose()
}
</script>

<style lang="scss" scoped>
.member-upgrade-popup {
  position: fixed;
  top: 0; left: 0; right: 0; bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 9999;
  animation: fadeIn 0.2s;
}

.popup-content {
  background: #fff;
  border-radius: 16px;
  width: 320px;
  padding: 24px;
  animation: slideUp 0.3s ease-out;
}

.popup-header {
  text-align: center;
  margin-bottom: 16px;

  .lock-icon { font-size: 36px; }
  .popup-title { font-size: 18px; font-weight: 600; color: #333; margin: 8px 0 4px; }
  .popup-subtitle { font-size: 13px; color: #999; }
}

.popup-features {
  border-top: 1px solid #f0f0f0;
  border-bottom: 1px solid #f0f0f0;
  padding: 12px 0;
  margin-bottom: 12px;

  .feature-item {
    display: flex;
    align-items: center;
    gap: 8px;
    padding: 6px 0;
    font-size: 14px;
    color: #333;

    .check-icon { color: #52C41A; }
  }
}

.popup-price {
  text-align: center;
  font-size: 13px;
  color: #FF8C2E;
  font-weight: 600;
  margin-bottom: 16px;
}

.ai-options {
  display: flex;
  flex-direction: column;
  gap: 12px;

  .ai-option-card {
    border: 1px solid #eee;
    border-radius: 12px;
    padding: 12px 16px;
    text-align: center;
    cursor: pointer;
    transition: border-color 0.2s;

    &:active { border-color: #FF8C2E; }

    .option-label { font-size: 14px; font-weight: 600; color: #333; }
    .option-price { font-size: 16px; color: #FF8C2E; font-weight: 600; margin: 4px 0; }
    .option-desc { font-size: 11px; color: #999; margin-bottom: 8px; }
  }

  .ai-option-vip {
    background: linear-gradient(135deg, #FFF7E6, #FFF1CC);
    border-color: #FFD700;
  }
}

.popup-actions {
  display: flex;
  gap: 12px;

  .el-button { flex: 1; }
}

@keyframes fadeIn { from { opacity: 0; } to { opacity: 1; } }
@keyframes slideUp { from { transform: translateY(20px); opacity: 0; } to { transform: translateY(0); opacity: 1; } }
</style>
