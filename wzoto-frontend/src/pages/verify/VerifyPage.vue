<template>
  <div class="verify-page">
    <div class="page-header">
      <h2 class="page-title">实名认证</h2>
      <p class="page-subtitle">
        {{ userStore.isParent ? '家长认证自动通过，认证后即可预约教员' : '上传学生证，人工审核通过后即可接单' }}
      </p>
    </div>

    <!-- 已认证状态展示 -->
    <div v-if="userStore.verifyStatus === 'APPROVED'" class="card status-card approved">
      <p class="status-icon">✅</p>
      <p class="status-text">已通过实名认证</p>
      <p class="status-detail">姓名：{{ verifyInfo?.realNameMasked }}</p>
    </div>

    <!-- 审核中 -->
    <div v-else-if="userStore.verifyStatus === 'PENDING'" class="card status-card pending">
      <p class="status-icon">⏳</p>
      <p class="status-text">认证审核中</p>
      <p class="status-detail">预计1-2个工作日完成审核</p>
    </div>

    <!-- 被拒绝 -->
    <div v-else-if="userStore.verifyStatus === 'REJECTED'" class="card status-card rejected">
      <p class="status-icon">❌</p>
      <p class="status-text">认证未通过</p>
      <p class="status-detail">原因：{{ verifyInfo?.remark || '请重新提交' }}</p>
      <el-button type="primary" round @click="retryVerify" style="margin-top: 12px">重新认证</el-button>
    </div>

    <!-- 认证表单 -->
    <div v-else class="card form-card">
      <el-form
        ref="formRef"
        :model="form"
        :rules="rules"
        label-position="top"
        size="large"
      >
        <el-form-item label="真实姓名" prop="realName">
          <el-input v-model="form.realName" placeholder="请输入真实姓名" maxlength="20" />
        </el-form-item>

        <el-form-item label="身份证号" prop="idCardNo">
          <el-input v-model="form.idCardNo" placeholder="请输入18位身份证号" maxlength="18" />
        </el-form-item>

        <!-- 大学生额外：学生证照片 -->
        <el-form-item v-if="userStore.isStudent" label="学生证照片" prop="studentCardImage">
          <div class="upload-area" @click="handleUpload">
            <div v-if="!form.studentCardImage" class="upload-placeholder">
              <el-icon size="32"><Plus /></el-icon>
              <p>上传学生证照片</p>
            </div>
            <img v-else :src="form.studentCardImage" class="upload-preview" />
          </div>
          <p class="upload-tip">请拍摄清晰的学生证内页照片</p>
        </el-form-item>
      </el-form>
    </div>

    <!-- 提交按钮 -->
    <div v-if="canSubmit" class="bottom-action">
      <el-button
        type="primary"
        class="btn-full"
        :loading="loading"
        @click="handleSubmit"
      >
        提交认证
      </el-button>
      <p class="action-tip">您的信息仅用于实名认证，不会向第三方泄露</p>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Plus } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'
import { submitParentVerify, submitStudentVerify, getVerifyStatus } from '@/api/verify'
import { showToast } from '@/utils/toast'

const router = useRouter()
const userStore = useUserStore()

const formRef = ref(null)
const loading = ref(false)
const verifyInfo = ref(null)

const form = reactive({
  realName: '',
  idCardNo: '',
  studentCardImage: '',
})

const canSubmit = computed(() => {
  return userStore.verifyStatus === 'NONE' || userStore.verifyStatus === 'REJECTED' || !userStore.verifyStatus
})

const idCardValidator = (rule, value, callback) => {
  if (!value) return callback(new Error('请输入身份证号'))
  if (!/^\d{17}[\dXx]$/.test(value)) return callback(new Error('身份证号格式不正确'))
  callback()
}

const rules = {
  realName: [{ required: true, message: '请输入真实姓名', trigger: 'blur' }],
  idCardNo: [{ validator: idCardValidator, trigger: 'blur' }],
}

onMounted(async () => {
  try {
    const res = await getVerifyStatus()
    if (res.data) {
      verifyInfo.value = res.data
    }
  } catch {
    // ignore
  }
})

async function handleSubmit() {
  if (!formRef.value) return
  await formRef.value.validate()

  loading.value = true
  try {
    let res
    if (userStore.isParent) {
      res = await submitParentVerify({ realName: form.realName, idCardNo: form.idCardNo })
    } else {
      if (!form.studentCardImage) {
        showToast('请上传学生证照片')
        return
      }
      res = await submitStudentVerify(form)
    }
    verifyInfo.value = res.data
    await userStore.fetchUserInfo()

    if (userStore.isParent) {
      showToast('认证成功 🎉')
    } else {
      showToast('认证已提交，等待审核')
    }
  } catch (e) {
    // 已处理
  } finally {
    loading.value = false
  }
}

function retryVerify() {
  // 重置用户认证状态，显示表单
  userStore.verifyStatus = 'NONE'
}

function handleUpload() {
  // TODO: 调用微信小程序 wx.chooseImage 或文件上传接口
  // 开发模式模拟
  form.studentCardImage = 'https://via.placeholder.com/300x200?text=Student+Card'
  showToast('已选择学生证照片（开发模式模拟）')
}
</script>

<style lang="scss" scoped>
.verify-page {
  min-height: 100vh;
  background: var(--color-bg);
  padding: 24px 16px;
}

.page-header {
  margin-bottom: 20px;

  .page-title {
    font-size: var(--font-size-xxl);
    font-weight: 700;
    margin-bottom: 6px;
  }

  .page-subtitle {
    font-size: var(--font-size-sm);
    color: var(--color-text-secondary);
  }
}

.status-card {
  text-align: center;
  padding: 32px 20px;

  .status-icon {
    font-size: 48px;
    margin-bottom: 12px;
  }

  .status-text {
    font-size: var(--font-size-xl);
    font-weight: 600;
    margin-bottom: 8px;
  }

  .status-detail {
    font-size: var(--font-size-sm);
    color: var(--color-text-secondary);
  }

  &.approved { border-left: 4px solid var(--color-success); }
  &.pending { border-left: 4px solid var(--color-warning); }
  &.rejected { border-left: 4px solid var(--color-danger); }
}

.form-card {
  padding: 24px 20px;
}

.upload-area {
  width: 100%;
  height: 160px;
  border: 2px dashed var(--color-border);
  border-radius: var(--radius-md);
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  overflow: hidden;

  &:active {
    border-color: var(--color-primary);
  }

  .upload-placeholder {
    text-align: center;
    color: var(--color-text-placeholder);

    p {
      font-size: var(--font-size-sm);
      margin-top: 8px;
    }
  }

  .upload-preview {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }
}

.upload-tip {
  font-size: var(--font-size-xs);
  color: var(--color-text-placeholder);
  margin-top: 8px;
}

.bottom-action {
  padding-top: 20px;

  .action-tip {
    text-align: center;
    font-size: var(--font-size-xs);
    color: var(--color-text-placeholder);
    margin-top: 10px;
  }
}
</style>