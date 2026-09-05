<template>
  <div class="ai-tutor-page">
    <div class="page-header">
      <h2>AI教员助手</h2>
      <p>AI优化简历 · 智能定价分析</p>
    </div>

    <!-- Tab切换 -->
    <el-tabs v-model="activeTab" class="opt-tabs">
      <el-tab-pane label="简历优化" name="resume">
        <ResumeOptimizationForm @success="onOptSuccess" />
      </el-tab-pane>
      <el-tab-pane label="定价分析" name="pricing">
        <PricingAnalysisForm @success="onOptSuccess" />
      </el-tab-pane>
      <el-tab-pane label="我的记录" name="history">
        <OptimizationHistory :refresh-key="historyRefreshKey" />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import ResumeOptimizationForm from './components/ResumeOptimizationForm.vue'
import PricingAnalysisForm from './components/PricingAnalysisForm.vue'
import OptimizationHistory from './components/OptimizationHistory.vue'

const activeTab = ref('resume')
const historyRefreshKey = ref(0)

function onOptSuccess() {
  historyRefreshKey.value++
}
</script>

<style scoped lang="scss">
.ai-tutor-page {
  max-width: 600px;
  margin: 0 auto;
  padding: 16px;

  .page-header {
    text-align: center;
    margin-bottom: 20px;

    h2 {
      margin: 0 0 8px;
      font-size: 20px;
      color: #303133;
    }

    p {
      margin: 0;
      font-size: 13px;
      color: #909399;
    }
  }

  .opt-tabs {
    :deep(.el-tabs__header) {
      margin-bottom: 16px;
    }
  }
}
</style>
