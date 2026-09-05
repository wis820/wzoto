<template>
  <div class="page parent-control-page">
    <div class="page-header">
      <el-icon class="back-icon" @click="router.back()"><ArrowLeft /></el-icon>
      <h1>时长管控</h1>
    </div>

    <div class="child-selector">
      <el-select v-model="selectedChildId" placeholder="选择子女" style="width: 100%" @change="loadConfig">
        <el-option v-for="child in children" :key="child.id" :label="child.name" :value="child.id" />
      </el-select>
    </div>

    <div v-if="selectedChildId" class="card config-card">
      <div class="lock-banner" :class="{ locked: config.locked }">
        <div class="lock-status">
          <span class="lock-icon">{{ config.locked ? '🔒' : '🔓' }}</span>
          <span>{{ config.locked ? '已锁定' : '未锁定' }}</span>
        </div>
        <el-button :type="config.locked ? 'success' : 'danger'" @click="toggleLock">
          {{ config.locked ? '解除锁定' : '一键锁定' }}
        </el-button>
      </div>

      <el-form :model="config" label-position="top">
        <el-form-item label="每日可用时长（分钟）">
          <el-slider v-model="config.dailyLimitMinutes" :min="0" :max="240" :step="10" show-stops />
          <div class="slider-value">{{ config.dailyLimitMinutes }} 分钟</div>
        </el-form-item>

        <el-form-item label="单次休息间隔（分钟）">
          <el-slider v-model="config.restIntervalMinutes" :min="5" :max="120" :step="5" show-stops />
          <div class="slider-value">{{ config.restIntervalMinutes }} 分钟</div>
        </el-form-item>

        <el-form-item label="禁用时段">
          <div class="time-range">
            <el-time-select v-model="config.forbiddenStartTime" start="00:00" step="00:30" end="23:30" placeholder="开始" />
            <span>至</span>
            <el-time-select v-model="config.forbiddenEndTime" start="00:00" step="00:30" end="23:30" placeholder="结束" />
          </div>
        </el-form-item>

        <el-form-item label="护眼模式">
          <div class="switch-list">
            <div class="switch-item">
              <span>开启护眼模式</span>
              <el-switch v-model="config.eyeProtectionMode" />
            </div>
            <div class="switch-item">
              <span>蓝光过滤</span>
              <el-switch v-model="config.blueLightFilter" />
            </div>
            <div class="switch-item">
              <span>坐姿提醒</span>
              <el-switch v-model="config.postureReminder" />
            </div>
          </div>
        </el-form-item>
      </el-form>
      <el-button type="primary" style="width: 100%; margin-top: 12px" @click="saveConfig">保存配置</el-button>
    </div>

    <!-- 夜间锁定开关 -->
    <div v-if="selectedChildId" class="card night-lock-card">
      <h2>🌙 夜间锁定</h2>
      <div class="switch-item">
        <span>启用夜间锁定（{{ config.forbiddenStartTime }} - {{ config.forbiddenEndTime }}）</span>
        <el-switch v-model="nightLockEnabled" @change="onNightLockChange" />
      </div>
      <p class="hint">启用后，孩子在设定时段内无法使用学习功能</p>
    </div>

    <!-- 学习时长超额提醒 -->
    <div v-if="selectedChildId" class="card overtime-card">
      <h2>⚠️ 时长超额提醒</h2>
      <div class="switch-item">
        <span>开启超额提醒</span>
        <el-switch v-model="overtimeAlertEnabled" />
      </div>
      <div class="overtime-config" v-if="overtimeAlertEnabled">
        <span>超额阈值：</span>
        <el-slider v-model="overtimeThreshold" :min="5" :max="60" :step="5" style="flex: 1" />
        <span>{{ overtimeThreshold }}分钟</span>
      </div>
      <p class="hint">超过每日设定时长后，每隔设定分钟数提醒一次</p>
    </div>

    <!-- AI功能使用记录 -->
    <div v-if="selectedChildId" class="card ai-usage-card">
      <h2>🤖 AI功能使用记录</h2>
      <div class="usage-stats">
        <div class="usage-item">
          <span class="usage-label">AI答疑</span>
          <span class="usage-value">{{ aiUsage.qaCount || 0 }} 次</span>
        </div>
        <div class="usage-item">
          <span class="usage-label">作文批改</span>
          <span class="usage-value">{{ aiUsage.compositionCount || 0 }} 次</span>
        </div>
        <div class="usage-item">
          <span class="usage-label">口语评测</span>
          <span class="usage-value">{{ aiUsage.oralCount || 0 }} 次</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ArrowLeft } from '@element-plus/icons-vue'
import { getMyChildren } from '@/api/child'
import { getControlConfig, saveControlConfig, lockControl, unlockControl } from '@/api/learning'
import { getQaHistory, getGradingHistory } from '@/api/student'
import { showToast } from '@/utils/toast'
import { trackPageView, trackEvent } from '@/utils/track'

const router = useRouter()
const children = ref([])
const selectedChildId = ref(null)

