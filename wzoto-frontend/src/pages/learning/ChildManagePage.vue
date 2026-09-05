<template>
  <div class="page child-manage-page">
    <div class="page-header">
      <el-icon class="back-icon" @click="router.back()"><ArrowLeft /></el-icon>
      <h1>子女管理</h1>
    </div>

    <div class="child-list">
      <div v-for="child in children" :key="child.id" class="card child-card">
        <div class="child-info">
          <div class="avatar">{{ child.name?.[0] || '孩' }}</div>
          <div class="info">
            <p class="name">{{ child.name }}</p>
            <p class="meta">{{ child.gradeDesc }} · {{ child.textbookVersionDesc }} · {{ child.school || '未填写学校' }}</p>
          </div>
        </div>
        <div class="actions">
          <el-button text type="primary" @click="openEdit(child)">编辑</el-button>
          <el-button text type="danger" @click="handleDelete(child.id)">删除</el-button>
        </div>
      </div>
    </div>

    <el-empty v-if="!loading && children.length === 0" description="暂无子女档案" />

    <div class="fab-wrapper">
      <el-button type="primary" size="large" round @click="openCreate">+ 新增子女</el-button>
    </div>

    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑子女' : '新增子女'" width="90%" class="mobile-dialog">
      <el-form :model="form" label-position="top">
        <el-form-item label="姓名">
          <el-input v-model="form.name" placeholder="请输入子女姓名" />
        </el-form-item>
        <el-form-item label="年级">
          <el-select v-model="form.grade" placeholder="请选择年级" style="width: 100%">
            <el-option v-for="g in gradeOptions" :key="g.code" :label="g.desc" :value="g.code" />
          </el-select>
        </el-form-item>
        <el-form-item label="教材版本">
          <el-select v-model="form.textbookVersion" placeholder="请选择教材版本" style="width: 100%">
            <el-option v-for="v in textbookOptions" :key="v.code" :label="v.desc" :value="v.code" />
          </el-select>
        </el-form-item>
        <el-form-item label="学校（选填）">
          <el-input v-model="form.school" placeholder="请输入学校名称" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ArrowLeft } from '@element-plus/icons-vue'
import { getMyChildren, createChild, updateChild, deleteChild } from '@/api/child'
import { showToast } from '@/utils/toast'
import { trackPageView, trackEvent } from '@/utils/track'

const router = useRouter()
const children = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const currentId = ref(null)

const form = reactive({
  name: '',
  grade: '',
  textbookVersion: '',
  school: '',
})

const gradeOptions = [
  { code: 'GRADE_1', desc: '一年级' },
  { code: 'GRADE_2', desc: '二年级' },
  { code: 'GRADE_3', desc: '三年级' },
  { code: 'GRADE_4', desc: '四年级' },
  { code: 'GRADE_5', desc: '五年级' },
  { code: 'GRADE_6', desc: '六年级' },
]

const textbookOptions = [
  { code: 'RENJIAO', desc: '人教版' },
  { code: 'BEISHIDA', desc: '北师大版' },
  { code: 'JIAOSHE', desc: '冀教版' },
  { code: 'SUDAJIAO', desc: '苏教版' },
  { code: 'WAIXIN', desc: '外研版' },
  { code: 'SHANGHAJIAO', desc: '沪教版' },
  { code: 'OTHER', desc: '其他' },
]

onMounted(() => {
  trackPageView('child_manage')
  loadChildren()
})

async function loadChildren() {
  loading.value = true
  try {
    const res = await getMyChildren()
    children.value = res.data || []
  } catch (e) {
    console.error(e)
  } finally {
    loading.value = false
  }
}

function openCreate() {
  isEdit.value = false
  currentId.value = null
  form.name = ''
  form.grade = ''
  form.textbookVersion = ''
  form.school = ''
  dialogVisible.value = true
}

function openEdit(child) {
  isEdit.value = true
  currentId.value = child.id
  form.name = child.name
  form.grade = child.grade
  form.textbookVersion = child.textbookVersion
  form.school = child.school || ''
  dialogVisible.value = true
}

async function handleSubmit() {
  if (!form.name || !form.grade || !form.textbookVersion) {
    showToast('请填写完整信息')
    return
  }
  try {
    if (isEdit.value) {
      await updateChild(currentId.value, { ...form })
      trackEvent('child_update', { childId: currentId.value })
    } else {
      await createChild({ ...form })
      trackEvent('child_create')
    }
    dialogVisible.value = false
    showToast('保存成功')
    loadChildren()
  } catch (e) {
    showToast(e.message || '保存失败')
  }
}

async function handleDelete(id) {
  try {
    await deleteChild(id)
    trackEvent('child_delete', { childId: id })
    showToast('删除成功')
    loadChildren()
  } catch (e) {
    showToast(e.message || '删除失败')
  }
}
</script>

<style lang="scss" scoped>
.child-manage-page {
  min-height: 100vh;
  background: var(--color-bg);
  padding: 16px;
}

.page-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;

  h1 {
    font-size: var(--font-size-xl);
    font-weight: 600;
  }

  .back-icon {
    font-size: 24px;
    cursor: pointer;
  }
}

.child-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.child-card {
  display: flex;
  align-items: center;
  justify-content: space-between;

  .child-info {
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
    }

    .name {
      font-size: var(--font-size-md);
      font-weight: 600;
    }

    .meta {
      font-size: var(--font-size-sm);
      color: var(--color-text-secondary);
      margin-top: 2px;
    }
  }
}

.fab-wrapper {
  position: fixed;
  bottom: 24px;
  left: 0;
  right: 0;
  display: flex;
  justify-content: center;
  z-index: 10;
}
</style>
