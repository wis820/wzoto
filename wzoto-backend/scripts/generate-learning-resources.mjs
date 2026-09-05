/**
 * 学习资源种子数据生成器
 * 生成 人教版 小学1-6年级 × 数学/语文/英语 的全量资源数据
 * 输出：wzoto-backend/sql/seed_data_comprehensive_resources.sql
 *
 * 包含：
 * - t_subject 学科
 * - t_knowledge_point 知识点树
 * - t_resource_chapter 教材章节
 * - t_micro_course 微课视频
 * - t_exercise_bank 习题
 * - t_test_paper 试卷
 * - t_audio_material 音频素材
 * - t_study_material 学习资料
 * - t_learning_resource 学习资源聚合表（前端页面查询）
 */

import { writeFileSync } from 'fs'
import { resolve } from 'path'

const SUBJECTS = [
  { code: 'MATH', name: '数学', icon: '/icons/math.svg', color: '#4F6EF7' },
  { code: 'CHINESE', name: '语文', icon: '/icons/chinese.svg', color: '#F76E4F' },
  { code: 'ENGLISH', name: '英语', icon: '/icons/english.svg', color: '#4FC76E' },
]

const GRADES = [
  { code: 'GRADE_1', name: '一年级', term: '上册' },
  { code: 'GRADE_2', name: '二年级', term: '上册' },
  { code: 'GRADE_3', name: '三年级', term: '上册' },
  { code: 'GRADE_4', name: '四年级', term: '上册' },
  { code: 'GRADE_5', name: '五年级', term: '上册' },
  { code: 'GRADE_6', name: '六年级', term: '上册' },
]

const TEXTBOOK_VERSION = 'RENJIAO'

// 知识点体系：每年级每学科8个知识点
const KNOWLEDGE_TOPICS = {
  GRADE_1: {
    MATH: ['数一数与比一比', '1-5的认识和加减法', '认识图形（一）', '6-10的认识和加减法',
           '11-20各数的认识', '认识钟表', '20以内的进位加法', '20以内的退位减法'],
    CHINESE: ['汉语拼音', '识字（一）', '课文（一）', '识字（二）',
              '课文（二）', '口语交际', '语文园地', '快乐读书吧'],
    ENGLISH: ['Hello!', 'My Classroom', 'My Body', 'Numbers',
              'Colours', 'Animals', 'Fruits', 'Family']
  },
  GRADE_2: {
    MATH: ['长度单位', '100以内的加法', '100以内的减法', '表内乘法（一）',
           '观察物体', '表内乘法（二）', '认识时间', '数学广角'],
    CHINESE: ['识字与写字', '课文阅读', '口语交际', '写话练习',
              '古诗欣赏', '童话故事', '寓言故事', '语文园地'],
    ENGLISH: ['My Friends', 'My Family', 'My Schoolbag', 'My Home',
              'Dinner\'s Ready', 'Meet My Family!', 'How Do You Feel?', 'Weather']
  },
  GRADE_3: {
    MATH: ['时、分、秒', '万以内的加法和减法', '测量', '倍的认识',
           '多位数乘一位数', '长方形和正方形', '分数的初步认识', '数学广角—集合'],
    CHINESE: ['古诗三首', '金秋时节', '中外童话', '预测与想象',
              '习作：写日记', '祖国河山', '我与自然', '美好品质'],
    ENGLISH: ['Hello!', 'Colours', 'Look at Me!', 'We Love Animals',
              'Let\'s Eat!', 'Happy Birthday!', 'How Many?', 'My Friends']
  },
  GRADE_4: {
    MATH: ['大数的认识', '公顷和平方千米', '角的度量', '三位数乘两位数',
           '平行四边形和梯形', '除数是两位数的除法', '条形统计图', '数学广角—优化'],
    CHINESE: ['自然奇观', '提问与思考', '连续观察', '神话故事',
              '习作：写一封信', '成长故事', '家国情怀', '历史传说'],
    ENGLISH: ['My Classroom', 'My Schoolbag', 'My Friends', 'My Home',
              'Dinner\'s Ready', 'Meet My Family!', 'How Much Is It?', 'My Friends']
  },
  GRADE_5: {
    MATH: ['小数乘法', '位置', '小数除法', '可能性',
           '简易方程', '多边形的面积', '数学广角—植树问题', '总复习'],
    CHINESE: ['万物有灵', '策略单元', '民间故事', '爱国情怀',
              '习作：介绍一种事物', '舐犊之情', '自然之趣', '读书明智'],
    ENGLISH: ['What\'s he like?', 'My week', 'What would you like?', 'What can you do?',
              'There is a big bed', 'In a nature park', 'At weekends', 'Chinese New Year']
  },
  GRADE_6: {
    MATH: ['分数乘法', '位置与方向（二）', '分数除法', '比',
           '圆', '百分数（一）', '扇形统计图', '数学广角—数与形'],
    CHINESE: ['触摸自然', '革命岁月', '阅读策略', '小说单元',
              '习作：围绕中心意思写', '保护环境', '艺术之美', '走近鲁迅'],
    ENGLISH: ['How can I get there?', 'Ways to go to school', 'My weekend plan', 'I have a pen pal',
              'What does he do?', 'How do you feel?', 'Protect the Earth', 'My Holiday']
  }
}

