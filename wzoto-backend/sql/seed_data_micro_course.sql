-- =============================================
-- 学霸到家 - 微课视频种子数据
-- 每年级每学科5-8条，约20%免费80%VIP
-- =============================================

-- 一年级 数学 微课
INSERT INTO t_micro_course (grade, subject, textbook_version, title, description, video_url, duration_seconds, difficulty, vip_only, sort_order, deleted, created_at, updated_at) VALUES
('GRADE_1', 'MATH', 'RENJIAO', '认识数字1-5', '学习数字1到5的写法和含义', '/videos/g1-math-01.mp4', 180, 'EASY', 0, 1, 0, NOW(), NOW()),
('GRADE_1', 'MATH', 'RENJIAO', '比大小', '学习大于号、小于号、等于号', '/videos/g1-math-02.mp4', 200, 'EASY', 0, 2, 0, NOW(), NOW()),
('GRADE_1', 'MATH', 'RENJIAO', '5以内的加法', '通过动画理解加法含义', '/videos/g1-math-03.mp4', 240, 'EASY', 1, 3, 0, NOW(), NOW()),
('GRADE_1', 'MATH', 'RENJIAO', '5以内的减法', '通过动画理解减法含义', '/videos/g1-math-04.mp4', 240, 'EASY', 1, 4, 0, NOW(), NOW()),
('GRADE_1', 'MATH', 'RENJIAO', '认识图形', '认识长方体、正方体、圆柱和球', '/videos/g1-math-05.mp4', 300, 'EASY', 1, 5, 0, NOW(), NOW()),
('GRADE_1', 'MATH', 'RENJIAO', '认识钟表', '学会看整时和半时', '/videos/g1-math-06.mp4', 250, 'MEDIUM', 1, 6, 0, NOW(), NOW());

-- 一年级 语文 微课
INSERT INTO t_micro_course (grade, subject, textbook_version, title, description, video_url, duration_seconds, difficulty, vip_only, sort_order, deleted, created_at, updated_at) VALUES
('GRADE_1', 'CHINESE', 'RENJIAO', '汉语拼音：声母', '学习23个声母的发音方法', '/videos/g1-cn-01.mp4', 360, 'EASY', 0, 1, 0, NOW(), NOW()),
('GRADE_1', 'CHINESE', 'RENJIAO', '汉语拼音：韵母', '学习24个韵母及拼读规则', '/videos/g1-cn-02.mp4', 400, 'EASY', 0, 2, 0, NOW(), NOW()),
('GRADE_1', 'CHINESE', 'RENJIAO', '识字：天地人', '认识"天地人"等基础汉字', '/videos/g1-cn-03.mp4', 280, 'EASY', 1, 3, 0, NOW(), NOW()),
('GRADE_1', 'CHINESE', 'RENJIAO', '课文：秋天', '朗读课文，感受秋天的美丽', '/videos/g1-cn-04.mp4', 300, 'EASY', 1, 4, 0, NOW(), NOW()),
('GRADE_1', 'CHINESE', 'RENJIAO', '笔画与偏旁', '认识基本笔画和常见偏旁', '/videos/g1-cn-05.mp4', 320, 'MEDIUM', 1, 5, 0, NOW(), NOW());

-- 一年级 英语 微课
INSERT INTO t_micro_course (grade, subject, textbook_version, title, description, video_url, duration_seconds, difficulty, vip_only, sort_order, deleted, created_at, updated_at) VALUES
('GRADE_1', 'ENGLISH', 'RENJIAO', 'Hello! 你好', '学习打招呼和自我介绍', '/videos/g1-en-01.mp4', 200, 'EASY', 0, 1, 0, NOW(), NOW()),
('GRADE_1', 'ENGLISH', 'RENJIAO', 'Numbers 1-10', '学习英文数字1到10', '/videos/g1-en-02.mp4', 220, 'EASY', 0, 2, 0, NOW(), NOW()),
('GRADE_1', 'ENGLISH', 'RENJIAO', 'Animals 动物', '认识常见动物的英文', '/videos/g1-en-03.mp4', 260, 'EASY', 1, 3, 0, NOW(), NOW()),
('GRADE_1', 'ENGLISH', 'RENJIAO', 'Colors 颜色', '学习基本颜色英文', '/videos/g1-en-04.mp4', 240, 'EASY', 1, 4, 0, NOW(), NOW()),
('GRADE_1', 'ENGLISH', 'RENJIAO', 'Fruits 水果', '学习常见水果英文', '/videos/g1-en-05.mp4', 250, 'EASY', 1, 5, 0, NOW(), NOW());

