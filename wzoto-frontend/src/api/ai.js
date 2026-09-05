import request from '@/utils/request'

/**
 * 创建AI学情诊断报告
 */
export function createAiReport(data) {
  return request.post('/ai/report', data)
}

/**
 * 获取我的AI报告列表
 */
export function getMyReports() {
  return request.get('/ai/reports')
}

/**
 * 获取报告详情
 */
export function getReportDetail(id) {
  return request.get(`/ai/report/${id}`)
}

/**
 * 付费解锁报告（模拟支付）
 */
export function payToUnlock(id) {
  return request.post(`/ai/report/${id}/pay`)
}

/**
 * 获取会员状态
 */
export function getMembershipStatus() {
  return request.get('/ai/membership-status')
}

/**
 * 创建AI教员简历优化
 */
export function createResumeOptimization(data) {
  return request.post('/ai/tutor/resume', data)
}

/**
 * 创建AI定价分析
 */
export function createPricingAnalysis(data) {
  return request.post('/ai/tutor/pricing', data)
}

/**
 * 获取我的教员优化记录
 */
export function getMyOptimizations() {
  return request.get('/ai/tutor/optimizations')
}

/**
 * 获取优化详情
 */
export function getOptimizationDetail(id) {
  return request.get(`/ai/tutor/${id}`)
}

/**
 * 付费解锁教员优化
 */
export function payToUnlockOptimization(id) {
  return request.post(`/ai/tutor/${id}/pay`)
}

/**
 * 开发环境登录
 */
export function devLogin(nickname) {
  return request.post('/auth/dev-login', { nickname })
}
