-- =============================================
-- 学霸到家 - 学习资源体系种子数据
-- 适用：1-6年级 × 3学科 × 人教版
-- 执行前提：所有DDL表已创建
-- =============================================

-- ========== 1. 学科基础数据 ==========
INSERT INTO t_subject (code, name, icon, color, sort_order, deleted, created_at, updated_at) VALUES
('MATH', '数学', '/icons/math.svg', '#4F6EF7', 1, 0, NOW(), NOW()),
('CHINESE', '语文', '/icons/chinese.svg', '#F76E4F', 2, 0, NOW(), NOW()),
('ENGLISH', '英语', '/icons/english.svg', '#4FC76E', 3, 0, NOW(), NOW());

-- ========== 2. 知识点树（人教版，按年级+学科） ==========
-- 一年级 数学
INSERT INTO t_knowledge_point (id, parent_id, grade, subject, textbook_version, name, description, depth, sort_order, deleted, created_at, updated_at) VALUES
(1001, NULL, 'GRADE_1', 'MATH', 'RENJIAO', '一年级上册', NULL, 1, 1, 0, NOW(), NOW()),
(1002, 1001, 'GRADE_1', 'MATH', 'RENJIAO', '准备课', '数一数、比一比', 2, 1, 0, NOW(), NOW()),
(1003, 1001, 'GRADE_1', 'MATH', 'RENJIAO', '位置', '上下前后左右', 2, 2, 0, NOW(), NOW()),
(1004, 1001, 'GRADE_1', 'MATH', 'RENJIAO', '1-5的认识和加减法', NULL, 2, 3, 0, NOW(), NOW()),
(1005, 1001, 'GRADE_1', 'MATH', 'RENJIAO', '认识图形（一）', '长方体、正方体、圆柱、球', 2, 4, 0, NOW(), NOW()),
(1006, 1001, 'GRADE_1', 'MATH', 'RENJIAO', '6-10的认识和加减法', NULL, 2, 5, 0, NOW(), NOW()),
(1007, 1001, 'GRADE_1', 'MATH', 'RENJIAO', '11-20各数的认识', NULL, 2, 6, 0, NOW(), NOW()),
(1008, 1001, 'GRADE_1', 'MATH', 'RENJIAO', '认识钟表', '整时、半时', 2, 7, 0, NOW(), NOW()),
(1009, 1001, 'GRADE_1', 'MATH', 'RENJIAO', '20以内的进位加法', NULL, 2, 8, 0, NOW(), NOW()),
(1010, NULL, 'GRADE_1', 'MATH', 'RENJIAO', '一年级下册', NULL, 1, 2, 0, NOW(), NOW()),
(1011, 1010, 'GRADE_1', 'MATH', 'RENJIAO', '认识图形（二）', '平面图形', 2, 1, 0, NOW(), NOW()),
(1012, 1010, 'GRADE_1', 'MATH', 'RENJIAO', '20以内的退位减法', NULL, 2, 2, 0, NOW(), NOW()),
(1013, 1010, 'GRADE_1', 'MATH', 'RENJIAO', '分类与整理', NULL, 2, 3, 0, NOW(), NOW()),
(1014, 1010, 'GRADE_1', 'MATH', 'RENJIAO', '100以内数的认识', NULL, 2, 4, 0, NOW(), NOW()),
(1015, 1010, 'GRADE_1', 'MATH', 'RENJIAO', '认识人民币', '元角分', 2, 5, 0, NOW(), NOW()),
(1016, 1010, 'GRADE_1', 'MATH', 'RENJIAO', '100以内的加法和减法（一）', NULL, 2, 6, 0, NOW(), NOW());