-- 二年级 数学 微课
INSERT INTO t_micro_course (grade, subject, textbook_version, title, description, video_url, duration_seconds, difficulty, vip_only, sort_order, deleted, created_at, updated_at) VALUES
('GRADE_2', 'MATH', 'RENJIAO', '认识厘米和米', '学习长度单位厘米和米', '/videos/g2-math-01.mp4', 280, 'EASY', 0, 1, 0, NOW(), NOW()),
('GRADE_2', 'MATH', 'RENJIAO', '100以内加法进位', '掌握两位数加两位数的进位加法', '/videos/g2-math-02.mp4', 320, 'MEDIUM', 1, 2, 0, NOW(), NOW()),
('GRADE_2', 'MATH', 'RENJIAO', '100以内减法退位', '掌握两位数减两位数的退位减法', '/videos/g2-math-03.mp4', 320, 'MEDIUM', 1, 3, 0, NOW(), NOW()),
('GRADE_2', 'MATH', 'RENJIAO', '乘法口诀：2-6', '背诵并理解2-6的乘法口诀', '/videos/g2-math-04.mp4', 400, 'MEDIUM', 1, 4, 0, NOW(), NOW()),
('GRADE_2', 'MATH', 'RENJIAO', '乘法口诀：7-9', '背诵并理解7-9的乘法口诀', '/videos/g2-math-05.mp4', 400, 'MEDIUM', 1, 5, 0, NOW(), NOW()),
('GRADE_2', 'MATH', 'RENJIAO', '认识角', '学习角的概念和画法', '/videos/g2-math-06.mp4', 260, 'EASY', 0, 6, 0, NOW(), NOW());

-- 二年级 语文 微课
INSERT INTO t_micro_course (grade, subject, textbook_version, title, description, video_url, duration_seconds, difficulty, vip_only, sort_order, deleted, created_at, updated_at) VALUES
('GRADE_2', 'CHINESE', 'RENJIAO', '小蝌蚪找妈妈', '课文朗读与理解', '/videos/g2-cn-01.mp4', 360, 'EASY', 0, 1, 0, NOW(), NOW()),
('GRADE_2', 'CHINESE', 'RENJIAO', '场景歌', '识字与量词学习', '/videos/g2-cn-02.mp4', 280, 'EASY', 0, 2, 0, NOW(), NOW()),
('GRADE_2', 'CHINESE', 'RENJIAO', '曹冲称象', '课文朗读与智慧故事', '/videos/g2-cn-03.mp4', 400, 'MEDIUM', 1, 3, 0, NOW(), NOW()),
('GRADE_2', 'CHINESE', 'RENJIAO', '看图写话入门', '学习看图写话的基本方法', '/videos/g2-cn-04.mp4', 350, 'MEDIUM', 1, 4, 0, NOW(), NOW()),
('GRADE_2', 'CHINESE', 'RENJIAO', '查字典方法', '部首查字法和音序查字法', '/videos/g2-cn-05.mp4', 300, 'MEDIUM', 1, 5, 0, NOW(), NOW());

-- 二年级 英语 微课
INSERT INTO t_micro_course (grade, subject, textbook_version, title, description, video_url, duration_seconds, difficulty, vip_only, sort_order, deleted, created_at, updated_at) VALUES
('GRADE_2', 'ENGLISH', 'RENJIAO', 'My Family', '学习家庭成员英文称呼', '/videos/g2-en-01.mp4', 240, 'EASY', 0, 1, 0, NOW(), NOW()),
('GRADE_2', 'ENGLISH', 'RENJIAO', 'My Body', '学习身体部位英文', '/videos/g2-en-02.mp4', 220, 'EASY', 1, 2, 0, NOW(), NOW()),
('GRADE_2', 'ENGLISH', 'RENJIAO', 'Weather 天气', '学习天气相关英文表达', '/videos/g2-en-03.mp4', 260, 'EASY', 1, 3, 0, NOW(), NOW()),
('GRADE_2', 'ENGLISH', 'RENJIAO', 'Food 食物', '学习常见食物英文', '/videos/g2-en-04.mp4', 240, 'EASY', 1, 4, 0, NOW(), NOW()),
('GRADE_2', 'ENGLISH', 'RENJIAO', 'Clothes 衣服', '学习衣物英文表达', '/videos/g2-en-05.mp4', 250, 'MEDIUM', 1, 5, 0, NOW(), NOW());

