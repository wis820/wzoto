-- =============================================
-- 学霸到家 - 家长管控配置表 DDL
-- =============================================
CREATE TABLE IF NOT EXISTS t_parent_control_config (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    parent_id BIGINT NOT NULL COMMENT '家长用户ID',
    child_id BIGINT NOT NULL COMMENT '子女ID',
    daily_duration_minutes INT NOT NULL DEFAULT 60 COMMENT '每日可用时长（分钟）',
    rest_interval_minutes INT NOT NULL DEFAULT 20 COMMENT '单次休息间隔（分钟）',
    rest_duration_minutes INT NOT NULL DEFAULT 5 COMMENT '休息时长（分钟）',
    forbidden_start_time TIME DEFAULT '22:00:00' COMMENT '禁用时段开始',
    forbidden_end_time TIME DEFAULT '07:00:00' COMMENT '禁用时段结束',
    locked TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否锁定学习入口：0否1是',
    eye_protection TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否开启护眼模式：0否1是',
    current_page VARCHAR(256) DEFAULT NULL COMMENT '孩子当前学习页面',
    last_login_at DATETIME DEFAULT NULL COMMENT '最后登录时间',
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    created_at DATETIME DEFAULT NULL,
    updated_at DATETIME DEFAULT NULL,
    UNIQUE INDEX uk_child_id (child_id),
    INDEX idx_parent_id (parent_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='家长管控配置表';