-- 一年级 语文
INSERT INTO t_knowledge_point (id, parent_id, grade, subject, textbook_version, name, description, depth, sort_order, deleted, created_at, updated_at) VALUES
(1101, NULL, 'GRADE_1', 'CHINESE', 'RENJIAO', '一年级上册', NULL, 1, 1, 0, NOW(), NOW()),
(1102, 1101, 'GRADE_1', 'CHINESE', 'RENJIAO', '汉语拼音', '声母韵母整体认读', 2, 1, 0, NOW(), NOW()),
(1103, 1101, 'GRADE_1', 'CHINESE', 'RENJIAO', '识字（一）', '天地人、金木水火土', 2, 2, 0, NOW(), NOW()),
(1104, 1101, 'GRADE_1', 'CHINESE', 'RENJIAO', '课文（一）', '秋天、小小的船', 2, 3, 0, NOW(), NOW()),
(1105, 1101, 'GRADE_1', 'CHINESE', 'RENJIAO', '识字（二）', '画、大小多少', 2, 4, 0, NOW(), NOW()),
(1106, 1101, 'GRADE_1', 'CHINESE', 'RENJIAO', '课文（二）', '影子、比尾巴', 2, 5, 0, NOW(), NOW()),
(1107, NULL, 'GRADE_1', 'CHINESE', 'RENJIAO', '一年级下册', NULL, 1, 2, 0, NOW(), NOW()),
(1108, 1107, 'GRADE_1', 'CHINESE', 'RENJIAO', '识字', '春夏秋冬、姓氏歌', 2, 1, 0, NOW(), NOW()),
(1109, 1107, 'GRADE_1', 'CHINESE', 'RENJIAO', '课文（一）', '吃水不忘挖井人', 2, 2, 0, NOW(), NOW()),
(1110, 1107, 'GRADE_1', 'CHINESE', 'RENJIAO', '课文（二）', '古诗二首', 2, 3, 0, NOW(), NOW());

-- 一年级 英语
INSERT INTO t_knowledge_point (id, parent_id, grade, subject, textbook_version, name, description, depth, sort_order, deleted, created_at, updated_at) VALUES
(1201, NULL, 'GRADE_1', 'ENGLISH', 'RENJIAO', '一年级起点上册', NULL, 1, 1, 0, NOW(), NOW()),
(1202, 1201, 'GRADE_1', 'ENGLISH', 'RENJIAO', 'Unit 1 School', '学校相关词汇', 2, 1, 0, NOW(), NOW()),
(1203, 1201, 'GRADE_1', 'ENGLISH', 'RENJIAO', 'Unit 2 Face', '五官词汇', 2, 2, 0, NOW(), NOW()),
(1204, 1201, 'GRADE_1', 'ENGLISH', 'RENJIAO', 'Unit 3 Animals', '动物词汇', 2, 3, 0, NOW(), NOW()),
(1205, 1201, 'GRADE_1', 'ENGLISH', 'RENJIAO', 'Unit 4 Numbers', '数字1-10', 2, 4, 0, NOW(), NOW()),
(1206, NULL, 'GRADE_1', 'ENGLISH', 'RENJIAO', '一年级起点下册', NULL, 1, 2, 0, NOW(), NOW()),
(1207, 1206, 'GRADE_1', 'ENGLISH', 'RENJIAO', 'Unit 1 Colors', '颜色词汇', 2, 1, 0, NOW(), NOW()),
(1208, 1206, 'GRADE_1', 'ENGLISH', 'RENJIAO', 'Unit 2 Fruits', '水果词汇', 2, 2, 0, NOW(), NOW());