-- 三年级 数学 微课
INSERT INTO t_micro_course (grade, subject, textbook_version, title, description, video_url, duration_seconds, difficulty, vip_only, sort_order, deleted, created_at, updated_at) VALUES
('GRADE_3', 'MATH', 'RENJIAO', '时分秒的认识', '学习时间单位和换算', '/videos/g3-math-01.mp4', 300, 'EASY', 0, 1, 0, NOW(), NOW()),
('GRADE_3', 'MATH', 'RENJIAO', '万以内加减法', '三位数加减法运算', '/videos/g3-math-02.mp4', 380, 'MEDIUM', 1, 2, 0, NOW(), NOW()),
('GRADE_3', 'MATH', 'RENJIAO', '倍的认识', '理解"倍"的概念', '/videos/g3-math-03.mp4', 320, 'MEDIUM', 1, 3, 0, NOW(), NOW()),
('GRADE_3', 'MATH', 'RENJIAO', '多位数乘一位数', '掌握乘法竖式计算', '/videos/g3-math-04.mp4', 360, 'MEDIUM', 1, 4, 0, NOW(), NOW()),
('GRADE_3', 'MATH', 'RENJIAO', '分数初步认识', '认识几分之一和几分之几', '/videos/g3-math-05.mp4', 340, 'MEDIUM', 1, 5, 0, NOW(), NOW()),
('GRADE_3', 'MATH', 'RENJIAO', '长方形和正方形周长', '学习周长的计算方法', '/videos/g3-math-06.mp4', 300, 'MEDIUM', 0, 6, 0, NOW(), NOW());

-- 三年级 语文 微课
INSERT INTO t_micro_course (grade, subject, textbook_version, title, description, video_url, duration_seconds, difficulty, vip_only, sort_order, deleted, created_at, updated_at) VALUES
('GRADE_3', 'CHINESE', 'RENJIAO', '秋天的雨', '课文精读与修辞感受', '/videos/g3-cn-01.mp4', 380, 'EASY', 0, 1, 0, NOW(), NOW()),
('GRADE_3', 'CHINESE', 'RENJIAO', '卖火柴的小女孩', '安徒生童话阅读理解', '/videos/g3-cn-02.mp4', 420, 'MEDIUM', 1, 2, 0, NOW(), NOW()),
('GRADE_3', 'CHINESE', 'RENJIAO', '习作：写日记', '学习日记的格式和写法', '/videos/g3-cn-03.mp4', 350, 'MEDIUM', 1, 3, 0, NOW(), NOW()),
('GRADE_3', 'CHINESE', 'RENJIAO', '古诗：望庐山瀑布', '古诗朗诵与意境理解', '/videos/g3-cn-04.mp4', 300, 'MEDIUM', 1, 4, 0, NOW(), NOW()),
('GRADE_3', 'CHINESE', 'RENJIAO', '阅读理解技巧', '学习找关键词、概括段意', '/videos/g3-cn-05.mp4', 400, 'HARD', 1, 5, 0, NOW(), NOW());

-- 三年级 英语 微课
INSERT INTO t_micro_course (grade, subject, textbook_version, title, description, video_url, duration_seconds, difficulty, vip_only, sort_order, deleted, created_at, updated_at) VALUES
('GRADE_3', 'ENGLISH', 'RENJIAO', 'Hello and Goodbye', '学习问候和告别用语', '/videos/g3-en-01.mp4', 200, 'EASY', 0, 1, 0, NOW(), NOW()),
('GRADE_3', 'ENGLISH', 'RENJIAO', 'Colours 颜色', '学习颜色的英文表达', '/videos/g3-en-02.mp4', 240, 'EASY', 0, 2, 0, NOW(), NOW()),
('GRADE_3', 'ENGLISH', 'RENJIAO', 'Look at me!', '学习身体部位和五官', '/videos/g3-en-03.mp4', 280, 'EASY', 1, 3, 0, NOW(), NOW()),
('GRADE_3', 'ENGLISH', 'RENJIAO', 'We love animals', '学习动物的英文名称', '/videos/g3-en-04.mp4', 300, 'MEDIUM', 1, 4, 0, NOW(), NOW()),
('GRADE_3', 'ENGLISH', 'RENJIAO', 'Let''s eat!', '学习食物和饮料英文', '/videos/g3-en-05.mp4', 280, 'MEDIUM', 1, 5, 0, NOW(), NOW());

