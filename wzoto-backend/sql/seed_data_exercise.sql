-- =============================================
-- 学霸到家 - 习题库种子数据
-- 每年级每学科6-10道，涵盖选择/填空/判断
-- 约20%免费80%VIP
-- =============================================

-- 一年级 数学 习题
INSERT INTO t_exercise_bank (grade, subject, textbook_version, question_type, question_content, options, correct_answer, explanation, difficulty, source_type, vip_only, sort_order, deleted, created_at, updated_at) VALUES
('GRADE_1', 'MATH', 'RENJIAO', 'CHOICE', '{"stem":"3+2等于几？"}', '["3","4","5","6"]', 'C', '3加2等于5', 'EASY', 'SYSTEM', 0, 1, 0, NOW(), NOW()),
('GRADE_1', 'MATH', 'RENJIAO', 'CHOICE', '{"stem":"下面哪个数最大？"}', '["5","8","3","6"]', 'B', '8是最大的数', 'EASY', 'SYSTEM', 0, 2, 0, NOW(), NOW()),
('GRADE_1', 'MATH', 'RENJIAO', 'FILL', '{"stem":"7+（）=10"}', NULL, '3', '10减7等于3', 'EASY', 'SYSTEM', 1, 3, 0, NOW(), NOW()),
('GRADE_1', 'MATH', 'RENJIAO', 'TRUE_FALSE', '{"stem":"5+3=9，对吗？"}', NULL, 'FALSE', '5加3等于8，不是9', 'EASY', 'SYSTEM', 1, 4, 0, NOW(), NOW()),
('GRADE_1', 'MATH', 'RENJIAO', 'CHOICE', '{"stem":"一个钟面指向3点，时针指向几？"}', '["1","2","3","4"]', 'C', '时针指向3就是3点', 'EASY', 'SYSTEM', 1, 5, 0, NOW(), NOW()),
('GRADE_1', 'MATH', 'RENJIAO', 'FILL', '{"stem":"15-8=（）"}', NULL, '7', '15减8等于7', 'MEDIUM', 'SYSTEM', 1, 6, 0, NOW(), NOW());

-- 一年级 语文 习题
INSERT INTO t_exercise_bank (grade, subject, textbook_version, question_type, question_content, options, correct_answer, explanation, difficulty, source_type, vip_only, sort_order, deleted, created_at, updated_at) VALUES
('GRADE_1', 'CHINESE', 'RENJIAO', 'CHOICE', '{"stem":"\"天\"的拼音是？"}', '["tiān","tián","diān","nián"]', 'A', '"天"读tiān，第一声', 'EASY', 'SYSTEM', 0, 1, 0, NOW(), NOW()),
('GRADE_1', 'CHINESE', 'RENJIAO', 'CHOICE', '{"stem":"下面哪个是声母？"}', '["a","o","b","e"]', 'C', 'b是声母，其他是韵母', 'EASY', 'SYSTEM', 0, 2, 0, NOW(), NOW()),
('GRADE_1', 'CHINESE', 'RENJIAO', 'FILL', '{"stem":"\"人\"字共有（）画"}', NULL, '2', '人字有撇和捺两画', 'EASY', 'SYSTEM', 1, 3, 0, NOW(), NOW()),
('GRADE_1', 'CHINESE', 'RENJIAO', 'TRUE_FALSE', '{"stem":"\"大\"的反义词是\"小\""}', NULL, 'TRUE', '大和小是反义词', 'EASY', 'SYSTEM', 1, 4, 0, NOW(), NOW()),
('GRADE_1', 'CHINESE', 'RENJIAO', 'CHOICE', '{"stem":"春天来了，小草从（）里长出来"}', '["天","地","水","火"]', 'B', '小草从地里长出来', 'EASY', 'SYSTEM', 1, 5, 0, NOW(), NOW()),
('GRADE_1', 'CHINESE', 'RENJIAO', 'FILL', '{"stem":"床前明月光，疑是地上霜。这句诗的作者是（）"}', NULL, '李白', '出自李白的《静夜思》', 'MEDIUM', 'SYSTEM', 1, 6, 0, NOW(), NOW());

