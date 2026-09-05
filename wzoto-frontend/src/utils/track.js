/**
 * 前端埋点工具
 * 本次仅输出日志，不接入第三方平台
 */

export function trackPageView(pageName, extra = {}) {
  const userId = localStorage.getItem('userId') || ''
  console.log(`[BI] page_view|${pageName}|userId=${userId}`, extra)
}

export function trackEvent(eventName, extra = {}) {
  const userId = localStorage.getItem('userId') || ''
  console.log(`[BI] event|${eventName}|userId=${userId}`, extra)
}