-- 四年级 数学 微课
INSERT INTO t_micro_course (grade, subject, textbook_version, title, description, video_url, duration_seconds, difficulty, vip_only, sort_order, deleted, created_at, updated_at) VALUES
('GRADE_4', 'MATH', 'RENJIAO', '大数的认识', '认识万、十万、百万、千万、亿', '/videos/g4-math-01.mp4', 360, 'EASY', 0, 1, 0, NOW(), NOW()),
('GRADE_4', 'MATH', 'RENJIAO', '三位数乘两位数', '掌握乘法竖式计算技巧', '/videos/g4-math-02.mp4', 380, 'MEDIUM', 1, 2, 0, NOW(), NOW()),
('GRADE_4', 'MATH', 'RENJIAO', '角的度量', '学习量角器的使用和角的分类', '/videos/g4-math-03.mp4', 340, 'MEDIUM', 1, 3, 0, NOW(), NOW()),
('GRADE_4', 'MATH', 'RENJIAO', '平行四边形和梯形', '认识平行四边行和梯形的特征', '/videos/g4-math-04.mp4', 360, 'MEDIUM', 1, 4, 0, NOW(), NOW()),
('GRADE_4', 'MATH', 'RENJIAO', '除数是两位数的除法', '掌握除法竖式计算', '/videos/g4-math-05.mp4', 400, 'HARD', 1, 5, 0, NOW(), NOW()),
('GRADE_4', 'MATH', 'RENJIAO', '小数的意义', '理解小数的含义和读写', '/videos/g4-math-06.mp4', 320, 'MEDIUM', 0, 6, 0, NOW(), NOW());

-- 四年级 语文 微课
INSERT INTO t_micro_course (grade, subject, textbook_version, title, description, video_url, duration_seconds, difficulty, vip_only, sort_order, deleted, created_at, updated_at) VALUES
('GRADE_4', 'CHINESE', 'RENJIAO', '观潮', '感受钱塘江大潮的壮观', '/videos/g4-cn-01.mp4', 380, 'EASY', 0, 1, 0, NOW(), NOW()),
('GRADE_4', 'CHINESE', 'RENJIAO', '爬山虎的脚', '学习观察和描写植物', '/videos/g4-cn-02.mp4', 360, 'MEDIUM', 1, 2, 0, NOW(), NOW()),
('GRADE_4', 'CHINESE', 'RENJIAO', '习作：推荐一个好地方', '学习写景作文技巧', '/videos/g4-cn-03.mp4', 400, 'MEDIUM', 1, 3, 0, NOW(), NOW()),
('GRADE_4', 'CHINESE', 'RENJIAO', '古诗三首', '暮江吟、题西林壁、雪梅', '/videos/g4-cn-04.mp4', 420, 'MEDIUM', 1, 4, 0, NOW(), NOW()),
('GRADE_4', 'CHINESE', 'RENJIAO', '批注阅读法', '学习边读边批注的阅读方法', '/videos/g4-cn-05.mp4', 350, 'HARD', 1, 5, 0, NOW(), NOW());

-- 四年级 英语 微课
INSERT INTO t_micro_course (grade, subject, textbook_version, title, description, video_url, duration_seconds, difficulty, vip_only, sort_order, deleted, created_at, updated_at) VALUES
('GRADE_4', 'ENGLISH', 'RENJIAO', 'My Classroom', '学习教室物品英文', '/videos/g4-en-01.mp4', 260, 'EASY', 0, 1, 0, NOW(), NOW()),
('GRADE_4', 'ENGLISH', 'RENJIAO', 'My Schoolbag', '学习书包里的文具英文', '/videos/g4-en-02.mp4', 280, 'EASY', 1, 2, 0, NOW(), NOW()),
('GRADE_4', 'ENGLISH', 'RENJIAO', 'My Friends', '描述朋友的英文表达', '/videos/g4-en-03.mp4', 300, 'MEDIUM', 1, 3, 0, NOW(), NOW()),
('GRADE_4', 'ENGLISH', 'RENJIAO', 'My Home', '学习家庭和房间英文', '/videos/g4-en-04.mp4', 320, 'MEDIUM', 1, 4, 0, NOW(), NOW()),
('GRADE_4', 'ENGLISH', 'RENJIAO', 'Dinner''s Ready', '学习食物和餐具英文', '/videos/g4-en-05.mp4', 300, 'MEDIUM', 1, 5, 0, NOW(), NOW());