// 视频标题模板
const VIDEO_TITLES = {
  MATH: (topic, idx) => [`${topic}：动画讲解`, `${topic}：例题精讲`, `${topic}：易错点突破`][idx % 3],
  CHINESE: (topic, idx) => [`${topic}：微课精讲`, `${topic}：朗读与赏析`, `${topic}：读写技巧`][idx % 3],
  ENGLISH: (topic, idx) => [`${topic}：词汇与句型`, `${topic}：情景对话`, `${topic}：儿歌律动`][idx % 3],
}

// ========== 四年级百度网盘MP4视频（语数英三科全部映射） ==========
// 来源：百度网盘「01-6年级人教版同步网课」→ 四年级语数英
// 共 311 个 MP4 文件：语文 81 + 数学 29 + 英语 42（上册）
// 视频托管基础URL（部署到R2后替换为 https://<account>.r2.cloudflarestorage.com/<bucket>/）
const R2_BASE = '/videos/'

// 视频文件按知识点精准映射（每个子数组 = 1个知识点对应的视频文件列表）
const G4_VIDEOS = {
  // ===== 数学上册（29个视频 → 8个知识点） =====
  MATH: [
    // [0] 大数的认识（7个）
    ['前言.MP4','亿以内数的认识（一）.MP4','亿以内数的认识（二）.MP4','数的产生和十进制计数法.MP4',
     '亿以上数的认识.MP4','计算机工具的认识.MP4','一亿有多大.MP4'],
    // [1] 公顷和平方千米（1个）
    ['公顷和平方千米.MP4'],
    // [2] 角的度量（4个）
    ['直线、射线、线段.MP4','角的度量.MP4','角的分类.MP4','角.MP4'],
    // [3] 三位数乘两位数（4个）
    ['笔算乘法.MP4','中间或末尾有0的乘法.MP4','积的变化规律和估算.MP4','单价公式和路程公式.MP4'],
    // [4] 平行四边形和梯形（3个）
    ['平行与垂直（一）.MP4','平行与垂直（二）.MP4','平行四边形和梯形.MP4'],
    // [5] 除数是两位数的除法（4个）
    ['口算除法.MP4','除数接近整十的除法.MP4','除数为两位数的除法.MP4','商的变化规律.MP4'],
    // [6] 条形统计图（1个）
    ['条形统计图.MP4'],
    // [7] 数学广角—优化（2个）
    ['数学广角（一）.MP4','数学广角（二）.MP4'],
  ],

  // ===== 语文上册（81个视频 → 8个知识点） =====
  CHINESE: [
    // [0] 自然奇观（7个）
    ['观潮（一）.MP4','观潮（二）.MP4','观潮（三）.MP4','雅鲁藏布大峡谷（一）.MP4',
     '雅鲁藏布大峡谷（二）.MP4','鸟的天堂（一）.MP4','鸟的天堂（二）.MP4'],
    // [1] 提问与思考（3个）
    ['巨人的花园（一）.MP4','巨人的花园（二）.MP4','幸福是什么（一）.MP4'],
    // [2] 连续观察（3个）
    ['爬山虎的脚（一）.MP4','爬山虎的脚（二）.MP4','蟋蟀的住宅（一）.MP4'],
    // [3] 神话故事（3个）
    ['幸福是什么（二）.MP4','去年的树（一）.MP4','去年的树（二）.MP4'],
    // [4] 习作：写一封信（1个）
    ['去年的树（三）.MP4'],
    // [5] 成长故事（5个）
    ['猫（一）.MP4','猫（二）.MP4','猫（三）.MP4','母鸡（一）.MP4','母鸡（二）.MP4'],
    // [6] 家国情怀（4个）
    ['长城（一）.MP4','长城（二）.MP4','颐和园（一）.MP4','秦兵马俑（一）.MP4'],
    // [7] 历史传说（7个）
    ['搭石（一）.MP4','搭石（二）.MP4','跨越海峡的生命桥（一）.MP4','为中华之崛起而读书（一）.MP4',
     '为中华之崛起而读书（二）.MP4','飞向蓝天的恐龙（一）.MP4','飞船上的特殊乘客（一）.MP4'],
  ],

  // ===== 英语上册（42个视频 → 6个Unit + Recycle） =====
  ENGLISH: [
    // [0] My Classroom = Unit 1（6个）
    ['Unit 1-1.MP4','Unit 1-2.MP4','Unit 1-3.MP4','Unit 1-4.MP4','Unit 1-5.MP4','Unit 1-6.MP4'],
    // [1] My Schoolbag = Unit 2（6个）
    ['Unit 2-1.MP4','Unit 2-2.MP4','Unit 2-3.MP4','Unit 2-4.MP4','Unit 2-5.MP4','Unit 2-6.MP4'],
    // [2] My Friends = Unit 3（6个）
    ['Unit 3-1.MP4','Unit 3-2.MP4','Unit 3-3.MP4','Unit 3-4.MP4','Unit 3-5.MP4','Unit 3-6.MP4'],
    // [3] My Home = Unit 4（6个）
    ['Unit 4-1.MP4','Unit 4-2.MP4','Unit 4-3.MP4','Unit 4-4.MP4','Unit 4-5.MP4','Unit 4-6.MP4'],
    // [4] Dinner's Ready = Unit 5（6个）
    ['Unit 5-1.MP4','Unit 5-2.MP4','Unit 5-3.MP4','Unit 5-4.MP4','Unit 5-5.MP4','Unit 5-6.MP4'],
    // [5] Meet My Family! = Unit 6（6个）
    ['Unit 6-1.MP4','Unit 6-2.MP4','Unit 6-3.MP4','Unit 6-4.MP4','Unit 6-5.MP4','Unit 6-6.MP4'],
    // [6] How Much Is It? = Recycle 1（3个）
    ['Recycle 1-1.MP4','Recycle 1-2.MP4','Recycle 1-3.MP4'],
    // [7] My Friends = Recycle 2（3个）
    ['Recycle 2-1.MP4','Recycle 2-2.MP4','Recycle 2-3.MP4'],
  ],
}

