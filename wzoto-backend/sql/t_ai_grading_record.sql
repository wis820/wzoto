-- =============================================
-- 学霸到家 - AI批改记录表 DDL（洋葱学院学习资源体系）
-- 作文批改 + 口语评测
-- =============================================
CREATE TABLE IF NOT EXISTS t_ai_grading_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    child_id BIGINT NOT NULL COMMENT '子女ID',
    parent_id BIGINT NOT NULL COMMENT '家长用户ID',
    grading_type VARCHAR(32) NOT NULL COMMENT '批改类型：COMPOSITION作文批改/PRONUNCIATION口语评测',
    subject VARCHAR(32) NOT NULL COMMENT '学科：CHINESE/ENGLISH',
    grade VARCHAR(16) NOT NULL COMMENT '年级',
    title VARCHAR(200) DEFAULT NULL COMMENT '标题（作文标题/跟读单词）',
    content_text TEXT DEFAULT NULL COMMENT '文本内容（作文正文）',
    content_audio_url VARCHAR(512) DEFAULT NULL COMMENT '音频URL（口语录音）',
    ai_result_json MEDIUMTEXT DEFAULT NULL COMMENT 'AI批改结果JSON（含分数/纠错/建议）',
    score INT DEFAULT NULL COMMENT 'AI评分（0-100）',
    error_count INT DEFAULT NULL COMMENT '错误数量（错别字/病句/发音错误）',
    suggestion TEXT DEFAULT NULL COMMENT '优化建议摘要',
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    created_at DATETIME DEFAULT NULL,
    updated_at DATETIME DEFAULT NULL,
    INDEX idx_child_id (child_id),
    INDEX idx_parent_id (parent_id),
    INDEX idx_grading_type (grading_type),
    INDEX idx_subject (subject),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='AI批改记录表';
