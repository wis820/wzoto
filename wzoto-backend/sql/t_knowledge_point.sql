-- =============================================
-- 学霸到家 - 知识点表 DDL（洋葱学院学习资源体系）
-- 树形结构：按年级+学科+教材版本组织，parent_id自关联
-- =============================================
CREATE TABLE IF NOT EXISTS t_knowledge_point (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    parent_id BIGINT DEFAULT NULL COMMENT '父节点ID（NULL为顶级章节）',
    grade VARCHAR(16) NOT NULL COMMENT '年级：GRADE_1~GRADE_6',
    subject VARCHAR(32) NOT NULL COMMENT '学科：MATH/CHINESE/ENGLISH',
    textbook_version VARCHAR(32) NOT NULL DEFAULT 'RENJIAO' COMMENT '教材版本',
    name VARCHAR(128) NOT NULL COMMENT '知识点/章节名称',
    description TEXT DEFAULT NULL COMMENT '知识点描述',
    depth INT NOT NULL DEFAULT 1 COMMENT '层级深度：1=册 2=单元 3=章 4=节 5=知识点',
    sort_order INT NOT NULL DEFAULT 0 COMMENT '同级排序号',
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    created_at DATETIME DEFAULT NULL,
    updated_at DATETIME DEFAULT NULL,
    INDEX idx_grade_subject (grade, subject),
    INDEX idx_parent_id (parent_id),
    INDEX idx_textbook_version (textbook_version),
    INDEX idx_depth (depth)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='知识点表';
