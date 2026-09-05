-- =============================================
-- 修复乱码：清理种子数据表后重新导入
-- 执行方式（cmd 原始字节重定向，保证 UTF-8 不被破坏）：
--   cmd /c "mysql --default-character-set=utf8 -uroot -proot wzoto < fix_reimport_cleanup.sql"
-- =============================================
SET NAMES utf8mb4;

DELETE FROM t_learning_resource;
DELETE FROM t_micro_course;
DELETE FROM t_exercise_bank;
DELETE FROM t_test_paper;
DELETE FROM t_audio_material;
DELETE FROM t_study_material;
DELETE FROM t_resource_chapter;
DELETE FROM t_knowledge_point;
DELETE FROM t_subject;

-- 新增四年级孩子档案（家长=测试家长 userId=2）
INSERT INTO t_child (parent_id, name, grade, textbook_version, deleted, created_at, updated_at)
SELECT 2, '测试小刚', 'GRADE_4', 'RENJIAO', 0, NOW(), NOW()
FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM t_child WHERE parent_id = 2 AND grade = 'GRADE_4' AND deleted = 0);