-- 二年级 数学
INSERT INTO t_knowledge_point (id, parent_id, grade, subject, textbook_version, name, description, depth, sort_order, deleted, created_at, updated_at) VALUES
(2001, NULL, 'GRADE_2', 'MATH', 'RENJIAO', '二年级上册', NULL, 1, 1, 0, NOW(), NOW()),
(2002, 2001, 'GRADE_2', 'MATH', 'RENJIAO', '长度单位', '厘米、米', 2, 1, 0, NOW(), NOW()),
(2003, 2001, 'GRADE_2', 'MATH', 'RENJIAO', '100以内的加法和减法（二）', NULL, 2, 2, 0, NOW(), NOW()),
(2004, 2001, 'GRADE_2', 'MATH', 'RENJIAO', '角的初步认识', NULL, 2, 3, 0, NOW(), NOW()),
(2005, 2001, 'GRADE_2', 'MATH', 'RENJIAO', '表内乘法（一）', '2-6的乘法口诀', 2, 4, 0, NOW(), NOW()),
(2006, 2001, 'GRADE_2', 'MATH', 'RENJIAO', '表内乘法（二）', '7-9的乘法口诀', 2, 5, 0, NOW(), NOW()),
(2007, NULL, 'GRADE_2', 'MATH', 'RENJIAO', '二年级下册', NULL, 1, 2, 0, NOW(), NOW()),
(2008, 2007, 'GRADE_2', 'MATH', 'RENJIAO', '数据收集整理', NULL, 2, 1, 0, NOW(), NOW()),
(2009, 2007, 'GRADE_2', 'MATH', 'RENJIAO', '表内除法（一）', NULL, 2, 2, 0, NOW(), NOW()),
(2010, 2007, 'GRADE_2', 'MATH', 'RENJIAO', '表内除法（二）', NULL, 2, 3, 0, NOW(), NOW()),
(2011, 2007, 'GRADE_2', 'MATH', 'RENJIAO', '万以内数的认识', NULL, 2, 4, 0, NOW(), NOW());

-- 二年级 语文
INSERT INTO t_knowledge_point (id, parent_id, grade, subject, textbook_version, name, description, depth, sort_order, deleted, created_at, updated_at) VALUES
(2101, NULL, 'GRADE_2', 'CHINESE', 'RENJIAO', '二年级上册', NULL, 1, 1, 0, NOW(), NOW()),
(2102, 2101, 'GRADE_2', 'CHINESE', 'RENJIAO', '课文（一）', '小蝌蚪找妈妈', 2, 1, 0, NOW(), NOW()),
(2103, 2101, 'GRADE_2', 'CHINESE', 'RENJIAO', '识字', '场景歌、树之歌', 2, 2, 0, NOW(), NOW()),
(2104, 2101, 'GRADE_2', 'CHINESE', 'RENJIAO', '课文（二）', '曹冲称象', 2, 3, 0, NOW(), NOW()),
(2105, NULL, 'GRADE_2', 'CHINESE', 'RENJIAO', '二年级下册', NULL, 1, 2, 0, NOW(), NOW()),
(2106, 2105, 'GRADE_2', 'CHINESE', 'RENJIAO', '课文（一）', '古诗二首', 2, 1, 0, NOW(), NOW()),
(2107, 2105, 'GRADE_2', 'CHINESE', 'RENJIAO', '识字', '神州谣', 2, 2, 0, NOW(), NOW());

-- 二年级 英语
INSERT INTO t_knowledge_point (id, parent_id, grade, subject, textbook_version, name, description, depth, sort_order, deleted, created_at, updated_at) VALUES
(2201, NULL, 'GRADE_2', 'ENGLISH', 'RENJIAO', '二年级起点上册', NULL, 1, 1, 0, NOW(), NOW()),
(2202, 2201, 'GRADE_2', 'ENGLISH', 'RENJIAO', 'Unit 1 Family', '家庭成员词汇', 2, 1, 0, NOW(), NOW()),
(2203, 2201, 'GRADE_2', 'ENGLISH', 'RENJIAO', 'Unit 2 Body', '身体部位词汇', 2, 2, 0, NOW(), NOW()),
(2204, NULL, 'GRADE_2', 'ENGLISH', 'RENJIAO', '二年级起点下册', NULL, 1, 2, 0, NOW(), NOW()),
(2205, 2204, 'GRADE_2', 'ENGLISH', 'RENJIAO', 'Unit 1 Weather', '天气词汇', 2, 1, 0, NOW(), NOW());

