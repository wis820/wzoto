-- H2 Schema (auto-generated)

DROP TABLE IF EXISTS t_ai_exercise_generated;
-- =============================================
-- 学霸到家 - AI生成练习题表 DDL（洋葱学院学习资源体系）
-- AI根据错题/薄弱点自动生成针对性练习题
-- =============================================
CREATE TABLE IF NOT EXISTS t_ai_exercise_generated (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    child_id BIGINT NOT NULL ,
    parent_id BIGINT NOT NULL ,
    source VARCHAR(32) NOT NULL ,
    source_id BIGINT DEFAULT NULL ,
    subject VARCHAR(32) NOT NULL ,
    grade VARCHAR(16) NOT NULL ,
    knowledge_point VARCHAR(128) DEFAULT NULL ,
    question_content TEXT NOT NULL ,
    correct_answer TEXT NOT NULL ,
    ai_explanation TEXT DEFAULT NULL ,
    difficulty VARCHAR(16) NOT NULL DEFAULT 'MEDIUM' ,
    answered TINYINT NOT NULL DEFAULT 0 ,
    is_correct TINYINT DEFAULT NULL ,
    answered_at DATETIME DEFAULT NULL ,
    deleted TINYINT NOT NULL DEFAULT 0,
    created_at DATETIME DEFAULT NULL,
    updated_at DATETIME DEFAULT NULL
);
CREATE INDEX IF NOT EXISTS idx_child_id ON t_ai_exercise_generated (child_id);
CREATE INDEX IF NOT EXISTS idx_source ON t_ai_exercise_generated (source);
CREATE INDEX IF NOT EXISTS idx_subject ON t_ai_exercise_generated (subject);
CREATE INDEX IF NOT EXISTS idx_answered ON t_ai_exercise_generated (answered);
CREATE INDEX IF NOT EXISTS idx_created_at ON t_ai_exercise_generated (created_at);

DROP TABLE IF EXISTS t_ai_grading_record;
-- =============================================
-- 学霸到家 - AI批改记录表 DDL（洋葱学院学习资源体系）
-- 作文批改 + 口语评测
-- =============================================
CREATE TABLE IF NOT EXISTS t_ai_grading_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    child_id BIGINT NOT NULL ,
    parent_id BIGINT NOT NULL ,
    grading_type VARCHAR(32) NOT NULL ,
    subject VARCHAR(32) NOT NULL ,
    grade VARCHAR(16) NOT NULL ,
    title VARCHAR(200) DEFAULT NULL ,
    content_text TEXT DEFAULT NULL ,
    content_audio_url VARCHAR(512) DEFAULT NULL ,
    ai_result_json TEXT DEFAULT NULL ,
    score INT DEFAULT NULL ,
    error_count INT DEFAULT NULL ,
    suggestion TEXT DEFAULT NULL ,
    deleted TINYINT NOT NULL DEFAULT 0,
    created_at DATETIME DEFAULT NULL,
    updated_at DATETIME DEFAULT NULL
);
CREATE INDEX IF NOT EXISTS idx_child_id ON t_ai_grading_record (child_id);
CREATE INDEX IF NOT EXISTS idx_parent_id ON t_ai_grading_record (parent_id);
CREATE INDEX IF NOT EXISTS idx_grading_type ON t_ai_grading_record (grading_type);
CREATE INDEX IF NOT EXISTS idx_subject ON t_ai_grading_record (subject);
CREATE INDEX IF NOT EXISTS idx_created_at ON t_ai_grading_record (created_at);

DROP TABLE IF EXISTS t_ai_learning_plan;
-- =============================================
-- 学霸到家 - AI学习规划记录表 DDL（洋葱学院学习资源体系）
-- AI根据学情自动生成每日学习计划
-- =============================================
CREATE TABLE IF NOT EXISTS t_ai_learning_plan (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    child_id BIGINT NOT NULL ,
    parent_id BIGINT NOT NULL ,
    plan_date DATE NOT NULL ,
    plan_content_json TEXT NOT NULL ,
    weak_points_json TEXT DEFAULT NULL ,
    ai_suggestion TEXT DEFAULT NULL ,
    applied TINYINT NOT NULL DEFAULT 0 ,
    applied_at DATETIME DEFAULT NULL ,
    generated_at DATETIME NOT NULL ,
    deleted TINYINT NOT NULL DEFAULT 0,
    created_at DATETIME DEFAULT NULL,
    updated_at DATETIME DEFAULT NULL
);
CREATE INDEX IF NOT EXISTS idx_child_id ON t_ai_learning_plan (child_id);
CREATE INDEX IF NOT EXISTS idx_plan_date ON t_ai_learning_plan (plan_date);
CREATE INDEX IF NOT EXISTS idx_child_date ON t_ai_learning_plan (child_id, plan_date);
CREATE INDEX IF NOT EXISTS idx_applied ON t_ai_learning_plan (applied);

