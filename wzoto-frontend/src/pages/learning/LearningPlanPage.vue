<template>
  <div class="page learning-plan-page">
    <div class="page-header">
      <el-icon class="back-icon" @click="router.back()"><ArrowLeft /></el-icon>
      <h1>学习计划</h1>
    </div>

    <div class="child-selector">
      <el-select v-model="selectedChildId" placeholder="选择子女" style="width: 100%" @change="loadConfig">
        <el-option v-for="child in children" :key="child.id" :label="child.name" :value="child.id" />
      </el-select>
    </div>

    <div v-if="selectedChildId" class="card config-card">
      <h2>每日学习配置</h2>
      <el-form :model="config" label-position="top">
        <el-form-item label="每日学习时长（分钟）">
          <el-slider v-model="config.dailyDurationMinutes" :min="15" :max="120" :step="5" show-stops />
          <div class="slider-value">{{ config.dailyDurationMinutes }} 分钟</div>
        </el-form-item>

        <el-form-item label="学科权重">
          <div class="weight-item">
            <span>语文</span>
            <el-slider v-model="config.chineseWeight" :min="0" :max="10" />
          </div>
          <div class="weight-item">
            <span>数学</span>
            <el-slider v-model="config.mathWeight" :min="0" :max="10" />
          </div>
          <div class="weight-item">
            <span>英语</span>
            <el-slider v-model="config.englishWeight" :min="0" :max="10" />
          </div>
        </el-form-item>

        <el-form-item label="专项练习开关">
          <el-checkbox v-model="config.specialCalculationEnabled">计算专项</el-checkbox>
          <el-checkbox v-model="config.specialApplicationEnabled">应用题专项</el-checkbox>
          <el-checkbox v-model="config.specialLiteracyEnabled">识字专项</el-checkbox>
          <el-checkbox v-model="config.specialWordsEnabled">背单词专项</el-checkbox>
        </el-form-item>
      </el-form>
      <el-button type="primary" style="width: 100%; margin-top: 12px" @click="saveConfig">保存计划</el-button>
    </div>

    <div v-if="selectedChildId" class="card special-card">
      <h2>专项练习下发</h2>
      <div class="special-grid">
        <div v-for="s in specialTypes" :key="s.code" class="special-item" @click="assignSpecial(s.code)">
          <span class="icon">{{ s.icon }}</span>
          <span class="name">{{ s.name }}</span>
        </div>
      </div>
    </div>

    <!-- AI自动生成计划 -->
    <div v-if="selectedChildId" class="card ai-plan-card">
      <h2>🤖 AI每日计划</h2>
      <div v-if="aiPlan" class="ai-plan-content">
        <p class="plan-date">📅 {{ aiPlan.planDate }}</p>
        <p class="plan-desc">{{ aiPlanDesc }}</p>
        <el-tag v-if="aiPlan.applied" type="success" size="small">已应用</el-tag>
        <el-button v-else type="primary" size="small" style="margin-top: 8px" @click="applyAiPlan">✅ 一键应用AI建议</el-button>
      </div>
      <div v-else class="no-plan">
        <p>尚未生成AI计划</p>
        <el-button type="primary" size="small" :loading="generatingPlan" @click="generatePlan">✨ 生成AI计划</el-button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ArrowLeft } from '@element-plus/icons-vue'
import { getMyChildren } from '@/api/child'
import { getLearningPlanConfig, saveLearningPlanConfig, assignSpecialTask } from '@/api/learning'
import { getTodayAiPlan, generateAiPlan, applyAiPlan as applyAiPlanApi } from '@/api/student'
import { showToast } from '@/utils/toast'
import { trackPageView, trackEvent } from '@/utils/track'

const router = useRouter()
const children = ref([])
const selectedChildId = ref(null)
const aiPlan = ref(null)
const generatingPlan = ref(false)

const config = reactive({
  dailyDurationMinutes: 30,
  chineseWeight: 3,
  mathWeight: 4,
  englishWeight: 3,
  specialCalculationEnabled: false,
  specialApplicationEnabled: false,
  specialLiteracyEnabled: false,
  specialWordsEnabled: false,
})

const specialTypes = [
  { code: 'calculation', name: '计算专项', icon: '🔢' },
  { code: 'application', name: '应用题专项', icon: '📐' },
  { code: 'literacy', name: '识字专项', icon: '📖' },
  { code: 'words', name: '背单词专项', icon: '🔤' },
]

onMounted(() => {
  trackPageView('learning_plan')
  loadChildren()
})

