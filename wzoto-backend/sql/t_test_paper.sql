-- =============================================
-- 学霸到家 - 试卷表 DDL（洋葱学院学习资源体系）
-- =============================================
CREATE TABLE IF NOT EXISTS t_test_paper (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    grade VARCHAR(16) NOT NULL COMMENT '年级：GRADE_1~GRADE_6',
    subject VARCHAR(32) NOT NULL COMMENT '学科：MATH/CHINESE/ENGLISH',
    textbook_version VARCHAR(32) NOT NULL DEFAULT 'RENJIAO' COMMENT '教材版本',
    title VARCHAR(200) NOT NULL COMMENT '试卷标题',
    paper_type VARCHAR(32) NOT NULL COMMENT '试卷类型：UNIT单元测试/MID_TERM期中/FINAL期末/MOCK模拟',
    description TEXT DEFAULT NULL COMMENT '试卷说明',
    total_score INT NOT NULL DEFAULT 100 COMMENT '总分',
    duration_minutes INT NOT NULL DEFAULT 60 COMMENT '建议用时（分钟）',
    questions_json MEDIUMTEXT NOT NULL COMMENT '题目集合JSON（包含题目ID和分值）',
    cover_url VARCHAR(512) DEFAULT NULL COMMENT '封面图URL',
    vip_only TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否仅会员',
    use_count INT NOT NULL DEFAULT 0 COMMENT '使用次数',
    sort_order INT NOT NULL DEFAULT 0 COMMENT '排序号',
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    created_at DATETIME DEFAULT NULL,
    updated_at DATETIME DEFAULT NULL,
    INDEX idx_grade_subject (grade, subject),
    INDEX idx_paper_type (paper_type),
    INDEX idx_textbook_version (textbook_version),
    INDEX idx_vip_only (vip_only)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='试卷表';