DROP TABLE IF EXISTS t_ai_qa_record;
-- =============================================
-- 学霸到家 - AI提问记录表 DDL（洋葱学院学习资源体系）
-- 支持拍照搜题+文字提问，AI分步启发式答疑
-- =============================================
CREATE TABLE IF NOT EXISTS t_ai_qa_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    child_id BIGINT NOT NULL ,
    parent_id BIGINT NOT NULL ,
    conversation_id VARCHAR(64) NOT NULL ,
    subject VARCHAR(32) NOT NULL ,
    grade VARCHAR(16) NOT NULL ,
    qa_type VARCHAR(16) NOT NULL ,
    question_text TEXT DEFAULT NULL ,
    question_image_url VARCHAR(512) DEFAULT NULL ,
    ocr_text TEXT DEFAULT NULL ,
    ai_response_json TEXT DEFAULT NULL ,
    knowledge_tags VARCHAR(512) DEFAULT NULL ,
    is_follow_up TINYINT NOT NULL DEFAULT 0 ,
    step_count INT DEFAULT NULL ,
    deleted TINYINT NOT NULL DEFAULT 0,
    created_at DATETIME DEFAULT NULL,
    updated_at DATETIME DEFAULT NULL
);
CREATE INDEX IF NOT EXISTS idx_child_id ON t_ai_qa_record (child_id);
CREATE INDEX IF NOT EXISTS idx_parent_id ON t_ai_qa_record (parent_id);
CREATE INDEX IF NOT EXISTS idx_conversation_id ON t_ai_qa_record (conversation_id);
CREATE INDEX IF NOT EXISTS idx_subject ON t_ai_qa_record (subject);
CREATE INDEX IF NOT EXISTS idx_created_at ON t_ai_qa_record (created_at);
CREATE INDEX IF NOT EXISTS idx_child_subject ON t_ai_qa_record (child_id, subject);

