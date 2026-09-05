-- =============================================
-- 学霸到家 - 学习资料表 DDL（洋葱学院学习资源体系）
-- 生字卡片/思维导图/PDF资料等
-- =============================================
CREATE TABLE IF NOT EXISTS t_study_material (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    grade VARCHAR(16) NOT NULL COMMENT '年级：GRADE_1~GRADE_6',
    subject VARCHAR(32) NOT NULL COMMENT '学科：MATH/CHINESE/ENGLISH',
    textbook_version VARCHAR(32) NOT NULL DEFAULT 'RENJIAO' COMMENT '教材版本',
    title VARCHAR(200) NOT NULL COMMENT '资料标题',
    material_type VARCHAR(32) NOT NULL COMMENT '资料类型：FLASHCARD生字卡片/MINDMAP思维导图/PDF文档/IMAGE图片',
    content_url VARCHAR(512) NOT NULL COMMENT '资料文件URL',
    preview_url VARCHAR(512) DEFAULT NULL COMMENT '预览图URL',
    description TEXT DEFAULT NULL COMMENT '资料描述',
    knowledge_point_id BIGINT DEFAULT NULL COMMENT '关联知识点ID',
    vip_only TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否仅会员',
    download_count INT NOT NULL DEFAULT 0 COMMENT '下载次数',
    sort_order INT NOT NULL DEFAULT 0 COMMENT '排序号',
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    created_at DATETIME DEFAULT NULL,
    updated_at DATETIME DEFAULT NULL,
    INDEX idx_grade_subject (grade, subject),
    INDEX idx_material_type (material_type),
    INDEX idx_textbook_version (textbook_version)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='学习资料表';
