import request from '@/utils/request'

/**
 * 微信登录
 */
export function wxLogin(code) {
  return request.post('/auth/wx-login', { code })
}

/**
 * 选择身份
 */
export function selectIdentity(userId, identityType) {
  return request.post('/auth/select-identity', { userId, identityType })
}

/**
 * 绑定手机号
 */
export function bindPhone(userId, phone) {
  return request.post('/auth/bind-phone', { userId, phone })
}

/**
 * 获取当前用户信息
 */
export function getUserInfo() {
  return request.get('/auth/me')
}
