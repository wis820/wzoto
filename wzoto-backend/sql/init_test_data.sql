-- =============================================
-- 学霸到家 - 初始化测试数据
-- 预置账号：管理员、测试家长、测试大学生、免费家长（通过dev-login使用nickname登录）
-- 执行前提：t_user, t_membership, t_ai_report 表已存在
-- =============================================

-- 1. 插入测试用户（密码统一 123456，BCrypt加密）
-- 注意：当前用户表无password字段，通过dev-login使用nickname登录
INSERT IGNORE INTO t_user (id, openid, phone, nickname, avatar, identity_type, verify_status, real_name, deleted, created_at, updated_at)
VALUES
(1, 'dev_admin_openid', '13800000001', '管理员', '', NULL, 'APPROVED', '系统管理员', 0, NOW(), NOW()),
(2, 'dev_parent_openid', '13800000002', '测试家长', '', 'PARENT', 'APPROVED', '张建国', 0, NOW(), NOW()),
(3, 'dev_student_openid', '13800000003', '测试大学生', '', 'STUDENT', 'APPROVED', '李明华', 0, NOW(), NOW()),
(4, 'dev_parent_free_openid', '13800000004', '免费家长', '', 'PARENT', 'APPROVED', '王测试', 0, NOW(), NOW());

-- 2. 插入测试会员数据
-- 家长2：年卡会员（有效期至2027-06-30）
INSERT IGNORE INTO t_membership (id, user_id, member_type, status, start_time, expire_time, auto_renew, deleted, created_at, updated_at)
VALUES
(1, 2, 'PARENT_YEAR', 'ACTIVE', '2025-06-01 00:00:00', '2027-06-30 23:59:59', 1, 0, NOW(), NOW());

-- 3. 插入测试AI报告数据（家长2的会员免费报告）
INSERT IGNORE INTO t_ai_report (id, user_id, child_name, weak_subjects, recent_scores, weak_point_desc, report_content, preview_content, report_type, is_member_report, price, payment_status, deleted, created_at, updated_at)
VALUES
(1, 2, '小明', '初中数学,初中英语', '数学72,英语68', '二元一次方程组解不对，英语阅读理解失分严重', '{"weaknessAnalysis":[{"subject":"初中数学","points":["二元一次方程组解法","一元二次方程应用题","几何证明辅助线添加"],"severity":"中等"},{"subject":"初中英语","points":["阅读理解主旨题","完形填空上下文推理","语法填空时态判断"],"severity":"较重"}],"suggestion":{"direction":"重点突破数学方程与英语阅读，辅以基础语法巩固","frequency":"建议每周2次数学+2次英语，每次1.5小时","duration":"预计2-3个月可见明显提升"},"recommendedSubjects":["数学","英语"]}', '您的孩子在初中数学和英语方面存在薄弱环节，建议重点加强方程解法和阅读理解训练。完整报告包含详细知识图谱和个性化辅导方案。', 'FULL', 1, 0.00, 'FREE', 0, NOW(), NOW());
