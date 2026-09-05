/**
 * 格式化工具 - 将后端英文枚举值转为中文显示
 */

const SUBJECT_MAP = {
  MATH: '数学',
  CHINESE: '语文',
  ENGLISH: '英语'
}

const PAPER_TYPE_MAP = {
  UNIT: '单元卷',
  MID_TERM: '期中卷',
  FINAL: '期末卷',
  MOCK: '模拟卷'
}

const DIFFICULTY_MAP = {
  EASY: '基础',
  MEDIUM: '提高',
  HARD: '挑战'
}

const QUESTION_TYPE_MAP = {
  CHOICE: '选择题',
  FILL: '填空题',
  JUDGE: '判断题',
  TRUE_FALSE: '判断题',
  SHORT_ANSWER: '简答题'
}

const AUDIO_TYPE_MAP = {
  FOLLOW_READ: '跟读',
  LISTENING: '听力',
  READ: '朗读'
}

const GRADE_MAP = {
  GRADE_1: '一年级',
  GRADE_2: '二年级',
  GRADE_3: '三年级',
  GRADE_4: '四年级',
  GRADE_5: '五年级',
  GRADE_6: '六年级'
}

const MASTERY_MAP = {
  WEAK: '薄弱',
  LEARNING: '学习中',
  PROFICIENT: '已掌握'
}

export function subjectLabel(s) {
  return SUBJECT_MAP[s] || s || ''
}

export function paperTypeLabel(t) {
  return PAPER_TYPE_MAP[t] || t || ''
}

export function difficultyLabel(d) {
  return DIFFICULTY_MAP[d] || d || ''
}

export function questionTypeLabel(t) {
  return QUESTION_TYPE_MAP[t] || t || ''
}

export function audioTypeLabel(t) {
  return AUDIO_TYPE_MAP[t] || t || ''
}

export function gradeLabel(g) {
  return GRADE_MAP[g] || g || ''
}

export function masteryLabel(m) {
  return MASTERY_MAP[m] || m || ''
}
