-- =============================================
-- 学霸到家 - 音频素材表 DDL（洋葱学院学习资源体系）
-- =============================================
CREATE TABLE IF NOT EXISTS t_audio_material (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    grade VARCHAR(16) NOT NULL COMMENT '年级：GRADE_1~GRADE_6',
    subject VARCHAR(32) NOT NULL COMMENT '学科：MATH/CHINESE/ENGLISH',
    textbook_version VARCHAR(32) NOT NULL DEFAULT 'RENJIAO' COMMENT '教材版本',
    title VARCHAR(200) NOT NULL COMMENT '素材标题',
    audio_type VARCHAR(32) NOT NULL COMMENT '音频类型：FOLLOW_READ跟读/LISTEN听力/READ朗读',
    audio_url VARCHAR(512) NOT NULL COMMENT '音频文件URL',
    duration_seconds INT NOT NULL DEFAULT 0 COMMENT '音频时长（秒）',
    text_content TEXT DEFAULT NULL COMMENT '对应文本内容',
    reference_text TEXT DEFAULT NULL COMMENT '标准参考文本（用于评测比对）',
    knowledge_point_id BIGINT DEFAULT NULL COMMENT '关联知识点ID',
    vip_only TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否仅会员',
    sort_order INT NOT NULL DEFAULT 0 COMMENT '排序号',
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    created_at DATETIME DEFAULT NULL,
    updated_at DATETIME DEFAULT NULL,
    INDEX idx_grade_subject (grade, subject),
    INDEX idx_audio_type (audio_type),
    INDEX idx_textbook_version (textbook_version)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='音频素材表';
