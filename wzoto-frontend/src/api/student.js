import request from '@/utils/request'

// ========== AI答疑 ==========

export function askQuestion(data) {
  return request.post('/ai/qa/ask', data)
}

export function followUpQuestion(data) {
  return request.post('/ai/qa/follow-up', data)
}

export function getQaHistory(childId) {
  return request.get(`/ai/qa/history/${childId}`)
}

export function getQaSession(conversationId) {
  return request.get(`/ai/qa/session/${conversationId}`)
}

// ========== AI批改 ==========

export function gradeComposition(data) {
  return request.post('/ai/grading/composition', data)
}

export function evaluatePronunciation(data) {
  return request.post('/ai/grading/pronunciation', data)
}

export function getGradingHistory(childId) {
  return request.get(`/ai/grading/history/${childId}`)
}

// ========== AI学习规划 ==========

export function generateAiPlan(childId) {
  return request.post(`/ai/plan/generate/${childId}`)
}

export function getTodayAiPlan(childId) {
  return request.get(`/ai/plan/today/${childId}`)
}

export function applyAiPlan(planId) {
  return request.put(`/ai/plan/apply/${planId}`)
}

// ========== 习题库 ==========

export function listExercises(params) {
  return request.get('/exercise/list', { params })
}

export function getTestPaper(id) {
  return request.get(`/exercise/paper/${id}`)
}

export function submitAnswer(data) {
  return request.post('/exercise/submit', data)
}

export function listTestPapers(params) {
  return request.get('/exercise/paper/list', { params })
}

// ========== 微课 ==========

export function listCourses(params) {
  return request.get('/course/list', { params })
}

export function getCourseDetail(id) {
  return request.get(`/course/${id}`)
}

export function reportCourseProgress(data) {
  return request.post('/course/progress', data)
}

export function getRecommendedCourses(childId) {
  return request.get(`/course/recommend/${childId}`)
}

// ========== 知识点 ==========

export function getKnowledgeTree(params) {
  return request.get('/knowledge/tree', { params })
}

export function getKnowledgeMastery(childId) {
  return request.get(`/knowledge/mastery/${childId}`)
}
