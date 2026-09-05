<template>
  <div class="select-identity-page">
    <!-- 顶部 -->
    <div class="page-header">
      <h2 class="page-title">选择你的身份</h2>
      <p class="page-subtitle">身份一经选择，不可随意切换</p>
    </div>

    <!-- 身份卡片 -->
    <div class="identity-cards">
      <!-- 家长 -->
      <div
        class="identity-card"
        :class="{ active: selected === 'PARENT' }"
        @click="selected = 'PARENT'"
      >
        <div class="card-icon">👨‍👩‍👧</div>
        <h3 class="card-title">我是家长</h3>
        <p class="card-desc">为孩子寻找优质大学生家教</p>
        <ul class="card-features">
          <li>✓ 浏览大学生教员档案</li>
          <li>✓ 按科目/距离/评分筛选</li>
          <li>✓ 一键预约试听课</li>
        </ul>
        <div v-if="selected === 'PARENT'" class="card-check">✓</div>
      </div>

      <!-- 大学生 -->
      <div
        class="identity-card"
        :class="{ active: selected === 'STUDENT' }"
        @click="selected = 'STUDENT'"
      >
        <div class="card-icon">🎓</div>
        <h3 class="card-title">我是大学生</h3>
        <p class="card-desc">利用业余时间做家教赚零花钱</p>
        <ul class="card-features">
          <li>✓ 完善个人教员档案</li>
          <li>✓ 接收家长预约请求</li>
          <li>✓ 自由设置辅导科目和时段</li>
        </ul>
        <div v-if="selected === 'STUDENT'" class="card-check">✓</div>
      </div>
    </div>

    <!-- 确认按钮 -->
    <div class="bottom-action">
      <el-button
        type="primary"
        class="btn-full"
        :disabled="!selected"
        :loading="loading"
        @click="handleConfirm"
      >
        确认选择
      </el-button>
      <p class="action-tip">⚠️ 请谨慎选择，确认后不可更改</p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { showToast } from '@/utils/toast'
import { ElMessageBox } from 'element-plus'

const router = useRouter()
const userStore = useUserStore()

const selected = ref('')
const loading = ref(false)

async function handleConfirm() {
  if (!selected.value) return

  try {
    await ElMessageBox.confirm(
      `您选择的是「${selected.value === 'PARENT' ? '家长' : '大学生'}」身份，确认后将无法更改，确定吗？`,
      '身份确认',
      {
        confirmButtonText: '确认选择',
        cancelButtonText: '再想想',
        type: 'warning',
      }
    )
  } catch {
    // 用户取消
    return
  }

  loading.value = true
  try {
    await userStore.chooseIdentity(selected.value)
    showToast('身份选择成功 🎉')
    router.replace('/')
  } catch (e) {
    // 错误已处理
  } finally {
    loading.value = false
  }
}
</script>

<style lang="scss" scoped>
.select-identity-page {
  min-height: 100vh;
  background: var(--color-bg);
  padding: 24px 16px;
  display: flex;
  flex-direction: column;
}

.page-header {
  text-align: center;
  margin-bottom: 24px;

  .page-title {
    font-size: var(--font-size-xxl);
    font-weight: 700;
    color: var(--color-text);
    margin-bottom: 8px;
  }

  .page-subtitle {
    font-size: var(--font-size-sm);
    color: var(--color-warning);
  }
}

.identity-cards {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.identity-card {
  position: relative;
  background: var(--color-bg-white);
  border: 2px solid var(--color-border);
  border-radius: var(--radius-lg);
  padding: 24px 20px;
  cursor: pointer;
  transition: all 0.25s ease;

  &:active {
    transform: scale(0.98);
  }

  &.active {
    border-color: var(--color-primary);
    background: rgba(79, 110, 247, 0.04);
    box-shadow: 0 4px 16px rgba(79, 110, 247, 0.15);
  }

  .card-icon {
    font-size: 36px;
    margin-bottom: 12px;
  }

  .card-title {
    font-size: var(--font-size-xl);
    font-weight: 600;
    margin-bottom: 4px;
  }

  .card-desc {
    font-size: var(--font-size-sm);
    color: var(--color-text-secondary);
    margin-bottom: 12px;
  }

  .card-features {
    list-style: none;
    padding: 0;

    li {
      font-size: var(--font-size-sm);
      color: var(--color-text-secondary);
      line-height: 1.8;
    }
  }

  .card-check {
    position: absolute;
    top: 16px;
    right: 16px;
    width: 28px;
    height: 28px;
    background: var(--color-primary);
    color: white;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 14px;
    font-weight: 700;
  }
}

.bottom-action {
  padding-top: 20px;

  .action-tip {
    text-align: center;
    font-size: var(--font-size-xs);
    color: var(--color-warning);
    margin-top: 10px;
  }
}
</style>