-- 一年级 英语 习题
INSERT INTO t_exercise_bank (grade, subject, textbook_version, question_type, question_content, options, correct_answer, explanation, difficulty, source_type, vip_only, sort_order, deleted, created_at, updated_at) VALUES
('GRADE_1', 'ENGLISH', 'RENJIAO', 'CHOICE', '{"stem":"Hello的中文意思是？"}', '["再见","你好","谢谢","对不起"]', 'B', 'Hello是打招呼用语，意为"你好"', 'EASY', 'SYSTEM', 0, 1, 0, NOW(), NOW()),
('GRADE_1', 'ENGLISH', 'RENJIAO', 'CHOICE', '{"stem":"数字5的英文是？"}', '["three","four","five","six"]', 'C', '5的英文是five', 'EASY', 'SYSTEM', 0, 2, 0, NOW(), NOW()),
('GRADE_1', 'ENGLISH', 'RENJIAO', 'CHOICE', '{"stem":"Cat是什么动物？"}', '["狗","猫","鸟","鱼"]', 'B', 'Cat是猫', 'EASY', 'SYSTEM', 1, 3, 0, NOW(), NOW()),
('GRADE_1', 'ENGLISH', 'RENJIAO', 'FILL', '{"stem":"红色的英文是r（）d"}', NULL, 'e', 'red是红色', 'EASY', 'SYSTEM', 1, 4, 0, NOW(), NOW());

-- 二年级 数学 习题
INSERT INTO t_exercise_bank (grade, subject, textbook_version, question_type, question_content, options, correct_answer, explanation, difficulty, source_type, vip_only, sort_order, deleted, created_at, updated_at) VALUES
('GRADE_2', 'MATH', 'RENJIAO', 'CHOICE', '{"stem":"1米等于多少厘米？"}', '["10","50","100","1000"]', 'C', '1米=100厘米', 'EASY', 'SYSTEM', 0, 1, 0, NOW(), NOW()),
('GRADE_2', 'MATH', 'RENJIAO', 'FILL', '{"stem":"36+47=（）"}', NULL, '83', '个位6+7=13进1，十位3+4+1=8，所以83', 'MEDIUM', 'SYSTEM', 1, 2, 0, NOW(), NOW()),
('GRADE_2', 'MATH', 'RENJIAO', 'CHOICE', '{"stem":"3×4等于多少？"}', '["7","10","12","14"]', 'C', '3乘4等于12', 'EASY', 'SYSTEM', 0, 3, 0, NOW(), NOW()),
('GRADE_2', 'MATH', 'RENJIAO', 'TRUE_FALSE', '{"stem":"5×6=35"}', NULL, 'FALSE', '5×6=30，不是35', 'EASY', 'SYSTEM', 1, 4, 0, NOW(), NOW()),
('GRADE_2', 'MATH', 'RENJIAO', 'FILL', '{"stem":"72÷8=（）"}', NULL, '9', '八九七十二，所以72÷8=9', 'MEDIUM', 'SYSTEM', 1, 5, 0, NOW(), NOW()),
('GRADE_2', 'MATH', 'RENJIAO', 'CHOICE', '{"stem":"下面哪个是直角？"}', '["锐角","直角","钝角","平角"]', 'B', '直角是90度的角', 'EASY', 'SYSTEM', 1, 6, 0, NOW(), NOW());

-- 二年级 语文 习题
INSERT INTO t_exercise_bank (grade, subject, textbook_version, question_type, question_content, options, correct_answer, explanation, difficulty, source_type, vip_only, sort_order, deleted, created_at, updated_at) VALUES
('GRADE_2', 'CHINESE', 'RENJIAO', 'CHOICE', '{"stem":"一（）花，填量词"}', '["只","朵","条","个"]', 'B', '花用"朵"做量词', 'EASY', 'SYSTEM', 0, 1, 0, NOW(), NOW()),
('GRADE_2', 'CHINESE', 'RENJIAO', 'FILL', '{"stem":"\"春\"字共有（）画"}', NULL, '9', '春字共9画', 'MEDIUM', 'SYSTEM', 1, 2, 0, NOW(), NOW()),
('GRADE_2', 'CHINESE', 'RENJIAO', 'CHOICE', '{"stem":"\"寻找\"的近义词是？"}', '["放弃","找寻","隐藏","丢弃"]', 'B', '寻找的近义词是找寻', 'EASY', 'SYSTEM', 1, 3, 0, NOW(), NOW()),
('GRADE_2', 'CHINESE', 'RENJIAO', 'TRUE_FALSE', '{"stem":"\"鸟\"字共有5画"}', NULL, 'TRUE', '鸟字共5画', 'MEDIUM', 'SYSTEM', 1, 4, 0, NOW(), NOW());

