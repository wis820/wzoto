import request from '@/utils/request'

/**
 * 创建子女档案
 */
export function createChild(data) {
  return request.post('/child', data)
}

/**
 * 更新子女档案
 */
export function updateChild(id, data) {
  return request.put(`/child/${id}`, data)
}

/**
 * 删除子女档案
 */
export function deleteChild(id) {
  return request.delete(`/child/${id}`)
}

/**
 * 获取当前家长子女列表
 */
export function getMyChildren() {
  return request.get('/children')
}

/**
 * 获取子女详情
 */
export function getChildDetail(id) {
  return request.get(`/child/${id}`)
}
