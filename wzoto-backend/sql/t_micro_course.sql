-- =============================================
-- 学霸到家 - 微课视频表 DDL（洋葱学院学习资源体系）
-- =============================================
CREATE TABLE IF NOT EXISTS t_micro_course (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    grade VARCHAR(16) NOT NULL COMMENT '年级：GRADE_1~GRADE_6',
    subject VARCHAR(32) NOT NULL COMMENT '学科：MATH/CHINESE/ENGLISH',
    textbook_version VARCHAR(32) NOT NULL DEFAULT 'RENJIAO' COMMENT '教材版本',
    chapter_id BIGINT DEFAULT NULL COMMENT '关联教材章节ID',
    knowledge_point_id BIGINT DEFAULT NULL COMMENT '关联知识点ID',
    title VARCHAR(200) NOT NULL COMMENT '微课标题',
    description TEXT DEFAULT NULL COMMENT '微课简介',
    cover_url VARCHAR(512) DEFAULT NULL COMMENT '封面图URL',
    video_url VARCHAR(512) NOT NULL COMMENT '视频播放URL',
    duration_seconds INT NOT NULL DEFAULT 0 COMMENT '视频时长（秒）',
    resolution VARCHAR(16) DEFAULT '1080P' COMMENT '分辨率',
    file_size_mb INT DEFAULT NULL COMMENT '文件大小（MB）',
    difficulty VARCHAR(16) NOT NULL DEFAULT 'MEDIUM' COMMENT '难度：EASY/MEDIUM/HARD',
    vip_only TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否仅会员：0否1是',
    play_count INT NOT NULL DEFAULT 0 COMMENT '播放次数',
    sort_order INT NOT NULL DEFAULT 0 COMMENT '排序号',
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    created_at DATETIME DEFAULT NULL,
    updated_at DATETIME DEFAULT NULL,
    INDEX idx_grade_subject (grade, subject),
    INDEX idx_textbook_version (textbook_version),
    INDEX idx_chapter_id (chapter_id),
    INDEX idx_knowledge_point_id (knowledge_point_id),
    INDEX idx_vip_only (vip_only),
    INDEX idx_difficulty (difficulty)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='微课视频表';