-- 三年级 数学
INSERT INTO t_knowledge_point (id, parent_id, grade, subject, textbook_version, name, description, depth, sort_order, deleted, created_at, updated_at) VALUES
(3001, NULL, 'GRADE_3', 'MATH', 'RENJIAO', '三年级上册', NULL, 1, 1, 0, NOW(), NOW()),
(3002, 3001, 'GRADE_3', 'MATH', 'RENJIAO', '时分秒', NULL, 2, 1, 0, NOW(), NOW()),
(3003, 3001, 'GRADE_3', 'MATH', 'RENJIAO', '万以内的加法和减法（一）', NULL, 2, 2, 0, NOW(), NOW()),
(3004, 3001, 'GRADE_3', 'MATH', 'RENJIAO', '测量', '毫米、分米、千米、吨', 2, 3, 0, NOW(), NOW()),
(3005, 3001, 'GRADE_3', 'MATH', 'RENJIAO', '万以内的加法和减法（二）', NULL, 2, 4, 0, NOW(), NOW()),
(3006, 3001, 'GRADE_3', 'MATH', 'RENJIAO', '倍的认识', NULL, 2, 5, 0, NOW(), NOW()),
(3007, 3001, 'GRADE_3', 'MATH', 'RENJIAO', '多位数乘一位数', NULL, 2, 6, 0, NOW(), NOW()),
(3008, 3001, 'GRADE_3', 'MATH', 'RENJIAO', '长方形和正方形', NULL, 2, 7, 0, NOW(), NOW()),
(3009, 3001, 'GRADE_3', 'MATH', 'RENJIAO', '分数的初步认识', NULL, 2, 8, 0, NOW(), NOW()),
(3010, NULL, 'GRADE_3', 'MATH', 'RENJIAO', '三年级下册', NULL, 1, 2, 0, NOW(), NOW()),
(3011, 3010, 'GRADE_3', 'MATH', 'RENJIAO', '位置与方向（一）', NULL, 2, 1, 0, NOW(), NOW()),
(3012, 3010, 'GRADE_3', 'MATH', 'RENJIAO', '除数是一位数的除法', NULL, 2, 2, 0, NOW(), NOW()),
(3013, 3010, 'GRADE_3', 'MATH', 'RENJIAO', '复式统计表', NULL, 2, 3, 0, NOW(), NOW()),
(3014, 3010, 'GRADE_3', 'MATH', 'RENJIAO', '两位数乘两位数', NULL, 2, 4, 0, NOW(), NOW()),
(3015, 3010, 'GRADE_3', 'MATH', 'RENJIAO', '面积', NULL, 2, 5, 0, NOW(), NOW());

-- 三年级 语文
INSERT INTO t_knowledge_point (id, parent_id, grade, subject, textbook_version, name, description, depth, sort_order, deleted, created_at, updated_at) VALUES
(3101, NULL, 'GRADE_3', 'CHINESE', 'RENJIAO', '三年级上册', NULL, 1, 1, 0, NOW(), NOW()),
(3102, 3101, 'GRADE_3', 'CHINESE', 'RENJIAO', '第一单元', '学校生活', 2, 1, 0, NOW(), NOW()),
(3103, 3101, 'GRADE_3', 'CHINESE', 'RENJIAO', '第二单元', '金秋时节', 2, 2, 0, NOW(), NOW()),
(3104, 3101, 'GRADE_3', 'CHINESE', 'RENJIAO', '第三单元', '童话世界', 2, 3, 0, NOW(), NOW()),
(3105, NULL, 'GRADE_3', 'CHINESE', 'RENJIAO', '三年级下册', NULL, 1, 2, 0, NOW(), NOW()),
(3106, 3105, 'GRADE_3', 'CHINESE', 'RENJIAO', '第一单元', '可爱的生灵', 2, 1, 0, NOW(), NOW()),
(3107, 3105, 'GRADE_3', 'CHINESE', 'RENJIAO', '第二单元', '寓言故事', 2, 2, 0, NOW(), NOW());

