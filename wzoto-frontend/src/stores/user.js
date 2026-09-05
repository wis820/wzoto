import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { wxLogin as wxLoginApi, selectIdentity as selectIdentityApi, bindPhone as bindPhoneApi, getUserInfo as getUserInfoApi } from '@/api/auth'

export const useUserStore = defineStore('user', () => {
  // ========== State ==========
  const token = ref(localStorage.getItem('token') || '')
  const userId = ref(null)
  const openid = ref('')
  const phone = ref('')
  const nickname = ref('')
  const avatar = ref('')
  const identityType = ref('')   // PARENT / STUDENT
  const hasIdentity = ref(false)
  const verifyStatus = ref('')   // NONE / PENDING / APPROVED / REJECTED
  const realName = ref('')

  // ========== Computed ==========
  const isLoggedIn = computed(() => !!token.value)
  const isParent = computed(() => identityType.value === 'PARENT')
  const isStudent = computed(() => identityType.value === 'STUDENT')
  const identityDesc = computed(() => {
    if (!hasIdentity.value) return '未选择'
    return isParent.value ? '家长' : '大学生'
  })
  const verifyDesc = computed(() => {
    const map = { NONE: '未认证', PENDING: '审核中', APPROVED: '已认证', REJECTED: '认证失败' }
    return map[verifyStatus.value] || '未认证'
  })

  // ========== Actions ==========

  /**
   * 微信登录
   */
  async function login(code) {
    const res = await wxLoginApi(code)
    const data = res.data
    setLoginData(data)
    return data
  }

  /**
   * 选择身份（一经选择不可更改）
   */
  async function chooseIdentity(type) {
    const res = await selectIdentityApi(userId.value, type)
    const data = res.data
    setLoginData(data)
    return data
  }

  /**
   * 绑定手机号
   */
  async function bindUserPhone(phoneNumber) {
    const res = await bindPhoneApi(userId.value, phoneNumber)
    const data = res.data
    setLoginData(data)
    phone.value = phoneNumber
    return data
  }

  /**
   * 拉取当前用户信息
   */
  async function fetchUserInfo() {
    if (!token.value) return null
    try {
      const res = await getUserInfoApi()
      const data = res.data
      userId.value = data.userId
      openid.value = data.openid
      phone.value = data.phone
      nickname.value = data.nickname
      avatar.value = data.avatar
      identityType.value = data.identityType || ''
      hasIdentity.value = data.hasIdentity || false
      verifyStatus.value = data.verifyStatus || 'NONE'
      realName.value = data.realName || ''
      return data
    } catch {
      return null
    }
  }

  /**
   * 退出登录
   */
  function logout() {
    token.value = ''
    userId.value = null
    openid.value = ''
    phone.value = ''
    nickname.value = ''
    avatar.value = ''
    identityType.value = ''
    hasIdentity.value = false
    verifyStatus.value = ''
    realName.value = ''
    localStorage.removeItem('token')
  }

  // ========== 内部方法 ==========

  function setLoginData(data) {
    if (data.token) {
      token.value = data.token
      localStorage.setItem('token', data.token)
    }
    userId.value = data.userId
    nickname.value = data.nickname || ''
    avatar.value = data.avatar || ''
    identityType.value = data.identityType || ''
    hasIdentity.value = data.hasIdentity || false
    verifyStatus.value = data.verifyStatus || 'NONE'
  }

  return {
    token, userId, openid, phone, nickname, avatar,
    identityType, hasIdentity, verifyStatus, realName,
    isLoggedIn, isParent, isStudent, identityDesc, verifyDesc,
    login, chooseIdentity, bindUserPhone, fetchUserInfo, logout, setLoginData,
  }
})
