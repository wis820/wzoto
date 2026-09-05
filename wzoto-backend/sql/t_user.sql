-- =============================================
-- 学霸到家 - 用户表 DDL
-- =============================================
CREATE DATABASE IF NOT EXISTS wzoto DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE wzoto;

-- 用户表
CREATE TABLE t_user (
    id           BIGINT       NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    openid       VARCHAR(64)  NOT NULL COMMENT '微信openid',
    unionid      VARCHAR(64)  DEFAULT NULL COMMENT '微信unionid',
    phone        VARCHAR(20)  DEFAULT NULL COMMENT '手机号',
    nickname     VARCHAR(64)  DEFAULT '微信用户' COMMENT '昵称',
    avatar       VARCHAR(512) DEFAULT NULL COMMENT '头像URL',
    identity_type VARCHAR(16) DEFAULT NULL COMMENT '身份类型：PARENT/STUDENT',
    verify_status VARCHAR(16) DEFAULT 'NONE' COMMENT '认证状态：NONE/PENDING/APPROVED/REJECTED',
    real_name    VARCHAR(32)  DEFAULT NULL COMMENT '真实姓名',
    deleted      TINYINT(1)   DEFAULT 0 COMMENT '是否删除：0否1是',
    created_at   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_openid (openid),
    KEY idx_phone (phone),
    KEY idx_identity_type (identity_type),
    KEY idx_verify_status (verify_status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';