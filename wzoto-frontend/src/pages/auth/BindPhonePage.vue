<template>
  <div class="bind-phone-page">
    <div class="page-header">
      <h2 class="page-title">绑定手机号</h2>
      <p class="page-subtitle">用于身份验证和接收预约通知</p>
    </div>

    <div class="card form-card">
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-position="top"
        size="large"
      >
        <el-form-item label="手机号" prop="phone">
          <el-input
            v-model="form.phone"
            placeholder="请输入手机号"
            maxlength="11"
            type="tel"
          >
            <template #prefix>
              <span style="color: var(--color-text-placeholder)">+86</span>
            </template>
          </el-input>
        </el-form-item>

        <el-form-item label="验证码" prop="code">
          <div class="code-input">
            <el-input
              v-model="form.code"
              placeholder="请输入验证码"
              maxlength="6"
              type="number"
            />
            <el-button
              type="primary"
              :disabled="countdown > 0"
              @click="handleSendCode"
              style="width: 120px"
            >
              {{ countdown > 0 ? `${countdown}s` : '获取验证码' }}
            </el-button>
          </div>
        </el-form-item>
      </el-form>
    </div>

    <div class="bottom-action">
      <el-button
        type="primary"
        class="btn-full"
        :loading="loading"
        @click="handleBind"
      >
        确认绑定
      </el-button>
      <p class="skip-action" @click="handleSkip">暂不绑定，稍后完善</p>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { showToast } from '@/utils/toast'

const router = useRouter()
const userStore = useUserStore()

const formRef = ref(null)
const loading = ref(false)
const countdown = ref(0)

const form = reactive({
  phone: '',
  code: '',
})

const phoneValidator = (rule, value, callback) => {
  if (!value) return callback(new Error('请输入手机号'))
  if (!/^1[3-9]\d{9}$/.test(value)) return callback(new Error('请输入正确的手机号'))
  callback()
}

const rules = {
  phone: [{ validator: phoneValidator, trigger: 'blur' }],
  code: [{ required: true, message: '请输入验证码', trigger: 'blur' }],
}

/** 发送验证码（微信小程序用getPhoneNumber，H5模拟） */
function handleSendCode() {
  if (!/^1[3-9]\d{9}$/.test(form.phone)) {
    showToast('请输入正确的手机号')
    return
  }
  // TODO: 调用后端发送验证码接口
  showToast('验证码已发送')
  countdown.value = 60
  const timer = setInterval(() => {
    countdown.value--
    if (countdown.value <= 0) clearInterval(timer)
  }, 1000)
}

/** 绑定手机号 */
async function handleBind() {
  if (!formRef.value) return
  await formRef.value.validate()

  loading.value = true
  try {
    await userStore.bindUserPhone(form.phone)
    showToast('绑定成功 🎉')
    router.replace('/')
  } catch (e) {
    // 已处理
  } finally {
    loading.value = false
  }
}

/** 跳过绑定 */
function handleSkip() {
  router.replace('/')
}
</script>

<style lang="scss" scoped>
.bind-phone-page {
  min-height: 100vh;
  background: var(--color-bg);
  padding: 24px 16px;
  display: flex;
  flex-direction: column;
}

.page-header {
  margin-bottom: 24px;

  .page-title {
    font-size: var(--font-size-xxl);
    font-weight: 700;
    color: var(--color-text);
    margin-bottom: 8px;
  }

  .page-subtitle {
    font-size: var(--font-size-sm);
    color: var(--color-text-secondary);
  }
}

.form-card {
  padding: 24px 20px;
}

.code-input {
  display: flex;
  gap: 12px;
  width: 100%;

  .el-input {
    flex: 1;
  }
}

.bottom-action {
  padding-top: 20px;

  .skip-action {
    text-align: center;
    font-size: var(--font-size-sm);
    color: var(--color-text-placeholder);
    margin-top: 12px;
    cursor: pointer;

    &:active {
      color: var(--color-primary);
    }
  }
}
</style>
