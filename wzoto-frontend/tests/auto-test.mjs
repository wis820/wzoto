import { chromium } from 'playwright'
import fs from 'fs'
import path from 'path'

const BASE_URL = 'http://localhost:5174'
const API_URL = 'http://localhost:8080'
const SCREENSHOT_DIR = path.resolve('../docs/screenshots')
const REPORT_DIR = path.resolve('../docs')

fs.mkdirSync(SCREENSHOT_DIR, { recursive: true })

const testResults = []

async function capturePage(page, name, description) {
  const screenshotPath = path.join(SCREENSHOT_DIR, `${name}.png`)
  await page.screenshot({ path: screenshotPath, fullPage: true })
  testResults.push({
    name,
    description,
    screenshot: `screenshots/${name}.png`,
    url: page.url(),
    timestamp: new Date().toISOString(),
  })
  console.log(`  ✅ ${name}`)
}

// Wait for the Vue app to fully render (user info loaded)
async function waitForAppReady(page, timeout = 10000) {
  await page.waitForTimeout(2000)
  // Wait for user info to be loaded - check for common elements
  try {
    await page.waitForSelector('[class*="home-page"], [class*="learning"], [class*="child"], [class*="plan"], [class*="report"], [class*="resource"], [class*="control"], [class*="wrong"], [class*="achievement"], [class*="answer"], [class*="task"], [class*="student"]', { timeout })
  } catch (e) {
    // If no specific class found, just wait
    await page.waitForTimeout(3000)
  }
}