-- 二年级 英语 习题
INSERT INTO t_exercise_bank (grade, subject, textbook_version, question_type, question_content, options, correct_answer, explanation, difficulty, source_type, vip_only, sort_order, deleted, created_at, updated_at) VALUES
('GRADE_2', 'ENGLISH', 'RENJIAO', 'CHOICE', '{"stem":"爸爸的英文是？"}', '["mother","father","sister","brother"]', 'B', '爸爸是father', 'EASY', 'SYSTEM', 0, 1, 0, NOW(), NOW()),
('GRADE_2', 'ENGLISH', 'RENJIAO', 'CHOICE', '{"stem":"It is sunny. 天气是？"}', '["下雨","晴天","下雪","多云"]', 'B', 'sunny是晴天', 'EASY', 'SYSTEM', 1, 2, 0, NOW(), NOW()),
('GRADE_2', 'ENGLISH', 'RENJIAO', 'FILL', '{"stem":"鼻子是n（）se"}', NULL, 'o', 'nose是鼻子', 'EASY', 'SYSTEM', 1, 3, 0, NOW(), NOW());

-- 三年级 数学 习题
INSERT INTO t_exercise_bank (grade, subject, textbook_version, question_type, question_content, options, correct_answer, explanation, difficulty, source_type, vip_only, sort_order, deleted, created_at, updated_at) VALUES
('GRADE_3', 'MATH', 'RENJIAO', 'CHOICE', '{"stem":"1小时等于多少分钟？"}', '["30","45","60","100"]', 'C', '1小时=60分钟', 'EASY', 'SYSTEM', 0, 1, 0, NOW(), NOW()),
('GRADE_3', 'MATH', 'RENJIAO', 'FILL', '{"stem":"256+378=（）"}', NULL, '634', '从个位加起，6+8=14进1，5+7+1=13进1，2+3+1=6', 'MEDIUM', 'SYSTEM', 1, 2, 0, NOW(), NOW()),
('GRADE_3', 'MATH', 'RENJIAO', 'CHOICE', '{"stem":"1/4读作？"}', '["四分之一","四分之二","四分之一","四分之三"]', 'A', '1/4读作四分之一', 'EASY', 'SYSTEM', 0, 3, 0, NOW(), NOW()),
('GRADE_3', 'MATH', 'RENJIAO', 'CHOICE', '{"stem":"120×3=？"}', '["320","360","420","460"]', 'B', '120×3=360', 'MEDIUM', 'SYSTEM', 1, 4, 0, NOW(), NOW()),
('GRADE_3', 'MATH', 'RENJIAO', 'TRUE_FALSE', '{"stem":"正方形有4条对称轴"}', NULL, 'TRUE', '正方形有4条对称轴', 'MEDIUM', 'SYSTEM', 1, 5, 0, NOW(), NOW()),
('GRADE_3', 'MATH', 'RENJIAO', 'FILL', '{"stem":"8的（）倍是56"}', NULL, '7', '56÷8=7，所以8的7倍是56', 'MEDIUM', 'SYSTEM', 1, 6, 0, NOW(), NOW());

