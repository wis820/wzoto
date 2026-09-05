-- 认证记录表
CREATE TABLE IF NOT EXISTS `t_verify_record` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `user_id` BIGINT NOT NULL COMMENT '用户ID',
    `verify_type` VARCHAR(32) NOT NULL COMMENT '认证类型：PARENT_IDCARD/STUDENT_IDCARD/STUDENT_CARD',
    `real_name` VARCHAR(64) NOT NULL COMMENT '真实姓名',
    `id_card_no` VARCHAR(128) NOT NULL COMMENT '身份证号（脱敏存储）',
    `student_card_image` VARCHAR(512) DEFAULT NULL COMMENT '学生证照片URL',
    `verify_status` VARCHAR(16) NOT NULL DEFAULT 'NONE' COMMENT '审核状态：NONE/PENDING/APPROVED/REJECTED',
    `remark` VARCHAR(256) DEFAULT NULL COMMENT '审核备注',
    `deleted` INT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-正常 1-删除',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    INDEX `idx_user_id` (`user_id`),
    INDEX `idx_verify_status` (`verify_status`),
    INDEX `idx_user_status` (`user_id`, `verify_status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='认证记录表';