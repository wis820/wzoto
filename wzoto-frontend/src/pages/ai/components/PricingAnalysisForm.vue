<template>
  <div class="pricing-form">
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
        <el-input v-model="form.subjects" placeholder="如：数学,物理" />
      </el-form-item>
      <el-form-item label="教学经验">
        <el-input v-model="form.experience" type="textarea" :rows="2" placeholder="描述你的教学经历..." />
      </el-form-item>
      <el-form-item label="当前时薪">
        <el-input-number v-model="form.currentRate" :min="30" :max="500" :step="10" />
        <span class="rate-unit">元/小时</span>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" :loading="loading" @click="handleSubmit" style="width: 100%">
          AI定价分析（免费）
        </el-button>
      </el-form-item>
    </el-form>

    <div v-if="result" class="result-section">
      <el-divider />
      <div v-if="result.optimizedContent" class="full-result">
        <h4>AI定价分析报告</h4>
        <div class="result-card" v-if="parsedContent">
          <div class="result-item">
            <strong>当前定价评估：</strong>
            <p>{{ parsedContent.currentRateAnalysis?.comment }}</p>
          </div>
          <div class="result-item">
            <strong>市场参考：</strong>
            <p>范围：{{ parsedContent.marketReference?.range }} | 均价：{{ parsedContent.marketReference?.average }}</p>
          </div>
          <div class="pricing-suggestions">
            <div class="price-tag conservative">
              <span class="label">保守价</span>
              <span class="value">¥{{ parsedContent.suggestedPricing?.conservative }}/小时</span>
            </div>
            <div class="price-tag optimal">
              <span class="label">推荐价</span>
              <span class="value">¥{{ parsedContent.suggestedPricing?.optimal }}/小时</span>
            </div>
            <div class="price-tag aggressive">
              <span class="label">激进价</span>
              <span class="value">¥{{ parsedContent.suggestedPricing?.aggressive }}/小时</span>
            </div>
          </div>
          <div class="result-item" v-if="parsedContent.suggestedPricing?.recommendation">
            <strong>建议：</strong>
            <p>{{ parsedContent.suggestedPricing.recommendation }}</p>
          </div>
          <div v-if="parsedContent.strategy" class="strategy-section">
            <strong>定价策略：</strong>
            <div class="strategy-item">
              <span class="strategy-label">短期：</span>{{ parsedContent.strategy.shortTerm }}
            </div>
            <div class="strategy-item">
              <span class="strategy-label">中期：</span>{{ parsedContent.strategy.midTerm }}
            </div>
            <div class="strategy-item">
              <span class="strategy-label">长期：</span>{{ parsedContent.strategy.longTerm }}
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { createPricingAnalysis } from '@/api/ai'

const emit = defineEmits(['success'])

const loading = ref(false)
const result = ref(null)

const form = ref({
  university: '',
  major: '',
  grade: '',
  subjects: '',
  experience: '',
  currentRate: 100
})

const parsedContent = computed(() => {
  if (!result.value?.optimizedContent) return null
  try {
    return JSON.parse(result.value.optimizedContent)
  } catch {
    return null
  }
})

async function handleSubmit() {
  if (!form.value.university || !form.value.major || !form.value.subjects) {
    ElMessage.warning('请填写学校、专业和辅导科目')
    return
  }
  loading.value = true
  try {
    const res = await createPricingAnalysis(form.value)
    result.value = res.data
    ElMessage.success('定价分析完成！')
    emit('success')
  } catch (e) {
    ElMessage.error(e.message || '分析失败')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped lang="scss">
.pricing-form {
  .rate-unit { margin-left: 8px; color: #909399; font-size: 13px; }

  .result-section {
    margin-top: 16px;

    .result-card {
      background: #f5f7fa;
      border-radius: 8px;
      padding: 16px;

      .result-item {
        margin-bottom: 12px;
        p { margin: 4px 0; color: #606266; }
      }

      .pricing-suggestions {
        display: flex;
        gap: 12px;
        margin: 16px 0;

        .price-tag {
          flex: 1;
          text-align: center;
          padding: 12px;
          border-radius: 8px;

          .label { display: block; font-size: 12px; color: #909399; }
          .value { display: block; font-size: 18px; font-weight: bold; margin-top: 4px; }
        }
        .conservative { background: #f0f9eb; .value { color: #67c23a; } }
        .optimal { background: #ecf5ff; .value { color: #409eff; } }
        .aggressive { background: #fdf6ec; .value { color: #e6a23c; } }
      }

      .strategy-section {
        margin-top: 12px;

        .strategy-item {
          margin: 4px 0;
          font-size: 13px;
          color: #606266;

          .strategy-label {
            color: #909399;
            margin-right: 4px;
          }
        }
      }
    }
  }
}
</style>
