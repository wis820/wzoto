-- =============================================
-- 学霸到家 - AI学情诊断报告表 DDL
-- =============================================
CREATE TABLE IF NOT EXISTS t_ai_report (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL COMMENT '家长用户ID',
    child_name VARCHAR(50) DEFAULT NULL COMMENT '子女姓名',
    weak_subjects VARCHAR(500) NOT NULL COMMENT '薄弱学科（逗号分隔）',
    recent_scores VARCHAR(200) DEFAULT NULL COMMENT '近期考试分数',
    weak_point_desc TEXT COMMENT '薄弱知识点描述',
    photo_urls VARCHAR(1000) DEFAULT NULL COMMENT '试卷/错题照片URL（逗号分隔）',
    report_content MEDIUMTEXT COMMENT 'AI生成报告完整内容（JSON格式）',
    preview_content TEXT COMMENT 'AI生成报告预览内容（非会员可见）',
    report_type VARCHAR(20) NOT NULL DEFAULT 'PREVIEW' COMMENT '报告类型：PREVIEW预览/FULL完整',
    is_member_report TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否会员免费报告：0否1是',
    price DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '报告价格',
    payment_status VARCHAR(20) NOT NULL DEFAULT 'UNPAID' COMMENT '支付状态：UNPAID/PAID/FREE',
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='AI学情诊断报告表';