const config = reactive({
  dailyLimitMinutes: 60,
  restIntervalMinutes: 20,
  forbiddenStartTime: '22:00',
  forbiddenEndTime: '07:00',
  locked: false,
  eyeProtectionMode: false,
  blueLightFilter: false,
  postureReminder: false,
})
const nightLockEnabled = ref(true)
const overtimeAlertEnabled = ref(false)
const overtimeThreshold = ref(15)
const aiUsage = ref({ qaCount: 0, compositionCount: 0, oralCount: 0 })

onMounted(() => {
  trackPageView('parent_control')
  loadChildren()
})

async function loadChildren() {
  try {
    const res = await getMyChildren()
    children.value = res.data || []
    if (children.value.length > 0) {
      selectedChildId.value = children.value[0].id
      loadConfig()
      loadAiUsage()
    }
  } catch (e) {
    console.error(e)
  }
}

async function loadConfig() {
  if (!selectedChildId.value) return
  try {
    const res = await getControlConfig(selectedChildId.value)
    const data = res.data
    if (data) {
      Object.assign(config, data)
    }
  } catch (e) {
    showToast(e.message || '加载配置失败')
  }
}

async function saveConfig() {
  if (!selectedChildId.value) {
    showToast('请先选择子女')
    return
  }
  try {
    await saveControlConfig(selectedChildId.value, { ...config })
    trackEvent('control_save', { childId: selectedChildId.value })
    showToast('保存成功')
  } catch (e) {
    showToast(e.message || '保存失败')
  }
}

async function toggleLock() {
  if (!selectedChildId.value) {
    showToast('请先选择子女')
    return
  }
  try {
    if (config.locked) {
      await unlockControl(selectedChildId.value)
      trackEvent('control_unlock', { childId: selectedChildId.value })
    } else {
      await lockControl(selectedChildId.value)
      trackEvent('control_lock', { childId: selectedChildId.value })
    }
    await loadConfig()
  } catch (e) {
    showToast(e.message || '操作失败')
  }
}

function onNightLockChange(val) {
  if (val) {
    config.forbiddenStartTime = '22:00'
    config.forbiddenEndTime = '07:00'
  } else {
    config.forbiddenStartTime = '00:00'
    config.forbiddenEndTime = '00:00'
  }
}

async function loadAiUsage() {
  if (!selectedChildId.value) return
  try {
    const [qaRes, gradingRes] = await Promise.all([
      getQaHistory(selectedChildId.value),
      getGradingHistory(selectedChildId.value),
    ])
    const qaData = qaRes.data || []
    const gradingData = gradingRes.data || []
    aiUsage.value = {
      qaCount: qaData.length,
      compositionCount: gradingData.filter(g => g.type === 'COMPOSITION').length,
      oralCount: gradingData.filter(g => g.type === 'PRONUNCIATION').length,
    }
  } catch { aiUsage.value = { qaCount: 0, compositionCount: 0, oralCount: 0 } }
}
</script>

<style lang="scss" scoped>
.parent-control-page {
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

.lock-banner {
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: #f6ffed;
  border: 1px solid #b7eb8f;
  border-radius: 12px;
  padding: 16px;
  margin-bottom: 20px;

  &.locked {
    background: #fff1f0;
    border-color: #ffa39e;
  }

  .lock-status {
    display: flex;
    align-items: center;
    gap: 8px;
    font-size: var(--font-size-md);
    font-weight: 600;

    .lock-icon {
      font-size: 24px;
    }
  }
}

.slider-value {
  text-align: right;
  font-size: var(--font-size-sm);
  color: var(--color-text-secondary);
}

.time-range {
  display: flex;
  align-items: center;
  gap: 8px;

  .el-time-select {
    flex: 1;
  }
}

.switch-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.switch-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  font-size: var(--font-size-sm);
}

.night-lock-card {
  margin-bottom: 16px;
  h2 { font-size: var(--font-size-lg); font-weight: 600; margin-bottom: 12px; }
  .hint { font-size: var(--font-size-xs); color: var(--color-text-placeholder); margin-top: 8px; }
}

.overtime-card {
  margin-bottom: 16px;
  h2 { font-size: var(--font-size-lg); font-weight: 600; margin-bottom: 12px; }
  .overtime-config {
    display: flex; align-items: center; gap: 8px; margin-top: 12px;
    span { font-size: var(--font-size-sm); white-space: nowrap; }
  }
  .hint { font-size: var(--font-size-xs); color: var(--color-text-placeholder); margin-top: 8px; }
}

.ai-usage-card {
  margin-bottom: 16px;
  h2 { font-size: var(--font-size-lg); font-weight: 600; margin-bottom: 12px; }
  .usage-stats {
    display: flex; flex-direction: column; gap: 8px;
    .usage-item {
      display: flex; justify-content: space-between; align-items: center;
      padding: 8px 12px; background: #f8f9fa; border-radius: var(--radius-sm);
      .usage-label { font-size: var(--font-size-sm); }
      .usage-value { font-size: var(--font-size-md); font-weight: 600; color: var(--color-primary); }
    }
  }
}
</style>