-- 三年级 语文 习题
INSERT INTO t_exercise_bank (grade, subject, textbook_version, question_type, question_content, options, correct_answer, explanation, difficulty, source_type, vip_only, sort_order, deleted, created_at, updated_at) VALUES
('GRADE_3', 'CHINESE', 'RENJIAO', 'CHOICE', '{"stem":"\"秋天的雨，是一把钥匙\"用了什么修辞？"}', '["比喻","拟人","夸张","排比"]', 'A', '把雨比作钥匙，是比喻', 'MEDIUM', 'SYSTEM', 0, 1, 0, NOW(), NOW()),
('GRADE_3', 'CHINESE', 'RENJIAO', 'FILL', '{"stem":"望庐山瀑布的作者是唐代诗人（）"}', NULL, '李白', '《望庐山瀑布》是李白所写', 'EASY', 'SYSTEM', 0, 2, 0, NOW(), NOW()),
('GRADE_3', 'CHINESE', 'RENJIAO', 'CHOICE', '{"stem":"\"果然\"的近义词是？"}', '["居然","竟然","果真","偶然"]', 'C', '果然的近义词是果真', 'EASY', 'SYSTEM', 1, 3, 0, NOW(), NOW()),
('GRADE_3', 'CHINESE', 'RENJIAO', 'TRUE_FALSE', '{"stem":"日记的格式需要在第一行写上日期和天气"}', NULL, 'TRUE', '日记格式要求写日期和天气', 'EASY', 'SYSTEM', 1, 4, 0, NOW(), NOW());

-- 三年级 英语 习题
INSERT INTO t_exercise_bank (grade, subject, textbook_version, question_type, question_content, options, correct_answer, explanation, difficulty, source_type, vip_only, sort_order, deleted, created_at, updated_at) VALUES
('GRADE_3', 'ENGLISH', 'RENJIAO', 'CHOICE', '{"stem":"How are you? 的回答是？"}', '["I''m fine, thank you.","I''m five.","I''m here.","Goodbye."]','A', 'How are you? 的回答是 I''m fine, thank you.', 'EASY', 'SYSTEM', 0, 1, 0, NOW(), NOW()),
('GRADE_3', 'ENGLISH', 'RENJIAO', 'CHOICE', '{"stem":"红色的英文是？"}', '["blue","green","red","yellow"]', 'C', '红色是red', 'EASY', 'SYSTEM', 0, 2, 0, NOW(), NOW()),
('GRADE_3', 'ENGLISH', 'RENJIAO', 'FILL', '{"stem":"I have a p（）_（狗）"}', NULL, 'et', 'pet是宠物', 'EASY', 'SYSTEM', 1, 3, 0, NOW(), NOW());

-- 四年级 数学 习题
INSERT INTO t_exercise_bank (grade, subject, textbook_version, question_type, question_content, options, correct_answer, explanation, difficulty, source_type, vip_only, sort_order, deleted, created_at, updated_at) VALUES
('GRADE_4', 'MATH', 'RENJIAO', 'CHOICE', '{"stem":"一亿写作？"}', '["10000000","100000000","1000000","1000000000"]', 'B', '一亿=100000000（1后面8个0）', 'EASY', 'SYSTEM', 0, 1, 0, NOW(), NOW()),
('GRADE_4', 'MATH', 'RENJIAO', 'FILL', '{"stem":"235×16=（）"}', NULL, '3760', '235×16=235×10+235×6=2350+1410=3760', 'MEDIUM', 'SYSTEM', 1, 2, 0, NOW(), NOW()),
('GRADE_4', 'MATH', 'RENJIAO', 'CHOICE', '{"stem":"0.5和0.50比较"}', '["0.5>0.50","0.5<0.50","0.5=0.50","无法比较"]', 'C', '0.5=0.50，小數末尾加0大小不变', 'EASY', 'SYSTEM', 0, 3, 0, NOW(), NOW()),
('GRADE_4', 'MATH', 'RENJIAO', 'TRUE_FALSE', '{"stem":"125×8=1000"}', NULL, 'TRUE', '125×8=1000', 'EASY', 'SYSTEM', 1, 4, 0, NOW(), NOW()),
('GRADE_4', 'MATH', 'RENJIAO', 'FILL', '{"stem":"三角形三个内角和等于（）度"}', NULL, '180', '三角形内角和等于180度', 'MEDIUM', 'SYSTEM', 1, 5, 0, NOW(), NOW()),
('GRADE_4', 'MATH', 'RENJIAO', 'CHOICE', '{"stem":"平行四边形的对边（）"}', '["不相等","相等","不一定相等","无法判断"]', 'B', '平行四边形对边平行且相等', 'MEDIUM', 'SYSTEM', 1, 6, 0, NOW(), NOW());