-- 五年级 数学 微课
INSERT INTO t_micro_course (grade, subject, textbook_version, title, description, video_url, duration_seconds, difficulty, vip_only, sort_order, deleted, created_at, updated_at) VALUES
('GRADE_5', 'MATH', 'RENJIAO', '小数乘法', '掌握小数乘整數和小数乘小数', '/videos/g5-math-01.mp4', 400, 'MEDIUM', 0, 1, 0, NOW(), NOW()),
('GRADE_5', 'MATH', 'RENJIAO', '小数除法', '掌握小数除法的计算方法', '/videos/g5-math-02.mp4', 420, 'MEDIUM', 1, 2, 0, NOW(), NOW()),
('GRADE_5', 'MATH', 'RENJIAO', '简易方程', '用字母表示数，解方程', '/videos/g5-math-03.mp4', 480, 'HARD', 1, 3, 0, NOW(), NOW()),
('GRADE_5', 'MATH', 'RENJIAO', '多边形面积', '平行四边形、三角形、梯形面积', '/videos/g5-math-04.mp4', 450, 'HARD', 1, 4, 0, NOW(), NOW()),
('GRADE_5', 'MATH', 'RENJIAO', '因数与倍数', '质数、合数、奇数、偶数', '/videos/g5-math-05.mp4', 400, 'MEDIUM', 1, 5, 0, NOW(), NOW()),
('GRADE_5', 'MATH', 'RENJIAO', '分数加减法', '同分母和异分母分数加减', '/videos/g5-math-06.mp4', 380, 'HARD', 0, 6, 0, NOW(), NOW());

-- 五年级 语文 微课
INSERT INTO t_micro_course (grade, subject, textbook_version, title, description, video_url, duration_seconds, difficulty, vip_only, sort_order, deleted, created_at, updated_at) VALUES
('GRADE_5', 'CHINESE', 'RENJIAO', '白鹭', '感受散文之美', '/videos/g5-cn-01.mp4', 380, 'MEDIUM', 0, 1, 0, NOW(), NOW()),
('GRADE_5', 'CHINESE', 'RENJIAO', '落花生', '理解借物喻人的写作手法', '/videos/g5-cn-02.mp4', 360, 'MEDIUM', 1, 2, 0, NOW(), NOW()),
('GRADE_5', 'CHINESE', 'RENJIAO', '习作：我的心爱之物', '学习状物作文写法', '/videos/g5-cn-03.mp4', 400, 'MEDIUM', 1, 3, 0, NOW(), NOW()),
('GRADE_5', 'CHINESE', 'RENJIAO', '圆明园的毁灭', '历史题材课文理解', '/videos/g5-cn-04.mp4', 420, 'MEDIUM', 1, 4, 0, NOW(), NOW()),
('GRADE_5', 'CHINESE', 'RENJIAO', '说明文写作', '学习说明方法：列数字、举例子', '/videos/g5-cn-05.mp4', 380, 'HARD', 1, 5, 0, NOW(), NOW());

-- 五年级 英语 微课
INSERT INTO t_micro_course (grade, subject, textbook_version, title, description, video_url, duration_seconds, difficulty, vip_only, sort_order, deleted, created_at, updated_at) VALUES
('GRADE_5', 'ENGLISH', 'RENJIAO', 'What''s he like?', '描述人物外貌和性格', '/videos/g5-en-01.mp4', 300, 'EASY', 0, 1, 0, NOW(), NOW()),
('GRADE_5', 'ENGLISH', 'RENJIAO', 'My week', '学习星期和课程安排', '/videos/g5-en-02.mp4', 320, 'MEDIUM', 1, 2, 0, NOW(), NOW()),
('GRADE_5', 'ENGLISH', 'RENJIAO', 'What would you like?', '学习点餐英文', '/videos/g5-en-03.mp4', 300, 'MEDIUM', 1, 3, 0, NOW(), NOW()),
('GRADE_5', 'ENGLISH', 'RENJIAO', 'My day', '学习日常作息表达', '/videos/g5-en-04.mp4', 320, 'MEDIUM', 1, 4, 0, NOW(), NOW()),
('GRADE_5', 'ENGLISH', 'RENJIAO', 'There is a river', '学习描述自然环境', '/videos/g5-en-05.mp4', 340, 'HARD', 1, 5, 0, NOW(), NOW());

