-- =============================================
-- 学霸到家 - 习题作答记录表 DDL
-- =============================================
CREATE TABLE IF NOT EXISTS t_exercise_answer_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    child_id BIGINT NOT NULL COMMENT '子女ID',
    resource_id BIGINT DEFAULT NULL COMMENT '关联学习资源ID（题库题目时为空）',
    question_id BIGINT DEFAULT NULL COMMENT '题目ID',
    subject VARCHAR(32) NOT NULL COMMENT '学科：MATH/CHINESE/ENGLISH',
    knowledge_point VARCHAR(128) DEFAULT NULL COMMENT '知识点',
    answer TEXT DEFAULT NULL COMMENT '学生答案',
    correct TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否正确：0否1是',
    cost_seconds INT DEFAULT NULL COMMENT '耗时（秒）',
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    created_at DATETIME DEFAULT NULL,
    updated_at DATETIME DEFAULT NULL,
    INDEX idx_child_id (child_id),
    INDEX idx_subject (subject),
    INDEX idx_resource_id (resource_id),
    INDEX idx_created_at (created_at),
    INDEX idx_child_subject (child_id, subject)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='习题作答记录表';
