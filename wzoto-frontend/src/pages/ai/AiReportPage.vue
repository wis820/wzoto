<template>
  <div class="ai-report-page">
    <!-- 顶部导航 -->
    <div class="nav-bar">
      <el-icon @click="router.back()"><ArrowLeft /></el-icon>
      <span class="nav-title">AI学情诊断</span>
      <span></span>
    </div>

    <!-- 表单模式 -->
    <div v-if="!showResult" class="form-section">
      <div class="card">
        <h3 class="section-title">🤖 AI学情诊断报告</h3>
        <p class="section-desc">填写孩子薄弱知识点，AI自动生成辅导规划</p>
        <div class="price-hint">
          <span v-if="memberStatus.isMember" class="member-hint">💎 会员免费 | 本月已用 {{ memberStatus.freeReportUsed }}/{{ memberStatus.freeReportQuota }}</span>
          <span v-else class="paid-hint">非会员 ¥19.9/次 | 会员免费</span>
        </div>
      </div>

      <div class="card form-card">
        <el-form :model="form" label-position="top" :rules="rules" ref="formRef">
          <el-form-item label="子女姓名" prop="childName">
            <el-input v-model="form.childName" placeholder="请输入子女姓名" />
          </el-form-item>
          <el-form-item label="薄弱学科" prop="weakSubjects">
            <el-checkbox-group v-model="subjectList" @change="onSubjectChange">
              <el-checkbox label="小学数学" value="小学数学" />
              <el-checkbox label="初中数学" value="初中数学" />
              <el-checkbox label="高中数学" value="高中数学" />
              <el-checkbox label="小学英语" value="小学英语" />
              <el-checkbox label="初中英语" value="初中英语" />
              <el-checkbox label="高中英语" value="高中英语" />
              <el-checkbox label="初中物理" value="初中物理" />
              <el-checkbox label="高中物理" value="高中物理" />
              <el-checkbox label="初中化学" value="初中化学" />
              <el-checkbox label="语文" value="语文" />
            </el-checkbox-group>
          </el-form-item>
          <el-form-item label="近期考试分数（选填）">
            <el-input v-model="form.recentScores" placeholder="如：数学72, 英语68" />
          </el-form-item>
          <el-form-item label="薄弱知识点描述" prop="weakPointDesc">
            <el-input v-model="form.weakPointDesc" type="textarea" :rows="3" placeholder="如：二元一次方程组解不对，英语阅读理解失分严重" />
          </el-form-item>
          <el-form-item label="上传试卷/错题照片（选填，最多6张）">
            <div class="upload-placeholder">
              <p class="upload-hint">📷 图片上传功能开发中</p>
            </div>
          </el-form-item>
        </el-form>
      </div>

      <el-button type="warning" class="btn-submit" :loading="loading" @click="handleSubmit">
        {{ memberStatus.isMember ? '🤖 免费生成报告' : '🤖 生成报告（¥19.9）' }}
      </el-button>
    </div>

    <!-- 报告结果模式 -->
    <div v-else class="result-section">
      <div class="card report-card" v-if="currentReport">
        <div class="report-header">
          <span class="report-icon">📊</span>
          <div>
            <h3>{{ currentReport.childName || '孩子' }} · 学情诊断报告</h3>
            <p class="report-time">{{ formatDate(currentReport.createdAt) }}</p>
          </div>
          <el-tag v-if="currentReport.reportType === 'FULL'" type="success" size="small">完整报告</el-tag>
          <el-tag v-else type="warning" size="small">预览</el-tag>
        </div>

        <!-- 完整报告内容 -->
        <div v-if="currentReport.reportType === 'FULL' && currentReport.reportContent" class="report-full">
          <div v-for="(item, idx) in parsedReport.weaknessAnalysis" :key="idx" class="weakness-block">
            <h4 class="block-title">{{ subjectLabel(item.subject) }} · 薄弱分析</h4>
            <div class="severity-tag" :class="severityClass(item.severity)">{{ item.severity }}</div>
            <ul class="points-list">
              <li v-for="(p, i) in item.points" :key="i">{{ p }}</li>
            </ul>
          </div>

          <div v-if="parsedReport.knowledgeGraph" class="info-block">
            <h4 class="block-title">🗺️ 知识图谱定位</h4>
            <div class="info-row">
              <span class="label">基础水平：</span>
              <span>{{ parsedReport.knowledgeGraph.foundationLevel }}</span>
            </div>
            <div class="info-row">
              <span class="label">核心缺口：</span>
              <span v-for="(gap, i) in parsedReport.knowledgeGraph.coreGaps" :key="i" class="gap-tag">{{ gap }}</span>
            </div>
          </div>

          <div v-if="parsedReport.suggestion" class="info-block suggest-block">
            <h4 class="block-title">📋 辅导建议</h4>
            <div class="info-row"><span class="label">方向：</span>{{ parsedReport.suggestion.direction }}</div>
            <div class="info-row"><span class="label">频次：</span>{{ parsedReport.suggestion.frequency }}</div>
            <div class="info-row"><span class="label">周期：</span>{{ parsedReport.suggestion.duration }}</div>
            <div v-if="parsedReport.suggestion.keyActions" class="actions-list">
              <span class="label">行动建议：</span>
              <ol>
                <li v-for="(a, i) in parsedReport.suggestion.keyActions" :key="i">{{ a }}</li>
              </ol>
            </div>
          </div>

          <div v-if="parsedReport.recommendedTutorProfile" class="info-block">
            <h4 class="block-title">🎓 推荐教员画像</h4>
            <div class="info-row"><span class="label">擅长学科：</span>{{ parsedReport.recommendedTutorProfile.subjects?.join('、') }}</div>
            <div class="info-row"><span class="label">学历背景：</span>{{ parsedReport.recommendedTutorProfile.education }}</div>
            <div class="info-row"><span class="label">特质要求：</span>{{ parsedReport.recommendedTutorProfile.traits?.join('、') }}</div>
          </div>
        </div>

        <!-- 预览模式（非会员） -->
        <div v-else class="report-preview">
          <p class="preview-text">{{ currentReport.previewContent }}</p>
          <div class="preview-lock">
            <span class="lock-icon">🔒</span>
            <p>完整报告包含详细知识图谱和个性化辅导方案</p>
            <el-button type="warning" @click="handleUnlock(currentReport.id)">付费解锁 ¥19.9</el-button>
          </div>
        </div>
      </div>

      <el-button class="btn-back" @click="showResult = false">返回重新诊断</el-button>
    </div>

    <!-- 历史报告列表 -->
    <div v-if="!showResult && reportList.length > 0" class="history-section">
      <h3 class="section-title">📊 历史诊断报告</h3>
      <div class="card report-item" v-for="r in reportList" :key="r.id" @click="viewReport(r)">
        <div class="report-item-header">
          <span class="report-item-title">{{ r.childName || '孩子' }} · {{ r.weakSubjects }}</span>
          <el-tag :type="r.reportType === 'FULL' ? 'success' : 'warning'" size="small">{{ r.reportType === 'FULL' ? '完整' : '预览' }}</el-tag>
        </div>
        <p class="report-item-time">{{ formatDate(r.createdAt) }}</p>
      </div>
    </div>

    <!-- 会员升级弹窗 -->
    <MemberUpgradePopup
      :visible="showVipPopup"
      featureName="AI学情诊断报告"
      :features="['免费预约线下试课', '无限私信所有教员', '不限次数查看老师完整资料']"
      :isParent="true"
      :showAiOptions="true"
      @close="showVipPopup = false"
      @buy-single="handleBuySingleReport"
      @go-vip="showVipPopup = false"
    />
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { useRouter } from 'vue-router'
import { ArrowLeft } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useUserStore } from '@/stores/user'
import { createAiReport, getMyReports, getReportDetail, payToUnlock, getMembershipStatus } from '@/api/ai'
import MemberUpgradePopup from '@/components/MemberUpgradePopup.vue'
import { subjectLabel } from '@/utils/format'

