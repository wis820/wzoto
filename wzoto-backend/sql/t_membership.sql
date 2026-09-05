-- =============================================
-- 学霸到家 - 会员表 DDL
-- =============================================
CREATE TABLE IF NOT EXISTS t_membership (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    member_type VARCHAR(20) NOT NULL COMMENT '会员类型：PARENT_MONTH/PARENT_YEAR/STUDENT_MONTH',
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '状态：ACTIVE/EXPIRED/CANCELLED',
    start_time DATETIME NOT NULL COMMENT '生效时间',
    expire_time DATETIME NOT NULL COMMENT '到期时间',
    auto_renew TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否自动续费：0否1是',
    transaction_id VARCHAR(64) DEFAULT NULL COMMENT '微信支付交易号',
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id),
    INDEX idx_status (status),
    INDEX idx_expire_time (expire_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='会员表';
