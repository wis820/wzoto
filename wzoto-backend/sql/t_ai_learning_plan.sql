-- =============================================
-- 学霸到家 - AI学习规划记录表 DDL（洋葱学院学习资源体系）
-- AI根据学情自动生成每日学习计划
-- =============================================
CREATE TABLE IF NOT EXISTS t_ai_learning_plan (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    child_id BIGINT NOT NULL COMMENT '子女ID',
    parent_id BIGINT NOT NULL COMMENT '家长用户ID',
    plan_date DATE NOT NULL COMMENT '计划日期',
    plan_content_json MEDIUMTEXT NOT NULL COMMENT '计划内容JSON（含任务列表、时间安排、知识点目标）',
    weak_points_json TEXT DEFAULT NULL COMMENT '基于分析的薄弱点JSON',
    ai_suggestion TEXT DEFAULT NULL COMMENT 'AI学习建议',
    applied TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否已应用到学习任务：0否1是',
    applied_at DATETIME DEFAULT NULL COMMENT '应用时间',
    generated_at DATETIME NOT NULL COMMENT 'AI生成时间',
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    created_at DATETIME DEFAULT NULL,
    updated_at DATETIME DEFAULT NULL,
    INDEX idx_child_id (child_id),
    INDEX idx_plan_date (plan_date),
    INDEX idx_child_date (child_id, plan_date),
    INDEX idx_applied (applied)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='AI学习规划记录表';