// 国家中小学智慧教育平台 - 按学科精准链接（作为备用补充）
const SMARTEDU_MATH_URL = 'https://basic.smartedu.cn/tchMaterial/detail?contentType=resources_course&catalogType=tchMaterial&subCatalog=tchMaterial_2_1_4_2'
const SMARTEDU_CHINESE_URL = 'https://basic.smartedu.cn/tchMaterial/detail?contentType=resources_course&catalogType=tchMaterial&subCatalog=tchMaterial_2_1_4_1'
const SMARTEDU_ENGLISH_URL = 'https://basic.smartedu.cn/tchMaterial/detail?contentType=resources_course&catalogType=tchMaterial&subCatalog=tchMaterial_2_1_4_3'

function buildGrade4VideoUrl(subject, topicIdx, videoIdx) {
  // 四年级三科全部用百度网盘MP4视频（90%），国家平台补充（10%）
  const mode = (topicIdx * 3 + videoIdx) % 10
  if (mode < 9) {
    const subjectVideos = G4_VIDEOS[subject]
    if (subjectVideos) {
      const topicVideos = subjectVideos[topicIdx] || subjectVideos[0]
      const fileIdx = videoIdx % topicVideos.length
      const folder = subject === 'MATH' ? 'grade4-math'
        : subject === 'CHINESE' ? 'grade4-chinese'
        : 'grade4-english'
      const fileName = encodeURIComponent(topicVideos[fileIdx])
      return { url: `${R2_BASE}${folder}/${fileName}`, sourceType: 'MP4' }
    }
  }
  // 国家平台备用
  const smartUrl = subject === 'MATH' ? SMARTEDU_MATH_URL
    : subject === 'CHINESE' ? SMARTEDU_CHINESE_URL
    : SMARTEDU_ENGLISH_URL
  return { url: smartUrl, sourceType: 'SMARTEDU' }
}