DROP TABLE IF EXISTS t_ai_report;
-- =============================================
-- 学霸到家 - AI学情诊断报告表 DDL
-- =============================================
CREATE TABLE IF NOT EXISTS t_ai_report (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL ,
    child_name VARCHAR(50) DEFAULT NULL ,
    weak_subjects VARCHAR(500) NOT NULL ,
    recent_scores VARCHAR(200) DEFAULT NULL ,
    weak_point_desc TEXT ,
    photo_urls VARCHAR(1000) DEFAULT NULL ,
    report_content TEXT ,
    preview_content TEXT ,
    report_type VARCHAR(20) NOT NULL DEFAULT 'PREVIEW' ,
    is_member_report TINYINT NOT NULL DEFAULT 0 ,
    price DECIMAL(10,2) NOT NULL DEFAULT 0.00 ,
    payment_status VARCHAR(20) NOT NULL DEFAULT 'UNPAID' ,
    deleted TINYINT NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX IF NOT EXISTS idx_user_id ON t_ai_report (user_id);
CREATE INDEX IF NOT EXISTS idx_created_at ON t_ai_report (created_at);

DROP TABLE IF EXISTS t_ai_tutor_optimization;
CREATE TABLE IF NOT EXISTS t_ai_tutor_optimization (
    id BIGINT NOT NULL AUTO_INCREMENT ,
    user_id BIGINT NOT NULL ,
    optimization_type VARCHAR(32) NOT NULL ,
    university VARCHAR(128) DEFAULT NULL ,
    major VARCHAR(64) DEFAULT NULL ,
    grade VARCHAR(16) DEFAULT NULL ,
    subjects VARCHAR(512) DEFAULT NULL ,
    current_bio TEXT DEFAULT NULL ,
    current_experience TEXT DEFAULT NULL ,
    current_rate INT DEFAULT NULL ,
    optimized_content TEXT DEFAULT NULL ,
    preview_content TEXT DEFAULT NULL ,
    is_member_report TINYINT NOT NULL DEFAULT 0 ,
    price DECIMAL(10,2) NOT NULL DEFAULT 0.00 ,
    payment_status VARCHAR(16) NOT NULL DEFAULT 'FREE' ,
    deleted INT NOT NULL DEFAULT 0 ,
    created_at DATETIME DEFAULT NULL ,
    updated_at DATETIME DEFAULT NULL ,
    PRIMARY KEY (id)
);
CREATE INDEX IF NOT EXISTS idx_user_id ON t_ai_tutor_optimization (user_id);
CREATE INDEX IF NOT EXISTS idx_type ON t_ai_tutor_optimization (optimization_type);

DROP TABLE IF EXISTS t_audio_material;
-- =============================================
-- 学霸到家 - 音频素材表 DDL（洋葱学院学习资源体系）
-- =============================================
CREATE TABLE IF NOT EXISTS t_audio_material (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    grade VARCHAR(16) NOT NULL ,
    subject VARCHAR(32) NOT NULL ,
    textbook_version VARCHAR(32) NOT NULL DEFAULT 'RENJIAO' ,
    title VARCHAR(200) NOT NULL ,
    audio_type VARCHAR(32) NOT NULL ,
    audio_url VARCHAR(512) NOT NULL ,
    duration_seconds INT NOT NULL DEFAULT 0 ,
    text_content TEXT DEFAULT NULL ,
    reference_text TEXT DEFAULT NULL ,
    knowledge_point_id BIGINT DEFAULT NULL ,
    vip_only TINYINT NOT NULL DEFAULT 0 ,
    sort_order INT NOT NULL DEFAULT 0 ,
    deleted TINYINT NOT NULL DEFAULT 0,
    created_at DATETIME DEFAULT NULL,
    updated_at DATETIME DEFAULT NULL
);
CREATE INDEX IF NOT EXISTS idx_grade_subject ON t_audio_material (grade, subject);
CREATE INDEX IF NOT EXISTS idx_audio_type ON t_audio_material (audio_type);
CREATE INDEX IF NOT EXISTS idx_textbook_version ON t_audio_material (textbook_version);

DROP TABLE IF EXISTS t_booking;
CREATE TABLE IF NOT EXISTS t_booking (
    id BIGINT NOT NULL AUTO_INCREMENT ,
    parent_id BIGINT NOT NULL ,
    tutor_id BIGINT NOT NULL ,
    tutor_profile_id BIGINT NOT NULL ,
    subject VARCHAR(64) NOT NULL ,
    booking_date DATE NOT NULL ,
    start_time TIME NOT NULL ,
    end_time TIME NOT NULL ,
    address VARCHAR(256) DEFAULT NULL ,
    message VARCHAR(512) DEFAULT NULL ,
    status VARCHAR(16) NOT NULL DEFAULT 'PENDING' ,
    reply VARCHAR(256) DEFAULT NULL ,
    deleted INT NOT NULL DEFAULT 0 ,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ,
    PRIMARY KEY (id)
);
CREATE INDEX IF NOT EXISTS idx_parent_id ON t_booking (parent_id);
CREATE INDEX IF NOT EXISTS idx_tutor_id ON t_booking (tutor_id);
CREATE INDEX IF NOT EXISTS idx_status ON t_booking (status);
CREATE INDEX IF NOT EXISTS idx_booking_date ON t_booking (booking_date);
CREATE INDEX IF NOT EXISTS idx_tutor_status ON t_booking (tutor_id, status);

DROP TABLE IF EXISTS t_child;
-- =============================================
-- 学霸到家 - 子女档案表 DDL（家长端小学生学习模块）
-- =============================================
CREATE TABLE IF NOT EXISTS t_child (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    parent_id BIGINT NOT NULL ,
    name VARCHAR(50) NOT NULL ,
    grade VARCHAR(16) NOT NULL ,
    textbook_version VARCHAR(32) NOT NULL DEFAULT 'RENJIAO' ,
    school VARCHAR(128) DEFAULT NULL ,
    avatar VARCHAR(512) DEFAULT NULL ,
    deleted TINYINT NOT NULL DEFAULT 0 ,
    created_at DATETIME DEFAULT NULL ,
    updated_at DATETIME DEFAULT NULL 
);
CREATE INDEX IF NOT EXISTS idx_parent_id ON t_child (parent_id);
CREATE INDEX IF NOT EXISTS idx_grade ON t_child (grade);

DROP TABLE IF EXISTS t_child_achievement;
-- =============================================
-- 学霸到家 - 成长激励表 DDL
-- =============================================
CREATE TABLE IF NOT EXISTS t_child_achievement (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    child_id BIGINT NOT NULL ,
    achievement_type VARCHAR(32) NOT NULL ,
    title VARCHAR(64) NOT NULL ,
    icon VARCHAR(256) DEFAULT NULL ,
    points INT NOT NULL DEFAULT 0 ,
    skin VARCHAR(64) DEFAULT NULL ,
    source VARCHAR(32) NOT NULL DEFAULT 'SYSTEM' ,
    task_name VARCHAR(128) DEFAULT NULL ,
    deleted TINYINT NOT NULL DEFAULT 0,
    created_at DATETIME DEFAULT NULL,
    updated_at DATETIME DEFAULT NULL
);
CREATE INDEX IF NOT EXISTS idx_child_id ON t_child_achievement (child_id);
CREATE INDEX IF NOT EXISTS idx_achievement_type ON t_child_achievement (achievement_type);
CREATE INDEX IF NOT EXISTS idx_created_at ON t_child_achievement (created_at);

DROP TABLE IF EXISTS t_course_watch_record;
-- =============================================
-- 学霸到家 - 上课观看记录表 DDL
-- =============================================
CREATE TABLE IF NOT EXISTS t_course_watch_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    child_id BIGINT NOT NULL ,
    resource_id BIGINT NOT NULL ,
    watch_duration_seconds INT NOT NULL DEFAULT 0 ,
    progress_percent INT NOT NULL DEFAULT 0 ,
    last_position_seconds INT NOT NULL DEFAULT 0 ,
    completed TINYINT NOT NULL DEFAULT 0 ,
    completed_at DATETIME DEFAULT NULL ,
    deleted TINYINT NOT NULL DEFAULT 0,
    created_at DATETIME DEFAULT NULL,
    updated_at DATETIME DEFAULT NULL
);
CREATE UNIQUE INDEX IF NOT EXISTS uk_child_resource ON t_course_watch_record (child_id, resource_id);
CREATE INDEX IF NOT EXISTS idx_child_id ON t_course_watch_record (child_id);
CREATE INDEX IF NOT EXISTS idx_resource_id ON t_course_watch_record (resource_id);
CREATE INDEX IF NOT EXISTS idx_completed ON t_course_watch_record (completed);