-- 三年级 英语
INSERT INTO t_knowledge_point (id, parent_id, grade, subject, textbook_version, name, description, depth, sort_order, deleted, created_at, updated_at) VALUES
(3201, NULL, 'GRADE_3', 'ENGLISH', 'RENJIAO', '三年级起点上册', NULL, 1, 1, 0, NOW(), NOW()),
(3202, 3201, 'GRADE_3', 'ENGLISH', 'RENJIAO', 'Unit 1 Hello!', '打招呼、自我介绍', 2, 1, 0, NOW(), NOW()),
(3203, 3201, 'GRADE_3', 'ENGLISH', 'RENJIAO', 'Unit 2 Colours', '颜色', 2, 2, 0, NOW(), NOW()),
(3204, 3201, 'GRADE_3', 'ENGLISH', 'RENJIAO', 'Unit 3 Look at me!', '身体部位', 2, 3, 0, NOW(), NOW()),
(3205, NULL, 'GRADE_3', 'ENGLISH', 'RENJIAO', '三年级起点下册', NULL, 1, 2, 0, NOW(), NOW()),
(3206, 3205, 'GRADE_3', 'ENGLISH', 'RENJIAO', 'Unit 1 Welcome back!', '国家与国籍', 2, 1, 0, NOW(), NOW());

-- 四年级 数学
INSERT INTO t_knowledge_point (id, parent_id, grade, subject, textbook_version, name, description, depth, sort_order, deleted, created_at, updated_at) VALUES
(4001, NULL, 'GRADE_4', 'MATH', 'RENJIAO', '四年级上册', NULL, 1, 1, 0, NOW(), NOW()),
(4002, 4001, 'GRADE_4', 'MATH', 'RENJIAO', '大数的认识', NULL, 2, 1, 0, NOW(), NOW()),
(4003, 4001, 'GRADE_4', 'MATH', 'RENJIAO', '公顷和平方千米', NULL, 2, 2, 0, NOW(), NOW()),
(4004, 4001, 'GRADE_4', 'MATH', 'RENJIAO', '角的度量', NULL, 2, 3, 0, NOW(), NOW()),
(4005, 4001, 'GRADE_4', 'MATH', 'RENJIAO', '三位数乘两位数', NULL, 2, 4, 0, NOW(), NOW()),
(4006, 4001, 'GRADE_4', 'MATH', 'RENJIAO', '平行四边形和梯形', NULL, 2, 5, 0, NOW(), NOW()),
(4007, 4001, 'GRADE_4', 'MATH', 'RENJIAO', '除数是两位数的除法', NULL, 2, 6, 0, NOW(), NOW()),
(4008, 4001, 'GRADE_4', 'MATH', 'RENJIAO', '条形统计图', NULL, 2, 7, 0, NOW(), NOW()),
(4009, NULL, 'GRADE_4', 'MATH', 'RENJIAO', '四年级下册', NULL, 1, 2, 0, NOW(), NOW()),
(4010, 4009, 'GRADE_4', 'MATH', 'RENJIAO', '四则运算', NULL, 2, 1, 0, NOW(), NOW()),
(4011, 4009, 'GRADE_4', 'MATH', 'RENJIAO', '观察物体（二）', NULL, 2, 2, 0, NOW(), NOW()),
(4012, 4009, 'GRADE_4', 'MATH', 'RENJIAO', '运算定律', '加法交换律、结合律', 2, 3, 0, NOW(), NOW()),
(4013, 4009, 'GRADE_4', 'MATH', 'RENJIAO', '小数的意义和性质', NULL, 2, 4, 0, NOW(), NOW()),
(4014, 4009, 'GRADE_4', 'MATH', 'RENJIAO', '三角形', NULL, 2, 5, 0, NOW(), NOW());

