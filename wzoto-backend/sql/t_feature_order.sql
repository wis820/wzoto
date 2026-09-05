-- =============================================
-- 学霸到家 - 功能订单表 DDL (P1: 简历置顶+加急审核+单次付费)
-- =============================================
CREATE TABLE IF NOT EXISTS `t_feature_order` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `feature_type` VARCHAR(32) NOT NULL COMMENT '功能类型：RESUME_PIN/EXPEDITE_VERIFY',
    `target_id` BIGINT DEFAULT NULL COMMENT '关联目标ID（tutor_profile_id/verify_record_id）',
    `amount` DECIMAL(10,2) NOT NULL COMMENT '支付金额',
    `payment_status` VARCHAR(16) NOT NULL DEFAULT 'UNPAID' COMMENT '支付状态：UNPAID/PAID',
    `payment_time` DATETIME DEFAULT NULL COMMENT '支付时间',
    `transaction_id` VARCHAR(64) DEFAULT NULL COMMENT '微信支付交易号',
    `expire_time` DATETIME DEFAULT NULL COMMENT '功能到期时间（如简历置顶7天后到期）',
    `deleted` INT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-正常 1-删除',
    `created_at` DATETIME DEFAULT NULL COMMENT '创建时间',
    `updated_at` DATETIME DEFAULT NULL COMMENT '更新时间',
    PRIMARY KEY (`id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_feature_type` (`feature_type`),
    INDEX `idx_payment_status` (`payment_status`),
    INDEX `idx_user_feature` (`user_id`, `feature_type`, `payment_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='功能订单表';

-- ALTER: t_tutor_profile 添加置顶到期时间字段
ALTER TABLE `t_tutor_profile` ADD COLUMN `pinned_until` DATETIME DEFAULT NULL COMMENT '置顶到期时间' AFTER `active`;

-- ALTER: t_verify_record 添加加急标记字段
ALTER TABLE `t_verify_record` ADD COLUMN `expedited` INT NOT NULL DEFAULT 0 COMMENT '是否加急：0否1是' AFTER `remark`;
