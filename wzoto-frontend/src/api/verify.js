import request from '@/utils/request'

/**
 * 家长提交实名认证
 */
export function submitParentVerify(data) {
  return request.post('/verify/parent', data)
}

/**
 * 大学生提交实名认证
 */
export function submitStudentVerify(data) {
  return request.post('/verify/student', data)
}

/**
 * 获取认证状态
 */
export function getVerifyStatus() {
  return request.get('/verify/status')
}