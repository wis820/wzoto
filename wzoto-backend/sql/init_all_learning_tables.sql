-- =============================================
-- 学习资源相关表 DDL + comprehensive 种子数据
-- 一次性创建所有缺失的表并灌入数据
-- =============================================
SET NAMES utf8mb4;

-- 清理旧表（可重复初始化）
DROP TABLE IF EXISTS t_learning_resource;
DROP TABLE IF EXISTS t_study_material;
DROP TABLE IF EXISTS t_audio_material;
DROP TABLE IF EXISTS t_test_paper;
DROP TABLE IF EXISTS t_exercise_bank;
DROP TABLE IF EXISTS t_micro_course;
DROP TABLE IF EXISTS t_resource_chapter;
DROP TABLE IF EXISTS t_knowledge_point;
DROP TABLE IF EXISTS t_subject;

-- 先创建所有学习资源相关表（IF NOT EXISTS，可重复执行）
source f:/wanglianyida/wzoto/wzoto-backend/sql/t_subject.sql;
source f:/wanglianyida/wzoto/wzoto-backend/sql/t_knowledge_point.sql;
source f:/wanglianyida/wzoto/wzoto-backend/sql/t_resource_chapter.sql;
source f:/wanglianyida/wzoto/wzoto-backend/sql/t_micro_course.sql;
source f:/wanglianyida/wzoto/wzoto-backend/sql/t_exercise_bank.sql;
source f:/wanglianyida/wzoto/wzoto-backend/sql/t_test_paper.sql;
source f:/wanglianyida/wzoto/wzoto-backend/sql/t_audio_material.sql;
source f:/wanglianyida/wzoto/wzoto-backend/sql/t_study_material.sql;
source f:/wanglianyida/wzoto/wzoto-backend/sql/t_learning_resource.sql;

-- comprehensive 种子数据
source f:/wanglianyida/wzoto/wzoto-backend/sql/seed_data_comprehensive_resources.sql;