-- 四年级 语文
INSERT INTO t_knowledge_point (id, parent_id, grade, subject, textbook_version, name, description, depth, sort_order, deleted, created_at, updated_at) VALUES
(4101, NULL, 'GRADE_4', 'CHINESE', 'RENJIAO', '四年级上册', NULL, 1, 1, 0, NOW(), NOW()),
(4102, 4101, 'GRADE_4', 'CHINESE', 'RENJIAO', '第一单元', '自然之美', 2, 1, 0, NOW(), NOW()),
(4103, 4101, 'GRADE_4', 'CHINESE', 'RENJIAO', '第二单元', '提问策略', 2, 2, 0, NOW(), NOW()),
(4104, NULL, 'GRADE_4', 'CHINESE', 'RENJIAO', '四年级下册', NULL, 1, 2, 0, NOW(), NOW()),
(4105, 4104, 'GRADE_4', 'CHINESE', 'RENJIAO', '第一单元', '田园生活', 2, 1, 0, NOW(), NOW());

-- 四年级 英语
INSERT INTO t_knowledge_point (id, parent_id, grade, subject, textbook_version, name, description, depth, sort_order, deleted, created_at, updated_at) VALUES
(4201, NULL, 'GRADE_4', 'ENGLISH', 'RENJIAO', '四年级上册', NULL, 1, 1, 0, NOW(), NOW()),
(4202, 4201, 'GRADE_4', 'ENGLISH', 'RENJIAO', 'Unit 1 My classroom', '教室物品', 2, 1, 0, NOW(), NOW()),
(4203, 4201, 'GRADE_4', 'ENGLISH', 'RENJIAO', 'Unit 2 My schoolbag', '书包物品', 2, 2, 0, NOW(), NOW()),
(4204, NULL, 'GRADE_4', 'ENGLISH', 'RENJIAO', '四年级下册', NULL, 1, 2, 0, NOW(), NOW()),
(4205, 4204, 'GRADE_4', 'ENGLISH', 'RENJIAO', 'Unit 1 My school', '学校场所', 2, 1, 0, NOW(), NOW());

-- 五年级 数学
INSERT INTO t_knowledge_point (id, parent_id, grade, subject, textbook_version, name, description, depth, sort_order, deleted, created_at, updated_at) VALUES
(5001, NULL, 'GRADE_5', 'MATH', 'RENJIAO', '五年级上册', NULL, 1, 1, 0, NOW(), NOW()),
(5002, 5001, 'GRADE_5', 'MATH', 'RENJIAO', '小数乘法', NULL, 2, 1, 0, NOW(), NOW()),
(5003, 5001, 'GRADE_5', 'MATH', 'RENJIAO', '位置', NULL, 2, 2, 0, NOW(), NOW()),
(5004, 5001, 'GRADE_5', 'MATH', 'RENJIAO', '小数除法', NULL, 2, 3, 0, NOW(), NOW()),
(5005, 5001, 'GRADE_5', 'MATH', 'RENJIAO', '可能性', NULL, 2, 4, 0, NOW(), NOW()),
(5006, 5001, 'GRADE_5', 'MATH', 'RENJIAO', '简易方程', NULL, 2, 5, 0, NOW(), NOW()),
(5007, 5001, 'GRADE_5', 'MATH', 'RENJIAO', '多边形的面积', NULL, 2, 6, 0, NOW(), NOW()),
(5008, NULL, 'GRADE_5', 'MATH', 'RENJIAO', '五年级下册', NULL, 1, 2, 0, NOW(), NOW()),
(5009, 5008, 'GRADE_5', 'MATH', 'RENJIAO', '观察物体（三）', NULL, 2, 1, 0, NOW(), NOW()),
(5010, 5008, 'GRADE_5', 'MATH', 'RENJIAO', '因数与倍数', NULL, 2, 2, 0, NOW(), NOW()),
(5011, 5008, 'GRADE_5', 'MATH', 'RENJIAO', '长方体和正方体', NULL, 2, 3, 0, NOW(), NOW()),
(5012, 5008, 'GRADE_5', 'MATH', 'RENJIAO', '分数的意义和性质', NULL, 2, 4, 0, NOW(), NOW()),
(5013, 5008, 'GRADE_5', 'MATH', 'RENJIAO', '图形的运动（三）', '旋转', 2, 5, 0, NOW(), NOW()),
(5014, 5008, 'GRADE_5', 'MATH', 'RENJIAO', '分数的加法和减法', NULL, 2, 6, 0, NOW(), NOW());

