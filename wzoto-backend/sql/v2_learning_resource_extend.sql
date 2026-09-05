-- =============================================
-- 学霸到家 - v2 学习资源扩展
-- 对标洋葱学院播放器 + 管理后台
-- 兼容 MySQL 5.5+
-- =============================================
SET NAMES utf8mb4;

-- 辅助：安全添加列（MySQL 5.5 兼容）
DROP PROCEDURE IF EXISTS safe_add_column;
DELIMITER //
CREATE PROCEDURE safe_add_column(
  IN p_table VARCHAR(64),
  IN p_column VARCHAR(64),
  IN p_definition VARCHAR(512)
)
BEGIN
  DECLARE col_count INT DEFAULT 0;
  SELECT COUNT(*) INTO col_count
  FROM information_schema.COLUMNS
  WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = p_table AND COLUMN_NAME = p_column;
  IF col_count = 0 THEN
    SET @sql = CONCAT('ALTER TABLE ', p_table, ' ADD COLUMN ', p_column, ' ', p_definition);
    PREPARE stmt FROM @sql;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
  END IF;
END //
DELIMITER ;

-- ========== 1. t_learning_resource 新增字段 ==========
CALL safe_add_column('t_learning_resource', 'source_type', "VARCHAR(32) DEFAULT NULL COMMENT '视频来源类型：MP4/BILIBILI/SMARTEDU/UPLOAD/CUSTOM'");
CALL safe_add_column('t_learning_resource', 'subtitle_url', "VARCHAR(512) DEFAULT NULL COMMENT '字幕文件URL（VTT/SRT）'");
CALL safe_add_column('t_learning_resource', 'quality_levels', "VARCHAR(512) DEFAULT NULL COMMENT '画质等级JSON'");
CALL safe_add_column('t_learning_resource', 'knowledge_markers', "VARCHAR(1024) DEFAULT NULL COMMENT '知识点标记JSON'");
CALL safe_add_column('t_learning_resource', 'status', "VARCHAR(16) NOT NULL DEFAULT 'PUBLISHED' COMMENT '资源状态：DRAFT/PUBLISHED/UNPUBLISHED/DELETED'");
CALL safe_add_column('t_learning_resource', 'publish_at', "DATETIME DEFAULT NULL COMMENT '上架时间'");
CALL safe_add_column('t_learning_resource', 'unpublish_at', "DATETIME DEFAULT NULL COMMENT '下架时间'");
CALL safe_add_column('t_learning_resource', 'operator_id', "BIGINT DEFAULT NULL COMMENT '操作人ID'");
CALL safe_add_column('t_learning_resource', 'description', "TEXT DEFAULT NULL COMMENT '资源描述'");

-- 为已有数据推断 source_type
UPDATE t_learning_resource
SET source_type = CASE
  WHEN content_url IS NULL THEN NULL
  WHEN content_url LIKE '%bilibili%' THEN 'BILIBILI'
  WHEN content_url LIKE '%smartedu.cn%' THEN 'SMARTEDU'
  WHEN content_url LIKE '%.mp4' OR content_url LIKE '%.webm' OR content_url LIKE '%.m3u8' THEN 'MP4'
  WHEN content_url LIKE 'http%' THEN 'CUSTOM'
  ELSE 'MP4'
END
WHERE source_type IS NULL;

-- ========== 2. t_micro_course 新增字段 ==========
CALL safe_add_column('t_micro_course', 'source_type', "VARCHAR(32) DEFAULT NULL COMMENT '视频来源类型'");
CALL safe_add_column('t_micro_course', 'subtitle_url', "VARCHAR(512) DEFAULT NULL COMMENT '字幕URL'");
CALL safe_add_column('t_micro_course', 'quality_levels', "VARCHAR(512) DEFAULT NULL COMMENT '画质JSON'");
CALL safe_add_column('t_micro_course', 'knowledge_markers', "VARCHAR(1024) DEFAULT NULL COMMENT '知识点标记JSON'");
CALL safe_add_column('t_micro_course', 'status', "VARCHAR(16) NOT NULL DEFAULT 'PUBLISHED' COMMENT '状态'");
CALL safe_add_column('t_micro_course', 'description', "TEXT DEFAULT NULL COMMENT '课程简介'");

UPDATE t_micro_course
SET source_type = CASE
  WHEN video_url IS NULL THEN NULL
  WHEN video_url LIKE '%bilibili%' THEN 'BILIBILI'
  WHEN video_url LIKE '%smartedu.cn%' THEN 'SMARTEDU'
  WHEN video_url LIKE '%.mp4' OR video_url LIKE '%.webm' OR video_url LIKE '%.m3u8' THEN 'MP4'
  WHEN video_url LIKE 'http%' THEN 'CUSTOM'
  ELSE 'MP4'
END
WHERE source_type IS NULL;

-- ========== 3. t_course_watch_record 补充字段 ==========
CALL safe_add_column('t_course_watch_record', 'first_watch_time', "DATETIME DEFAULT NULL COMMENT '首次观看时间'");
CALL safe_add_column('t_course_watch_record', 'last_watch_time', "DATETIME DEFAULT NULL COMMENT '最后观看时间'");

-- ========== 4. t_course_play_log 播放明细日志 ==========
CREATE TABLE IF NOT EXISTS t_course_play_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL COMMENT '用户ID',
    child_id BIGINT NOT NULL COMMENT '子女ID',
    resource_id BIGINT NOT NULL COMMENT '资源ID',
    session_id VARCHAR(64) NOT NULL COMMENT '播放会话ID',
    `current_time` INT NOT NULL DEFAULT 0 COMMENT '当前播放位置（秒）',
    duration INT NOT NULL DEFAULT 0 COMMENT '视频总时长（秒）',
    percent INT NOT NULL DEFAULT 0 COMMENT '进度百分比',
    playback_rate DECIMAL(3,2) NOT NULL DEFAULT 1.00 COMMENT '播放倍速',
    quality VARCHAR(32) DEFAULT NULL COMMENT '画质',
    source_type VARCHAR(32) DEFAULT NULL COMMENT '来源类型',
    event_type VARCHAR(32) NOT NULL DEFAULT 'HEARTBEAT' COMMENT '事件：PLAY/PAUSE/SEEK/HEARTBEAT/FINISH',
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    created_at DATETIME DEFAULT NULL,
    INDEX idx_user_resource (user_id, resource_id),
    INDEX idx_child_id (child_id),
    INDEX idx_session (session_id),
    INDEX idx_created (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='播放明细日志表';

-- 清理辅助存储过程
DROP PROCEDURE IF EXISTS safe_add_column;
