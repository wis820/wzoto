import request from '@/utils/request'

// ========== 学习计划 ==========

export function getLearningPlanConfig(childId) {
  return request.get(`/learning/plan/${childId}`)
}

export function saveLearningPlanConfig(childId, data) {
  return request.post(`/learning/plan/${childId}`, data)
}

export function assignSpecialTask(childId, data) {
  return request.post(`/learning/plan/${childId}/special-task`, data)
}

// ========== 学习任务 ==========

export function listLearningTasks(childId, date) {
  const params = date ? { date } : {}
  return request.get(`/learning/tasks/${childId}`, { params })
}

export function completeLearningTask(taskId) {
  return request.post(`/learning/task/${taskId}/complete`)
}

// ========== 学情报告 ==========

export function getLearningReportOverview(childId) {
  return request.get(`/learning/report/${childId}`)
}

export function getDailyReport(childId, date) {
  const params = date ? { date } : {}
  return request.get(`/learning/report/${childId}/daily`, { params })
}

export function getWeeklyReport(childId, weekStart) {
  const params = weekStart ? { weekStart } : {}
  return request.get(`/learning/report/${childId}/weekly`, { params })
}

export function getMonthlyReport(childId, year, month) {
  const params = {}
  if (year) params.year = year
  if (month) params.month = month
  return request.get(`/learning/report/${childId}/monthly`, { params })
}

export function getWeakPoints(childId, subject, topN = 10) {
  const params = { topN }
  if (subject) params.subject = subject
  return request.get(`/learning/report/${childId}/weak-points`, { params })
}

export function exportWrongQuestions(childId, subject) {
  const params = {}
  if (subject) params.subject = subject
  return request.get(`/learning/report/${childId}/wrong-questions/export`, { params })
}

// ========== 学习资源 ==========

export function listLearningResources(params) {
  return request.get('/learning/resources', { params })
}

export function getLearningResourceDetail(id) {
  return request.get(`/learning/resource/${id}`)
}

export function unlockLearningResource(id) {
  return request.post(`/learning/resource/${id}/unlock`)
}

// ========== 家长管控 ==========

export function getControlConfig(childId) {
  return request.get(`/learning/control/${childId}`)
}

export function saveControlConfig(childId, data) {
  return request.post(`/learning/control/${childId}`, data)
}

export function lockControl(childId) {
  return request.post(`/learning/control/${childId}/lock`)
}

export function unlockControl(childId) {
  return request.post(`/learning/control/${childId}/unlock`)
}

// ========== 错题管理 ==========

export function listWrongQuestions(childId, params = {}) {
  return request.get(`/learning/wrong-questions/${childId}`, { params })
}

export function batchAddWrongQuestionsToReview(childId, ids) {
  return request.post('/learning/wrong-questions/batch-review', ids, { params: { childId } })
}

export function markWrongQuestionMastered(id) {
  return request.post(`/learning/wrong-question/${id}/mastered`)
}

// ========== 播放进度 ==========

export function reportPlayProgress(data) {
  return request.post('/learning/play-progress', data)
}

export function getPlayProgress(resourceId, childId) {
  return request.get(`/learning/play-progress/${resourceId}`, { params: { childId } })
}

// ========== 课程目录树 ==========

export function getCourseTree(params) {
  return request.get('/learning/courses', { params })
}

// ========== 成长激励 ==========

export function listAchievements(childId, achievementType) {
  const params = achievementType ? { achievementType } : {}
  return request.get(`/learning/achievements/${childId}`, { params })
}

export function assignAchievementTask(childId, data) {
  return request.post(`/learning/achievements/${childId}/task`, null, { params: data })
}
