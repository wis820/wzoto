-- =============================================
-- 学霸到家 - 错题库表 DDL
-- =============================================
CREATE TABLE IF NOT EXISTS t_wrong_question_bank (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    child_id BIGINT NOT NULL COMMENT '子女ID',
    question_id BIGINT DEFAULT NULL COMMENT '题目ID',
    subject VARCHAR(32) NOT NULL COMMENT '学科：MATH/CHINESE/ENGLISH',
    knowledge_point VARCHAR(128) DEFAULT NULL COMMENT '知识点',
    question_content TEXT DEFAULT NULL COMMENT '题目内容JSON',
    wrong_answer TEXT DEFAULT NULL COMMENT '错误答案',
    correct_answer TEXT DEFAULT NULL COMMENT '正确答案',
    wrong_count INT NOT NULL DEFAULT 1 COMMENT '错误次数',
    mastery_level VARCHAR(32) NOT NULL DEFAULT 'NOT_MASTERED' COMMENT '掌握度：NOT_MASTERED/GENERAL/PROFICIENT',
    in_review_plan TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否加入复习计划：0否1是',
    last_wrong_at DATETIME DEFAULT NULL COMMENT '最近错误时间',
    mastered_at DATETIME DEFAULT NULL COMMENT '标记掌握时间',
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    created_at DATETIME DEFAULT NULL,
    updated_at DATETIME DEFAULT NULL,
    INDEX idx_child_id (child_id),
    INDEX idx_subject (subject),
    INDEX idx_knowledge_point (knowledge_point),
    INDEX idx_mastery_level (mastery_level),
    INDEX idx_in_review_plan (in_review_plan),
    INDEX idx_child_mastery (child_id, mastery_level)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='错题库表';
