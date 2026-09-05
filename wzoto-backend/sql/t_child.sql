-- =============================================
-- 学霸到家 - 子女档案表 DDL（家长端小学生学习模块）
-- =============================================
CREATE TABLE IF NOT EXISTS t_child (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    parent_id BIGINT NOT NULL COMMENT '家长用户ID',
    name VARCHAR(50) NOT NULL COMMENT '子女姓名',
    grade VARCHAR(16) NOT NULL COMMENT '年级：GRADE_1~GRADE_6',
    textbook_version VARCHAR(32) NOT NULL DEFAULT 'RENJIAO' COMMENT '教材版本：RENJIAO/BEISHIDA/JIAOSHE/SUDAJIAO/WAIXIN/SHANGHAJIAO/OTHER',
    school VARCHAR(128) DEFAULT NULL COMMENT '学校名称',
    avatar VARCHAR(512) DEFAULT NULL COMMENT '头像URL',
    deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-正常 1-删除',
    created_at DATETIME DEFAULT NULL COMMENT '创建时间',
    updated_at DATETIME DEFAULT NULL COMMENT '更新时间',
    INDEX idx_parent_id (parent_id),
    INDEX idx_grade (grade)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='子女档案表';
