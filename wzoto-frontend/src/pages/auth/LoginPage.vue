<template>
  <div class="login-page">
    <!-- 顶部装饰 -->
    <div class="login-header">
      <div class="logo-area">
        <div class="logo-icon">📚</div>
        <h1 class="app-name">学霸到家</h1>
        <p class="app-slogan">名校大学生 · 线下1对1辅导</p>
      </div>
    </div>

    <!-- 登录区域 -->
    <div class="login-body">
      <div class="card login-card">
        <!-- 微信一键登录 -->
        <el-button
          type="primary"
          class="btn-full wx-login-btn"
          :loading="loading"
          @click="handleWxLogin"
        >
          <el-icon size="20" style="margin-right: 6px"><ChatDotRound /></el-icon>
          微信一键登录
        </el-button>

        <div class="login-divider">
          <span>安全保障</span>
        </div>

        <div class="login-tips">
          <p>✓ 微信授权安全登录</p>
          <p>✓ 您的信息仅用于家教撮合</p>
          <p>✓ 不收取任何辅导费用</p>
        </div>
      </div>

      <!-- 开发模式：模拟登录 -->
      <div v-if="isDev" class="card dev-card">
        <p class="dev-title">🔧 开发模式 - 快捷登录</p>
        <div class="dev-btns">
          <el-button type="primary" class="dev-btn" @click="handleDevLogin('测试家长')">
            👨‍👩‍👧 家长登录
          </el-button>
          <el-button type="success" class="dev-btn" @click="handleDevLogin('测试大学生')">
            🎓 大学生登录
          </el-button>
        </div>
        <div style="margin-top: 16px; border-top: 1px solid #eee; padding-top: 12px">
          <p class="dev-title" style="margin-bottom: 8px">自定义Code登录</p>
          <el-input
            v-model="mockCode"
            placeholder="输入模拟验证码（任意字符串）"
            style="margin-bottom: 12px"
          />
          <el-button type="warning" class="btn-full" @click="handleMockLogin">
            模拟微信登录
          </el-button>
        </div>
      </div>
    </div>

    <!-- 底部协议 -->
    <div class="login-footer">
      <p>登录即表示同意 <a href="#" class="text-primary">《用户服务协议》</a> 和 <a href="#" class="text-primary">《隐私政策》</a></p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ChatDotRound } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'
import { showToast } from '@/utils/toast'
import request from '@/utils/request'

const router = useRouter()
const userStore = useUserStore()

const loading = ref(false)
const mockCode = ref('')
const isDev = ref(import.meta.env.DEV)

/**
 * 微信登录
 * 微信小程序环境：调用 wx.login() 获取 code
 * H5环境：跳转微信OAuth授权
 */
async function handleWxLogin() {
  loading.value = true
  try {
    // 微信小程序环境
    if (window.wx) {
      wx.login({
        success: async (res) => {
          if (res.code) {
            await doLogin(res.code)
          } else {
            showToast('微信登录失败')
          }
        },
        fail: () => showToast('微信登录失败'),
      })
    } else {
      // H5环境 - 使用模拟code
      const code = 'wx_h5_mock_' + Date.now()
      await doLogin(code)
    }
  } finally {
    loading.value = false
  }
}

/**
 * 模拟登录（仅开发模式）
 */
async function handleMockLogin() {
  if (!mockCode.value.trim()) {
    showToast('请输入模拟code')
    return
  }
  loading.value = true
  try {
    await doLogin(mockCode.value.trim())
  } finally {
    loading.value = false
  }
}

/**
 * 开发环境快捷登录
 */
async function handleDevLogin(nickname) {
  loading.value = true
  try {
    const res = await request.post('/auth/dev-login', { nickname })
    if (res.code === 200 && res.data.token) {
      userStore.setLoginData(res.data)
      showToast(`以 ${res.data.nickname} 身份登录成功 🎉`)
      if (!res.data.hasIdentity) {
        router.replace('/select-identity')
      } else {
        router.replace('/')
      }
    } else {
      showToast(res.msg || '登录失败')
    }
  } catch (e) {
    console.error('开发登录失败', e)
  } finally {
    loading.value = false
  }
}

/**
 * 执行登录逻辑
 */
async function doLogin(code) {
  try {
    const data = await userStore.login(code)
    showToast('登录成功 🎉')

    // 根据状态跳转
    if (!data.hasIdentity) {
      router.replace('/select-identity')
    } else {
      router.replace('/')
    }
  } catch (e) {
    // 错误已由request拦截器处理
  }
}
</script>

<style lang="scss" scoped>
.login-page {
  min-height: 100vh;
  background: linear-gradient(180deg, var(--color-primary) 0%, var(--color-primary-light) 40%, var(--color-bg) 60%);
  display: flex;
  flex-direction: column;
  padding: 0 24px;
}

.login-header {
  padding-top: 80px;
  padding-bottom: 48px;
  text-align: center;

  .logo-area {
    .logo-icon {
      width: 72px;
      height: 72px;
      margin: 0 auto 16px;
      font-size: 40px;
      background: white;
      border-radius: 20px;
      display: flex;
      align-items: center;
      justify-content: center;
      box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
    }

    .app-name {
      font-size: 28px;
      font-weight: 700;
      color: white;
      margin-bottom: 6px;
    }

    .app-slogan {
      font-size: var(--font-size-sm);
      color: rgba(255, 255, 255, 0.85);
    }
  }
}

.login-body {
  flex: 1;
}

.login-card {
  padding: 32px 24px;
}

.wx-login-btn {
  height: 52px;
  font-size: var(--font-size-lg);
  font-weight: 600;
  border-radius: var(--radius-round);
  background: #07C160;
  border-color: #07C160;

  &:hover, &:focus {
    background: #06AE56;
    border-color: #06AE56;
  }
}

.login-divider {
  display: flex;
  align-items: center;
  margin: 24px 0;

  &::before, &::after {
    content: '';
    flex: 1;
    height: 1px;
    background: var(--color-border);
  }

  span {
    padding: 0 12px;
    font-size: var(--font-size-xs);
    color: var(--color-text-placeholder);
  }
}

.login-tips {
  p {
    font-size: var(--font-size-sm);
    color: var(--color-text-secondary);
    line-height: 2;
  }
}

.dev-card {
  margin-top: 16px;

  .dev-title {
    font-size: var(--font-size-sm);
    color: var(--color-warning);
    margin-bottom: 12px;
    font-weight: 500;
  }

  .dev-btns {
    display: flex;
    gap: 12px;
    margin-bottom: 12px;

    .dev-btn {
      flex: 1;
      height: 44px;
      font-size: 15px;
      font-weight: 600;
      border-radius: var(--radius-round);
    }
  }
}

.login-footer {
  padding: 24px 0;
  text-align: center;

  p {
    font-size: var(--font-size-xs);
    color: var(--color-text-placeholder);
  }
}
</style>
