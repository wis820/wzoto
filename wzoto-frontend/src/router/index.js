import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '@/stores/user'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/pages/auth/LoginPage.vue'),
    meta: { title: '登录', noAuth: true },
  },
  {
    path: '/select-identity',
    name: 'SelectIdentity',
    component: () => import('@/pages/auth/SelectIdentityPage.vue'),
    meta: { title: '选择身份', noAuth: false, needIdentity: false },
  },
  {
    path: '/bind-phone',
    name: 'BindPhone',
    component: () => import('@/pages/auth/BindPhonePage.vue'),
    meta: { title: '绑定手机号', noAuth: false },
  },
  {
    path: '/verify',
    name: 'Verify',
    component: () => import('@/pages/verify/VerifyPage.vue'),
    meta: { title: '实名认证', needIdentity: true },
  },
  {
    path: '/tutor-profile/edit',
    name: 'TutorProfileEdit',
    component: () => import('@/pages/profile/TutorProfileEditPage.vue'),
    meta: { title: '教员档案', needIdentity: true, studentOnly: true },
  },
  {
    path: '/tutors',
    name: 'TutorList',
    component: () => import('@/pages/profile/TutorListPage.vue'),
    meta: { title: '找教员', needIdentity: true, parentOnly: true },
  },
  {
    path: '/bookings',
    name: 'BookingList',
    component: () => import('@/pages/booking/BookingListPage.vue'),
    meta: { title: '预约列表', needIdentity: true },
  },
  {
    path: '/ai-report',
    name: 'AiReport',
    component: () => import('@/pages/ai/AiReportPage.vue'),
    meta: { title: 'AI学情诊断', needIdentity: true, parentOnly: true },
  },
  {
    path: '/ai-tutor',
    name: 'AiTutor',
    component: () => import('@/pages/ai/AiTutorOptimizationPage.vue'),
    meta: { title: 'AI教员助手', needIdentity: true, studentOnly: true },
  },
  {
    path: '/feature',
    name: 'Feature',
    component: () => import('@/pages/feature/FeaturePage.vue'),
    meta: { title: '增值功能', needIdentity: true, studentOnly: true },
  },
  {
    path: '/learning',
    name: 'LearningHome',
    component: () => import('@/pages/learning/LearningHomePage.vue'),
    meta: { title: '小学学习', needIdentity: true, parentOnly: true },
  },
  {
    path: '/learning/children',
    name: 'ChildManage',
    component: () => import('@/pages/learning/ChildManagePage.vue'),
    meta: { title: '子女管理', needIdentity: true, parentOnly: true },
  },
  {
    path: '/learning/plan',
    name: 'LearningPlan',
    component: () => import('@/pages/learning/LearningPlanPage.vue'),
    meta: { title: '学习计划', needIdentity: true, parentOnly: true },
  },
  {
    path: '/learning/tasks',
    name: 'LearningTask',
    component: () => import('@/pages/learning/LearningTaskPage.vue'),
    meta: { title: '学习任务', needIdentity: true, parentOnly: true },
  },
  {
    path: '/learning/report',
    name: 'LearningReport',
    component: () => import('@/pages/learning/LearningReportPage.vue'),
    meta: { title: '学情报告', needIdentity: true, parentOnly: true },
  },
  {
    path: '/learning/resources',
    name: 'LearningResource',
    component: () => import('@/pages/learning/LearningResourcePage.vue'),
    meta: { title: '学习资源', needIdentity: true, parentOnly: true },
  },
  {
    path: '/learning/courses',
    name: 'CourseDirectory',
    component: () => import('@/pages/learning/CourseDirectoryPage.vue'),
    meta: { title: '课程学习', needIdentity: true, parentOnly: true },
  },
  {
    path: '/learning/video-player',
    name: 'VideoPlayer',
    component: () => import('@/pages/learning/VideoPlayerPage.vue'),
    meta: { title: '视频播放', needIdentity: true, parentOnly: true },
  },
  {
    path: '/learning/control',
    name: 'ParentControl',
    component: () => import('@/pages/learning/ParentControlPage.vue'),
    meta: { title: '时长管控', needIdentity: true, parentOnly: true },
  },
  {
    path: '/learning/wrong-questions',
    name: 'WrongQuestion',
    component: () => import('@/pages/learning/WrongQuestionPage.vue'),
    meta: { title: '错题本', needIdentity: true, parentOnly: true },
  },
  {
    path: '/learning/ai-answer',
    name: 'AiAnswer',
    component: () => import('@/pages/learning/AiAnswerPage.vue'),
    meta: { title: 'AI答疑', needIdentity: true, parentOnly: true },
  },
  {
    path: '/learning/achievements',
    name: 'Achievement',
    component: () => import('@/pages/learning/AchievementPage.vue'),
    meta: { title: '成长激励', needIdentity: true, parentOnly: true },
  },
  // ========== 学生端页面 ==========
  {
    path: '/student/home',
    name: 'StudentHome',
    component: () => import('@/pages/student/StudentHomePage.vue'),
    meta: { title: '学习中心', needIdentity: true, parentOnly: true },
  },
  {
    path: '/student/courses',
    name: 'StudentCourses',
    component: () => import('@/pages/student/StudentCoursePage.vue'),
    meta: { title: '课程专区', needIdentity: true, parentOnly: true },
  },
  {
    path: '/student/exercise',
    name: 'StudentExercise',
    component: () => import('@/pages/student/StudentExercisePage.vue'),
    meta: { title: '专项练习', needIdentity: true, parentOnly: true },
  },
  {
    path: '/student/ai-qa',
    name: 'StudentAiQa',
    component: () => import('@/pages/student/StudentAiQaPage.vue'),
    meta: { title: 'AI答疑', needIdentity: true, parentOnly: true },
  },
  {
    path: '/student/wrong-book',
    name: 'StudentWrongBook',
    component: () => import('@/pages/student/StudentWrongBookPage.vue'),
    meta: { title: '错题本', needIdentity: true, parentOnly: true },
  },
  {
    path: '/student/oral',
    name: 'StudentOral',
    component: () => import('@/pages/student/StudentOralPage.vue'),
    meta: { title: '口语跟读', needIdentity: true, parentOnly: true },
  },
  {
    path: '/student/composition',
    name: 'StudentComposition',
    component: () => import('@/pages/student/StudentCompositionPage.vue'),
    meta: { title: '作文批改', needIdentity: true, parentOnly: true },
  },
  {
    path: '/',
    name: 'Home',
    component: () => import('@/pages/home/HomePage.vue'),
    meta: { title: '首页', needIdentity: true },
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/',
  },
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