const aiPlanDesc = computed(() => {
  if (!aiPlan.value) return ''
  try {
    const content = JSON.parse(aiPlan.value.planContentJson)
    return content.summary || 'AI已生成每日学习计划'
  } catch { return 'AI已生成每日学习计划' }
})

async function loadChildren() {
  try {
    const res = await getMyChildren()
    children.value = res.data || []
    if (children.value.length > 0) {
      selectedChildId.value = children.value[0].id
      loadConfig()
      loadAiPlan()
    }
  } catch (e) {
    console.error(e)
  }
}

async function loadConfig() {
  if (!selectedChildId.value) return
  try {
    const res = await getLearningPlanConfig(selectedChildId.value)
    const data = res.data
    if (data) {
      config.dailyDurationMinutes = data.dailyDurationMinutes || 30
      config.chineseWeight = data.chineseWeight || 3
      config.mathWeight = data.mathWeight || 4
      config.englishWeight = data.englishWeight || 3
      config.specialCalculationEnabled = data.specialCalculationEnabled || false
      config.specialApplicationEnabled = data.specialApplicationEnabled || false
      config.specialLiteracyEnabled = data.specialLiteracyEnabled || false
      config.specialWordsEnabled = data.specialWordsEnabled || false
    }
  } catch (e) {
    showToast(e.message || '加载计划失败')
  }
}

async function saveConfig() {
  if (!selectedChildId.value) {
    showToast('请先选择子女')
    return
  }
  try {
    await saveLearningPlanConfig(selectedChildId.value, { ...config })
    trackEvent('plan_save', { childId: selectedChildId.value })
    showToast('保存成功')
  } catch (e) {
    showToast(e.message || '保存失败')
  }
}

async function assignSpecial(code) {
  if (!selectedChildId.value) {
    showToast('请先选择子女')
    return
  }
  try {
    await assignSpecialTask(selectedChildId.value, { specialType: code, count: 5 })
    trackEvent('plan_special_task', { childId: selectedChildId.value, specialType: code })
    showToast('专项练习已下发')
  } catch (e) {
    showToast(e.message || '下发失败')
  }
}

async function loadAiPlan() {
  if (!selectedChildId.value) return
  try {
    const res = await getTodayAiPlan(selectedChildId.value)
    aiPlan.value = res.data
  } catch { aiPlan.value = null }
}

async function generatePlan() {
  generatingPlan.value = true
  try {
    const res = await generateAiPlan(selectedChildId.value)
    aiPlan.value = res.data
    showToast('AI计划已生成')
  } catch (e) {
    showToast(e.message || '生成失败')
  } finally {
    generatingPlan.value = false
  }
}

async function applyAiPlan() {
  if (!aiPlan.value) return
  try {
    await applyAiPlanApi(aiPlan.value.id)
    showToast('AI计划已应用到学习任务')
    loadAiPlan()
  } catch (e) {
    showToast(e.message || '应用失败')
  }
}
</script>

<style lang="scss" scoped>
.learning-plan-page {
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

.child-selector {
  margin-bottom: 16px;
}

.config-card, .special-card {
  margin-bottom: 16px;

  h2 {
    font-size: var(--font-size-lg);
    font-weight: 600;
    margin-bottom: 16px;
  }
}

.slider-value {
  text-align: right;
  font-size: var(--font-size-sm);
  color: var(--color-text-secondary);
}

.weight-item {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 8px;

  span {
    width: 40px;
    font-size: var(--font-size-sm);
  }

  .el-slider {
    flex: 1;
  }
}

.special-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
}

.special-item {
  background: #f5f7fa;
  border-radius: 12px;
  padding: 16px;
  text-align: center;
  cursor: pointer;

  &:active {
    opacity: 0.85;
  }

  .icon {
    display: block;
    font-size: 32px;
    margin-bottom: 8px;
  }

  .name {
    font-size: var(--font-size-sm);
  }
}

.ai-plan-card {
  margin-bottom: 16px;
  h2 { font-size: var(--font-size-lg); font-weight: 600; margin-bottom: 12px; }
  .plan-date { font-size: var(--font-size-sm); color: var(--color-text-secondary); margin-bottom: 4px; }
  .plan-desc { font-size: var(--font-size-md); line-height: 1.6; margin-bottom: 8px; }
  .no-plan {
    text-align: center; padding: 16px;
    p { font-size: var(--font-size-sm); color: var(--color-text-placeholder); margin-bottom: 12px; }
  }
}
</style>