const router = useRouter()
const userStore = useUserStore()

const formRef = ref(null)
const loading = ref(false)
const showResult = ref(false)
const showVipPopup = ref(false)
const currentReport = ref(null)
const reportList = ref([])
const subjectList = ref([])

const form = reactive({
  childName: '',
  weakSubjects: '',
  recentScores: '',
  weakPointDesc: '',
  photoUrls: '',
})

const memberStatus = reactive({
  isMember: false,
  freeReportUsed: 0,
  freeReportQuota: 2,
})

const rules = {
  weakSubjects: [{ required: true, message: '请选择薄弱学科', trigger: 'change' }],
  weakPointDesc: [{ required: true, message: '请描述薄弱知识点', trigger: 'blur' }],
}

const parsedReport = computed(() => {
  if (!currentReport.value?.reportContent) return {}
  try {
    return JSON.parse(currentReport.value.reportContent)
  } catch {
    return {}
  }
})

function onSubjectChange(val) {
  form.weakSubjects = val.join(',')
}

function severityClass(severity) {
  const map = { '严重': 'sev-critical', '较重': 'sev-high', '中等': 'sev-medium', '轻度': 'sev-low' }
  return map[severity] || 'sev-medium'
}

function formatDate(dateStr) {
  if (!dateStr) return ''
  return new Date(dateStr).toLocaleDateString('zh-CN')
}