DROP TABLE IF EXISTS t_exercise_answer_record;
-- =============================================
-- 学霸到家 - 习题作答记录表 DDL
-- =============================================
CREATE TABLE IF NOT EXISTS t_exercise_answer_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    child_id BIGINT NOT NULL ,
    resource_id BIGINT DEFAULT NULL ,
    question_id BIGINT DEFAULT NULL ,
    subject VARCHAR(32) NOT NULL ,
    knowledge_point VARCHAR(128) DEFAULT NULL ,
    answer TEXT DEFAULT NULL ,
    correct TINYINT NOT NULL DEFAULT 0 ,
    cost_seconds INT DEFAULT NULL ,
    deleted TINYINT NOT NULL DEFAULT 0,
    created_at DATETIME DEFAULT NULL,
    updated_at DATETIME DEFAULT NULL
);
CREATE INDEX IF NOT EXISTS idx_child_id ON t_exercise_answer_record (child_id);
CREATE INDEX IF NOT EXISTS idx_subject ON t_exercise_answer_record (subject);
CREATE INDEX IF NOT EXISTS idx_resource_id ON t_exercise_answer_record (resource_id);
CREATE INDEX IF NOT EXISTS idx_created_at ON t_exercise_answer_record (created_at);
CREATE INDEX IF NOT EXISTS idx_child_subject ON t_exercise_answer_record (child_id, subject);

DROP TABLE IF EXISTS t_exercise_bank;
-- =============================================
-- 学霸到家 - 习题库表 DDL（洋葱学院学习资源体系）
-- =============================================
CREATE TABLE IF NOT EXISTS t_exercise_bank (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    grade VARCHAR(16) NOT NULL ,
    subject VARCHAR(32) NOT NULL ,
    textbook_version VARCHAR(32) NOT NULL DEFAULT 'RENJIAO' ,
    knowledge_point_id BIGINT DEFAULT NULL ,
    question_type VARCHAR(32) NOT NULL ,
    question_content TEXT NOT NULL ,
    options TEXT DEFAULT NULL ,
    correct_answer TEXT NOT NULL ,
    explanation TEXT DEFAULT NULL ,
    difficulty VARCHAR(16) NOT NULL DEFAULT 'MEDIUM' ,
    source_type VARCHAR(32) NOT NULL DEFAULT 'SYSTEM' ,
    vip_only TINYINT NOT NULL DEFAULT 0 ,
    use_count INT NOT NULL DEFAULT 0 ,
    correct_rate INT DEFAULT NULL ,
    sort_order INT NOT NULL DEFAULT 0 ,
    deleted TINYINT NOT NULL DEFAULT 0,
    created_at DATETIME DEFAULT NULL,
    updated_at DATETIME DEFAULT NULL
);
CREATE INDEX IF NOT EXISTS idx_grade_subject ON t_exercise_bank (grade, subject);
CREATE INDEX IF NOT EXISTS idx_knowledge_point_id ON t_exercise_bank (knowledge_point_id);
CREATE INDEX IF NOT EXISTS idx_question_type ON t_exercise_bank (question_type);
CREATE INDEX IF NOT EXISTS idx_difficulty ON t_exercise_bank (difficulty);
CREATE INDEX IF NOT EXISTS idx_source_type ON t_exercise_bank (source_type);
CREATE INDEX IF NOT EXISTS idx_vip_only ON t_exercise_bank (vip_only);

DROP TABLE IF EXISTS t_feature_order;
-- =============================================
-- 学霸到家 - 功能订单表 DDL (P1: 简历置顶+加急审核+单次付费)
-- =============================================
CREATE TABLE IF NOT EXISTS t_feature_order (
    id BIGINT NOT NULL AUTO_INCREMENT ,
    user_id BIGINT NOT NULL ,
    feature_type VARCHAR(32) NOT NULL ,
    target_id BIGINT DEFAULT NULL ,
    amount DECIMAL(10,2) NOT NULL ,
    payment_status VARCHAR(16) NOT NULL DEFAULT 'UNPAID' ,
    payment_time DATETIME DEFAULT NULL ,
    transaction_id VARCHAR(64) DEFAULT NULL ,
    expire_time DATETIME DEFAULT NULL ,
    deleted INT NOT NULL DEFAULT 0 ,
    created_at DATETIME DEFAULT NULL ,
    updated_at DATETIME DEFAULT NULL ,
    PRIMARY KEY (id)
);

-- ALTER: t_tutor_profile 添加置顶到期时间字段
-- ALTER: t_verify_record 添加加急标记字段;
CREATE INDEX IF NOT EXISTS idx_user_id ON t_feature_order (user_id);
CREATE INDEX IF NOT EXISTS idx_feature_type ON t_feature_order (feature_type);
CREATE INDEX IF NOT EXISTS idx_payment_status ON t_feature_order (payment_status);
CREATE INDEX IF NOT EXISTS idx_user_feature ON t_feature_order (user_id, feature_type, payment_status);