function getVideoUrl(grade, subject, topicIdx, videoIdx) {
  if (grade === 'GRADE_4') {
    return buildGrade4VideoUrl(subject, topicIdx, videoIdx)
  }
  return null // 其他年级暂无真实URL
}

// 学习资料标题
const MATERIAL_TITLES = {
  FLASHCARD: (topic) => `${topic}知识卡片`,
  MINDMAP: (topic) => `${topic}思维导图`,
  PDF: (topic) => `${topic}同步练习册`,
}

let idCounters = {
  knowledgePoint: 1000,
  resourceChapter: 5000,
  microCourse: 10000,
  exercise: 20000,
  testPaper: 30000,
  audio: 40000,
  studyMaterial: 50000,
  learningResource: 60000,
}

const sql = []
sql.push(`-- =============================================`)
sql.push(`-- 学霸到家 -  comprehensive 学习资源种子数据`)
sql.push(`-- 生成时间：${new Date().toISOString()}`)
sql.push(`-- 覆盖：1-6年级 × 数学/语文/英语 × 人教版`)
sql.push(`-- 包含：知识点、章节、微课、习题、试卷、音频、资料、聚合资源`)
sql.push(`-- =============================================`)
sql.push(`SET NAMES utf8mb4;`)
sql.push(``)

// 1. 学科
sql.push(`-- ========== 1. 学科基础数据 ==========`)
sql.push(`INSERT IGNORE INTO t_subject (code, name, icon, color, sort_order, deleted, created_at, updated_at) VALUES`)
const subjectValues = SUBJECTS.map((s, i) =>
  `('${s.code}', '${s.name}', '${s.icon}', '${s.color}', ${i + 1}, 0, NOW(), NOW())`
)
sql.push(subjectValues.join(',\n') + ';')
sql.push(``)

