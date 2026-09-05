import request from '@/utils/request'

/**
 * 创建简历置顶订单
 */
export function createResumePinOrder() {
  return request.post('/feature/resume-pin')
}

/**
 * 创建加急审核订单
 */
export function createExpediteVerifyOrder() {
  return request.post('/feature/expedite-verify')
}

/**
 * 支付功能订单（模拟支付）
 */
export function payFeatureOrder(id) {
  return request.post(`/feature/${id}/pay`)
}

/**
 * 获取我的功能订单列表
 */
export function getMyFeatureOrders() {
  return request.get('/feature/orders')
}

/**
 * 获取订单详情
 */
export function getFeatureOrderDetail(id) {
  return request.get(`/feature/${id}`)
}