DROP TABLE IF EXISTS t_knowledge_point;
-- =============================================
-- 学霸到家 - 知识点表 DDL（洋葱学院学习资源体系）
-- 树形结构：按年级+学科+教材版本组织，parent_id自关联
-- =============================================
CREATE TABLE IF NOT EXISTS t_knowledge_point (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    parent_id BIGINT DEFAULT NULL ,
    grade VARCHAR(16) NOT NULL ,
    subject VARCHAR(32) NOT NULL ,
    textbook_version VARCHAR(32) NOT NULL DEFAULT 'RENJIAO' ,
    name VARCHAR(128) NOT NULL ,
    description TEXT DEFAULT NULL ,
    depth INT NOT NULL DEFAULT 1 ,
    sort_order INT NOT NULL DEFAULT 0 ,
    deleted TINYINT NOT NULL DEFAULT 0,
    created_at DATETIME DEFAULT NULL,
    updated_at DATETIME DEFAULT NULL
);
CREATE INDEX IF NOT EXISTS idx_grade_subject ON t_knowledge_point (grade, subject);
CREATE INDEX IF NOT EXISTS idx_parent_id ON t_knowledge_point (parent_id);
CREATE INDEX IF NOT EXISTS idx_textbook_version ON t_knowledge_point (textbook_version);
CREATE INDEX IF NOT EXISTS idx_depth ON t_knowledge_point (depth);

DROP TABLE IF EXISTS t_learning_plan_config;
-- =============================================
-- 学霸到家 - 学习计划配置表 DDL
-- =============================================
CREATE TABLE IF NOT EXISTS t_learning_plan_config (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    child_id BIGINT NOT NULL ,
    daily_duration_minutes INT NOT NULL DEFAULT 30 ,
    math_weight INT NOT NULL DEFAULT 35 ,
    chinese_weight INT NOT NULL DEFAULT 35 ,
    english_weight INT NOT NULL DEFAULT 30 ,
    special_calculation TINYINT NOT NULL DEFAULT 1 ,
    special_application TINYINT NOT NULL DEFAULT 1 ,
    special_literacy TINYINT NOT NULL DEFAULT 0 ,
    special_words TINYINT NOT NULL DEFAULT 0 ,
    auto_generate TINYINT NOT NULL DEFAULT 1 ,
    deleted TINYINT NOT NULL DEFAULT 0,
    created_at DATETIME DEFAULT NULL,
    updated_at DATETIME DEFAULT NULL
);
CREATE UNIQUE INDEX IF NOT EXISTS uk_child_id ON t_learning_plan_config (child_id);
CREATE INDEX IF NOT EXISTS idx_created_at ON t_learning_plan_config (created_at);

DROP TABLE IF EXISTS t_learning_resource;
-- =============================================
-- 学霸到家 - 学习资源表 DDL
-- =============================================
CREATE TABLE IF NOT EXISTS t_learning_resource (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    grade VARCHAR(16) NOT NULL ,
    subject VARCHAR(32) NOT NULL ,
    textbook_version VARCHAR(32) DEFAULT NULL ,
    resource_type VARCHAR(32) NOT NULL ,
    title VARCHAR(128) NOT NULL ,
    cover_url VARCHAR(512) DEFAULT NULL ,
    content_url VARCHAR(512) DEFAULT NULL ,
    duration_seconds INT DEFAULT NULL ,
    knowledge_point VARCHAR(128) DEFAULT NULL ,
    tags VARCHAR(256) DEFAULT NULL ,
    source_type VARCHAR(32) DEFAULT NULL ,
    subtitle_url VARCHAR(512) DEFAULT NULL ,
    quality_levels VARCHAR(512) DEFAULT NULL ,
    knowledge_markers VARCHAR(1024) DEFAULT NULL ,
    status VARCHAR(16) DEFAULT 'PUBLISHED' ,
    description VARCHAR(512) DEFAULT NULL ,
    publish_at DATETIME DEFAULT NULL ,
    unpublish_at DATETIME DEFAULT NULL ,
    operator_id BIGINT DEFAULT NULL ,
    vip_only TINYINT NOT NULL DEFAULT 0 ,
    sort_order INT NOT NULL DEFAULT 0 ,
    deleted TINYINT NOT NULL DEFAULT 0,
    created_at DATETIME DEFAULT NULL,
    updated_at DATETIME DEFAULT NULL
);
CREATE INDEX IF NOT EXISTS idx_grade_subject ON t_learning_resource (grade, subject);
CREATE INDEX IF NOT EXISTS idx_textbook_version ON t_learning_resource (textbook_version);
CREATE INDEX IF NOT EXISTS idx_resource_type ON t_learning_resource (resource_type);
CREATE INDEX IF NOT EXISTS idx_vip_only ON t_learning_resource (vip_only);
CREATE INDEX IF NOT EXISTS idx_knowledge_point ON t_learning_resource (knowledge_point);

DROP TABLE IF EXISTS t_learning_task;
-- =============================================
-- 学霸到家 - 学习任务记录表 DDL
-- =============================================
CREATE TABLE IF NOT EXISTS t_learning_task (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    child_id BIGINT NOT NULL ,
    plan_config_id BIGINT DEFAULT NULL ,
    task_type VARCHAR(32) NOT NULL ,
    subject VARCHAR(32) NOT NULL ,
    title VARCHAR(128) NOT NULL ,
    content TEXT DEFAULT NULL ,
    resource_id BIGINT DEFAULT NULL ,
    status VARCHAR(32) NOT NULL DEFAULT 'PENDING' ,
    assigned_date DATE NOT NULL ,
    completed_at DATETIME DEFAULT NULL ,
    deleted TINYINT NOT NULL DEFAULT 0,
    created_at DATETIME DEFAULT NULL,
    updated_at DATETIME DEFAULT NULL
);
CREATE INDEX IF NOT EXISTS idx_child_id ON t_learning_task (child_id);
CREATE INDEX IF NOT EXISTS idx_assigned_date ON t_learning_task (assigned_date);
CREATE INDEX IF NOT EXISTS idx_status ON t_learning_task (status);
CREATE INDEX IF NOT EXISTS idx_child_date ON t_learning_task (child_id, assigned_date);
CREATE INDEX IF NOT EXISTS idx_child_status ON t_learning_task (child_id, status);