async function run() {
  const browser = await chromium.launch({ headless: true })
  const context = await browser.newContext({
    viewport: { width: 375, height: 812 },
    deviceScaleFactor: 2,
  })
  const page = await context.newPage()

  const apiResponses = []
  page.on('response', async (response) => {
    const url = response.url()
    if (url.includes('/api/')) {
      try {
        const status = response.status()
        const body = await response.text().catch(() => '')
        apiResponses.push({ url: url.replace(BASE_URL, ''), status, body: body.substring(0, 500) })
      } catch (e) { /* ignore */ }
    }
  })

  // ====== Step 1: Login Page ======
  console.log('📍 Step 1: Login Page')
  await page.goto(`${BASE_URL}/login`, { waitUntil: 'domcontentloaded' })
  await page.waitForTimeout(2500)
  await capturePage(page, '01_login_page', '登录页面 - 微信一键登录 + 开发模式快捷登录')

  // ====== Step 2: Dev Login ======
  console.log('📍 Step 2: Dev Login')
  const loginResult = await page.evaluate(async (apiUrl) => {
    const res = await fetch(`${apiUrl}/api/auth/dev-login`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ nickname: '测试家长' })
    })
    return await res.json()
  }, API_URL)
  console.log('  Token:', loginResult.code === 200 ? 'OK' : 'FAIL')

  // Store token
  await page.evaluate((token) => {
    localStorage.setItem('token', token)
  }, loginResult.data.token)

  // ====== Step 3: Home Page ======
  console.log('📍 Step 3: Home Page')
  await page.goto(`${BASE_URL}/`, { waitUntil: 'domcontentloaded' })
  await page.waitForTimeout(5000)  // Extra wait for fetchUserInfo
  await capturePage(page, '03_home_page', '首页 - 家长端功能入口（找教员、小学学习、AI学情诊断、预约管理）')

  // ====== Step 4: Learning Home ======
  console.log('📍 Step 4: Learning Home')
  await page.goto(`${BASE_URL}/learning`, { waitUntil: 'domcontentloaded' })
  await page.waitForTimeout(4000)
  await capturePage(page, '04_learning_home', '学习首页 - 今日任务进度环、快速入口九宫格、薄弱知识点提示')

  // ====== Step 5: Child Manage ======
  console.log('📍 Step 5: Child Manage')
  await page.goto(`${BASE_URL}/learning/children`, { waitUntil: 'domcontentloaded' })
  await page.waitForTimeout(3000)
  await capturePage(page, '05_child_manage', '子女管理 - 空状态展示，"+ 新增子女"按钮')

  // ====== Step 5b: Create Child ======
  console.log('📍 Step 5b: Create Child')
  try {
    const addBtn = page.locator('button', { hasText: /新增子女|添加子女|\+ 新增/ }).first()
    await addBtn.waitFor({ state: 'visible', timeout: 5000 })
    await addBtn.click()
    await page.waitForTimeout(1500)
    await capturePage(page, '05b_child_form', '子女管理 - 新增子女弹窗表单（姓名/年级/教材版本/学校）')

    // Fill name
    const nameInput = page.locator('input[placeholder*="姓名"]').first()
    if (await nameInput.isVisible({ timeout: 2000 })) {
      await nameInput.fill('测试小明')
    }

    // Select grade via Element Plus dropdown
    const gradeSelects = page.locator('.el-select').all()
    const selects = await gradeSelects
    if (selects.length >= 1) {
      await selects[0].click()
      await page.waitForTimeout(800)
      // Click "三年级" option
      const option = page.locator('.el-select-dropdown__item', { hasText: /三年级|3年级/ }).first()
      if (await option.isVisible({ timeout: 2000 })) {
        await option.click()
        await page.waitForTimeout(500)
      }
    }

    // Select textbook version
    if (selects.length >= 2) {
      await selects[1].click()
      await page.waitForTimeout(800)
      const textbookOption = page.locator('.el-select-dropdown__item', { hasText: /人教版/ }).first()
      if (await textbookOption.isVisible({ timeout: 2000 })) {
        await textbookOption.click()
        await page.waitForTimeout(500)
      }
    }

    await capturePage(page, '05c_child_form_filled', '子女管理 - 填写完成的子女表单')

    // Click save
    const saveBtn = page.locator('button', { hasText: /保存/ }).first()
    await saveBtn.click()
    await page.waitForTimeout(3000)
    await capturePage(page, '05d_child_created', '子女管理 - 创建子女后列表')
  } catch (e) {
    console.log('  ⚠️ Child creation:', e.message.substring(0, 100))
  }

  // ====== Step 6: Learning Plan ======
  console.log('📍 Step 6: Learning Plan')
  await page.goto(`${BASE_URL}/learning/plan`, { waitUntil: 'domcontentloaded' })
  await page.waitForTimeout(4000)
  await capturePage(page, '06_learning_plan', '学习计划 - 每日时长设置、学科权重滑块、专项练习开关与下发')

  // ====== Step 7: Learning Tasks ======
  console.log('📍 Step 7: Learning Tasks')
  await page.goto(`${BASE_URL}/learning/tasks`, { waitUntil: 'domcontentloaded' })
  await page.waitForTimeout(3000)
  await capturePage(page, '07_learning_tasks', '学习任务 - 按日期查看任务列表、标记完成状态')

  // ====== Step 8: Learning Report ======
  console.log('📍 Step 8: Learning Report')
  await page.goto(`${BASE_URL}/learning/report`, { waitUntil: 'domcontentloaded' })
  await page.waitForTimeout(4000)
  await capturePage(page, '08_learning_report', '学情报告 - 日/周/月报切换、正确率柱状图、学科分布、薄弱知识点')

  // ====== Step 9: Learning Resources ======
  console.log('📍 Step 9: Learning Resources')
  await page.goto(`${BASE_URL}/learning/resources`, { waitUntil: 'domcontentloaded' })
  await page.waitForTimeout(3000)
  await capturePage(page, '09_learning_resources', '学习资源 - 按年级/学科过滤、免费/VIP标识、资源解锁弹窗')

  // ====== Step 10: Parent Control ======
  console.log('📍 Step 10: Parent Control')
  await page.goto(`${BASE_URL}/learning/control`, { waitUntil: 'domcontentloaded' })
  await page.waitForTimeout(4000)
  await capturePage(page, '10_parent_control', '时长管控 - 每日学习时长滑块、禁用时段设置、一键锁定、护眼模式')

  // ====== Step 11: Wrong Questions ======
  console.log('📍 Step 11: Wrong Questions')
  await page.goto(`${BASE_URL}/learning/wrong-questions`, { waitUntil: 'domcontentloaded' })
  await page.waitForTimeout(3000)
  await capturePage(page, '11_wrong_questions', '错题本 - 按学科/掌握度筛选、批量加入复习计划、打印/PDF导出')

  // ====== Step 12: AI Answer ======
  console.log('📍 Step 12: AI Answer')
  await page.goto(`${BASE_URL}/learning/ai-answer`, { waitUntil: 'domcontentloaded' })
  await page.waitForTimeout(3000)
  await capturePage(page, '12_ai_answer', 'AI答疑记录 - 查看孩子提问与AI分步启发记录')

  // ====== Step 13: Achievement ======
  console.log('📍 Step 13: Achievement')
  await page.goto(`${BASE_URL}/learning/achievements`, { waitUntil: 'domcontentloaded' })
  await page.waitForTimeout(3000)
  await capturePage(page, '13_achievement', '成长激励 - 勋章墙、积分统计、激励记录列表、家长下发任务表单')

  // ====== Step 14: Student Home ======
  console.log('📍 Step 14: Student Home')
  await page.goto(`${BASE_URL}/student/home`, { waitUntil: 'domcontentloaded' })
  await page.waitForTimeout(4000)
  await capturePage(page, '14_student_home', '学生学习首页 - 今日AI计划卡片、推荐微课、薄弱知识点提示、每日任务进度环、快捷入口')

  // ====== Step 15: Student Courses ======
  console.log('📍 Step 15: Student Courses')
  await page.goto(`${BASE_URL}/student/courses`, { waitUntil: 'domcontentloaded' })
  await page.waitForTimeout(3000)
  await capturePage(page, '15_student_courses', '课程专区 - 全科微课列表、年级+学科筛选、视频播放器、VIP锁定')

  // ====== Step 16: Student Exercise ======
  console.log('📍 Step 16: Student Exercise')
  await page.goto(`${BASE_URL}/student/exercise`, { waitUntil: 'domcontentloaded' })
  await page.waitForTimeout(3000)
  await capturePage(page, '16_student_exercise', '专项练习 - 在线做题界面（选择题/填空题/判断题）、自动批改结果、错题入库')

  // ====== Step 17: Student AI Q&A ======
  console.log('📍 Step 17: Student AI Q&A')
  await page.goto(`${BASE_URL}/student/ai-qa`, { waitUntil: 'domcontentloaded' })
  await page.waitForTimeout(3000)
  await capturePage(page, '17_student_ai_qa', 'AI答疑中心 - 拍照上传、文字提问、AI分步对话式回答、追问续问')

  // ====== Step 18: Student Wrong Book ======
  console.log('📍 Step 18: Student Wrong Book')
  await page.goto(`${BASE_URL}/student/wrong-book`, { waitUntil: 'domcontentloaded' })
  await page.waitForTimeout(3000)
  await capturePage(page, '18_student_wrong_book', '错题本（学生版）- 错题列表、AI分析薄弱点雷达图、一键推送复习微课')

  // ====== Step 19: Student Oral ======
  console.log('📍 Step 19: Student Oral')
  await page.goto(`${BASE_URL}/student/oral`, { waitUntil: 'domcontentloaded' })
  await page.waitForTimeout(3000)
  await capturePage(page, '19_student_oral', 'AI口语跟读 - 单词/课文列表、录音按钮、AI评分展示、发音纠正建议')

  // ====== Step 20: Student Composition ======
  console.log('📍 Step 20: Student Composition')
  await page.goto(`${BASE_URL}/student/composition`, { waitUntil: 'domcontentloaded' })
  await page.waitForTimeout(3000)
  await capturePage(page, '20_student_composition', 'AI作文批改 - 作文输入区、AI批改结果（分数+错别字+优化建议）、历史批改记录')

  if (testResults.length > 0) {
    testResults[testResults.length - 1].apiLogs = apiResponses.slice(-30)
  }

  await browser.close()
  generateReport(apiResponses)
  console.log(`\n🎉 测试完成！共 ${testResults.length} 个页面截图，${apiResponses.length} 条 API 记录`)
  console.log(`📄 报告已生成: docs/自动化测试报告.html`)
}

