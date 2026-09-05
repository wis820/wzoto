import { ElMessage } from 'element-plus'

/**
 * 轻量级Toast提示 - 基于Element Plus Message
 */
export function showToast(msg, duration = 2000) {
  ElMessage({
    message: msg,
    duration,
    center: true,
    grouping: true,
  })
}