-- 四年级 语文 习题
INSERT INTO t_exercise_bank (grade, subject, textbook_version, question_type, question_content, options, correct_answer, explanation, difficulty, source_type, vip_only, sort_order, deleted, created_at, updated_at) VALUES
('GRADE_4', 'CHINESE', 'RENJIAO', 'CHOICE', '{"stem":"《观潮》描写的是哪里的潮水？"}', '["长江","黄河","钱塘江","珠江"]', 'C', '《观潮》写钱塘江大潮', 'EASY', 'SYSTEM', 0, 1, 0, NOW(), NOW()),
('GRADE_4', 'CHINESE', 'RENJIAO', 'FILL', '{"stem":"不识庐山真面目，只缘身在此山中。出自苏轼的《（）》"}', NULL, '题西林壁', '出自苏轼《题西林壁》', 'MEDIUM', 'SYSTEM', 1, 2, 0, NOW(), NOW()),
('GRADE_4', 'CHINESE', 'RENJIAO', 'TRUE_FALSE', '{"stem":"爬山虎的脚的作者是叶圣陶"}', NULL, 'TRUE', '《爬山虎的脚》是叶圣陶的作品', 'EASY', 'SYSTEM', 1, 3, 0, NOW(), NOW()),
('GRADE_4', 'CHINESE', 'RENJIAO', 'CHOICE', '{"stem":"\"鼓舞\"的近义词是？"}', '["打击","鼓励","阻止","放弃"]', 'B', '鼓舞的近义词是鼓励', 'EASY', 'SYSTEM', 1, 4, 0, NOW(), NOW());

-- 四年级 英语 习题
INSERT INTO t_exercise_bank (grade, subject, textbook_version, question_type, question_content, options, correct_answer, explanation, difficulty, source_type, vip_only, sort_order, deleted, created_at, updated_at) VALUES
('GRADE_4', 'ENGLISH', 'RENJIAO', 'CHOICE', '{"stem":"教室的英文是？"}', '["school","classroom","home","library"]', 'B', '教室是classroom', 'EASY', 'SYSTEM', 0, 1, 0, NOW(), NOW()),
('GRADE_4', 'ENGLISH', 'RENJIAO', 'FILL', '{"stem":"I have a new sch（）l"}', NULL, 'oo', 'school是学校', 'EASY', 'SYSTEM', 1, 2, 0, NOW(), NOW()),
('GRADE_4', 'ENGLISH', 'RENJIAO', 'CHOICE', '{"stem":"What color is it? 回答："}', '["It''s a book.","It''s red.","It''s big.","It''s mine."]', 'B', '问颜色用颜色回答', 'EASY', 'SYSTEM', 1, 3, 0, NOW(), NOW());

-- 五年级 数学 习题
INSERT INTO t_exercise_bank (grade, subject, textbook_version, question_type, question_content, options, correct_answer, explanation, difficulty, source_type, vip_only, sort_order, deleted, created_at, updated_at) VALUES
('GRADE_5', 'MATH', 'RENJIAO', 'CHOICE', '{"stem":"2.5×4=？"}', '["8","9","10","12"]', 'C', '2.5×4=10', 'EASY', 'SYSTEM', 0, 1, 0, NOW(), NOW()),
('GRADE_5', 'MATH', 'RENJIAO', 'FILL', '{"stem":"3.6÷0.6=（）"}', NULL, '6', '3.6÷0.6=36÷6=6', 'MEDIUM', 'SYSTEM', 1, 2, 0, NOW(), NOW()),
('GRADE_5', 'MATH', 'RENJIAO', 'CHOICE', '{"stem":"x+5=12，x=？"}', '["5","6","7","8"]', 'C', 'x=12-5=7', 'EASY', 'SYSTEM', 0, 3, 0, NOW(), NOW()),
('GRADE_5', 'MATH', 'RENJIAO', 'FILL', '{"stem":"三角形底8cm，高5cm，面积=（）cm²"}', NULL, '20', '三角形面积=底×高÷2=8×5÷2=20', 'MEDIUM', 'SYSTEM', 1, 4, 0, NOW(), NOW()),
('GRADE_5', 'MATH', 'RENJIAO', 'CHOICE', '{"stem":"36的因数有几个？"}', '["7","8","9","10"]', 'C', '36的因数：1,2,3,4,6,9,12,18,36共9个', 'HARD', 'SYSTEM', 1, 5, 0, NOW(), NOW()),
('GRADE_5', 'MATH', 'RENJIAO', 'TRUE_FALSE', '{"stem":"1/3 + 1/4 = 2/7"}', NULL, 'FALSE', '1/3+1/4=4/12+3/12=7/12', 'HARD', 'SYSTEM', 1, 6, 0, NOW(), NOW());