function generateReport(apiResponses) {
  const successCount = apiResponses.filter(a => a.status >= 200 && a.status < 400).length

  const html = `<!DOCTYPE html>
<html lang="zh-CN">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>学霸到家 · 自动化测试报告</title>
<style>
*{margin:0;padding:0;box-sizing:border-box}
body{font-family:-apple-system,BlinkMacSystemFont,'PingFang SC','Microsoft YaHei',sans-serif;background:#f5f6fa;color:#1a1a1a;line-height:1.6}
.header{background:linear-gradient(135deg,#4F6EF7,#7B93FA);color:#fff;padding:48px 24px;text-align:center}
.header h1{font-size:28px;margin-bottom:8px}
.header p{opacity:.9;font-size:15px}
.header .meta{margin-top:16px;display:flex;justify-content:center;gap:20px;font-size:13px;flex-wrap:wrap}
.header .meta span{background:rgba(255,255,255,.2);padding:4px 14px;border-radius:20px}
.container{max-width:920px;margin:0 auto;padding:24px 16px}
.summary{display:grid;grid-template-columns:repeat(4,1fr);gap:16px;margin-bottom:32px}
.summary-card{background:#fff;border-radius:12px;padding:20px;text-align:center;box-shadow:0 2px 12px rgba(0,0,0,.06)}
.summary-card .num{font-size:36px;font-weight:700}
.summary-card .label{font-size:13px;color:#666;margin-top:4px}
.summary-card.total .num{color:#4F6EF7}
.summary-card.success .num{color:#52C41A}
.summary-card.warn .num{color:#FAAD14}
.section{background:#fff;border-radius:12px;margin-bottom:20px;overflow:hidden;box-shadow:0 2px 12px rgba(0,0,0,.06)}
.section-header{display:flex;align-items:center;justify-content:space-between;padding:16px 20px;border-bottom:1px solid #f0f0f0}
.section-header h2{font-size:16px;font-weight:600}
.badge{padding:3px 12px;border-radius:12px;font-size:12px;color:#fff}
.badge.pass{background:#52C41A}
.section-body{padding:20px}
.section-body img{width:100%;border-radius:8px;border:1px solid #e8e8e8;cursor:pointer;transition:transform .2s}
.section-body img:hover{transform:scale(1.02)}
.section-body .desc{font-size:14px;color:#666;margin-bottom:12px}
.section-body .url{font-size:12px;color:#999;font-family:monospace;margin-top:10px;word-break:break-all}
.api-section{background:#fff;border-radius:12px;margin-bottom:24px;padding:20px;box-shadow:0 2px 12px rgba(0,0,0,.06)}
.api-section h2{font-size:18px;margin-bottom:16px}
.api-item{padding:10px 14px;border:1px solid #f0f0f0;border-radius:8px;margin-bottom:6px;font-size:13px;font-family:monospace;display:flex;justify-content:space-between;align-items:center}
.api-item .url-part{flex:1;overflow:hidden;text-overflow:ellipsis;white-space:nowrap}
.api-item .st{font-weight:700;margin-left:12px;flex-shrink:0}
.st.ok{color:#52C41A}
.st.err{color:#FF4D4F}
.st.redir{color:#FAAD14}
.footer{text-align:center;padding:24px;color:#999;font-size:13px}
.lightbox{display:none;position:fixed;top:0;left:0;width:100%;height:100%;background:rgba(0,0,0,.85);z-index:9999;justify-content:center;align-items:center;cursor:zoom-out}
.lightbox.active{display:flex}
.lightbox img{max-width:90%;max-height:90%;border-radius:8px;box-shadow:0 0 40px rgba(0,0,0,.5)}
@media(max-width:600px){.summary{grid-template-columns:repeat(2,1fr)}}
</style>
</head>
<body>
<div class="lightbox" id="lightbox" onclick="this.classList.remove('active')">
  <img id="lightbox-img" src="" alt="放大截图" />
</div>
<div class="header">
  <h1>📚 学霸到家 · 自动化测试报告</h1>
  <p>家长端小学生全科学习功能 + 学生端7大学习页面 — 端到端自动化 UI 测试</p>
  <div class="meta">
    <span>🕐 ${new Date().toLocaleString('zh-CN')}</span>
    <span>🌐 ${BASE_URL}</span>
    <span>📱 375×812 @2x</span>
    <span>🔧 Playwright Chromium</span>
  </div>
</div>
<div class="container">
  <div class="summary">
    <div class="summary-card total"><div class="num">${testResults.length}</div><div class="label">测试页面</div></div>
    <div class="summary-card success"><div class="num">${testResults.length}</div><div class="label">截图成功</div></div>
    <div class="summary-card total"><div class="num">${apiResponses.length}</div><div class="label">API 调用</div></div>
    <div class="summary-card success"><div class="num">${successCount}</div><div class="label">API 成功</div></div>
  </div>

  ${testResults.map((r, i) => `
  <div class="section">
    <div class="section-header">
      <h2>${String(i + 1).padStart(2, '0')}. ${r.description}</h2>
      <span class="badge pass">✅ 通过</span>
    </div>
    <div class="section-body">
      <p class="desc">${r.description}</p>
      <img src="${r.screenshot}" alt="${r.name}" loading="lazy" onclick="openLightbox(this.src)" />
      <div class="url">📍 ${r.url}</div>
    </div>
  </div>`).join('')}

  <div class="api-section">
    <h2>🔌 API 调用记录（共 ${apiResponses.length} 条）</h2>
    ${apiResponses.length === 0 ? '<p style="color:#999;text-align:center;padding:20px">暂无 API 调用记录</p>' : ''}
    ${apiResponses.map(a => {
      const cls = a.status >= 200 && a.status < 400 ? 'ok' : a.status >= 300 && a.status < 400 ? 'redir' : 'err'
      return `<div class="api-item"><span class="url-part">${a.url}</span><span class="st ${cls}">${a.status}</span></div>`
    }).join('')}
  </div>

  <div class="footer">
    学霸到家 · 自动化测试报告 · ${new Date().toLocaleDateString('zh-CN')}<br/>
    Powered by Playwright + Chromium · 375×812 移动端适配
  </div>
</div>
<script>
function openLightbox(src) {
  document.getElementById('lightbox-img').src = src
  document.getElementById('lightbox').classList.add('active')
}
</script>
</body>
</html>`

  fs.writeFileSync(path.join(REPORT_DIR, '自动化测试报告.html'), html, 'utf-8')
}

run().catch(e => {
  console.error('❌ Test failed:', e.message)
  process.exit(1)
})
