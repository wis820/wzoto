-- =============================================
-- 学霸到家 - 试卷种子数据
-- 每年级2-3套（单元测试+期中+期末）
-- =============================================

-- 一年级
INSERT INTO t_test_paper (grade, subject, textbook_version, title, paper_type, total_score, duration_minutes, questions_json, vip_only, sort_order, deleted, created_at, updated_at) VALUES
('GRADE_1', 'MATH', 'RENJIAO', '一年级数学上册单元测试（一）', 'UNIT', 100, 40, '{"questionIds":[1,2,3,4,5,6],"scores":[20,20,15,15,15,15]}', 0, 1, 0, NOW(), NOW()),
('GRADE_1', 'MATH', 'RENJIAO', '一年级数学期中考试卷', 'MID_TERM', 100, 60, '{"questionIds":[1,2,3,4,5,6],"scores":[20,20,15,15,15,15]}', 1, 2, 0, NOW(), NOW()),
('GRADE_1', 'CHINESE', 'RENJIAO', '一年级语文上册单元测试（一）', 'UNIT', 100, 40, '{"questionIds":[1,2,3,4,5,6],"scores":[20,20,15,15,15,15]}', 0, 1, 0, NOW(), NOW()),
('GRADE_1', 'CHINESE', 'RENJIAO', '一年级语文期中考试卷', 'MID_TERM', 100, 60, '{"questionIds":[1,2,3,4,5,6],"scores":[20,20,15,15,15,15]}', 1, 2, 0, NOW(), NOW()),
('GRADE_1', 'ENGLISH', 'RENJIAO', '一年级英语上册单元测试（一）', 'UNIT', 100, 30, '{"questionIds":[1,2,3,4],"scores":[25,25,25,25]}', 0, 1, 0, NOW(), NOW()),
('GRADE_1', 'ENGLISH', 'RENJIAO', '一年级英语期中考试卷', 'MID_TERM', 100, 40, '{"questionIds":[1,2,3,4],"scores":[25,25,25,25]}', 1, 2, 0, NOW(), NOW());

-- 二年级
INSERT INTO t_test_paper (grade, subject, textbook_version, title, paper_type, total_score, duration_minutes, questions_json, vip_only, sort_order, deleted, created_at, updated_at) VALUES
('GRADE_2', 'MATH', 'RENJIAO', '二年级数学上册单元测试（一）', 'UNIT', 100, 45, '{"questionIds":[1,2,3,4,5,6],"scores":[20,20,15,15,15,15]}', 0, 1, 0, NOW(), NOW()),
('GRADE_2', 'MATH', 'RENJIAO', '二年级数学期中考试卷', 'MID_TERM', 100, 60, '{"questionIds":[1,2,3,4,5,6],"scores":[20,20,15,15,15,15]}', 1, 2, 0, NOW(), NOW()),
('GRADE_2', 'CHINESE', 'RENJIAO', '二年级语文上册单元测试（一）', 'UNIT', 100, 45, '{"questionIds":[1,2,3,4],"scores":[25,25,25,25]}', 0, 1, 0, NOW(), NOW()),
('GRADE_2', 'CHINESE', 'RENJIAO', '二年级语文期中考试卷', 'MID_TERM', 100, 60, '{"questionIds":[1,2,3,4],"scores":[25,25,25,25]}', 1, 2, 0, NOW(), NOW()),
('GRADE_2', 'ENGLISH', 'RENJIAO', '二年级英语单元测试（一）', 'UNIT', 100, 30, '{"questionIds":[1,2,3],"scores":[35,35,30]}', 0, 1, 0, NOW(), NOW());

-- 三年级
INSERT INTO t_test_paper (grade, subject, textbook_version, title, paper_type, total_score, duration_minutes, questions_json, vip_only, sort_order, deleted, created_at, updated_at) VALUES
('GRADE_3', 'MATH', 'RENJIAO', '三年级数学上册单元测试（一）', 'UNIT', 100, 50, '{"questionIds":[1,2,3,4,5,6],"scores":[15,20,15,15,20,15]}', 0, 1, 0, NOW(), NOW()),
('GRADE_3', 'MATH', 'RENJIAO', '三年级数学期中考试卷', 'MID_TERM', 100, 60, '{"questionIds":[1,2,3,4,5,6],"scores":[15,20,15,15,20,15]}', 1, 2, 0, NOW(), NOW()),
('GRADE_3', 'MATH', 'RENJIAO', '三年级数学期末考试卷', 'FINAL', 100, 70, '{"questionIds":[1,2,3,4,5,6],"scores":[15,20,15,15,20,15]}', 1, 3, 0, NOW(), NOW()),
('GRADE_3', 'CHINESE', 'RENJIAO', '三年级语文上册单元测试（一）', 'UNIT', 100, 50, '{"questionIds":[1,2,3,4],"scores":[25,25,25,25]}', 0, 1, 0, NOW(), NOW()),
('GRADE_3', 'CHINESE', 'RENJIAO', '三年级语文期中考试卷', 'MID_TERM', 100, 60, '{"questionIds":[1,2,3,4],"scores":[25,25,25,25]}', 1, 2, 0, NOW(), NOW()),
('GRADE_3', 'ENGLISH', 'RENJIAO', '三年级英语单元测试（一）', 'UNIT', 100, 40, '{"questionIds":[1,2,3],"scores":[35,35,30]}', 0, 1, 0, NOW(), NOW());