-- 六年级 数学 微课
INSERT INTO t_micro_course (grade, subject, textbook_version, title, description, video_url, duration_seconds, difficulty, vip_only, sort_order, deleted, created_at, updated_at) VALUES
('GRADE_6', 'MATH', 'RENJIAO', '分数乘法', '分数乘整数和分数乘分数', '/videos/g6-math-01.mp4', 420, 'MEDIUM', 0, 1, 0, NOW(), NOW()),
('GRADE_6', 'MATH', 'RENJIAO', '分数除法', '分数除法的意义和计算', '/videos/g6-math-02.mp4', 450, 'HARD', 1, 2, 0, NOW(), NOW()),
('GRADE_6', 'MATH', 'RENJIAO', '比的认识', '理解比的意义和化简', '/videos/g6-math-03.mp4', 380, 'MEDIUM', 1, 3, 0, NOW(), NOW()),
('GRADE_6', 'MATH', 'RENJIAO', '圆的面积', '推导圆面积公式', '/videos/g6-math-04.mp4', 400, 'HARD', 1, 4, 0, NOW(), NOW()),
('GRADE_6', 'MATH', 'RENJIAO', '百分数应用', '折扣、利率、税率问题', '/videos/g6-math-05.mp4', 480, 'HARD', 1, 5, 0, NOW(), NOW()),
('GRADE_6', 'MATH', 'RENJIAO', '小升初总复习：数与代数', '小学阶段数学知识系统回顾', '/videos/g6-math-06.mp4', 600, 'HARD', 0, 6, 0, NOW(), NOW());

-- 六年级 语文 微课
INSERT INTO t_micro_course (grade, subject, textbook_version, title, description, video_url, duration_seconds, difficulty, vip_only, sort_order, deleted, created_at, updated_at) VALUES
('GRADE_6', 'CHINESE', 'RENJIAO', '草原', '老舍散文精读', '/videos/g6-cn-01.mp4', 400, 'MEDIUM', 0, 1, 0, NOW(), NOW()),
('GRADE_6', 'CHINESE', 'RENJIAO', '七律·长征', '毛泽东诗词赏析', '/videos/g6-cn-02.mp4', 360, 'HARD', 1, 2, 0, NOW(), NOW()),
('GRADE_6', 'CHINESE', 'RENJIAO', '习作：变形记', '学习想象作文写法', '/videos/g6-cn-03.mp4', 400, 'MEDIUM', 1, 3, 0, NOW(), NOW()),
('GRADE_6', 'CHINESE', 'RENJIAO', '文言文二则', '伯牙鼓琴、书戴嵩画牛', '/videos/g6-cn-04.mp4', 450, 'HARD', 1, 4, 0, NOW(), NOW()),
('GRADE_6', 'CHINESE', 'RENJIAO', '小升初作文冲刺', '审题、立意、选材技巧', '/videos/g6-cn-05.mp4', 500, 'HARD', 0, 5, 0, NOW(), NOW());

-- 六年级 英语 微课
INSERT INTO t_micro_course (grade, subject, textbook_version, title, description, video_url, duration_seconds, difficulty, vip_only, sort_order, deleted, created_at, updated_at) VALUES
('GRADE_6', 'ENGLISH', 'RENJIAO', 'How can I get there?', '学习方位词和问路', '/videos/g6-en-01.mp4', 320, 'MEDIUM', 0, 1, 0, NOW(), NOW()),
('GRADE_6', 'ENGLISH', 'RENJIAO', 'Ways to go to school', '学习交通方式表达', '/videos/g6-en-02.mp4', 300, 'MEDIUM', 1, 2, 0, NOW(), NOW()),
('GRADE_6', 'ENGLISH', 'RENJIAO', 'My weekend plan', '学习will表达将来时', '/videos/g6-en-03.mp4', 340, 'MEDIUM', 1, 3, 0, NOW(), NOW()),
('GRADE_6', 'ENGLISH', 'RENJIAO', 'How tall are you?', '学习比较级和形容词', '/videos/g6-en-04.mp4', 320, 'MEDIUM', 1, 4, 0, NOW(), NOW()),
('GRADE_6', 'ENGLISH', 'RENJIAO', 'Last weekend', '学习一般过去时', '/videos/g6-en-05.mp4', 380, 'HARD', 0, 5, 0, NOW(), NOW()),
('GRADE_6', 'ENGLISH', 'RENJIAO', '小升初英语冲刺', '小学语法和词汇系统复习', '/videos/g6-en-06.mp4', 500, 'HARD', 1, 6, 0, NOW(), NOW());
