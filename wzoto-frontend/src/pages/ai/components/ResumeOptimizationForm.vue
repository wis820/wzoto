<template>
  <div class="resume-form">
    <el-form :model="form" label-width="80px" size="default">
      <el-form-item label="学校">
        <el-input v-model="form.university" placeholder="如：清华大学" />
      </el-form-item>
      <el-form-item label="专业">
        <el-input v-model="form.major" placeholder="如：数学与应用数学" />
      </el-form-item>
      <el-form-item label="年级">
        <el-select v-model="form.grade" placeholder="选择年级" style="width: 100%">
          <el-option label="大一" value="大一" />
          <el-option label="大二" value="大二" />
          <el-option label="大三" value="大三" />
          <el-option label="大四" value="大四" />
          <el-option label="研一" value="研一" />
          <el-option label="研二" value="研二" />
          <el-option label="研三" value="研三" />
        </el-select>
      </el-form-item>
      <el-form-item label="辅导科目">
        <el-input v-model="form.subjects" placeholder="如：数学,物理,英语" />
      </el-form-item>
      <el-form-item label="个人简介">
        <el-input v-model="form.bio" type="textarea" :rows="3" placeholder="简单介绍自己..." />
      </el-form-item>
      <el-form-item label="教学经验">
        <el-input v-model="form.experience" type="textarea" :rows="3" placeholder="描述你的教学经历..." />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :loading="loading" @click="handleSubmit" style="width: 100%">
          AI优化简历
        </el-button>
      </el-form-item>
    </el-form>

    <!-- 结果展示 -->
    <div v-if="result" class="result-section">
      <el-divider />
      <div v-if="result.optimizedContent" class="full-result">
        <h4>AI优化结果</h4>
        <div class="result-card" v-if="parsedContent">
          <div class="result-item">
            <strong>优化简介：</strong>
            <p>{{ parsedContent.optimizedBio }}</p>
          </div>
          <div class="result-item">
            <strong>优化经验：</strong>
            <p>{{ parsedContent.optimizedExperience }}</p>
          </div>
          <div v-if="parsedContent.highlights" class="result-item">
            <strong>核心亮点：</strong>
            <ul>
              <li v-for="(h, i) in parsedContent.highlights" :key="i">{{ h.title }}：{{ h.desc }}</li>
            </ul>
          </div>
          <div v-if="parsedContent.improvementSuggestions" class="result-item">
            <strong>改进建议：</strong>
            <ul>
              <li v-for="(s, i) in parsedContent.improvementSuggestions" :key="i">{{ s }}</li>
            </ul>
          </div>
          <div class="score-bar">
            <span>吸引力评分：</span>
            <el-progress :percentage="parsedContent.attractivenessScore || 0" :color="scoreColor" />
          </div>
        </div>
      </div>
      <div v-else class="preview-result">
        <el-alert :title="result.previewContent" type="info" :closable="false" show-icon />
        <div class="pay-section">
          <p class="pay-tip">非会员需支付 ¥{{ result.price }} 解锁完整优化结果</p>
          <el-button type="warning" @click="handlePay" :loading="paying">立即解锁</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { createResumeOptimization, payToUnlockOptimization } from '@/api/ai'

const emit = defineEmits(['success'])

const loading = ref(false)
const paying = ref(false)
const result = ref(null)

const form = ref({
  university: '',
  major: '',
  grade: '',
  subjects: '',
  bio: '',
  experience: ''
})

const parsedContent = computed(() => {
  if (!result.value?.optimizedContent) return null
  try {
    return JSON.parse(result.value.optimizedContent)
  } catch {
    return null
  }
})

const scoreColor = computed(() => {
  const score = parsedContent.value?.attractivenessScore || 0
  if (score >= 80) return '#67c23a'
  if (score >= 60) return '#e6a23c'
  return '#f56c6c'
})

async function handleSubmit() {
  if (!form.value.university || !form.value.major || !form.value.subjects) {
    ElMessage.warning('请填写学校、专业和辅导科目')
    return
  }
  loading.value = true
  try {
    const res = await createResumeOptimization(form.value)
    result.value = res.data
    if (res.data.paymentStatus === 'FREE') {
      ElMessage.success('简历优化完成！')
    } else {
      ElMessage.info('预览已生成，解锁查看完整结果')
    }
    emit('success')
  } catch (e) {
    ElMessage.error(e.message || '生成失败')
  } finally {
    loading.value = false
  }
}

async function handlePay() {
  paying.value = true
  try {
    const res = await payToUnlockOptimization(result.value.id)
    result.value = res.data
    ElMessage.success('解锁成功！')
  } catch (e) {
    ElMessage.error(e.message || '解锁失败')
  } finally {
    paying.value = false
  }
}
</script>

<style scoped lang="scss">
.resume-form {
  .result-section {
    margin-top: 16px;

    .result-card {
      background: #f5f7fa;
      border-radius: 8px;
      padding: 16px;

      .result-item {
        margin-bottom: 12px;

        p { margin: 4px 0; color: #606266; }
        ul { margin: 4px 0 0 16px; color: #606266; }
      }

      .score-bar {
        display: flex;
        align-items: center;
        gap: 12px;
        margin-top: 8px;
      }
    }

    .pay-section {
      text-align: center;
      padding: 16px;

      .pay-tip {
        color: #e6a23c;
        font-size: 14px;
        margin-bottom: 12px;
      }
    }
  }
}
</style>
