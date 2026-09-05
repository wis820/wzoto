<template>
  <div class="profile-edit-page">
    <div class="page-header">
      <h2 class="page-title">{{ isEdit ? '编辑教员档案' : '创建教员档案' }}</h2>
      <p class="page-subtitle">完善档案后，家长才能找到你</p>
    </div>

    <div class="card form-card">
      <el-form ref="formRef" :model="form" :rules="rules" label-position="top" size="large">
        <el-form-item label="学校名称" prop="university">
          <el-input v-model="form.university" placeholder="如：北京大学" />
        </el-form-item>

        <el-form-item label="专业" prop="major">
          <el-input v-model="form.major" placeholder="如：数学与应用数学" />
        </el-form-item>

        <el-form-item label="年级">
          <el-select v-model="form.grade" placeholder="请选择年级" style="width:100%">
            <el-option label="大一" value="大一" />
            <el-option label="大二" value="大二" />
            <el-option label="大三" value="大三" />
            <el-option label="大四" value="大四" />
            <el-option label="研一" value="研一" />
            <el-option label="研二" value="研二" />
            <el-option label="研三" value="研三" />
          </el-select>
        </el-form-item>

        <el-form-item label="可辅导科目" prop="subjects">
          <el-input v-model="form.subjects" placeholder="用逗号分隔，如：小学数学,初中数学,高中物理" />
        </el-form-item>

        <el-form-item label="时薪（元/小时）" prop="hourlyRate">
          <el-input-number v-model="form.hourlyRate" :min="20" :max="500" :step="10" style="width:100%" />
        </el-form-item>

        <el-form-item label="个人简介">
          <el-input v-model="form.bio" type="textarea" :rows="3" placeholder="介绍你的教学风格、优势..." maxlength="500" show-word-limit />
        </el-form-item>

        <el-form-item label="教学经验">
          <el-input v-model="form.experience" type="textarea" :rows="3" placeholder="描述你的家教/教学经历..." maxlength="500" show-word-limit />
        </el-form-item>

        <el-form-item label="上课区域">
          <el-input v-model="form.districts" placeholder="用逗号分隔，如：海淀区,朝阳区" />
        </el-form-item>
      </el-form>
    </div>

    <div class="bottom-action">
      <el-button type="primary" class="btn-full" :loading="loading" @click="handleSubmit">
        {{ isEdit ? '保存修改' : '创建档案' }}
      </el-button>

      <el-button v-if="isEdit" class="btn-full" style="margin-top:12px;margin-left:0" @click="handleToggleActive">
        {{ profile?.active ? '下架档案' : '上架档案' }}
      </el-button>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { createTutorProfile, updateTutorProfile, toggleTutorActive, getMyTutorProfile } from '@/api/tutor'
import { showToast } from '@/utils/toast'

const router = useRouter()
const userStore = useUserStore()

const formRef = ref(null)
const loading = ref(false)
const profile = ref(null)
const isEdit = computed(() => !!profile.value)

const form = reactive({
  university: '',
  major: '',
  grade: '',
  subjects: '',
  hourlyRate: 80,
  bio: '',
  experience: '',
  districts: '',
})

const rules = {
  university: [{ required: true, message: '请输入学校名称', trigger: 'blur' }],
  major: [{ required: true, message: '请输入专业', trigger: 'blur' }],
  subjects: [{ required: true, message: '请输入可辅导科目', trigger: 'blur' }],
  hourlyRate: [{ required: true, message: '请设置时薪', trigger: 'change' }],
}

onMounted(async () => {
  try {
    const res = await getMyTutorProfile()
    if (res.data) {
      profile.value = res.data
      Object.assign(form, {
        university: res.data.university || '',
        major: res.data.major || '',
        grade: res.data.grade || '',
        subjects: res.data.subjects || '',
        hourlyRate: res.data.hourlyRate || 80,
        bio: res.data.bio || '',
        experience: res.data.experience || '',
        districts: res.data.districts || '',
      })
    }
  } catch {
    // 还没有档案，忽略
  }
})

async function handleSubmit() {
  if (!formRef.value) return
  await formRef.value.validate()

  loading.value = true
  try {
    if (isEdit.value) {
      await updateTutorProfile(form)
      showToast('档案已更新')
    } else {
      await createTutorProfile(form)
      showToast('档案创建成功 🎉')
    }
    router.back()
  } catch (e) {
    // 已处理
  } finally {
    loading.value = false
  }
}

async function handleToggleActive() {
  try {
    const res = await toggleTutorActive()
    profile.value = res.data
    showToast(profile.value.active ? '已上架' : '已下架')
  } catch (e) {
    // 已处理
  }
}
</script>

<style lang="scss" scoped>
.profile-edit-page {
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

.form-card {
  padding: 24px 20px;
}

.bottom-action {
  padding-top: 20px;
}
</style>