-- 五年级 语文
INSERT INTO t_knowledge_point (id, parent_id, grade, subject, textbook_version, name, description, depth, sort_order, deleted, created_at, updated_at) VALUES
(5101, NULL, 'GRADE_5', 'CHINESE', 'RENJIAO', '五年级上册', NULL, 1, 1, 0, NOW(), NOW()),
(5102, 5101, 'GRADE_5', 'CHINESE', 'RENJIAO', '第一单元', '万物有灵', 2, 1, 0, NOW(), NOW()),
(5103, 5101, 'GRADE_5', 'CHINESE', 'RENJIAO', '第二单元', '阅读策略', 2, 2, 0, NOW(), NOW()),
(5104, NULL, 'GRADE_5', 'CHINESE', 'RENJIAO', '五年级下册', NULL, 1, 2, 0, NOW(), NOW()),
(5105, 5104, 'GRADE_5', 'CHINESE', 'RENJIAO', '第一单元', '童年往事', 2, 1, 0, NOW(), NOW());

-- 五年级 英语
INSERT INTO t_knowledge_point (id, parent_id, grade, subject, textbook_version, name, description, depth, sort_order, deleted, created_at, updated_at) VALUES
(5201, NULL, 'GRADE_5', 'ENGLISH', 'RENJIAO', '五年级上册', NULL, 1, 1, 0, NOW(), NOW()),
(5202, 5201, 'GRADE_5', 'ENGLISH', 'RENJIAO', 'Unit 1 What''s he like?', '描述人物', 2, 1, 0, NOW(), NOW()),
(5203, 5201, 'GRADE_5', 'ENGLISH', 'RENJIAO', 'Unit 2 My days of the week', '星期与课程', 2, 2, 0, NOW(), NOW()),
(5204, NULL, 'GRADE_5', 'ENGLISH', 'RENJIAO', '五年级下册', NULL, 1, 2, 0, NOW(), NOW()),
(5205, 5204, 'GRADE_5', 'ENGLISH', 'RENJIAO', 'Unit 1 My day', '日常作息', 2, 1, 0, NOW(), NOW());

