<template>
  <div class="home-page">
    <!-- 顶部栏 -->
    <div class="top-bar">
      <div class="user-info" v-if="userStore.isLoggedIn">
        <div class="avatar">{{ userStore.nickname?.[0] || '用' }}</div>
        <div class="info">
          <p class="name">{{ userStore.nickname || '用户' }}</p>
          <p class="identity">
            <el-tag :type="userStore.isParent ? 'primary' : 'success'" size="small" round>
              {{ userStore.identityDesc }}
            </el-tag>
            <el-tag v-if="userStore.verifyStatus !== 'APPROVED'" type="warning" size="small" round style="margin-left: 6px">
              {{ userStore.verifyDesc }}
            </el-tag>
          </p>
        </div>
      </div>
      <el-button text @click="handleLogout">退出</el-button>
    </div>

    <!-- 实名认证引导 -->
    <div v-if="userStore.verifyStatus === 'NONE'" class="card verify-guide" @click="goVerify">
      <div class="guide-left">
        <span class="guide-icon">🛡️</span>
        <div>
          <p class="guide-title">完成实名认证</p>
          <p class="guide-desc">认证后才能发布/预约课程</p>
        </div>
      </div>
      <el-icon><ArrowRight /></el-icon>
    </div>

    <!-- 功能入口 -->
    <div v-if="userStore.verifyStatus === 'APPROVED'" class="action-grid">
      <!-- 家长端 -->
      <div v-if="userStore.isParent" class="card action-card" @click="router.push('/tutors')">
        <span class="action-icon">🔍</span>
        <p class="action-title">找教员</p>
        <p class="action-desc">浏览大学生教员档案</p>
      </div>
      <!-- 小学学习 - 家长端专属 -->
      <div v-if="userStore.isParent" class="card action-card learning-card" @click="router.push('/learning')">
        <span class="action-icon">📚</span>
        <p class="action-title">小学学习</p>
        <p class="action-desc">全科学习·计划·报告·错题</p>
      </div>
      <!-- AI学情诊断 - 家长端专属 -->
      <div v-if="userStore.isParent" class="card action-card ai-card" @click="router.push('/ai-report')">
        <span class="action-icon">🤖</span>
        <p class="action-title">AI学情诊断</p>
        <p class="action-desc">智能分析薄弱点·生成辅导规划</p>
      </div>
      <!-- 大学生端 -->
      <div v-if="userStore.isStudent" class="card action-card" @click="router.push('/tutor-profile/edit')">
        <span class="action-icon">📝</span>
        <p class="action-title">我的档案</p>
        <p class="action-desc">完善教员档案，开始接单</p>
      </div>
      <!-- AI教员助手 - 大学生端专属 -->
      <div v-if="userStore.isStudent" class="card action-card ai-tutor-card" @click="router.push('/ai-tutor')">
        <span class="action-icon">🤖</span>
        <p class="action-title">AI教员助手</p>
        <p class="action-desc">优化简历·智能定价分析</p>
      </div>
      <!-- 增值功能 - 大学生端专属 -->
      <div v-if="userStore.isStudent" class="card action-card feature-card" @click="router.push('/feature')">
        <span class="action-icon">🚀</span>
        <p class="action-title">增值功能</p>
        <p class="action-desc">简历置顶·加急审核</p>
      </div>
      <!-- 通用 -->
      <div class="card action-card" @click="router.push('/bookings')">
        <span class="action-icon">📋</span>
        <p class="action-title">预约管理</p>
        <p class="action-desc">{{ userStore.isParent ? '查看我的预约' : '查看收到的预约' }}</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { ArrowRight } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()

function goVerify() {
  router.push('/verify')
}

function handleLogout() {
  userStore.logout()
  router.replace('/login')
}
</script>

<style lang="scss" scoped>
.home-page {
  min-height: 100vh;
  background: var(--color-bg);
  padding: 16px;
}

.top-bar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16px;

  .user-info {
    display: flex;
    align-items: center;
    gap: 12px;

    .avatar {
      width: 44px;
      height: 44px;
      border-radius: 50%;
      background: var(--color-primary);
      color: white;
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 18px;
      font-weight: 600;
    }

    .info {
      .name {
        font-size: var(--font-size-lg);
        font-weight: 600;
      }
      .identity {
        margin-top: 2px;
      }
    }
  }
}

.verify-guide {
  display: flex;
  align-items: center;
  justify-content: space-between;
  cursor: pointer;
  border-left: 4px solid var(--color-warning);

  &:active { opacity: 0.85; }

  .guide-left {
    display: flex;
    align-items: center;
    gap: 12px;
  }

  .guide-icon { font-size: 28px; }
  .guide-title { font-size: var(--font-size-md); font-weight: 600; }
  .guide-desc { font-size: var(--font-size-xs); color: var(--color-text-secondary); margin-top: 2px; }
}

.action-grid {
  margin-top: 8px;
}

.action-card {
  cursor: pointer;
  display: flex;
  flex-direction: column;
  align-items: center;
  text-align: center;
  padding: 28px 20px;

  &:active { opacity: 0.85; }

  .action-icon { font-size: 40px; margin-bottom: 12px; }
  .action-title { font-size: var(--font-size-lg); font-weight: 600; margin-bottom: 4px; }
  .action-desc { font-size: var(--font-size-sm); color: var(--color-text-secondary); }
}

.ai-card {
  border-left: 4px solid #FF8C2E;
  background: linear-gradient(135deg, #fff7e6, #ffffff);
}

.ai-tutor-card {
  border-left: 4px solid #6C5CE7;
  background: linear-gradient(135deg, #f0edff, #ffffff);
}

.feature-card {
  border-left: 4px solid #00b894;
  background: linear-gradient(135deg, #e6fff9, #ffffff);
}

.learning-card {
  border-left: 4px solid #FF6B6B;
  background: linear-gradient(135deg, #fff0f0, #ffffff);
}
</style>
