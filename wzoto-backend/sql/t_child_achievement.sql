-- =============================================
-- 学霸到家 - 成长激励表 DDL
-- =============================================
CREATE TABLE IF NOT EXISTS t_child_achievement (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    child_id BIGINT NOT NULL COMMENT '子女ID',
    achievement_type VARCHAR(32) NOT NULL COMMENT '勋章类型：STUDY_STAR/CALCULATION_MASTER/READING_STAR/PERSISTENCE/MATH_WIZARD/ENGLISH_STAR',
    title VARCHAR(64) NOT NULL COMMENT '勋章名称',
    icon VARCHAR(256) DEFAULT NULL COMMENT '勋章图标URL',
    points INT NOT NULL DEFAULT 0 COMMENT '获得积分',
    skin VARCHAR(64) DEFAULT NULL COMMENT '解锁皮肤',
    source VARCHAR(32) NOT NULL DEFAULT 'SYSTEM' COMMENT '来源：SYSTEM系统发放/PARENT家长任务',
    task_name VARCHAR(128) DEFAULT NULL COMMENT '家长任务名称',
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    created_at DATETIME DEFAULT NULL,
    updated_at DATETIME DEFAULT NULL,
    INDEX idx_child_id (child_id),
    INDEX idx_achievement_type (achievement_type),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='成长激励表';