-- 五年级 语文 习题
INSERT INTO t_exercise_bank (grade, subject, textbook_version, question_type, question_content, options, correct_answer, explanation, difficulty, source_type, vip_only, sort_order, deleted, created_at, updated_at) VALUES
('GRADE_5', 'CHINESE', 'RENJIAO', 'CHOICE', '{"stem":"《落花生》借物喻人，花生比喻什么样的人？"}', '["外表美","默默奉献","骄傲","聪明"]', 'B', '花生比喻默默奉献、不图虚名的人', 'MEDIUM', 'SYSTEM', 0, 1, 0, NOW(), NOW()),
('GRADE_5', 'CHINESE', 'RENJIAO', 'FILL', '{"stem":"举头望明月，（）"}', NULL, '低头思故乡', '出自李白《静夜思》', 'EASY', 'SYSTEM', 0, 2, 0, NOW(), NOW()),
('GRADE_5', 'CHINESE', 'RENJIAO', 'CHOICE', '{"stem":"说明文常用的说明方法不包括？"}', '["列数字","举例子","打比方","押韵"]', 'D', '押韵不是说明方法', 'EASY', 'SYSTEM', 1, 3, 0, NOW(), NOW()),
('GRADE_5', 'CHINESE', 'RENJIAO', 'TRUE_FALSE', '{"stem":"\"津津有味\"是AABB式的词语"}', NULL, 'FALSE', '津津有味是AABC式', 'MEDIUM', 'SYSTEM', 1, 4, 0, NOW(), NOW());

-- 五年级 英语 习题
INSERT INTO t_exercise_bank (grade, subject, textbook_version, question_type, question_content, options, correct_answer, explanation, difficulty, source_type, vip_only, sort_order, deleted, created_at, updated_at) VALUES
('GRADE_5', 'ENGLISH', 'RENJIAO', 'CHOICE', '{"stem":"Monday的中文是？"}', '["星期二","星期一","星期三","星期四"]', 'B', 'Monday是星期一', 'EASY', 'SYSTEM', 0, 1, 0, NOW(), NOW()),
('GRADE_5', 'ENGLISH', 'RENJIAO', 'FILL', '{"stem":"I often read b（）ks"}', NULL, 'oo', 'books是书', 'EASY', 'SYSTEM', 1, 2, 0, NOW(), NOW()),
('GRADE_5', 'ENGLISH', 'RENJIAO', 'CHOICE', '{"stem":"What would you like? 回答："}', '["I like fish.","I''d like some rice.","Yes, I would.","No, I wouldn''t."]', 'B', 'What would you like? 回答I''d like...', 'MEDIUM', 'SYSTEM', 1, 3, 0, NOW(), NOW());

-- 六年级 数学 习题
INSERT INTO t_exercise_bank (grade, subject, textbook_version, question_type, question_content, options, correct_answer, explanation, difficulty, source_type, vip_only, sort_order, deleted, created_at, updated_at) VALUES
('GRADE_6', 'MATH', 'RENJIAO', 'CHOICE', '{"stem":"2/3 × 3/4 = ？"}', '["1/2","2/4","3/4","5/7"]', 'A', '2/3×3/4=6/12=1/2', 'MEDIUM', 'SYSTEM', 0, 1, 0, NOW(), NOW()),
('GRADE_6', 'MATH', 'RENJIAO', 'FILL', '{"stem":"一个圆的半径是3cm，面积=（）cm²（π取3.14）"}', NULL, '28.26', '圆面积=πr²=3.14×3×3=28.26', 'HARD', 'SYSTEM', 1, 2, 0, NOW(), NOW()),
('GRADE_6', 'MATH', 'RENJIAO', 'CHOICE', '{"stem":"25%化成分数是？"}', '["1/2","1/4","1/5","3/4"]', 'B', '25%=25/100=1/4', 'EASY', 'SYSTEM', 0, 3, 0, NOW(), NOW()),
('GRADE_6', 'MATH', 'RENJIAO', 'CHOICE', '{"stem":"一件商品打八折，原价100元，现价？"}', '["80元","85元","90元","75元"]', 'A', '八折=80%，100×80%=80元', 'EASY', 'SYSTEM', 0, 4, 0, NOW(), NOW()),
('GRADE_6', 'MATH', 'RENJIAO', 'FILL', '{"stem":"4:6化简后=（）"}', NULL, '2:3', '4:6=2:3（同除以2）', 'MEDIUM', 'SYSTEM', 1, 5, 0, NOW(), NOW()),
('GRADE_6', 'MATH', 'RENJIAO', 'TRUE_FALSE', '{"stem":"负数都比0小"}', NULL, 'TRUE', '负数都比0小，正数都比0大', 'EASY', 'SYSTEM', 1, 6, 0, NOW(), NOW()),
('GRADE_6', 'MATH', 'RENJIAO', 'CHOICE', '{"stem":"把3米长的绳子平均分成5段，每段是？"}', '["3/5米","5/3米","1/5米","3米"]', 'A', '3÷5=3/5米', 'MEDIUM', 'SYSTEM', 1, 7, 0, NOW(), NOW());

