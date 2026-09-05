-- =============================================
-- 学霸到家 - AI生成练习题表 DDL（洋葱学院学习资源体系）
-- AI根据错题/薄弱点自动生成针对性练习题
-- =============================================
CREATE TABLE IF NOT EXISTS t_ai_exercise_generated (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    child_id BIGINT NOT NULL COMMENT '子女ID',
    parent_id BIGINT NOT NULL COMMENT '家长用户ID',
    source VARCHAR(32) NOT NULL COMMENT '生成来源：WRONG_QUESTION错题补学/LEARNING_PLAN学习计划/AI_QA答疑追问',
    source_id BIGINT DEFAULT NULL COMMENT '来源记录ID（错题ID/计划ID/提问ID）',
    subject VARCHAR(32) NOT NULL COMMENT '学科',
    grade VARCHAR(16) NOT NULL COMMENT '年级',
    knowledge_point VARCHAR(128) DEFAULT NULL COMMENT '目标知识点',
    question_content TEXT NOT NULL COMMENT '题目内容JSON',
    correct_answer TEXT NOT NULL COMMENT '正确答案',
    ai_explanation TEXT DEFAULT NULL COMMENT 'AI解析说明',
    difficulty VARCHAR(16) NOT NULL DEFAULT 'MEDIUM' COMMENT '难度',
    answered TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否已作答：0否1是',
    is_correct TINYINT(1) DEFAULT NULL COMMENT '作答是否正确',
    answered_at DATETIME DEFAULT NULL COMMENT '作答时间',
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    created_at DATETIME DEFAULT NULL,
    updated_at DATETIME DEFAULT NULL,
    INDEX idx_child_id (child_id),
    INDEX idx_source (source),
    INDEX idx_subject (subject),
    INDEX idx_answered (answered),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='AI生成练习题表';
