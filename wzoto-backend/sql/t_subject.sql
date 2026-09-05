-- =============================================
-- 学霸到家 - 学科表 DDL（洋葱学院学习资源体系）
-- =============================================
CREATE TABLE IF NOT EXISTS t_subject (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(32) NOT NULL COMMENT '学科编码：MATH/CHINESE/ENGLISH',
    name VARCHAR(32) NOT NULL COMMENT '学科名称',
    icon VARCHAR(256) DEFAULT NULL COMMENT '学科图标URL',
    color VARCHAR(16) DEFAULT NULL COMMENT '学科主题色',
    sort_order INT NOT NULL DEFAULT 0 COMMENT '排序号',
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    created_at DATETIME DEFAULT NULL,
    updated_at DATETIME DEFAULT NULL,
    UNIQUE INDEX uk_code (code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='学科表';
