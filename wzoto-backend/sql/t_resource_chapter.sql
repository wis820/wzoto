-- =============================================
-- 学霸到家 - 教材章节表 DDL（洋葱学院学习资源体系）
-- 按年级+学科+教材版本组织章节目录树
-- =============================================
CREATE TABLE IF NOT EXISTS t_resource_chapter (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    parent_id BIGINT DEFAULT NULL COMMENT '父章节ID（NULL为顶级）',
    grade VARCHAR(16) NOT NULL COMMENT '年级：GRADE_1~GRADE_6',
    subject VARCHAR(32) NOT NULL COMMENT '学科：MATH/CHINESE/ENGLISH',
    textbook_version VARCHAR(32) NOT NULL DEFAULT 'RENJIAO' COMMENT '教材版本',
    title VARCHAR(200) NOT NULL COMMENT '章节标题',
    depth INT NOT NULL DEFAULT 1 COMMENT '层级：1=册 2=单元 3=课/章',
    sort_order INT NOT NULL DEFAULT 0 COMMENT '同级排序号',
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    created_at DATETIME DEFAULT NULL,
    updated_at DATETIME DEFAULT NULL,
    INDEX idx_grade_subject (grade, subject),
    INDEX idx_parent_id (parent_id),
    INDEX idx_textbook_version (textbook_version)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='教材章节表';