-- 四年级
INSERT INTO t_test_paper (grade, subject, textbook_version, title, paper_type, total_score, duration_minutes, questions_json, vip_only, sort_order, deleted, created_at, updated_at) VALUES
('GRADE_4', 'MATH', 'RENJIAO', '四年级数学上册单元测试（一）', 'UNIT', 100, 50, '{"questionIds":[1,2,3,4,5,6],"scores":[15,20,15,15,20,15]}', 0, 1, 0, NOW(), NOW()),
('GRADE_4', 'MATH', 'RENJIAO', '四年级数学期中考试卷', 'MID_TERM', 100, 70, '{"questionIds":[1,2,3,4,5,6],"scores":[15,20,15,15,20,15]}', 1, 2, 0, NOW(), NOW()),
('GRADE_4', 'CHINESE', 'RENJIAO', '四年级语文上册单元测试（一）', 'UNIT', 100, 50, '{"questionIds":[1,2,3,4],"scores":[25,25,25,25]}', 0, 1, 0, NOW(), NOW()),
('GRADE_4', 'CHINESE', 'RENJIAO', '四年级语文期中考试卷', 'MID_TERM', 100, 70, '{"questionIds":[1,2,3,4],"scores":[25,25,25,25]}', 1, 2, 0, NOW(), NOW()),
('GRADE_4', 'ENGLISH', 'RENJIAO', '四年级英语单元测试（一）', 'UNIT', 100, 40, '{"questionIds":[1,2,3],"scores":[35,35,30]}', 0, 1, 0, NOW(), NOW());

-- 五年级
INSERT INTO t_test_paper (grade, subject, textbook_version, title, paper_type, total_score, duration_minutes, questions_json, vip_only, sort_order, deleted, created_at, updated_at) VALUES
('GRADE_5', 'MATH', 'RENJIAO', '五年级数学上册单元测试（一）', 'UNIT', 100, 50, '{"questionIds":[1,2,3,4,5,6],"scores":[15,20,15,15,20,15]}', 0, 1, 0, NOW(), NOW()),
('GRADE_5', 'MATH', 'RENJIAO', '五年级数学期中考试卷', 'MID_TERM', 100, 70, '{"questionIds":[1,2,3,4,5,6],"scores":[15,20,15,15,20,15]}', 1, 2, 0, NOW(), NOW()),
('GRADE_5', 'MATH', 'RENJIAO', '五年级数学期末考试卷', 'FINAL', 100, 80, '{"questionIds":[1,2,3,4,5,6],"scores":[15,20,15,15,20,15]}', 1, 3, 0, NOW(), NOW()),
('GRADE_5', 'CHINESE', 'RENJIAO', '五年级语文上册单元测试（一）', 'UNIT', 100, 50, '{"questionIds":[1,2,3,4],"scores":[25,25,25,25]}', 0, 1, 0, NOW(), NOW()),
('GRADE_5', 'CHINESE', 'RENJIAO', '五年级语文期中考试卷', 'MID_TERM', 100, 70, '{"questionIds":[1,2,3,4],"scores":[25,25,25,25]}', 1, 2, 0, NOW(), NOW()),
('GRADE_5', 'ENGLISH', 'RENJIAO', '五年级英语单元测试（一）', 'UNIT', 100, 45, '{"questionIds":[1,2,3],"scores":[35,35,30]}', 0, 1, 0, NOW(), NOW());