DROP TABLE IF EXISTS t_membership;
-- =============================================
-- 学霸到家 - 会员表 DDL
-- =============================================
CREATE TABLE IF NOT EXISTS t_membership (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL ,
    member_type VARCHAR(20) NOT NULL ,
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' ,
    start_time DATETIME NOT NULL ,
    expire_time DATETIME NOT NULL ,
    auto_renew TINYINT NOT NULL DEFAULT 0 ,
    transaction_id VARCHAR(64) DEFAULT NULL ,
    deleted TINYINT NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);
CREATE INDEX IF NOT EXISTS idx_user_id ON t_membership (user_id);
CREATE INDEX IF NOT EXISTS idx_status ON t_membership (status);
CREATE INDEX IF NOT EXISTS idx_expire_time ON t_membership (expire_time);

DROP TABLE IF EXISTS t_micro_course;
-- =============================================
-- 学霸到家 - 微课视频表 DDL（洋葱学院学习资源体系）
-- =============================================
CREATE TABLE IF NOT EXISTS t_micro_course (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    grade VARCHAR(16) NOT NULL ,
    subject VARCHAR(32) NOT NULL ,
    textbook_version VARCHAR(32) NOT NULL DEFAULT 'RENJIAO' ,
    chapter_id BIGINT DEFAULT NULL ,
    knowledge_point_id BIGINT DEFAULT NULL ,
    title VARCHAR(200) NOT NULL ,
    description TEXT DEFAULT NULL ,
    cover_url VARCHAR(512) DEFAULT NULL ,
    video_url VARCHAR(512) NOT NULL ,
    duration_seconds INT NOT NULL DEFAULT 0 ,
    resolution VARCHAR(16) DEFAULT '1080P' ,
    file_size_mb INT DEFAULT NULL ,
    difficulty VARCHAR(16) NOT NULL DEFAULT 'MEDIUM' ,
    vip_only TINYINT NOT NULL DEFAULT 0 ,
    play_count INT NOT NULL DEFAULT 0 ,
    sort_order INT NOT NULL DEFAULT 0 ,
    deleted TINYINT NOT NULL DEFAULT 0,
    created_at DATETIME DEFAULT NULL,
    updated_at DATETIME DEFAULT NULL
);
CREATE INDEX IF NOT EXISTS idx_grade_subject ON t_micro_course (grade, subject);
CREATE INDEX IF NOT EXISTS idx_textbook_version ON t_micro_course (textbook_version);
CREATE INDEX IF NOT EXISTS idx_chapter_id ON t_micro_course (chapter_id);
CREATE INDEX IF NOT EXISTS idx_knowledge_point_id ON t_micro_course (knowledge_point_id);
CREATE INDEX IF NOT EXISTS idx_vip_only ON t_micro_course (vip_only);
CREATE INDEX IF NOT EXISTS idx_difficulty ON t_micro_course (difficulty);

DROP TABLE IF EXISTS t_parent_control_config;
-- =============================================
-- 学霸到家 - 家长管控配置表 DDL
-- =============================================
CREATE TABLE IF NOT EXISTS t_parent_control_config (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    parent_id BIGINT NOT NULL ,
    child_id BIGINT NOT NULL ,
    daily_duration_minutes INT NOT NULL DEFAULT 60 ,
    rest_interval_minutes INT NOT NULL DEFAULT 20 ,
    rest_duration_minutes INT NOT NULL DEFAULT 5 ,
    forbidden_start_time TIME DEFAULT '22:00:00' ,
    forbidden_end_time TIME DEFAULT '07:00:00' ,
    locked TINYINT NOT NULL DEFAULT 0 ,
    eye_protection TINYINT NOT NULL DEFAULT 0 ,
    current_page VARCHAR(256) DEFAULT NULL ,
    last_login_at DATETIME DEFAULT NULL ,
    deleted TINYINT NOT NULL DEFAULT 0,
    created_at DATETIME DEFAULT NULL,
    updated_at DATETIME DEFAULT NULL
);
CREATE UNIQUE INDEX IF NOT EXISTS uk_child_id ON t_parent_control_config (child_id);
CREATE INDEX IF NOT EXISTS idx_parent_id ON t_parent_control_config (parent_id);

