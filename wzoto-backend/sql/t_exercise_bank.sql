-- =============================================
-- 学霸到家 - 习题库表 DDL（洋葱学院学习资源体系）
-- =============================================
CREATE TABLE IF NOT EXISTS t_exercise_bank (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    grade VARCHAR(16) NOT NULL COMMENT '年级：GRADE_1~GRADE_6',
    subject VARCHAR(32) NOT NULL COMMENT '学科：MATH/CHINESE/ENGLISH',
    textbook_version VARCHAR(32) NOT NULL DEFAULT 'RENJIAO' COMMENT '教材版本',
    knowledge_point_id BIGINT DEFAULT NULL COMMENT '关联知识点ID',
    question_type VARCHAR(32) NOT NULL COMMENT '题目类型：CHOICE/FILL/TRUE_FALSE/SHORT_ANSWER',
    question_content TEXT NOT NULL COMMENT '题目内容JSON（题干+选项）',
    options TEXT DEFAULT NULL COMMENT '选项JSON数组（选择题用）',
    correct_answer TEXT NOT NULL COMMENT '正确答案',
    explanation TEXT DEFAULT NULL COMMENT '答案解析',
    difficulty VARCHAR(16) NOT NULL DEFAULT 'MEDIUM' COMMENT '难度：EASY/MEDIUM/HARD',
    source_type VARCHAR(32) NOT NULL DEFAULT 'SYSTEM' COMMENT '来源：SYSTEM系统/AI生成/USER自定义',
    vip_only TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否仅会员',
    use_count INT NOT NULL DEFAULT 0 COMMENT '使用次数',
    correct_rate INT DEFAULT NULL COMMENT '正确率（百分比）',
    sort_order INT NOT NULL DEFAULT 0 COMMENT '排序号',
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    created_at DATETIME DEFAULT NULL,
    updated_at DATETIME DEFAULT NULL,
    INDEX idx_grade_subject (grade, subject),
    INDEX idx_knowledge_point_id (knowledge_point_id),
    INDEX idx_question_type (question_type),
    INDEX idx_difficulty (difficulty),
    INDEX idx_source_type (source_type),
    INDEX idx_vip_only (vip_only)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='习题库表';