async function handleSubmit() {
  try {
    await formRef.value.validate()
  } catch { return }

  // 非会员弹窗提示
  if (!memberStatus.isMember) {
    showVipPopup.value = true
    return
  }

  loading.value = true
  try {
    const res = await createAiReport(form)
    currentReport.value = res.data
    showResult.value = true
    ElMessage.success('报告生成成功')
  } catch (e) {
    ElMessage.error(e.message || '生成失败')
  } finally {
    loading.value = false
  }
}

async function handleBuySingleReport() {
  loading.value = true
  try {
    const res = await createAiReport(form)
    // 自动付费解锁
    if (res.data.reportType === 'PREVIEW') {
      const payRes = await payToUnlock(res.data.id)
      currentReport.value = payRes.data
    } else {
      currentReport.value = res.data
    }
    showResult.value = true
    ElMessage.success('支付成功，报告已解锁')
  } catch (e) {
    ElMessage.error(e.message || '操作失败')
  } finally {
    loading.value = false
  }
}

async function viewReport(r) {
  try {
    const res = await getReportDetail(r.id)
    currentReport.value = res.data
    showResult.value = true
  } catch (e) {
    ElMessage.error(e.message || '获取报告失败')
  }
}

async function handleUnlock(reportId) {
  try {
    await ElMessageBox.confirm('确认支付 ¥19.9 解锁完整报告？', '付费解锁', { type: 'warning' })
    const res = await payToUnlock(reportId)
    currentReport.value = res.data
    ElMessage.success('解锁成功')
  } catch { /* cancel */ }
}

async function loadMemberStatus() {
  try {
    const res = await getMembershipStatus()
    Object.assign(memberStatus, res.data)
  } catch { /* ignore */ }
}

async function loadReports() {
  try {
    const res = await getMyReports()
    reportList.value = res.data || []
  } catch { /* ignore */ }
}

onMounted(() => {
  loadMemberStatus()
  loadReports()
})
</script>

<style lang="scss" scoped>
.ai-report-page {
  min-height: 100vh;
  background: var(--color-bg, #f5f6fa);
  padding-bottom: 24px;
}

.nav-bar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 16px;
  background: #fff;
  position: sticky;
  top: 0;
  z-index: 10;
  box-shadow: 0 1px 4px rgba(0,0,0,0.06);

  .nav-title { font-size: 17px; font-weight: 600; }
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 8px;
}