// 路由守卫
router.beforeEach(async (to, from, next) => {
  // 设置标题
  document.title = to.meta.title ? `${to.meta.title} - 学霸到家` : '学霸到家'

  const userStore = useUserStore()

  // 不需要登录的页面直接放行
  if (to.meta.noAuth) {
    // 已登录用户访问登录页 → 跳转首页
    if (to.name === 'Login' && userStore.isLoggedIn) {
      if (!userStore.hasIdentity) {
        return next('/select-identity')
      }
      return next('/')
    }
    return next()
  }

  // 需要登录但未登录 → 跳转登录
  if (!userStore.isLoggedIn) {
    return next('/login')
  }

  // 已登录但还没拉取过用户信息
  if (!userStore.userId) {
    await userStore.fetchUserInfo()
  }

  // 需要已选身份但未选 → 跳转身份选择
  if (to.meta.needIdentity && !userStore.hasIdentity) {
    return next('/select-identity')
  }

  // 已选身份访问身份选择页 → 跳转首页（身份不可更改）
  if (to.name === 'SelectIdentity' && userStore.hasIdentity) {
    return next('/')
  }

  // 家长专属页面，非家长跳转首页
  if (to.meta.parentOnly && !userStore.isParent) {
    return next('/')
  }

  // 大学生专属页面，非大学生跳转首页
  if (to.meta.studentOnly && !userStore.isStudent) {
    return next('/')
  }

  next()
})

export default router
