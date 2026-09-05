-- =============================================
-- 学霸到家 - AI提问记录表 DDL（洋葱学院学习资源体系）
-- 支持拍照搜题+文字提问，AI分步启发式答疑
-- =============================================
CREATE TABLE IF NOT EXISTS t_ai_qa_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    child_id BIGINT NOT NULL COMMENT '子女ID',
    parent_id BIGINT NOT NULL COMMENT '家长用户ID',
    conversation_id VARCHAR(64) NOT NULL COMMENT '会话ID（同一轮对话共享）',
    subject VARCHAR(32) NOT NULL COMMENT '学科：MATH/CHINESE/ENGLISH',
    grade VARCHAR(16) NOT NULL COMMENT '年级',
    qa_type VARCHAR(16) NOT NULL COMMENT '提问类型：PHOTO拍照/TEXT文字',
    question_text TEXT DEFAULT NULL COMMENT '问题文字内容',
    question_image_url VARCHAR(512) DEFAULT NULL COMMENT '问题图片URL（拍照搜题）',
    ocr_text TEXT DEFAULT NULL COMMENT 'OCR识别提取的文字',
    ai_response_json MEDIUMTEXT DEFAULT NULL COMMENT 'AI回复JSON（含分步解析、知识点、变式题）',
    knowledge_tags VARCHAR(512) DEFAULT NULL COMMENT '知识点标签（逗号分隔）',
    is_follow_up TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否为追问：0否1是',
    step_count INT DEFAULT NULL COMMENT '解题步骤数',
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    created_at DATETIME DEFAULT NULL,
    updated_at DATETIME DEFAULT NULL,
    INDEX idx_child_id (child_id),
    INDEX idx_parent_id (parent_id),
    INDEX idx_conversation_id (conversation_id),
    INDEX idx_subject (subject),
    INDEX idx_created_at (created_at),
    INDEX idx_child_subject (child_id, subject)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='AI提问记录表';