DROP TABLE IF EXISTS t_resource_chapter;
-- =============================================
-- 学霸到家 - 教材章节表 DDL（洋葱学院学习资源体系）
-- 按年级+学科+教材版本组织章节目录树
-- =============================================
CREATE TABLE IF NOT EXISTS t_resource_chapter (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    parent_id BIGINT DEFAULT NULL ,
    grade VARCHAR(16) NOT NULL ,
    subject VARCHAR(32) NOT NULL ,
    textbook_version VARCHAR(32) NOT NULL DEFAULT 'RENJIAO' ,
    title VARCHAR(200) NOT NULL ,
    depth INT NOT NULL DEFAULT 1 ,
    sort_order INT NOT NULL DEFAULT 0 ,
    deleted TINYINT NOT NULL DEFAULT 0,
    created_at DATETIME DEFAULT NULL,
    updated_at DATETIME DEFAULT NULL
);
CREATE INDEX IF NOT EXISTS idx_grade_subject ON t_resource_chapter (grade, subject);
CREATE INDEX IF NOT EXISTS idx_parent_id ON t_resource_chapter (parent_id);
CREATE INDEX IF NOT EXISTS idx_textbook_version ON t_resource_chapter (textbook_version);

DROP TABLE IF EXISTS t_study_material;
-- =============================================
-- 学霸到家 - 学习资料表 DDL（洋葱学院学习资源体系）
-- 生字卡片/思维导图/PDF资料等
-- =============================================
CREATE TABLE IF NOT EXISTS t_study_material (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    grade VARCHAR(16) NOT NULL ,
    subject VARCHAR(32) NOT NULL ,
    textbook_version VARCHAR(32) NOT NULL DEFAULT 'RENJIAO' ,
    title VARCHAR(200) NOT NULL ,
    material_type VARCHAR(32) NOT NULL ,
    content_url VARCHAR(512) NOT NULL ,
    preview_url VARCHAR(512) DEFAULT NULL ,
    description TEXT DEFAULT NULL ,
    knowledge_point_id BIGINT DEFAULT NULL ,
    vip_only TINYINT NOT NULL DEFAULT 0 ,
    download_count INT NOT NULL DEFAULT 0 ,
    sort_order INT NOT NULL DEFAULT 0 ,
    deleted TINYINT NOT NULL DEFAULT 0,
    created_at DATETIME DEFAULT NULL,
    updated_at DATETIME DEFAULT NULL
);
CREATE INDEX IF NOT EXISTS idx_grade_subject ON t_study_material (grade, subject);
CREATE INDEX IF NOT EXISTS idx_material_type ON t_study_material (material_type);
CREATE INDEX IF NOT EXISTS idx_textbook_version ON t_study_material (textbook_version);

DROP TABLE IF EXISTS t_subject;
-- =============================================
-- 学霸到家 - 学科表 DDL（洋葱学院学习资源体系）
-- =============================================
CREATE TABLE IF NOT EXISTS t_subject (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(32) NOT NULL ,
    name VARCHAR(32) NOT NULL ,
    icon VARCHAR(256) DEFAULT NULL ,
    color VARCHAR(16) DEFAULT NULL ,
    sort_order INT NOT NULL DEFAULT 0 ,
    deleted TINYINT NOT NULL DEFAULT 0,
    created_at DATETIME DEFAULT NULL,
    updated_at DATETIME DEFAULT NULL
);
CREATE UNIQUE INDEX IF NOT EXISTS uk_code ON t_subject (code);

DROP TABLE IF EXISTS t_test_paper;
-- =============================================
-- 学霸到家 - 试卷表 DDL（洋葱学院学习资源体系）
-- =============================================
CREATE TABLE IF NOT EXISTS t_test_paper (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    grade VARCHAR(16) NOT NULL ,
    subject VARCHAR(32) NOT NULL ,
    textbook_version VARCHAR(32) NOT NULL DEFAULT 'RENJIAO' ,
    title VARCHAR(200) NOT NULL ,
    paper_type VARCHAR(32) NOT NULL ,
    description TEXT DEFAULT NULL ,
    total_score INT NOT NULL DEFAULT 100 ,
    duration_minutes INT NOT NULL DEFAULT 60 ,
    questions_json TEXT NOT NULL ,
    cover_url VARCHAR(512) DEFAULT NULL ,
    vip_only TINYINT NOT NULL DEFAULT 0 ,
    use_count INT NOT NULL DEFAULT 0 ,
    sort_order INT NOT NULL DEFAULT 0 ,
    deleted TINYINT NOT NULL DEFAULT 0,
    created_at DATETIME DEFAULT NULL,
    updated_at DATETIME DEFAULT NULL
);
CREATE INDEX IF NOT EXISTS idx_grade_subject ON t_test_paper (grade, subject);
CREATE INDEX IF NOT EXISTS idx_paper_type ON t_test_paper (paper_type);
CREATE INDEX IF NOT EXISTS idx_textbook_version ON t_test_paper (textbook_version);
CREATE INDEX IF NOT EXISTS idx_vip_only ON t_test_paper (vip_only);