-- 六年级
INSERT INTO t_test_paper (grade, subject, textbook_version, title, paper_type, total_score, duration_minutes, questions_json, vip_only, sort_order, deleted, created_at, updated_at) VALUES
('GRADE_6', 'MATH', 'RENJIAO', '六年级数学上册单元测试（一）', 'UNIT', 100, 50, '{"questionIds":[1,2,3,4,5,6,7],"scores":[15,15,15,15,15,15,10]}', 0, 1, 0, NOW(), NOW()),
('GRADE_6', 'MATH', 'RENJIAO', '六年级数学期中考试卷', 'MID_TERM', 100, 80, '{"questionIds":[1,2,3,4,5,6,7],"scores":[15,15,15,15,15,15,10]}', 1, 2, 0, NOW(), NOW()),
('GRADE_6', 'MATH', 'RENJIAO', '小升初数学模拟试卷', 'MOCK', 120, 90, '{"questionIds":[1,2,3,4,5,6,7],"scores":[20,20,15,15,20,15,15]}', 1, 3, 0, NOW(), NOW()),
('GRADE_6', 'CHINESE', 'RENJIAO', '六年级语文上册单元测试（一）', 'UNIT', 100, 50, '{"questionIds":[1,2,3,4],"scores":[25,25,25,25]}', 0, 1, 0, NOW(), NOW()),
('GRADE_6', 'CHINESE', 'RENJIAO', '六年级语文期中考试卷', 'MID_TERM', 100, 80, '{"questionIds":[1,2,3,4],"scores":[25,25,25,25]}', 1, 2, 0, NOW(), NOW()),
('GRADE_6', 'CHINESE', 'RENJIAO', '小升初语文模拟试卷', 'MOCK', 100, 90, '{"questionIds":[1,2,3,4],"scores":[25,25,25,25]}', 1, 3, 0, NOW(), NOW()),
('GRADE_6', 'ENGLISH', 'RENJIAO', '六年级英语单元测试（一）', 'UNIT', 100, 45, '{"questionIds":[1,2,3,4],"scores":[25,25,25,25]}', 0, 1, 0, NOW(), NOW()),
('GRADE_6', 'ENGLISH', 'RENJIAO', '小升初英语模拟试卷', 'MOCK', 100, 60, '{"questionIds":[1,2,3,4],"scores":[25,25,25,25]}', 1, 2, 0, NOW(), NOW());

-- =============================================
-- 音频素材种子数据
-- 英语跟读 + 语文朗读
-- =============================================

-- 英语跟读素材
INSERT INTO t_audio_material (grade, subject, textbook_version, title, audio_type, audio_url, duration_seconds, text_content, reference_text, vip_only, sort_order, deleted, created_at, updated_at) VALUES
('GRADE_1', 'ENGLISH', 'RENJIAO', '跟读：Hello', 'FOLLOW_READ', '/audio/g1-en-hello.mp3', 3, 'Hello', 'Hello', 0, 1, 0, NOW(), NOW()),
('GRADE_1', 'ENGLISH', 'RENJIAO', '跟读：Good morning', 'FOLLOW_READ', '/audio/g1-en-goodmorning.mp3', 4, 'Good morning', 'Good morning', 0, 2, 0, NOW(), NOW()),
('GRADE_1', 'ENGLISH', 'RENJIAO', '跟读：Thank you', 'FOLLOW_READ', '/audio/g1-en-thankyou.mp3', 3, 'Thank you', 'Thank you', 1, 3, 0, NOW(), NOW()),
('GRADE_2', 'ENGLISH', 'RENJIAO', '跟读：I love my family', 'FOLLOW_READ', '/audio/g2-en-family.mp3', 5, 'I love my family', 'I love my family', 0, 1, 0, NOW(), NOW()),
('GRADE_2', 'ENGLISH', 'RENJIAO', '跟读：How are you', 'FOLLOW_READ', '/audio/g2-en-howareyou.mp3', 4, 'How are you?', 'How are you?', 0, 2, 0, NOW(), NOW()),
('GRADE_2', 'ENGLISH', 'RENJIAO', '跟读：I like apples', 'FOLLOW_READ', '/audio/g2-en-apples.mp3', 4, 'I like apples', 'I like apples', 1, 3, 0, NOW(), NOW()),
('GRADE_3', 'ENGLISH', 'RENJIAO', '跟读：Nice to meet you', 'FOLLOW_READ', '/audio/g3-en-nicetomeetyou.mp3', 5, 'Nice to meet you', 'Nice to meet you', 0, 1, 0, NOW(), NOW()),
('GRADE_3', 'ENGLISH', 'RENJIAO', '跟读：What is your name', 'FOLLOW_READ', '/audio/g3-en-whatisyourname.mp3', 5, 'What is your name?', 'What is your name?', 1, 2, 0, NOW(), NOW()),
('GRADE_4', 'ENGLISH', 'RENJIAO', '跟读：Let me clean the window', 'FOLLOW_READ', '/audio/g4-en-cleanwindow.mp3', 6, 'Let me clean the window', 'Let me clean the window', 1, 1, 0, NOW(), NOW()),
('GRADE_4', 'ENGLISH', 'RENJIAO', '跟读：I have a new schoolbag', 'FOLLOW_READ', '/audio/g4-en-schoolbag.mp3', 5, 'I have a new schoolbag', 'I have a new schoolbag', 0, 2, 0, NOW(), NOW()),
('GRADE_5', 'ENGLISH', 'RENJIAO', '跟读：What can you do', 'FOLLOW_READ', '/audio/g5-en-whatcanyoudo.mp3', 5, 'What can you do?', 'What can you do?', 1, 1, 0, NOW(), NOW()),
('GRADE_5', 'ENGLISH', 'RENJIAO', '跟读：I can cook', 'FOLLOW_READ', '/audio/g5-en-icancook.mp3', 4, 'I can cook', 'I can cook', 1, 2, 0, NOW(), NOW()),
('GRADE_6', 'ENGLISH', 'RENJIAO', '跟读：How can I get to the museum', 'FOLLOW_READ', '/audio/g6-en-museum.mp3', 7, 'How can I get to the museum?', 'How can I get to the museum?', 1, 1, 0, NOW(), NOW()),
('GRADE_6', 'ENGLISH', 'RENJIAO', '跟读：Turn left at the crossing', 'FOLLOW_READ', '/audio/g6-en-turnleft.mp3', 5, 'Turn left at the crossing', 'Turn left at the crossing', 0, 2, 0, NOW(), NOW());