// 辅助函数
function escapeSql(str) {
  return (str || '').replace(/'/g, "''").replace(/\\/g, '\\\\')
}

function makeJson(obj) {
  return escapeSql(JSON.stringify(obj))
}

function randomDifficulty() {
  const arr = ['EASY', 'EASY', 'MEDIUM', 'MEDIUM', 'HARD']
  return arr[Math.floor(Math.random() * arr.length)]
}

function randomVipOnly(isGrade4 = false) {
  // 四年级：70%免费，30%VIP（让更多孩子能学习）
  // 其他年级：50%免费，50%VIP
  if (isGrade4) return Math.random() < 0.7 ? 0 : 1
  return Math.random() < 0.5 ? 0 : 1
}

function formatDuration(seconds) {
  return seconds
}

// 存储生成的记录，用于后续生成聚合资源
const generated = {
  microCourses: [],
  exercises: [],
  testPapers: [],
  studyMaterials: [],
  audios: [],
}

// 2. 知识点、章节、微课、习题、试卷、音频、资料
sql.push(`-- ========== 2. 知识点树 ==========`)
sql.push(`INSERT IGNORE INTO t_knowledge_point (id, parent_id, grade, subject, textbook_version, name, description, depth, sort_order, deleted, created_at, updated_at) VALUES`)

const knowledgeValues = []
const chapterValues = []
const microCourseValues = []
const exerciseValues = []
const testPaperValues = []
const audioValues = []
const studyMaterialValues = []
const learningResourceValues = []

for (const grade of GRADES) {
  for (const subject of SUBJECTS) {
    const topics = KNOWLEDGE_TOPICS[grade.code][subject.code]
    const gradeSubjectId = idCounters.knowledgePoint++

    // 顶级：年级-学科-册
    knowledgeValues.push(
      `(${gradeSubjectId}, NULL, '${grade.code}', '${subject.code}', '${TEXTBOOK_VERSION}', '${escapeSql(grade.name + subject.name + '上册')}', NULL, 1, ${grade.code.replace('GRADE_', '')}, 0, NOW(), NOW())`
    )

    // 章节顶级
    const chapterRootId = idCounters.resourceChapter++
    chapterValues.push(
      `(${chapterRootId}, NULL, '${grade.code}', '${subject.code}', '${TEXTBOOK_VERSION}', '${escapeSql(grade.name + subject.name + '上册')}', 1, ${grade.code.replace('GRADE_', '')}, 0, NOW(), NOW())`
    )

    topics.forEach((topic, idx) => {
      const knowledgeId = idCounters.knowledgePoint++
      const chapterId = idCounters.resourceChapter++

      // 知识点
      knowledgeValues.push(
        `(${knowledgeId}, ${gradeSubjectId}, '${grade.code}', '${subject.code}', '${TEXTBOOK_VERSION}', '${escapeSql(topic)}', '${escapeSql(subject.name + grade.name + '重点知识')}', 2, ${idx + 1}, 0, NOW(), NOW())`
      )

      // 章节
      chapterValues.push(
        `(${chapterId}, ${chapterRootId}, '${grade.code}', '${subject.code}', '${TEXTBOOK_VERSION}', '${escapeSql(topic)}', 2, ${idx + 1}, 0, NOW(), NOW())`
      )

      // 微课：每个知识点3-4个视频（四年级4个，其他年级2-3个）
      const isGrade4 = grade.code === 'GRADE_4'
      const videoCount = isGrade4 ? (3 + (idx % 2)) : (2 + (idx % 2))
      for (let v = 0; v < videoCount; v++) {
        const courseId = idCounters.microCourse++
        const title = VIDEO_TITLES[subject.code](topic, v)
        const seconds = isGrade4 ? (300 + Math.floor(Math.random() * 480)) : (180 + Math.floor(Math.random() * 300))
        const vipOnly = randomVipOnly(isGrade4)
        const videoNo = `${grade.code.replace('_', '').toLowerCase()}-${subject.code.toLowerCase()}-${String(knowledgeId).slice(-2)}-${v + 1}`

        // 四年级语数英优先使用真实 playable URL
        const videoInfo = isGrade4 ? getVideoUrl(grade.code, subject.code, idx, v) : null
        const videoUrl = videoInfo ? videoInfo.url : `/videos/${videoNo}.mp4`
        const sourceType = videoInfo ? videoInfo.sourceType : 'MP4'
        const coverUrl = videoInfo
          ? (videoInfo.sourceType === 'BILIBILI'
              ? '/covers/bilibili-default.jpg'
              : videoInfo.sourceType === 'SMARTEDU'
                ? '/covers/smartedu-default.jpg'
                : '/covers/mp4-default.jpg')
          : `/covers/${videoNo}.jpg`

        microCourseValues.push(
          `(${courseId}, '${grade.code}', '${subject.code}', '${TEXTBOOK_VERSION}', ${chapterId}, ${knowledgeId}, '${escapeSql(title)}', '${escapeSql(topic + '知识点动画讲解')}', '${coverUrl}', '${videoUrl}', ${seconds}, '1080P', ${Math.floor(seconds / 60)}, '${randomDifficulty()}', ${vipOnly}, 0, ${v + 1}, 0, NOW(), NOW())`
        )
        generated.microCourses.push({ id: courseId, grade: grade.code, subject: subject.code, title, seconds, vipOnly, knowledgePoint: topic })

        // 对应的聚合资源 VIDEO（含 source_type）
        const lrId = idCounters.learningResource++
        learningResourceValues.push(
          `(${lrId}, '${grade.code}', '${subject.code}', '${TEXTBOOK_VERSION}', 'VIDEO', '${escapeSql(title)}', '${coverUrl}', '${videoUrl}', ${seconds}, '${escapeSql(topic)}', '${escapeSql(JSON.stringify([topic, subject.name]))}', ${vipOnly}, ${v + 1}, 0, NOW(), NOW(), '${sourceType}')`
        )
      }

      // 习题：每个知识点3道
      for (let e = 0; e < 3; e++) {
        const exerciseId = idCounters.exercise++
        const types = ['CHOICE', 'FILL', 'TRUE_FALSE']
        const type = types[e % 3]
        const difficulty = randomDifficulty()
        const vipOnly = randomVipOnly(isGrade4)

        let questionContent, options, correctAnswer, explanation
        if (type === 'CHOICE') {
          questionContent = makeJson({ stem: `${topic}相关选择题，请选择正确答案：` })
          options = makeJson(['A', 'B', 'C', 'D'])
          correctAnswer = 'B'
          explanation = escapeSql('本题考查' + topic + '的基础知识，正确答案是B。')
        } else if (type === 'FILL') {
          questionContent = makeJson({ stem: `${topic}：请填写横线上的内容。` })
          options = 'NULL'
          correctAnswer = escapeSql('答案')
          explanation = escapeSql('本题考查' + topic + '的核心概念。')
        } else {
          questionContent = makeJson({ stem: `${topic}：下面的说法是否正确？` })
          options = 'NULL'
          correctAnswer = 'TRUE'
          explanation = escapeSql('本题考查' + topic + '的判断能力。')
        }

        exerciseValues.push(
          `(${exerciseId}, '${grade.code}', '${subject.code}', '${TEXTBOOK_VERSION}', ${knowledgeId}, '${type}', '${questionContent}', ${options === 'NULL' ? 'NULL' : "'" + options + "'"}, '${correctAnswer}', '${explanation}', '${difficulty}', 'SYSTEM', ${vipOnly}, 0, NULL, ${e + 1}, 0, NOW(), NOW())`
        )
        generated.exercises.push({ id: exerciseId, grade: grade.code, subject: subject.code, type, vipOnly, knowledgePoint: topic })

        // 聚合资源 EXERCISE
        const lrId = idCounters.learningResource++
        learningResourceValues.push(
          `(${lrId}, '${grade.code}', '${subject.code}', '${TEXTBOOK_VERSION}', 'EXERCISE', '${escapeSql(topic + '：' + (type === 'CHOICE' ? '选择题' : type === 'FILL' ? '填空题' : '判断题'))}', '/covers/exercise-${exerciseId}.jpg', NULL, NULL, '${escapeSql(topic)}', '${escapeSql(JSON.stringify([topic, '练习']))}', ${vipOnly}, ${e + 1}, 0, NOW(), NOW(), NULL)`
        )
      }

      // 学习资料：每个知识点1-2个
      const materialTypes = ['FLASHCARD', 'MINDMAP']
      materialTypes.forEach((mType, mIdx) => {
        const materialId = idCounters.studyMaterial++
        const title = MATERIAL_TITLES[mType](topic)
        const vipOnly = randomVipOnly(isGrade4)
        const fileNo = `${grade.code.replace('_', '').toLowerCase()}-${subject.code.toLowerCase()}-${String(knowledgeId).slice(-2)}-${mType.toLowerCase()}`

        studyMaterialValues.push(
          `(${materialId}, '${grade.code}', '${subject.code}', '${TEXTBOOK_VERSION}', '${escapeSql(title)}', '${mType}', '/materials/${fileNo}.pdf', '/materials/${fileNo}.png', '${escapeSql(topic + '同步学习资料')}', ${knowledgeId}, ${vipOnly}, 0, ${mIdx + 1}, 0, NOW(), NOW())`
        )
        generated.studyMaterials.push({ id: materialId, grade: grade.code, subject: subject.code, type: mType, vipOnly, knowledgePoint: topic, title })

        // 聚合资源 PDF
        const lrId = idCounters.learningResource++
        learningResourceValues.push(
          `(${lrId}, '${grade.code}', '${subject.code}', '${TEXTBOOK_VERSION}', 'PDF', '${escapeSql(title)}', '/materials/${fileNo}.png', '/materials/${fileNo}.pdf', NULL, '${escapeSql(topic)}', '${escapeSql(JSON.stringify([topic, '资料']))}', ${vipOnly}, ${mIdx + 1}, 0, NOW(), NOW(), NULL)`
        )
      })
    })

    // 音频：每个年级学科2条
    const audioTypes = subject.code === 'ENGLISH' ? ['FOLLOW_READ', 'LISTENING'] : ['READ']
    audioTypes.forEach((audioType, aIdx) => {
      const audioId = idCounters.audio++
      const title = subject.code === 'ENGLISH'
        ? (audioType === 'FOLLOW_READ' ? `${grade.name}英语跟读素材` : `${grade.name}英语听力练习`)
        : `${grade.name}语文课文朗读`
      const vipOnly = randomVipOnly(grade.code === 'GRADE_4')
      const audioNo = `${grade.code.replace('_', '').toLowerCase()}-${subject.code.toLowerCase()}-${audioType.toLowerCase()}`

      audioValues.push(
        `(${audioId}, '${grade.code}', '${subject.code}', '${TEXTBOOK_VERSION}', '${escapeSql(title)}', '${audioType}', '/audios/${audioNo}.mp3', ${120 + Math.floor(Math.random() * 180)}, '${escapeSql(title + '，帮助学生提升听说能力')}', '${escapeSql(title + '参考文本')}', NULL, ${vipOnly}, ${aIdx + 1}, 0, NOW(), NOW())`
      )
      generated.audios.push({ id: audioId, grade: grade.code, subject: subject.code, type: audioType, vipOnly, title })
    })

    // 试卷：每个年级学科4套（单元、期中、期末、模拟）
    const paperTypes = [
      { code: 'UNIT', name: '单元测试卷', duration: 40 },
      { code: 'MID_TERM', name: '期中考试卷', duration: 60 },
      { code: 'FINAL', name: '期末考试卷', duration: 90 },
      { code: 'MOCK', name: '模拟冲刺卷', duration: 60 },
    ]
    paperTypes.forEach((pt, ptIdx) => {
      const paperId = idCounters.testPaper++
      const title = `${grade.name}${subject.name}${pt.name}`
      const vipOnly = ptIdx > 0 ? 1 : 0 // 单元测试免费，其余VIP
      const paperNo = `${grade.code.replace('_', '').toLowerCase()}-${subject.code.toLowerCase()}-${pt.code.toLowerCase()}`

      testPaperValues.push(
        `(${paperId}, '${grade.code}', '${subject.code}', '${TEXTBOOK_VERSION}', '${escapeSql(title)}', '${pt.code}', '${escapeSql(title + '，满分100分')}', 100, ${pt.duration}, '${makeJson({ questionIds: [1, 2, 3, 4, 5, 6], scores: [20, 20, 15, 15, 15, 15] })}', '/covers/${paperNo}.jpg', ${vipOnly}, 0, ${ptIdx + 1}, 0, NOW(), NOW())`
      )
      generated.testPapers.push({ id: paperId, grade: grade.code, subject: subject.code, type: pt.code, vipOnly, title })

      // 聚合资源 PDF（试卷作为PDF资源）
      const lrId = idCounters.learningResource++
      learningResourceValues.push(
        `(${lrId}, '${grade.code}', '${subject.code}', '${TEXTBOOK_VERSION}', 'PDF', '${escapeSql(title)}', '/covers/${paperNo}.jpg', '/papers/${paperNo}.pdf', NULL, '${escapeSql(title)}', '${escapeSql(JSON.stringify([pt.name, subject.name]))}', ${vipOnly}, ${ptIdx + 1}, 0, NOW(), NOW(), NULL)`
      )
    })
  }
}

// 写入知识点
sql.push(knowledgeValues.join(',\n') + ';')
sql.push(``)

// 3. 教材章节
sql.push(`-- ========== 3. 教材章节 ==========`)
sql.push(`INSERT IGNORE INTO t_resource_chapter (id, parent_id, grade, subject, textbook_version, title, depth, sort_order, deleted, created_at, updated_at) VALUES`)
sql.push(chapterValues.join(',\n') + ';')
sql.push(``)

// 4. 微课视频
sql.push(`-- ========== 4. 微课视频 ==========`)
sql.push(`INSERT IGNORE INTO t_micro_course (id, grade, subject, textbook_version, chapter_id, knowledge_point_id, title, description, cover_url, video_url, duration_seconds, resolution, file_size_mb, difficulty, vip_only, play_count, sort_order, deleted, created_at, updated_at) VALUES`)
sql.push(microCourseValues.join(',\n') + ';')
sql.push(``)

// 5. 习题库
sql.push(`-- ========== 5. 习题库 ==========`)
sql.push(`INSERT IGNORE INTO t_exercise_bank (id, grade, subject, textbook_version, knowledge_point_id, question_type, question_content, options, correct_answer, explanation, difficulty, source_type, vip_only, use_count, correct_rate, sort_order, deleted, created_at, updated_at) VALUES`)
sql.push(exerciseValues.join(',\n') + ';')
sql.push(``)

// 6. 试卷
sql.push(`-- ========== 6. 试卷 ==========`)
sql.push(`INSERT IGNORE INTO t_test_paper (id, grade, subject, textbook_version, title, paper_type, description, total_score, duration_minutes, questions_json, cover_url, vip_only, use_count, sort_order, deleted, created_at, updated_at) VALUES`)
sql.push(testPaperValues.join(',\n') + ';')
sql.push(``)

// 7. 音频素材
sql.push(`-- ========== 7. 音频素材 ==========`)
sql.push(`INSERT IGNORE INTO t_audio_material (id, grade, subject, textbook_version, title, audio_type, audio_url, duration_seconds, text_content, reference_text, knowledge_point_id, vip_only, sort_order, deleted, created_at, updated_at) VALUES`)
sql.push(audioValues.join(',\n') + ';')
sql.push(``)

// 8. 学习资料
sql.push(`-- ========== 8. 学习资料 ==========`)
sql.push(`INSERT IGNORE INTO t_study_material (id, grade, subject, textbook_version, title, material_type, content_url, preview_url, description, knowledge_point_id, vip_only, download_count, sort_order, deleted, created_at, updated_at) VALUES`)
sql.push(studyMaterialValues.join(',\n') + ';')
sql.push(``)

// 9. 学习资源聚合表
sql.push(`-- ========== 9. 学习资源聚合表（前端页面查询） ==========`)
sql.push(`INSERT IGNORE INTO t_learning_resource (id, grade, subject, textbook_version, resource_type, title, cover_url, content_url, duration_seconds, knowledge_point, tags, vip_only, sort_order, deleted, created_at, updated_at, source_type) VALUES`)
// 分批写入避免SQL过长
const BATCH_SIZE = 500
for (let i = 0; i < learningResourceValues.length; i += BATCH_SIZE) {
  const batch = learningResourceValues.slice(i, i + BATCH_SIZE)
  const suffix = i + BATCH_SIZE >= learningResourceValues.length ? ';' : ';'
  sql.push(batch.join(',\n') + suffix)
  if (i + BATCH_SIZE < learningResourceValues.length) {
    sql.push(`INSERT IGNORE INTO t_learning_resource (id, grade, subject, textbook_version, resource_type, title, cover_url, content_url, duration_seconds, knowledge_point, tags, vip_only, sort_order, deleted, created_at, updated_at, source_type) VALUES`)
  }
}
sql.push(``)

// 统计
sql.push(`-- ========== 生成统计 ==========`)
sql.push(`SELECT 't_knowledge_point' as tbl, COUNT(*) as cnt FROM t_knowledge_point;`)
sql.push(`SELECT 't_resource_chapter' as tbl, COUNT(*) as cnt FROM t_resource_chapter;`)
sql.push(`SELECT 't_micro_course' as tbl, COUNT(*) as cnt FROM t_micro_course;`)
sql.push(`SELECT 't_exercise_bank' as tbl, COUNT(*) as cnt FROM t_exercise_bank;`)
sql.push(`SELECT 't_test_paper' as tbl, COUNT(*) as cnt FROM t_test_paper;`)
sql.push(`SELECT 't_audio_material' as tbl, COUNT(*) as cnt FROM t_audio_material;`)
sql.push(`SELECT 't_study_material' as tbl, COUNT(*) as cnt FROM t_study_material;`)
sql.push(`SELECT 't_learning_resource' as tbl, COUNT(*) as cnt FROM t_learning_resource;`)

const outputPath = resolve('f:\\wanglianyida\\wzoto\\wzoto-backend\\sql\\seed_data_comprehensive_resources.sql')
writeFileSync(outputPath, sql.join('\n'), 'utf8')

console.log(`生成完成：${outputPath}`)
console.log(`统计：`)
console.log(`  知识点：${knowledgeValues.length}`)
console.log(`  章节：${chapterValues.length}`)
console.log(`  微课：${microCourseValues.length}`)
console.log(`  习题：${exerciseValues.length}`)
console.log(`  试卷：${testPaperValues.length}`)
console.log(`  音频：${audioValues.length}`)
console.log(`  学习资料：${studyMaterialValues.length}`)
console.log(`  聚合资源：${learningResourceValues.length}`)