.section-desc {
  font-size: 13px;
  color: #999;
  margin-bottom: 8px;
}

.card {
  background: #fff;
  border-radius: 12px;
  padding: 16px;
  margin: 12px 16px;
  box-shadow: 0 1px 4px rgba(0,0,0,0.04);
}

.price-hint {
  .member-hint { color: #4A7BFF; font-size: 13px; font-weight: 500; }
  .paid-hint { color: #FF8C2E; font-size: 13px; }
}

.form-card {
  :deep(.el-form-item__label) { font-weight: 600; font-size: 14px; }
  :deep(.el-checkbox-group) { display: flex; flex-wrap: wrap; gap: 4px; }
  :deep(.el-checkbox) { margin-right: 0; }
}

.upload-placeholder {
  width: 100%;
  height: 80px;
  border: 2px dashed #ddd;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  .upload-hint { color: #ccc; font-size: 13px; }
}

.btn-submit {
  display: block;
  width: calc(100% - 32px);
  margin: 16px auto;
  height: 48px;
  font-size: 16px;
  font-weight: 600;
  border-radius: 24px;
}

.btn-back {
  display: block;
  width: calc(100% - 32px);
  margin: 16px auto;
  height: 44px;
  border-radius: 22px;
}

/* 报告结果 */
.report-card {
  .report-header {
    display: flex;
    align-items: center;
    gap: 12px;
    margin-bottom: 16px;
    .report-icon { font-size: 32px; }
    h3 { font-size: 16px; font-weight: 600; }
    .report-time { font-size: 12px; color: #999; margin-top: 2px; }
  }
}

.weakness-block {
  background: #f8f9ff;
  border-radius: 10px;
  padding: 12px;
  margin-bottom: 12px;
  position: relative;

  .block-title { font-size: 15px; font-weight: 600; margin-bottom: 8px; }
  .severity-tag {
    position: absolute; top: 12px; right: 12px;
    font-size: 12px; padding: 2px 8px; border-radius: 10px; font-weight: 600;
    &.sev-critical { background: #fff1f0; color: #f5222d; }
    &.sev-high { background: #fff7e6; color: #fa8c16; }
    &.sev-medium { background: #e6f7ff; color: #1890ff; }
    &.sev-low { background: #f6ffed; color: #52c41a; }
  }
  .points-list {
    padding-left: 20px;
    li { font-size: 14px; color: #555; padding: 2px 0; }
  }
}

.info-block {
  background: #fafafa;
  border-radius: 10px;
  padding: 12px;
  margin-bottom: 12px;
  .block-title { font-size: 15px; font-weight: 600; margin-bottom: 8px; }
  .info-row { font-size: 14px; color: #555; padding: 3px 0; .label { color: #333; font-weight: 500; } }
  .gap-tag { background: #fff1f0; color: #f5222d; padding: 2px 8px; border-radius: 4px; font-size: 12px; margin-right: 4px; }
}

.suggest-block { background: #f0f7ff; }

.actions-list {
  margin-top: 6px;
  ol { padding-left: 20px; li { font-size: 14px; color: #555; padding: 2px 0; } }
}

.report-preview {
  .preview-text { font-size: 14px; color: #666; line-height: 1.6; }
  .preview-lock {
    text-align: center;
    margin-top: 20px;
    padding: 16px;
    background: #fff7e6;
    border-radius: 12px;
    .lock-icon { font-size: 28px; }
    p { font-size: 13px; color: #999; margin: 8px 0 12px; }
  }
}

/* 历史报告 */
.history-section {
  .section-title { padding: 0 16px; font-size: 15px; }
}

.report-item {
  cursor: pointer;
  &:active { opacity: 0.85; }
  .report-item-header { display: flex; justify-content: space-between; align-items: center; }
  .report-item-title { font-size: 14px; font-weight: 500; }
  .report-item-time { font-size: 12px; color: #999; margin-top: 4px; }
}
</style>
