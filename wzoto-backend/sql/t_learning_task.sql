-- =============================================
-- 学霸到家 - 学习任务记录表 DDL
-- =============================================
CREATE TABLE IF NOT EXISTS t_learning_task (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    child_id BIGINT NOT NULL COMMENT '子女ID',
    plan_config_id BIGINT DEFAULT NULL COMMENT '学习计划配置ID',
    task_type VARCHAR(32) NOT NULL COMMENT '任务类型：VIDEO/EXERCISE/REVIEW/SPECIAL_CALCULATION/SPECIAL_APPLICATION/SPECIAL_LITERACY/SPECIAL_WORDS',
    subject VARCHAR(32) NOT NULL COMMENT '学科：MATH/CHINESE/ENGLISH',
    title VARCHAR(128) NOT NULL COMMENT '任务标题',
    content TEXT DEFAULT NULL COMMENT '任务内容描述/JSON',
    resource_id BIGINT DEFAULT NULL COMMENT '关联学习资源ID',
    status VARCHAR(32) NOT NULL DEFAULT 'PENDING' COMMENT '状态：PENDING/IN_PROGRESS/COMPLETED/SKIPPED',
    assigned_date DATE NOT NULL COMMENT '任务日期',
    completed_at DATETIME DEFAULT NULL COMMENT '完成时间',
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    created_at DATETIME DEFAULT NULL,
    updated_at DATETIME DEFAULT NULL,
    INDEX idx_child_id (child_id),
    INDEX idx_assigned_date (assigned_date),
    INDEX idx_status (status),
    INDEX idx_child_date (child_id, assigned_date),
    INDEX idx_child_status (child_id, status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='学习任务记录表';