-- 六年级 语文 习题
INSERT INTO t_exercise_bank (grade, subject, textbook_version, question_type, question_content, options, correct_answer, explanation, difficulty, source_type, vip_only, sort_order, deleted, created_at, updated_at) VALUES
('GRADE_6', 'CHINESE', 'RENJIAO', 'CHOICE', '{"stem":"《草原》的作者是？"}', '["鲁迅","老舍","巴金","冰心"]', 'B', '《草原》是老舍的作品', 'EASY', 'SYSTEM', 0, 1, 0, NOW(), NOW()),
('GRADE_6', 'CHINESE', 'RENJIAO', 'FILL', '{"stem":"红军不怕远征难，万水千山只等闲。出自（）的诗"}', NULL, '毛泽东', '出自毛泽东《七律·长征》', 'EASY', 'SYSTEM', 0, 2, 0, NOW(), NOW()),
('GRADE_6', 'CHINESE', 'RENJIAO', 'CHOICE', '{"stem":"\"伯牙鼓琴\"中\"鼓\"的意思是？"}', '["鼓励","打鼓","弹奏","鼓掌"]', 'C', '鼓在这里是弹奏的意思', 'MEDIUM', 'SYSTEM', 1, 3, 0, NOW(), NOW()),
('GRADE_6', 'CHINESE', 'RENJIAO', 'TRUE_FALSE', '{"stem":"\"千里之行，始于足下\"出自《论语》"}', NULL, 'FALSE', '出自《道德经》，不是《论语》', 'HARD', 'SYSTEM', 1, 4, 0, NOW(), NOW());

-- 六年级 英语 习题
INSERT INTO t_exercise_bank (grade, subject, textbook_version, question_type, question_content, options, correct_answer, explanation, difficulty, source_type, vip_only, sort_order, deleted, created_at, updated_at) VALUES
('GRADE_6', 'ENGLISH', 'RENJIAO', 'CHOICE', '{"stem":"How can I get to the park? 回答："}', '["Turn left at the crossing.","I like the park.","It''s big.","Yes, you can."]', 'A', '问路要用方向指引回答', 'MEDIUM', 'SYSTEM', 0, 1, 0, NOW(), NOW()),
('GRADE_6', 'ENGLISH', 'RENJIAO', 'FILL', '{"stem":"I go to school by b（）s"}', NULL, 'u', 'bus是公交车', 'EASY', 'SYSTEM', 0, 2, 0, NOW(), NOW()),
('GRADE_6', 'ENGLISH', 'RENJIAO', 'CHOICE', '{"stem":"She is t（）ler than me. 应填？"}', '["al","al","all","a"]', 'B', 'tall的比较级是taller', 'MEDIUM', 'SYSTEM', 1, 3, 0, NOW(), NOW()),
('GRADE_6', 'ENGLISH', 'RENJIAO', 'CHOICE', '{"stem":"What did you do last weekend? 回答："}', '["I play football.","I played football.","I will play football.","I am playing."]', 'B', '问过去的事用过去时回答', 'MEDIUM', 'SYSTEM', 1, 4, 0, NOW(), NOW());
