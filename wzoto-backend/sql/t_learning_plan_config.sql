-- =============================================
-- 学霸到家 - 学习计划配置表 DDL
-- =============================================
CREATE TABLE IF NOT EXISTS t_learning_plan_config (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    child_id BIGINT NOT NULL COMMENT '子女ID',
    daily_duration_minutes INT NOT NULL DEFAULT 30 COMMENT '每日学习时长（分钟）',
    math_weight INT NOT NULL DEFAULT 35 COMMENT '数学学科权重%',
    chinese_weight INT NOT NULL DEFAULT 35 COMMENT '语文学科权重%',
    english_weight INT NOT NULL DEFAULT 30 COMMENT '英语学科权重%',
    special_calculation TINYINT(1) NOT NULL DEFAULT 1 COMMENT '计算专项开关：0关1开',
    special_application TINYINT(1) NOT NULL DEFAULT 1 COMMENT '应用题专项开关：0关1开',
    special_literacy TINYINT(1) NOT NULL DEFAULT 0 COMMENT '识字专项开关：0关1开',
    special_words TINYINT(1) NOT NULL DEFAULT 0 COMMENT '背单词专项开关：0关1开',
    auto_generate TINYINT(1) NOT NULL DEFAULT 1 COMMENT '是否自动生成每日任务：0否1是',
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    created_at DATETIME DEFAULT NULL,
    updated_at DATETIME DEFAULT NULL,
    UNIQUE INDEX uk_child_id (child_id),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='学习计划配置表';
