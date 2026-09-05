-- =============================================
-- 学霸到家 - 上课观看记录表 DDL
-- =============================================
CREATE TABLE IF NOT EXISTS t_course_watch_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    child_id BIGINT NOT NULL COMMENT '子女ID',
    resource_id BIGINT NOT NULL COMMENT '学习资源ID',
    watch_duration_seconds INT NOT NULL DEFAULT 0 COMMENT '累计观看时长（秒）',
    progress_percent INT NOT NULL DEFAULT 0 COMMENT '进度百分比 0-100',
    last_position_seconds INT NOT NULL DEFAULT 0 COMMENT '上次播放位置（秒）',
    completed TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否完成：0否1是',
    completed_at DATETIME DEFAULT NULL COMMENT '完成时间',
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    created_at DATETIME DEFAULT NULL,
    updated_at DATETIME DEFAULT NULL,
    UNIQUE INDEX uk_child_resource (child_id, resource_id),
    INDEX idx_child_id (child_id),
    INDEX idx_resource_id (resource_id),
    INDEX idx_completed (completed)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='上课观看记录表';