DROP TABLE IF EXISTS t_tutor_profile;
CREATE TABLE IF NOT EXISTS t_tutor_profile (
    id BIGINT NOT NULL AUTO_INCREMENT ,
    user_id BIGINT NOT NULL ,
    university VARCHAR(128) NOT NULL ,
    major VARCHAR(64) DEFAULT NULL ,
    grade VARCHAR(16) DEFAULT NULL ,
    subjects VARCHAR(512) NOT NULL ,
    hourly_rate INT NOT NULL ,
    bio VARCHAR(1024) DEFAULT NULL ,
    experience VARCHAR(1024) DEFAULT NULL ,
    districts VARCHAR(512) DEFAULT NULL ,
    available_times TEXT DEFAULT NULL ,
    rating DECIMAL(3,2) NOT NULL DEFAULT 0.00 ,
    review_count INT NOT NULL DEFAULT 0 ,
    order_count INT NOT NULL DEFAULT 0 ,
    active TINYINT NOT NULL DEFAULT 1 ,
    deleted INT NOT NULL DEFAULT 0 ,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ,
    PRIMARY KEY (id)
);
CREATE UNIQUE INDEX IF NOT EXISTS uk_user_id ON t_tutor_profile (user_id);
CREATE INDEX IF NOT EXISTS idx_active_rating ON t_tutor_profile (active, rating);
CREATE INDEX IF NOT EXISTS idx_subjects ON t_tutor_profile (subjects);

DROP TABLE IF EXISTS t_user;
-- =============================================
-- 学霸到家 - 用户表 DDL
-- =============================================



-- 用户表
CREATE TABLE IF NOT EXISTS t_user (
    id           BIGINT       NOT NULL AUTO_INCREMENT ,
    openid       VARCHAR(64)  NOT NULL ,
    unionid      VARCHAR(64)  DEFAULT NULL ,
    phone        VARCHAR(20)  DEFAULT NULL ,
    nickname     VARCHAR(64)  DEFAULT '微信用户' ,
    avatar       VARCHAR(512) DEFAULT NULL ,
    identity_type VARCHAR(16) DEFAULT NULL ,
    verify_status VARCHAR(16) DEFAULT 'NONE' ,
    real_name    VARCHAR(32)  DEFAULT NULL ,
    deleted      TINYINT   DEFAULT 0 ,
    created_at   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ,
    updated_at   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ,
    PRIMARY KEY (id)
);
CREATE UNIQUE INDEX IF NOT EXISTS uk_openid ON t_user (openid);
CREATE INDEX IF NOT EXISTS idx_phone ON t_user (phone);
CREATE INDEX IF NOT EXISTS idx_identity_type ON t_user (identity_type);
CREATE INDEX IF NOT EXISTS idx_verify_status ON t_user (verify_status);

DROP TABLE IF EXISTS t_verify_record;
-- 认证记录表
CREATE TABLE IF NOT EXISTS t_verify_record (
    id BIGINT NOT NULL AUTO_INCREMENT ,
    user_id BIGINT NOT NULL ,
    verify_type VARCHAR(32) NOT NULL ,
    real_name VARCHAR(64) NOT NULL ,
    id_card_no VARCHAR(128) NOT NULL ,
    student_card_image VARCHAR(512) DEFAULT NULL ,
    verify_status VARCHAR(16) NOT NULL DEFAULT 'NONE' ,
    remark VARCHAR(256) DEFAULT NULL ,
    deleted INT NOT NULL DEFAULT 0 ,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ,
    PRIMARY KEY (id)
);
CREATE INDEX IF NOT EXISTS idx_user_id ON t_verify_record (user_id);
CREATE INDEX IF NOT EXISTS idx_verify_status ON t_verify_record (verify_status);
CREATE INDEX IF NOT EXISTS idx_user_status ON t_verify_record (user_id, verify_status);

DROP TABLE IF EXISTS t_wrong_question_bank;
-- =============================================
-- 学霸到家 - 错题库表 DDL
-- =============================================
CREATE TABLE IF NOT EXISTS t_wrong_question_bank (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    child_id BIGINT NOT NULL ,
    question_id BIGINT DEFAULT NULL ,
    subject VARCHAR(32) NOT NULL ,
    knowledge_point VARCHAR(128) DEFAULT NULL ,
    question_content TEXT DEFAULT NULL ,
    wrong_answer TEXT DEFAULT NULL ,
    correct_answer TEXT DEFAULT NULL ,
    wrong_count INT NOT NULL DEFAULT 1 ,
    mastery_level VARCHAR(32) NOT NULL DEFAULT 'NOT_MASTERED' ,
    in_review_plan TINYINT NOT NULL DEFAULT 0 ,
    last_wrong_at DATETIME DEFAULT NULL ,
    mastered_at DATETIME DEFAULT NULL ,
    deleted TINYINT NOT NULL DEFAULT 0,
    created_at DATETIME DEFAULT NULL,
    updated_at DATETIME DEFAULT NULL
);
CREATE INDEX IF NOT EXISTS idx_child_id ON t_wrong_question_bank (child_id);
CREATE INDEX IF NOT EXISTS idx_subject ON t_wrong_question_bank (subject);
CREATE INDEX IF NOT EXISTS idx_knowledge_point ON t_wrong_question_bank (knowledge_point);
CREATE INDEX IF NOT EXISTS idx_mastery_level ON t_wrong_question_bank (mastery_level);
CREATE INDEX IF NOT EXISTS idx_in_review_plan ON t_wrong_question_bank (in_review_plan);
CREATE INDEX IF NOT EXISTS idx_child_mastery ON t_wrong_question_bank (child_id, mastery_level);

-- === ALTER statements (run after all tables created) ===
ALTER TABLE t_tutor_profile ADD COLUMN pinned_until DATETIME DEFAULT NULL;
ALTER TABLE t_verify_record ADD COLUMN expedited INT NOT NULL DEFAULT 0;