-- 语文朗读素材
INSERT INTO t_audio_material (grade, subject, textbook_version, title, audio_type, audio_url, duration_seconds, text_content, reference_text, vip_only, sort_order, deleted, created_at, updated_at) VALUES
('GRADE_1', 'CHINESE', 'RENJIAO', '朗读：秋天', 'READ', '/audio/g1-cn-autumn.mp3', 30, '天气凉了，树叶黄了，一片片叶子从树上落下来。', '天气凉了，树叶黄了，一片片叶子从树上落下来。', 0, 1, 0, NOW(), NOW()),
('GRADE_1', 'CHINESE', 'RENJIAO', '朗读：小小的船', 'READ', '/audio/g1-cn-boat.mp3', 25, '弯弯的月儿小小的船，小小的船儿两头尖。', '弯弯的月儿小小的船，小小的船儿两头尖。', 0, 2, 0, NOW(), NOW()),
('GRADE_2', 'CHINESE', 'RENJIAO', '朗读：小蝌蚪找妈妈', 'READ', '/audio/g2-cn-tadpole.mp3', 60, '池塘里有一群小蝌蚪，大大的脑袋，黑灰色的身子...', '池塘里有一群小蝌蚪，大大的脑袋，黑灰色的身子...', 1, 1, 0, NOW(), NOW()),
('GRADE_2', 'CHINESE', 'RENJIAO', '朗读：场景歌', 'READ', '/audio/g2-cn-scene.mp3', 30, '一只海鸥，一片沙滩。一艘军舰，一条帆船。', '一只海鸥，一片沙滩。一艘军舰，一条帆船。', 0, 2, 0, NOW(), NOW()),
('GRADE_3', 'CHINESE', 'RENJIAO', '朗读：望庐山瀑布', 'READ', '/audio/g3-cn-waterfall.mp3', 20, '日照香炉生紫烟，遥看瀑布挂前川。飞流直下三千尺，疑是银河落九天。', '日照香炉生紫烟，遥看瀑布挂前川。飞流直下三千尺，疑是银河落九天。', 0, 1, 0, NOW(), NOW()),
('GRADE_4', 'CHINESE', 'RENJIAO', '朗读：观潮', 'READ', '/audio/g4-cn-tidal.mp3', 90, '钱塘江大潮，自古以来被称为天下奇观...', '钱塘江大潮，自古以来被称为天下奇观...', 1, 1, 0, NOW(), NOW()),
('GRADE_5', 'CHINESE', 'RENJIAO', '朗读：白鹭', 'READ', '/audio/g5-cn-egret.mp3', 60, '白鹭是一首精巧的诗...', '白鹭是一首精巧的诗...', 1, 1, 0, NOW(), NOW()),
('GRADE_6', 'CHINESE', 'RENJIAO', '朗读：草原', 'READ', '/audio/g6-cn-grassland.mp3', 90, '这次，我看到了草原。那里的天比别处的更可爱...', '这次，我看到了草原。那里的天比别处的更可爱...', 1, 1, 0, NOW(), NOW());