-- 六年级 数学
INSERT INTO t_knowledge_point (id, parent_id, grade, subject, textbook_version, name, description, depth, sort_order, deleted, created_at, updated_at) VALUES
(6001, NULL, 'GRADE_6', 'MATH', 'RENJIAO', '六年级上册', NULL, 1, 1, 0, NOW(), NOW()),
(6002, 6001, 'GRADE_6', 'MATH', 'RENJIAO', '分数乘法', NULL, 2, 1, 0, NOW(), NOW()),
(6003, 6001, 'GRADE_6', 'MATH', 'RENJIAO', '位置与方向（二）', NULL, 2, 2, 0, NOW(), NOW()),
(6004, 6001, 'GRADE_6', 'MATH', 'RENJIAO', '分数除法', NULL, 2, 3, 0, NOW(), NOW()),
(6005, 6001, 'GRADE_6', 'MATH', 'RENJIAO', '比', NULL, 2, 4, 0, NOW(), NOW()),
(6006, 6001, 'GRADE_6', 'MATH', 'RENJIAO', '圆', NULL, 2, 5, 0, NOW(), NOW()),
(6007, 6001, 'GRADE_6', 'MATH', 'RENJIAO', '百分数（一）', NULL, 2, 6, 0, NOW(), NOW()),
(6008, 6001, 'GRADE_6', 'MATH', 'RENJIAO', '扇形统计图', NULL, 2, 7, 0, NOW(), NOW()),
(6009, NULL, 'GRADE_6', 'MATH', 'RENJIAO', '六年级下册', NULL, 1, 2, 0, NOW(), NOW()),
(6010, 6009, 'GRADE_6', 'MATH', 'RENJIAO', '负数', NULL, 2, 1, 0, NOW(), NOW()),
(6011, 6009, 'GRADE_6', 'MATH', 'RENJIAO', '百分数（二）', '折扣、成数、税率', 2, 2, 0, NOW(), NOW()),
(6012, 6009, 'GRADE_6', 'MATH', 'RENJIAO', '圆柱与圆锥', NULL, 2, 3, 0, NOW(), NOW()),
(6013, 6009, 'GRADE_6', 'MATH', 'RENJIAO', '比例', NULL, 2, 4, 0, NOW(), NOW()),
(6014, 6009, 'GRADE_6', 'MATH', 'RENJIAO', '数学广角——鸽巢问题', NULL, 2, 5, 0, NOW(), NOW()),
(6015, 6009, 'GRADE_6', 'MATH', 'RENJIAO', '小升初总复习', NULL, 2, 6, 0, NOW(), NOW());

-- 六年级 语文
INSERT INTO t_knowledge_point (id, parent_id, grade, subject, textbook_version, name, description, depth, sort_order, deleted, created_at, updated_at) VALUES
(6101, NULL, 'GRADE_6', 'CHINESE', 'RENJIAO', '六年级上册', NULL, 1, 1, 0, NOW(), NOW()),
(6102, 6101, 'GRADE_6', 'CHINESE', 'RENJIAO', '第一单元', '触摸自然', 2, 1, 0, NOW(), NOW()),
(6103, 6101, 'GRADE_6', 'CHINESE', 'RENJIAO', '第二单元', '革命岁月', 2, 2, 0, NOW(), NOW()),
(6104, NULL, 'GRADE_6', 'CHINESE', 'RENJIAO', '六年级下册', NULL, 1, 2, 0, NOW(), NOW()),
(6105, 6104, 'GRADE_6', 'CHINESE', 'RENJIAO', '第一单元', '民风民俗', 2, 1, 0, NOW(), NOW()),
(6106, 6104, 'GRADE_6', 'CHINESE', 'RENJIAO', '小升初总复习', NULL, 2, 2, 0, NOW(), NOW());

-- 六年级 英语
INSERT INTO t_knowledge_point (id, parent_id, grade, subject, textbook_version, name, description, depth, sort_order, deleted, created_at, updated_at) VALUES
(6201, NULL, 'GRADE_6', 'ENGLISH', 'RENJIAO', '六年级上册', NULL, 1, 1, 0, NOW(), NOW()),
(6202, 6201, 'GRADE_6', 'ENGLISH', 'RENJIAO', 'Unit 1 How can I get there?', '方位与路线', 2, 1, 0, NOW(), NOW()),
(6203, 6201, 'GRADE_6', 'ENGLISH', 'RENJIAO', 'Unit 2 Ways to go to school', '交通方式', 2, 2, 0, NOW(), NOW()),
(6204, NULL, 'GRADE_6', 'ENGLISH', 'RENJIAO', '六年级下册', NULL, 1, 2, 0, NOW(), NOW()),
(6205, 6204, 'GRADE_6', 'ENGLISH', 'RENJIAO', 'Unit 1 How tall are you?', '比较级', 2, 1, 0, NOW(), NOW()),
(6206, 6204, 'GRADE_6', 'ENGLISH', 'RENJIAO', 'Unit 2 Last weekend', '过去时', 2, 2, 0, NOW(), NOW());
