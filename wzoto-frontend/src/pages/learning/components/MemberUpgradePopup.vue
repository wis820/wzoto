<template>
  <el-dialog v-model="visible" title="开通学习会员" width="90%" class="mobile-dialog member-popup" :show-close="false">
    <div class="member-content">
      <div class="member-icon">👑</div>
      <h2>解锁全部学习权益</h2>
      <ul class="benefit-list">
        <li>📚 全年级全科动画微课</li>
        <li>✏️ 无限互动练习与题库</li>
        <li>🤖 AI 答疑分步启发</li>
        <li>❌ 错题本导出/打印</li>
        <li>📊 完整学情日报/周报/月报</li>
      </ul>
      <div class="price-options">
        <div class="price-card" :class="{ active: selected === 'month' }" @click="selected = 'month'">
          <p class="duration">月卡</p>
          <p class="price">¥{{ monthPrice }}</p>
          <p class="unit">/ 30天</p>
        </div>
        <div class="price-card" :class="{ active: selected === 'year' }" @click="selected = 'year'">
          <p class="duration">年卡</p>
          <p class="price">¥{{ yearPrice }}</p>
          <p class="unit">/ 365天</p>
          <span class="tag">推荐</span>
        </div>
      </div>
    </div>
    <template #footer>
      <el-button @click="close">取消</el-button>
      <el-button type="primary" @click="confirm">立即开通</el-button>
    </template>
  </el-dialog>
</template>

<script setup>
import { ref, computed } from 'vue'
import { trackEvent } from '@/utils/track'

const props = defineProps({
  modelValue: Boolean,
})

const emit = defineEmits(['update:modelValue', 'confirm'])

const visible = computed({
  get: () => props.modelValue,
  set: (val) => emit('update:modelValue', val),
})

const selected = ref('month')
const monthPrice = ref(19.9)
const yearPrice = ref(198)

function close() {
  visible.value = false
}

function confirm() {
  trackEvent('member_upgrade_confirm', { type: selected.value })
  emit('confirm', selected.value)
  close()
}
</script>

<style lang="scss" scoped>
.member-popup {
  .member-content {
    text-align: center;

    .member-icon {
      font-size: 48px;
      margin-bottom: 8px;
    }

    h2 {
      font-size: var(--font-size-xl);
      font-weight: 600;
      margin-bottom: 16px;
    }

    .benefit-list {
      text-align: left;
      list-style: none;
      padding: 0;
      margin-bottom: 20px;

      li {
        padding: 8px 0;
        font-size: var(--font-size-sm);
        border-bottom: 1px solid #f0f0f0;
      }
    }

    .price-options {
      display: grid;
      grid-template-columns: repeat(2, 1fr);
      gap: 12px;

      .price-card {
        border: 2px solid #e4e7ed;
        border-radius: 12px;
        padding: 16px;
        cursor: pointer;
        position: relative;

        &.active {
          border-color: var(--color-primary);
          background: #f0f9ff;
        }

        .duration {
          font-size: var(--font-size-md);
          font-weight: 600;
        }

        .price {
          font-size: 28px;
          font-weight: 700;
          color: var(--color-primary);
          margin: 8px 0;
        }

        .unit {
          font-size: var(--font-size-xs);
          color: var(--color-text-secondary);
        }

        .tag {
          position: absolute;
          top: -8px;
          right: 8px;
          background: #ff4d4f;
          color: white;
          font-size: 10px;
          padding: 2px 6px;
          border-radius: 4px;
        }
      }
    }
  }
}
</style>
