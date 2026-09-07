-- =============================================
-- H2 Compatible Schema (auto-generated from MySQL DDL)
-- Generated: 2026-09-07T03:54:07.708Z
-- =============================================
SET MODE MySQL;

-- === t_ai_exercise_generated.sql ===
DROP TABLE IF EXISTS t_ai_exercise_generated;
-- =============================================
-- 瀛﹂湼鍒板 - AI鐢熸垚缁冧範棰樿〃 DDL锛堟磱钁卞闄㈠涔犺祫婧愪綋绯伙級
-- AI鏍规嵁閿欓/钖勫急鐐硅嚜鍔ㄧ敓鎴愰拡瀵规€х粌涔犻
-- =============================================
CREATE TABLE IF NOT EXISTS t_ai_exercise_generated (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    child_id BIGINT NOT NULL COMMENT '瀛愬コID',
    parent_id BIGINT NOT NULL COMMENT '瀹堕暱鐢ㄦ埛ID',
    source VARCHAR(32) NOT NULL COMMENT '鐢熸垚鏉ユ簮锛歐RONG_QUESTION閿欓琛ュ/LEARNING_PLAN瀛︿範璁″垝/AI_QA绛旂枒杩介棶',
    source_id BIGINT DEFAULT NULL COMMENT '鏉ユ簮璁板綍ID锛堥敊棰業D/璁″垝ID/鎻愰棶ID锛?,
    subject VARCHAR(32) NOT NULL COMMENT '瀛︾',
    grade VARCHAR(16) NOT NULL COMMENT '骞寸骇',
    knowledge_point VARCHAR(128) DEFAULT NULL COMMENT '鐩爣鐭ヨ瘑鐐?,
    question_content TEXT NOT NULL COMMENT '棰樼洰鍐呭JSON',
    correct_answer TEXT NOT NULL COMMENT '姝ｇ‘绛旀',
    ai_explanation TEXT DEFAULT NULL COMMENT 'AI瑙ｆ瀽璇存槑',
    difficulty VARCHAR(16) NOT NULL DEFAULT 'MEDIUM' COMMENT '闅惧害',
    answered TINYINT(1) NOT NULL DEFAULT 0 COMMENT '鏄惁宸蹭綔绛旓細0鍚?鏄?,
    is_correct TINYINT(1) DEFAULT NULL COMMENT '浣滅瓟鏄惁姝ｇ‘',
    answered_at DATETIME DEFAULT NULL COMMENT '浣滅瓟鏃堕棿',
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    created_at DATETIME DEFAULT NULL,
    updated_at DATETIME DEFAULT NULL
);

-- === t_ai_grading_record.sql ===
DROP TABLE IF EXISTS t_ai_grading_record;
-- =============================================
-- 瀛﹂湼鍒板 - AI鎵规敼璁板綍琛?DDL锛堟磱钁卞闄㈠涔犺祫婧愪綋绯伙級
-- 浣滄枃鎵规敼 + 鍙ｈ璇勬祴
-- =============================================
CREATE TABLE IF NOT EXISTS t_ai_grading_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    child_id BIGINT NOT NULL COMMENT '瀛愬コID',
    parent_id BIGINT NOT NULL COMMENT '瀹堕暱鐢ㄦ埛ID',
    grading_type VARCHAR(32) NOT NULL COMMENT '鎵规敼绫诲瀷锛欳OMPOSITION浣滄枃鎵规敼/PRONUNCIATION鍙ｈ璇勬祴',
    subject VARCHAR(32) NOT NULL COMMENT '瀛︾锛欳HINESE/ENGLISH',
    grade VARCHAR(16) NOT NULL COMMENT '骞寸骇',
    title VARCHAR(200) DEFAULT NULL COMMENT '鏍囬锛堜綔鏂囨爣棰?璺熻鍗曡瘝锛?,
    content_text TEXT DEFAULT NULL COMMENT '鏂囨湰鍐呭锛堜綔鏂囨鏂囷級',
    content_audio_url VARCHAR(512) DEFAULT NULL COMMENT '闊抽URL锛堝彛璇綍闊筹級',
    ai_result_json TEXT DEFAULT NULL COMMENT 'AI鎵规敼缁撴灉JSON锛堝惈鍒嗘暟/绾犻敊/寤鸿锛?,
    score INT DEFAULT NULL COMMENT 'AI璇勫垎锛?-100锛?,
    error_count INT DEFAULT NULL COMMENT '閿欒鏁伴噺锛堥敊鍒瓧/鐥呭彞/鍙戦煶閿欒锛?,
    suggestion TEXT DEFAULT NULL COMMENT '浼樺寲寤鸿鎽樿',
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    created_at DATETIME DEFAULT NULL,
    updated_at DATETIME DEFAULT NULL
);

-- === t_ai_learning_plan.sql ===
DROP TABLE IF EXISTS t_ai_learning_plan;
-- =============================================
-- 瀛﹂湼鍒板 - AI瀛︿範瑙勫垝璁板綍琛?DDL锛堟磱钁卞闄㈠涔犺祫婧愪綋绯伙級
-- AI鏍规嵁瀛︽儏鑷姩鐢熸垚姣忔棩瀛︿範璁″垝
-- =============================================
CREATE TABLE IF NOT EXISTS t_ai_learning_plan (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    child_id BIGINT NOT NULL COMMENT '瀛愬コID',
    parent_id BIGINT NOT NULL COMMENT '瀹堕暱鐢ㄦ埛ID',
    plan_date DATE NOT NULL COMMENT '璁″垝鏃ユ湡',
    plan_content_json TEXT NOT NULL COMMENT '璁″垝鍐呭JSON锛堝惈浠诲姟鍒楄〃銆佹椂闂村畨鎺掋€佺煡璇嗙偣鐩爣锛?,
    weak_points_json TEXT DEFAULT NULL COMMENT '鍩轰簬鍒嗘瀽鐨勮杽寮辩偣JSON',
    ai_suggestion TEXT DEFAULT NULL COMMENT 'AI瀛︿範寤鸿',
    applied TINYINT(1) NOT NULL DEFAULT 0 COMMENT '鏄惁宸插簲鐢ㄥ埌瀛︿範浠诲姟锛?鍚?鏄?,
    applied_at DATETIME DEFAULT NULL COMMENT '搴旂敤鏃堕棿',
    generated_at DATETIME NOT NULL COMMENT 'AI鐢熸垚鏃堕棿',
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    created_at DATETIME DEFAULT NULL,
    updated_at DATETIME DEFAULT NULL
);

-- === t_ai_qa_record.sql ===
DROP TABLE IF EXISTS t_ai_qa_record;
-- =============================================
-- 瀛﹂湼鍒板 - AI鎻愰棶璁板綍琛?DDL锛堟磱钁卞闄㈠涔犺祫婧愪綋绯伙級
-- 鏀寔鎷嶇収鎼滈+鏂囧瓧鎻愰棶锛孉I鍒嗘鍚彂寮忕瓟鐤?
-- =============================================
CREATE TABLE IF NOT EXISTS t_ai_qa_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    child_id BIGINT NOT NULL COMMENT '瀛愬コID',
    parent_id BIGINT NOT NULL COMMENT '瀹堕暱鐢ㄦ埛ID',
    conversation_id VARCHAR(64) NOT NULL COMMENT '浼氳瘽ID锛堝悓涓€杞璇濆叡浜級',
    subject VARCHAR(32) NOT NULL COMMENT '瀛︾锛歁ATH/CHINESE/ENGLISH',
    grade VARCHAR(16) NOT NULL COMMENT '骞寸骇',
    qa_type VARCHAR(16) NOT NULL COMMENT '鎻愰棶绫诲瀷锛歅HOTO鎷嶇収/TEXT鏂囧瓧',
    question_text TEXT DEFAULT NULL COMMENT '闂鏂囧瓧鍐呭',
    question_image_url VARCHAR(512) DEFAULT NULL COMMENT '闂鍥剧墖URL锛堟媿鐓ф悳棰橈級',
    ocr_text TEXT DEFAULT NULL COMMENT 'OCR璇嗗埆鎻愬彇鐨勬枃瀛?,
    ai_response_json TEXT DEFAULT NULL COMMENT 'AI鍥炲JSON锛堝惈鍒嗘瑙ｆ瀽銆佺煡璇嗙偣銆佸彉寮忛锛?,
    knowledge_tags VARCHAR(512) DEFAULT NULL COMMENT '鐭ヨ瘑鐐规爣绛撅紙閫楀彿鍒嗛殧锛?,
    is_follow_up TINYINT(1) NOT NULL DEFAULT 0 COMMENT '鏄惁涓鸿拷闂細0鍚?鏄?,
    step_count INT DEFAULT NULL COMMENT '瑙ｉ姝ラ鏁?,
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    created_at DATETIME DEFAULT NULL,
    updated_at DATETIME DEFAULT NULL
);

-- === t_ai_report.sql ===
DROP TABLE IF EXISTS t_ai_report;
-- =============================================
-- 瀛﹂湼鍒板 - AI瀛︽儏璇婃柇鎶ュ憡琛?DDL
-- =============================================
CREATE TABLE IF NOT EXISTS t_ai_report (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL COMMENT '瀹堕暱鐢ㄦ埛ID',
    child_name VARCHAR(50) DEFAULT NULL COMMENT '瀛愬コ濮撳悕',
    weak_subjects VARCHAR(500) NOT NULL COMMENT '钖勫急瀛︾锛堥€楀彿鍒嗛殧锛?,
    recent_scores VARCHAR(200) DEFAULT NULL COMMENT '杩戞湡鑰冭瘯鍒嗘暟',
    weak_point_desc TEXT COMMENT '钖勫急鐭ヨ瘑鐐规弿杩?,
    photo_urls VARCHAR(1000) DEFAULT NULL COMMENT '璇曞嵎/閿欓鐓х墖URL锛堥€楀彿鍒嗛殧锛?,
    report_content TEXT COMMENT 'AI鐢熸垚鎶ュ憡瀹屾暣鍐呭锛圝SON鏍煎紡锛?,
    preview_content TEXT COMMENT 'AI鐢熸垚鎶ュ憡棰勮鍐呭锛堥潪浼氬憳鍙锛?,
    report_type VARCHAR(20) NOT NULL DEFAULT 'PREVIEW' COMMENT '鎶ュ憡绫诲瀷锛歅REVIEW棰勮/FULL瀹屾暣',
    is_member_report TINYINT(1) NOT NULL DEFAULT 0 COMMENT '鏄惁浼氬憳鍏嶈垂鎶ュ憡锛?鍚?鏄?,
    price DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '鎶ュ憡浠锋牸',
    payment_status VARCHAR(20) NOT NULL DEFAULT 'UNPAID' COMMENT '鏀粯鐘舵€侊細UNPAID/PAID/FREE',
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- === t_ai_tutor_optimization.sql ===
DROP TABLE IF EXISTS t_ai_tutor_optimization;
CREATE TABLE IF NOT EXISTS t_ai_tutor_optimization (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '涓婚敭ID',
    user_id BIGINT NOT NULL COMMENT '鏁欏憳鐢ㄦ埛ID',
    optimization_type VARCHAR(32) NOT NULL COMMENT '浼樺寲绫诲瀷锛歊ESUME-绠€鍘嗕紭鍖? PRICING-瀹氫环鍒嗘瀽',
    university VARCHAR(128) DEFAULT NULL COMMENT '瀛︽牎鍚嶇О',
    major VARCHAR(64) DEFAULT NULL COMMENT '涓撲笟',
    grade VARCHAR(16) DEFAULT NULL COMMENT '骞寸骇',
    subjects VARCHAR(512) DEFAULT NULL COMMENT '杈呭绉戠洰',
    current_bio TEXT DEFAULT NULL COMMENT '褰撳墠涓汉绠€浠?,
    current_experience TEXT DEFAULT NULL COMMENT '褰撳墠鏁欏缁忛獙',
    current_rate INT DEFAULT NULL COMMENT '褰撳墠鏃惰柂',
    optimized_content TEXT DEFAULT NULL COMMENT 'AI浼樺寲缁撴灉JSON',
    preview_content TEXT DEFAULT NULL COMMENT '棰勮鎽樿',
    is_member_report TINYINT(1) NOT NULL DEFAULT 0 COMMENT '鏄惁浼氬憳鍏嶈垂',
    price DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '浠锋牸',
    payment_status VARCHAR(16) NOT NULL DEFAULT 'FREE' COMMENT '鏀粯鐘舵€侊細FREE/PAID',
    deleted INT NOT NULL DEFAULT 0 COMMENT '閫昏緫鍒犻櫎',
    created_at DATETIME DEFAULT NULL COMMENT '鍒涘缓鏃堕棿',
    updated_at DATETIME DEFAULT NULL COMMENT '鏇存柊鏃堕棿',
    PRIMARY KEY (id)
);

-- === t_audio_material.sql ===
DROP TABLE IF EXISTS t_audio_material;
-- =============================================
-- 瀛﹂湼鍒板 - 闊抽绱犳潗琛?DDL锛堟磱钁卞闄㈠涔犺祫婧愪綋绯伙級
-- =============================================
CREATE TABLE IF NOT EXISTS t_audio_material (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    grade VARCHAR(16) NOT NULL COMMENT '骞寸骇锛欸RADE_1~GRADE_6',
    subject VARCHAR(32) NOT NULL COMMENT '瀛︾锛歁ATH/CHINESE/ENGLISH',
    textbook_version VARCHAR(32) NOT NULL DEFAULT 'RENJIAO' COMMENT '鏁欐潗鐗堟湰',
    title VARCHAR(200) NOT NULL COMMENT '绱犳潗鏍囬',
    audio_type VARCHAR(32) NOT NULL COMMENT '闊抽绫诲瀷锛欶OLLOW_READ璺熻/LISTEN鍚姏/READ鏈楄',
    audio_url VARCHAR(512) NOT NULL COMMENT '闊抽鏂囦欢URL',
    duration_seconds INT NOT NULL DEFAULT 0 COMMENT '闊抽鏃堕暱锛堢锛?,
    text_content TEXT DEFAULT NULL COMMENT '瀵瑰簲鏂囨湰鍐呭',
    reference_text TEXT DEFAULT NULL COMMENT '鏍囧噯鍙傝€冩枃鏈紙鐢ㄤ簬璇勬祴姣斿锛?,
    knowledge_point_id BIGINT DEFAULT NULL COMMENT '鍏宠仈鐭ヨ瘑鐐笽D',
    vip_only TINYINT(1) NOT NULL DEFAULT 0 COMMENT '鏄惁浠呬細鍛?,
    sort_order INT NOT NULL DEFAULT 0 COMMENT '鎺掑簭鍙?,
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    created_at DATETIME DEFAULT NULL,
    updated_at DATETIME DEFAULT NULL
);

-- === t_booking.sql ===
DROP TABLE IF EXISTS t_booking;
CREATE TABLE IF NOT EXISTS t_booking (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '涓婚敭ID',
    parent_id BIGINT NOT NULL COMMENT '瀹堕暱鐢ㄦ埛ID',
    tutor_id BIGINT NOT NULL COMMENT '鏁欏憳鐢ㄦ埛ID',
    tutor_profile_id BIGINT NOT NULL COMMENT '鏁欏憳妗ｆID',
    subject VARCHAR(64) NOT NULL COMMENT '杈呭绉戠洰',
    booking_date DATE NOT NULL COMMENT '涓婅鏃ユ湡',
    start_time TIME NOT NULL COMMENT '寮€濮嬫椂闂?,
    end_time TIME NOT NULL COMMENT '缁撴潫鏃堕棿',
    address VARCHAR(256) DEFAULT NULL COMMENT '涓婅鍦板潃',
    message VARCHAR(512) DEFAULT NULL COMMENT '瀹堕暱鐣欒█',
    status VARCHAR(16) NOT NULL DEFAULT 'PENDING' COMMENT '鐘舵€侊細PENDING/CONFIRMED/COMPLETED/CANCELLED/REJECTED',
    reply VARCHAR(256) DEFAULT NULL COMMENT '鏁欏憳鍥炲',
    deleted INT NOT NULL DEFAULT 0 COMMENT '閫昏緫鍒犻櫎',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '鍒涘缓鏃堕棿',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '鏇存柊鏃堕棿',
    PRIMARY KEY (id)
);

-- === t_child.sql ===
DROP TABLE IF EXISTS t_child;
-- =============================================
-- 瀛﹂湼鍒板 - 瀛愬コ妗ｆ琛?DDL锛堝闀跨灏忓鐢熷涔犳ā鍧楋級
-- =============================================
CREATE TABLE IF NOT EXISTS t_child (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    parent_id BIGINT NOT NULL COMMENT '瀹堕暱鐢ㄦ埛ID',
    name VARCHAR(50) NOT NULL COMMENT '瀛愬コ濮撳悕',
    grade VARCHAR(16) NOT NULL COMMENT '骞寸骇锛欸RADE_1~GRADE_6',
    textbook_version VARCHAR(32) NOT NULL DEFAULT 'RENJIAO' COMMENT '鏁欐潗鐗堟湰锛歊ENJIAO/BEISHIDA/JIAOSHE/SUDAJIAO/WAIXIN/SHANGHAJIAO/OTHER',
    school VARCHAR(128) DEFAULT NULL COMMENT '瀛︽牎鍚嶇О',
    avatar VARCHAR(512) DEFAULT NULL COMMENT '澶村儚URL',
    deleted TINYINT(1) NOT NULL DEFAULT 0 COMMENT '閫昏緫鍒犻櫎锛?-姝ｅ父 1-鍒犻櫎',
    created_at DATETIME DEFAULT NULL COMMENT '鍒涘缓鏃堕棿',
    updated_at DATETIME DEFAULT NULL COMMENT '鏇存柊鏃堕棿'
);

-- === t_child_achievement.sql ===
DROP TABLE IF EXISTS t_child_achievement;
-- =============================================
-- 瀛﹂湼鍒板 - 鎴愰暱婵€鍔辫〃 DDL
-- =============================================
CREATE TABLE IF NOT EXISTS t_child_achievement (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    child_id BIGINT NOT NULL COMMENT '瀛愬コID',
    achievement_type VARCHAR(32) NOT NULL COMMENT '鍕嬬珷绫诲瀷锛歋TUDY_STAR/CALCULATION_MASTER/READING_STAR/PERSISTENCE/MATH_WIZARD/ENGLISH_STAR',
    title VARCHAR(64) NOT NULL COMMENT '鍕嬬珷鍚嶇О',
    icon VARCHAR(256) DEFAULT NULL COMMENT '鍕嬬珷鍥炬爣URL',
    points INT NOT NULL DEFAULT 0 COMMENT '鑾峰緱绉垎',
    skin VARCHAR(64) DEFAULT NULL COMMENT '瑙ｉ攣鐨偆',
    source VARCHAR(32) NOT NULL DEFAULT 'SYSTEM' COMMENT '鏉ユ簮锛歋YSTEM绯荤粺鍙戞斁/PARENT瀹堕暱浠诲姟',
    task_name VARCHAR(128) DEFAULT NULL COMMENT '瀹堕暱浠诲姟鍚嶇О',
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    created_at DATETIME DEFAULT NULL,
    updated_at DATETIME DEFAULT NULL
);

-- === t_course_watch_record.sql ===
DROP TABLE IF EXISTS t_course_watch_record;
-- =============================================
-- 瀛﹂湼鍒板 - 涓婅瑙傜湅璁板綍琛?DDL
-- =============================================
CREATE TABLE IF NOT EXISTS t_course_watch_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    child_id BIGINT NOT NULL COMMENT '瀛愬コID',
    resource_id BIGINT NOT NULL COMMENT '瀛︿範璧勬簮ID',
    watch_duration_seconds INT NOT NULL DEFAULT 0 COMMENT '绱瑙傜湅鏃堕暱锛堢锛?,
    progress_percent INT NOT NULL DEFAULT 0 COMMENT '杩涘害鐧惧垎姣?0-100',
    last_position_seconds INT NOT NULL DEFAULT 0 COMMENT '涓婃鎾斁浣嶇疆锛堢锛?,
    completed TINYINT(1) NOT NULL DEFAULT 0 COMMENT '鏄惁瀹屾垚锛?鍚?鏄?,
    completed_at DATETIME DEFAULT NULL COMMENT '瀹屾垚鏃堕棿',
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    created_at DATETIME DEFAULT NULL,
    updated_at DATETIME DEFAULT NULL
);

-- === t_exercise_answer_record.sql ===
DROP TABLE IF EXISTS t_exercise_answer_record;
-- =============================================
-- 瀛﹂湼鍒板 - 涔犻浣滅瓟璁板綍琛?DDL
-- =============================================
CREATE TABLE IF NOT EXISTS t_exercise_answer_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    child_id BIGINT NOT NULL COMMENT '瀛愬コID',
    resource_id BIGINT DEFAULT NULL COMMENT '鍏宠仈瀛︿範璧勬簮ID锛堥搴撻鐩椂涓虹┖锛?,
    question_id BIGINT DEFAULT NULL COMMENT '棰樼洰ID',
    subject VARCHAR(32) NOT NULL COMMENT '瀛︾锛歁ATH/CHINESE/ENGLISH',
    knowledge_point VARCHAR(128) DEFAULT NULL COMMENT '鐭ヨ瘑鐐?,
    answer TEXT DEFAULT NULL COMMENT '瀛︾敓绛旀',
    correct TINYINT(1) NOT NULL DEFAULT 0 COMMENT '鏄惁姝ｇ‘锛?鍚?鏄?,
    cost_seconds INT DEFAULT NULL COMMENT '鑰楁椂锛堢锛?,
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    created_at DATETIME DEFAULT NULL,
    updated_at DATETIME DEFAULT NULL
);

-- === t_exercise_bank.sql ===
DROP TABLE IF EXISTS t_exercise_bank;
-- =============================================
-- 瀛﹂湼鍒板 - 涔犻搴撹〃 DDL锛堟磱钁卞闄㈠涔犺祫婧愪綋绯伙級
-- =============================================
CREATE TABLE IF NOT EXISTS t_exercise_bank (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    grade VARCHAR(16) NOT NULL COMMENT '骞寸骇锛欸RADE_1~GRADE_6',
    subject VARCHAR(32) NOT NULL COMMENT '瀛︾锛歁ATH/CHINESE/ENGLISH',
    textbook_version VARCHAR(32) NOT NULL DEFAULT 'RENJIAO' COMMENT '鏁欐潗鐗堟湰',
    knowledge_point_id BIGINT DEFAULT NULL COMMENT '鍏宠仈鐭ヨ瘑鐐笽D',
    question_type VARCHAR(32) NOT NULL COMMENT '棰樼洰绫诲瀷锛欳HOICE/FILL/TRUE_FALSE/SHORT_ANSWER',
    question_content TEXT NOT NULL COMMENT '棰樼洰鍐呭JSON锛堥骞?閫夐」锛?,
    options TEXT DEFAULT NULL COMMENT '閫夐」JSON鏁扮粍锛堥€夋嫨棰樼敤锛?,
    correct_answer TEXT NOT NULL COMMENT '姝ｇ‘绛旀',
    explanation TEXT DEFAULT NULL COMMENT '绛旀瑙ｆ瀽',
    difficulty VARCHAR(16) NOT NULL DEFAULT 'MEDIUM' COMMENT '闅惧害锛欵ASY/MEDIUM/HARD',
    source_type VARCHAR(32) NOT NULL DEFAULT 'SYSTEM' COMMENT '鏉ユ簮锛歋YSTEM绯荤粺/AI鐢熸垚/USER鑷畾涔?,
    vip_only TINYINT(1) NOT NULL DEFAULT 0 COMMENT '鏄惁浠呬細鍛?,
    use_count INT NOT NULL DEFAULT 0 COMMENT '浣跨敤娆℃暟',
    correct_rate INT DEFAULT NULL COMMENT '姝ｇ‘鐜囷紙鐧惧垎姣旓級',
    sort_order INT NOT NULL DEFAULT 0 COMMENT '鎺掑簭鍙?,
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    created_at DATETIME DEFAULT NULL,
    updated_at DATETIME DEFAULT NULL
);

-- === t_feature_order.sql ===
DROP TABLE IF EXISTS t_feature_order;
-- =============================================
-- 瀛﹂湼鍒板 - 鍔熻兘璁㈠崟琛?DDL (P1: 绠€鍘嗙疆椤?鍔犳€ュ鏍?鍗曟浠樿垂)
-- =============================================
CREATE TABLE IF NOT EXISTS t_feature_order (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '涓婚敭ID',
    user_id BIGINT NOT NULL COMMENT '鐢ㄦ埛ID',
    feature_type VARCHAR(32) NOT NULL COMMENT '鍔熻兘绫诲瀷锛歊ESUME_PIN/EXPEDITE_VERIFY',
    target_id BIGINT DEFAULT NULL COMMENT '鍏宠仈鐩爣ID锛坱utor_profile_id/verify_record_id锛?,
    amount DECIMAL(10,2) NOT NULL COMMENT '鏀粯閲戦',
    payment_status VARCHAR(16) NOT NULL DEFAULT 'UNPAID' COMMENT '鏀粯鐘舵€侊細UNPAID/PAID',
    payment_time DATETIME DEFAULT NULL COMMENT '鏀粯鏃堕棿',
    transaction_id VARCHAR(64) DEFAULT NULL COMMENT '寰俊鏀粯浜ゆ槗鍙?,
    expire_time DATETIME DEFAULT NULL COMMENT '鍔熻兘鍒版湡鏃堕棿锛堝绠€鍘嗙疆椤?澶╁悗鍒版湡锛?,
    deleted INT NOT NULL DEFAULT 0 COMMENT '閫昏緫鍒犻櫎锛?-姝ｅ父 1-鍒犻櫎',
    created_at DATETIME DEFAULT NULL COMMENT '鍒涘缓鏃堕棿',
    updated_at DATETIME DEFAULT NULL COMMENT '鏇存柊鏃堕棿',
    PRIMARY KEY (id)
);

-- ALTER: t_tutor_profile 娣诲姞缃《鍒版湡鏃堕棿瀛楁
ALTER TABLE t_tutor_profile ADD COLUMN pinned_until DATETIME DEFAULT NULL COMMENT '缃《鍒版湡鏃堕棿' AFTER active;

-- ALTER: t_verify_record 娣诲姞鍔犳€ユ爣璁板瓧娈?
ALTER TABLE t_verify_record ADD COLUMN expedited INT NOT NULL DEFAULT 0 COMMENT '鏄惁鍔犳€ワ細0鍚?鏄? AFTER remark;

-- === t_knowledge_point.sql ===
DROP TABLE IF EXISTS t_knowledge_point;
-- =============================================
-- 瀛﹂湼鍒板 - 鐭ヨ瘑鐐硅〃 DDL锛堟磱钁卞闄㈠涔犺祫婧愪綋绯伙級
-- 鏍戝舰缁撴瀯锛氭寜骞寸骇+瀛︾+鏁欐潗鐗堟湰缁勭粐锛宲arent_id鑷叧鑱?
-- =============================================
CREATE TABLE IF NOT EXISTS t_knowledge_point (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    parent_id BIGINT DEFAULT NULL COMMENT '鐖惰妭鐐笽D锛圢ULL涓洪《绾х珷鑺傦級',
    grade VARCHAR(16) NOT NULL COMMENT '骞寸骇锛欸RADE_1~GRADE_6',
    subject VARCHAR(32) NOT NULL COMMENT '瀛︾锛歁ATH/CHINESE/ENGLISH',
    textbook_version VARCHAR(32) NOT NULL DEFAULT 'RENJIAO' COMMENT '鏁欐潗鐗堟湰',
    name VARCHAR(128) NOT NULL COMMENT '鐭ヨ瘑鐐?绔犺妭鍚嶇О',
    description TEXT DEFAULT NULL COMMENT '鐭ヨ瘑鐐规弿杩?,
    depth INT NOT NULL DEFAULT 1 COMMENT '灞傜骇娣卞害锛?=鍐?2=鍗曞厓 3=绔?4=鑺?5=鐭ヨ瘑鐐?,
    sort_order INT NOT NULL DEFAULT 0 COMMENT '鍚岀骇鎺掑簭鍙?,
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    created_at DATETIME DEFAULT NULL,
    updated_at DATETIME DEFAULT NULL
);

-- === t_learning_plan_config.sql ===
DROP TABLE IF EXISTS t_learning_plan_config;
-- =============================================
-- 瀛﹂湼鍒板 - 瀛︿範璁″垝閰嶇疆琛?DDL
-- =============================================
CREATE TABLE IF NOT EXISTS t_learning_plan_config (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    child_id BIGINT NOT NULL COMMENT '瀛愬コID',
    daily_duration_minutes INT NOT NULL DEFAULT 30 COMMENT '姣忔棩瀛︿範鏃堕暱锛堝垎閽燂級',
    math_weight INT NOT NULL DEFAULT 35 COMMENT '鏁板瀛︾鏉冮噸%',
    chinese_weight INT NOT NULL DEFAULT 35 COMMENT '璇枃瀛︾鏉冮噸%',
    english_weight INT NOT NULL DEFAULT 30 COMMENT '鑻辫瀛︾鏉冮噸%',
    special_calculation TINYINT(1) NOT NULL DEFAULT 1 COMMENT '璁＄畻涓撻」寮€鍏筹細0鍏?寮€',
    special_application TINYINT(1) NOT NULL DEFAULT 1 COMMENT '搴旂敤棰樹笓椤瑰紑鍏筹細0鍏?寮€',
    special_literacy TINYINT(1) NOT NULL DEFAULT 0 COMMENT '璇嗗瓧涓撻」寮€鍏筹細0鍏?寮€',
    special_words TINYINT(1) NOT NULL DEFAULT 0 COMMENT '鑳屽崟璇嶄笓椤瑰紑鍏筹細0鍏?寮€',
    auto_generate TINYINT(1) NOT NULL DEFAULT 1 COMMENT '鏄惁鑷姩鐢熸垚姣忔棩浠诲姟锛?鍚?鏄?,
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    created_at DATETIME DEFAULT NULL,
    updated_at DATETIME DEFAULT NULL
);

-- === t_learning_resource.sql ===
DROP TABLE IF EXISTS t_learning_resource;
-- =============================================
-- 瀛﹂湼鍒板 - 瀛︿範璧勬簮琛?DDL
-- =============================================
CREATE TABLE IF NOT EXISTS t_learning_resource (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    grade VARCHAR(16) NOT NULL COMMENT '骞寸骇锛欸RADE_1~GRADE_6',
    subject VARCHAR(32) NOT NULL COMMENT '瀛︾锛歁ATH/CHINESE/ENGLISH',
    textbook_version VARCHAR(32) DEFAULT NULL COMMENT '鏁欐潗鐗堟湰',
    resource_type VARCHAR(32) NOT NULL COMMENT '璧勬簮绫诲瀷锛歏IDEO/EXERCISE/PDF/QUESTION',
    title VARCHAR(128) NOT NULL COMMENT '璧勬簮鏍囬',
    cover_url VARCHAR(512) DEFAULT NULL COMMENT '灏侀潰鍥綰RL',
    content_url VARCHAR(512) DEFAULT NULL COMMENT '璧勬簮鍐呭URL',
    duration_seconds INT DEFAULT NULL COMMENT '瑙嗛鏃堕暱锛堢锛?,
    knowledge_point VARCHAR(128) DEFAULT NULL COMMENT '鐭ヨ瘑鐐?,
    tags VARCHAR(256) DEFAULT NULL COMMENT '鏍囩JSON',
    source_type VARCHAR(32) DEFAULT NULL COMMENT '瑙嗛婧愮被鍨嬶細MP4/BILIBILI/SMARTEDU',
    subtitle_url VARCHAR(512) DEFAULT NULL COMMENT '瀛楀箷鏂囦欢URL',
    quality_levels VARCHAR(512) DEFAULT NULL COMMENT '鐢昏川绛夌骇JSON',
    knowledge_markers VARCHAR(1024) DEFAULT NULL COMMENT '鐭ヨ瘑鐐规爣璁癑SON',
    status VARCHAR(16) DEFAULT 'PUBLISHED' COMMENT '璧勬簮鐘舵€?,
    description VARCHAR(512) DEFAULT NULL COMMENT '璧勬簮鎻忚堪',
    publish_at DATETIME DEFAULT NULL COMMENT '鍙戝竷鏃堕棿',
    unpublish_at DATETIME DEFAULT NULL COMMENT '涓嬫灦鏃堕棿',
    operator_id BIGINT DEFAULT NULL COMMENT '鎿嶄綔鍛?ID',
    vip_only TINYINT(1) NOT NULL DEFAULT 0 COMMENT '鏄惁浠呬細鍛樺彲瑙侊細0鍚?鏄?,
    sort_order INT NOT NULL DEFAULT 0 COMMENT '鎺掑簭鍙?,
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    created_at DATETIME DEFAULT NULL,
    updated_at DATETIME DEFAULT NULL
);

-- === t_learning_task.sql ===
DROP TABLE IF EXISTS t_learning_task;
-- =============================================
-- 瀛﹂湼鍒板 - 瀛︿範浠诲姟璁板綍琛?DDL
-- =============================================
CREATE TABLE IF NOT EXISTS t_learning_task (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    child_id BIGINT NOT NULL COMMENT '瀛愬コID',
    plan_config_id BIGINT DEFAULT NULL COMMENT '瀛︿範璁″垝閰嶇疆ID',
    task_type VARCHAR(32) NOT NULL COMMENT '浠诲姟绫诲瀷锛歏IDEO/EXERCISE/REVIEW/SPECIAL_CALCULATION/SPECIAL_APPLICATION/SPECIAL_LITERACY/SPECIAL_WORDS',
    subject VARCHAR(32) NOT NULL COMMENT '瀛︾锛歁ATH/CHINESE/ENGLISH',
    title VARCHAR(128) NOT NULL COMMENT '浠诲姟鏍囬',
    content TEXT DEFAULT NULL COMMENT '浠诲姟鍐呭鎻忚堪/JSON',
    resource_id BIGINT DEFAULT NULL COMMENT '鍏宠仈瀛︿範璧勬簮ID',
    status VARCHAR(32) NOT NULL DEFAULT 'PENDING' COMMENT '鐘舵€侊細PENDING/IN_PROGRESS/COMPLETED/SKIPPED',
    assigned_date DATE NOT NULL COMMENT '浠诲姟鏃ユ湡',
    completed_at DATETIME DEFAULT NULL COMMENT '瀹屾垚鏃堕棿',
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    created_at DATETIME DEFAULT NULL,
    updated_at DATETIME DEFAULT NULL
);

-- === t_membership.sql ===
DROP TABLE IF EXISTS t_membership;
-- =============================================
-- 瀛﹂湼鍒板 - 浼氬憳琛?DDL
-- =============================================
CREATE TABLE IF NOT EXISTS t_membership (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL COMMENT '鐢ㄦ埛ID',
    member_type VARCHAR(20) NOT NULL COMMENT '浼氬憳绫诲瀷锛歅ARENT_MONTH/PARENT_YEAR/STUDENT_MONTH',
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' COMMENT '鐘舵€侊細ACTIVE/EXPIRED/CANCELLED',
    start_time DATETIME NOT NULL COMMENT '鐢熸晥鏃堕棿',
    expire_time DATETIME NOT NULL COMMENT '鍒版湡鏃堕棿',
    auto_renew TINYINT(1) NOT NULL DEFAULT 0 COMMENT '鏄惁鑷姩缁垂锛?鍚?鏄?,
    transaction_id VARCHAR(64) DEFAULT NULL COMMENT '寰俊鏀粯浜ゆ槗鍙?,
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- === t_micro_course.sql ===
DROP TABLE IF EXISTS t_micro_course;
-- =============================================
-- 瀛﹂湼鍒板 - 寰瑙嗛琛?DDL锛堟磱钁卞闄㈠涔犺祫婧愪綋绯伙級
-- =============================================
CREATE TABLE IF NOT EXISTS t_micro_course (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    grade VARCHAR(16) NOT NULL COMMENT '骞寸骇锛欸RADE_1~GRADE_6',
    subject VARCHAR(32) NOT NULL COMMENT '瀛︾锛歁ATH/CHINESE/ENGLISH',
    textbook_version VARCHAR(32) NOT NULL DEFAULT 'RENJIAO' COMMENT '鏁欐潗鐗堟湰',
    chapter_id BIGINT DEFAULT NULL COMMENT '鍏宠仈鏁欐潗绔犺妭ID',
    knowledge_point_id BIGINT DEFAULT NULL COMMENT '鍏宠仈鐭ヨ瘑鐐笽D',
    title VARCHAR(200) NOT NULL COMMENT '寰鏍囬',
    description TEXT DEFAULT NULL COMMENT '寰绠€浠?,
    cover_url VARCHAR(512) DEFAULT NULL COMMENT '灏侀潰鍥綰RL',
    video_url VARCHAR(512) NOT NULL COMMENT '瑙嗛鎾斁URL',
    duration_seconds INT NOT NULL DEFAULT 0 COMMENT '瑙嗛鏃堕暱锛堢锛?,
    resolution VARCHAR(16) DEFAULT '1080P' COMMENT '鍒嗚鲸鐜?,
    file_size_mb INT DEFAULT NULL COMMENT '鏂囦欢澶у皬锛圡B锛?,
    difficulty VARCHAR(16) NOT NULL DEFAULT 'MEDIUM' COMMENT '闅惧害锛欵ASY/MEDIUM/HARD',
    vip_only TINYINT(1) NOT NULL DEFAULT 0 COMMENT '鏄惁浠呬細鍛橈細0鍚?鏄?,
    play_count INT NOT NULL DEFAULT 0 COMMENT '鎾斁娆℃暟',
    sort_order INT NOT NULL DEFAULT 0 COMMENT '鎺掑簭鍙?,
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    created_at DATETIME DEFAULT NULL,
    updated_at DATETIME DEFAULT NULL
);

-- === t_parent_control_config.sql ===
DROP TABLE IF EXISTS t_parent_control_config;
-- =============================================
-- 瀛﹂湼鍒板 - 瀹堕暱绠℃帶閰嶇疆琛?DDL
-- =============================================
CREATE TABLE IF NOT EXISTS t_parent_control_config (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    parent_id BIGINT NOT NULL COMMENT '瀹堕暱鐢ㄦ埛ID',
    child_id BIGINT NOT NULL COMMENT '瀛愬コID',
    daily_duration_minutes INT NOT NULL DEFAULT 60 COMMENT '姣忔棩鍙敤鏃堕暱锛堝垎閽燂級',
    rest_interval_minutes INT NOT NULL DEFAULT 20 COMMENT '鍗曟浼戞伅闂撮殧锛堝垎閽燂級',
    rest_duration_minutes INT NOT NULL DEFAULT 5 COMMENT '浼戞伅鏃堕暱锛堝垎閽燂級',
    forbidden_start_time TIME DEFAULT '22:00:00' COMMENT '绂佺敤鏃舵寮€濮?,
    forbidden_end_time TIME DEFAULT '07:00:00' COMMENT '绂佺敤鏃舵缁撴潫',
    locked TINYINT(1) NOT NULL DEFAULT 0 COMMENT '鏄惁閿佸畾瀛︿範鍏ュ彛锛?鍚?鏄?,
    eye_protection TINYINT(1) NOT NULL DEFAULT 0 COMMENT '鏄惁寮€鍚姢鐪兼ā寮忥細0鍚?鏄?,
    current_page VARCHAR(256) DEFAULT NULL COMMENT '瀛╁瓙褰撳墠瀛︿範椤甸潰',
    last_login_at DATETIME DEFAULT NULL COMMENT '鏈€鍚庣櫥褰曟椂闂?,
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    created_at DATETIME DEFAULT NULL,
    updated_at DATETIME DEFAULT NULL
);

-- === t_resource_chapter.sql ===
DROP TABLE IF EXISTS t_resource_chapter;
-- =============================================
-- 瀛﹂湼鍒板 - 鏁欐潗绔犺妭琛?DDL锛堟磱钁卞闄㈠涔犺祫婧愪綋绯伙級
-- 鎸夊勾绾?瀛︾+鏁欐潗鐗堟湰缁勭粐绔犺妭鐩綍鏍?
-- =============================================
CREATE TABLE IF NOT EXISTS t_resource_chapter (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    parent_id BIGINT DEFAULT NULL COMMENT '鐖剁珷鑺侷D锛圢ULL涓洪《绾э級',
    grade VARCHAR(16) NOT NULL COMMENT '骞寸骇锛欸RADE_1~GRADE_6',
    subject VARCHAR(32) NOT NULL COMMENT '瀛︾锛歁ATH/CHINESE/ENGLISH',
    textbook_version VARCHAR(32) NOT NULL DEFAULT 'RENJIAO' COMMENT '鏁欐潗鐗堟湰',
    title VARCHAR(200) NOT NULL COMMENT '绔犺妭鏍囬',
    depth INT NOT NULL DEFAULT 1 COMMENT '灞傜骇锛?=鍐?2=鍗曞厓 3=璇?绔?,
    sort_order INT NOT NULL DEFAULT 0 COMMENT '鍚岀骇鎺掑簭鍙?,
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    created_at DATETIME DEFAULT NULL,
    updated_at DATETIME DEFAULT NULL
);

-- === t_study_material.sql ===
DROP TABLE IF EXISTS t_study_material;
-- =============================================
-- 瀛﹂湼鍒板 - 瀛︿範璧勬枡琛?DDL锛堟磱钁卞闄㈠涔犺祫婧愪綋绯伙級
-- 鐢熷瓧鍗＄墖/鎬濈淮瀵煎浘/PDF璧勬枡绛?
-- =============================================
CREATE TABLE IF NOT EXISTS t_study_material (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    grade VARCHAR(16) NOT NULL COMMENT '骞寸骇锛欸RADE_1~GRADE_6',
    subject VARCHAR(32) NOT NULL COMMENT '瀛︾锛歁ATH/CHINESE/ENGLISH',
    textbook_version VARCHAR(32) NOT NULL DEFAULT 'RENJIAO' COMMENT '鏁欐潗鐗堟湰',
    title VARCHAR(200) NOT NULL COMMENT '璧勬枡鏍囬',
    material_type VARCHAR(32) NOT NULL COMMENT '璧勬枡绫诲瀷锛欶LASHCARD鐢熷瓧鍗＄墖/MINDMAP鎬濈淮瀵煎浘/PDF鏂囨。/IMAGE鍥剧墖',
    content_url VARCHAR(512) NOT NULL COMMENT '璧勬枡鏂囦欢URL',
    preview_url VARCHAR(512) DEFAULT NULL COMMENT '棰勮鍥綰RL',
    description TEXT DEFAULT NULL COMMENT '璧勬枡鎻忚堪',
    knowledge_point_id BIGINT DEFAULT NULL COMMENT '鍏宠仈鐭ヨ瘑鐐笽D',
    vip_only TINYINT(1) NOT NULL DEFAULT 0 COMMENT '鏄惁浠呬細鍛?,
    download_count INT NOT NULL DEFAULT 0 COMMENT '涓嬭浇娆℃暟',
    sort_order INT NOT NULL DEFAULT 0 COMMENT '鎺掑簭鍙?,
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    created_at DATETIME DEFAULT NULL,
    updated_at DATETIME DEFAULT NULL
);

-- === t_subject.sql ===
DROP TABLE IF EXISTS t_subject;
-- =============================================
-- 瀛﹂湼鍒板 - 瀛︾琛?DDL锛堟磱钁卞闄㈠涔犺祫婧愪綋绯伙級
-- =============================================
CREATE TABLE IF NOT EXISTS t_subject (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(32) NOT NULL COMMENT '瀛︾缂栫爜锛歁ATH/CHINESE/ENGLISH',
    name VARCHAR(32) NOT NULL COMMENT '瀛︾鍚嶇О',
    icon VARCHAR(256) DEFAULT NULL COMMENT '瀛︾鍥炬爣URL',
    color VARCHAR(16) DEFAULT NULL COMMENT '瀛︾涓婚鑹?,
    sort_order INT NOT NULL DEFAULT 0 COMMENT '鎺掑簭鍙?,
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    created_at DATETIME DEFAULT NULL,
    updated_at DATETIME DEFAULT NULL
);

-- === t_test_paper.sql ===
DROP TABLE IF EXISTS t_test_paper;
-- =============================================
-- 瀛﹂湼鍒板 - 璇曞嵎琛?DDL锛堟磱钁卞闄㈠涔犺祫婧愪綋绯伙級
-- =============================================
CREATE TABLE IF NOT EXISTS t_test_paper (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    grade VARCHAR(16) NOT NULL COMMENT '骞寸骇锛欸RADE_1~GRADE_6',
    subject VARCHAR(32) NOT NULL COMMENT '瀛︾锛歁ATH/CHINESE/ENGLISH',
    textbook_version VARCHAR(32) NOT NULL DEFAULT 'RENJIAO' COMMENT '鏁欐潗鐗堟湰',
    title VARCHAR(200) NOT NULL COMMENT '璇曞嵎鏍囬',
    paper_type VARCHAR(32) NOT NULL COMMENT '璇曞嵎绫诲瀷锛歎NIT鍗曞厓娴嬭瘯/MID_TERM鏈熶腑/FINAL鏈熸湯/MOCK妯℃嫙',
    description TEXT DEFAULT NULL COMMENT '璇曞嵎璇存槑',
    total_score INT NOT NULL DEFAULT 100 COMMENT '鎬诲垎',
    duration_minutes INT NOT NULL DEFAULT 60 COMMENT '寤鸿鐢ㄦ椂锛堝垎閽燂級',
    questions_json TEXT NOT NULL COMMENT '棰樼洰闆嗗悎JSON锛堝寘鍚鐩甀D鍜屽垎鍊硷級',
    cover_url VARCHAR(512) DEFAULT NULL COMMENT '灏侀潰鍥綰RL',
    vip_only TINYINT(1) NOT NULL DEFAULT 0 COMMENT '鏄惁浠呬細鍛?,
    use_count INT NOT NULL DEFAULT 0 COMMENT '浣跨敤娆℃暟',
    sort_order INT NOT NULL DEFAULT 0 COMMENT '鎺掑簭鍙?,
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    created_at DATETIME DEFAULT NULL,
    updated_at DATETIME DEFAULT NULL
);

-- === t_tutor_profile.sql ===
DROP TABLE IF EXISTS t_tutor_profile;
CREATE TABLE IF NOT EXISTS t_tutor_profile (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '涓婚敭ID',
    user_id BIGINT NOT NULL COMMENT '鐢ㄦ埛ID',
    university VARCHAR(128) NOT NULL COMMENT '瀛︽牎鍚嶇О',
    major VARCHAR(64) DEFAULT NULL COMMENT '涓撲笟',
    grade VARCHAR(16) DEFAULT NULL COMMENT '骞寸骇',
    subjects VARCHAR(512) NOT NULL COMMENT '鍙緟瀵肩鐩紙閫楀彿鍒嗛殧锛?,
    hourly_rate INT NOT NULL COMMENT '鏃惰柂锛堝厓/灏忔椂锛?,
    bio VARCHAR(1024) DEFAULT NULL COMMENT '涓汉绠€浠?,
    experience VARCHAR(1024) DEFAULT NULL COMMENT '鏁欏缁忛獙',
    districts VARCHAR(512) DEFAULT NULL COMMENT '涓婅鍖哄煙锛堥€楀彿鍒嗛殧锛?,
    available_times TEXT DEFAULT NULL COMMENT '涓婅鏃舵JSON',
    rating DECIMAL(3,2) NOT NULL DEFAULT 0.00 COMMENT '璇勫垎',
    review_count INT NOT NULL DEFAULT 0 COMMENT '璇勪环鏁伴噺',
    order_count INT NOT NULL DEFAULT 0 COMMENT '瀹屾垚璁㈠崟鏁?,
    active TINYINT(1) NOT NULL DEFAULT 1 COMMENT '鏄惁涓婃灦',
    deleted INT NOT NULL DEFAULT 0 COMMENT '閫昏緫鍒犻櫎',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '鍒涘缓鏃堕棿',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '鏇存柊鏃堕棿',
    PRIMARY KEY (id),
    INDEX idx_subjects (subjects))
);

-- === t_user.sql ===
DROP TABLE IF EXISTS t_user;
-- =============================================
-- 瀛﹂湼鍒板 - 鐢ㄦ埛琛?DDL
-- =============================================



-- 鐢ㄦ埛琛?
CREATE TABLE IF NOT EXISTS t_user (
    id           BIGINT       NOT NULL AUTO_INCREMENT COMMENT '涓婚敭ID',
    openid       VARCHAR(64)  NOT NULL COMMENT '寰俊openid',
    unionid      VARCHAR(64)  DEFAULT NULL COMMENT '寰俊unionid',
    phone        VARCHAR(20)  DEFAULT NULL COMMENT '鎵嬫満鍙?,
    nickname     VARCHAR(64)  DEFAULT '寰俊鐢ㄦ埛' COMMENT '鏄电О',
    avatar       VARCHAR(512) DEFAULT NULL COMMENT '澶村儚URL',
    identity_type VARCHAR(16) DEFAULT NULL COMMENT '韬唤绫诲瀷锛歅ARENT/STUDENT',
    verify_status VARCHAR(16) DEFAULT 'NONE' COMMENT '璁よ瘉鐘舵€侊細NONE/PENDING/APPROVED/REJECTED',
    real_name    VARCHAR(32)  DEFAULT NULL COMMENT '鐪熷疄濮撳悕',
    deleted      TINYINT(1)   DEFAULT 0 COMMENT '鏄惁鍒犻櫎锛?鍚?鏄?,
    created_at   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '鍒涘缓鏃堕棿',
    updated_at   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '鏇存柊鏃堕棿',
    PRIMARY KEY (id)
);

-- === t_verify_record.sql ===
DROP TABLE IF EXISTS t_verify_record;
-- 璁よ瘉璁板綍琛?
CREATE TABLE IF NOT EXISTS t_verify_record (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '涓婚敭ID',
    user_id BIGINT NOT NULL COMMENT '鐢ㄦ埛ID',
    verify_type VARCHAR(32) NOT NULL COMMENT '璁よ瘉绫诲瀷锛歅ARENT_IDCARD/STUDENT_IDCARD/STUDENT_CARD',
    real_name VARCHAR(64) NOT NULL COMMENT '鐪熷疄濮撳悕',
    id_card_no VARCHAR(128) NOT NULL COMMENT '韬唤璇佸彿锛堣劚鏁忓瓨鍌級',
    student_card_image VARCHAR(512) DEFAULT NULL COMMENT '瀛︾敓璇佺収鐗嘦RL',
    verify_status VARCHAR(16) NOT NULL DEFAULT 'NONE' COMMENT '瀹℃牳鐘舵€侊細NONE/PENDING/APPROVED/REJECTED',
    remark VARCHAR(256) DEFAULT NULL COMMENT '瀹℃牳澶囨敞',
    deleted INT NOT NULL DEFAULT 0 COMMENT '閫昏緫鍒犻櫎锛?-姝ｅ父 1-鍒犻櫎',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '鍒涘缓鏃堕棿',
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '鏇存柊鏃堕棿',
    PRIMARY KEY (id)
);

-- === t_wrong_question_bank.sql ===
DROP TABLE IF EXISTS t_wrong_question_bank;
-- =============================================
-- 瀛﹂湼鍒板 - 閿欓搴撹〃 DDL
-- =============================================
CREATE TABLE IF NOT EXISTS t_wrong_question_bank (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    child_id BIGINT NOT NULL COMMENT '瀛愬コID',
    question_id BIGINT DEFAULT NULL COMMENT '棰樼洰ID',
    subject VARCHAR(32) NOT NULL COMMENT '瀛︾锛歁ATH/CHINESE/ENGLISH',
    knowledge_point VARCHAR(128) DEFAULT NULL COMMENT '鐭ヨ瘑鐐?,
    question_content TEXT DEFAULT NULL COMMENT '棰樼洰鍐呭JSON',
    wrong_answer TEXT DEFAULT NULL COMMENT '閿欒绛旀',
    correct_answer TEXT DEFAULT NULL COMMENT '姝ｇ‘绛旀',
    wrong_count INT NOT NULL DEFAULT 1 COMMENT '閿欒娆℃暟',
    mastery_level VARCHAR(32) NOT NULL DEFAULT 'NOT_MASTERED' COMMENT '鎺屾彙搴︼細NOT_MASTERED/GENERAL/PROFICIENT',
    in_review_plan TINYINT(1) NOT NULL DEFAULT 0 COMMENT '鏄惁鍔犲叆澶嶄範璁″垝锛?鍚?鏄?,
    last_wrong_at DATETIME DEFAULT NULL COMMENT '鏈€杩戦敊璇椂闂?,
    mastered_at DATETIME DEFAULT NULL COMMENT '鏍囪鎺屾彙鏃堕棿',
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    created_at DATETIME DEFAULT NULL,
    updated_at DATETIME DEFAULT NULL
);

-- === v2 extend ===
-- =============================================
-- 瀛﹂湼鍒板 - v2 瀛︿範璧勬簮鎵╁睍
-- 瀵规爣娲嬭懕瀛﹂櫌鎾斁鍣?+ 绠＄悊鍚庡彴
-- 鍏煎 MySQL 5.5+
-- =============================================
SET NAMES utf8mb4;

-- 杈呭姪锛氬畨鍏ㄦ坊鍔犲垪锛圡ySQL 5.5 鍏煎锛?
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

-- ========== 1. t_learning_resource 鏂板瀛楁 ==========
CALL safe_add_column('t_learning_resource', 'source_type', "VARCHAR(32) DEFAULT NULL COMMENT '瑙嗛鏉ユ簮绫诲瀷锛歁P4/BILIBILI/SMARTEDU/UPLOAD/CUSTOM'");
CALL safe_add_column('t_learning_resource', 'subtitle_url', "VARCHAR(512) DEFAULT NULL COMMENT '瀛楀箷鏂囦欢URL锛圴TT/SRT锛?");
CALL safe_add_column('t_learning_resource', 'quality_levels', "VARCHAR(512) DEFAULT NULL COMMENT '鐢昏川绛夌骇JSON'");
CALL safe_add_column('t_learning_resource', 'knowledge_markers', "VARCHAR(1024) DEFAULT NULL COMMENT '鐭ヨ瘑鐐规爣璁癑SON'");
CALL safe_add_column('t_learning_resource', 'status', "VARCHAR(16) NOT NULL DEFAULT 'PUBLISHED' COMMENT '璧勬簮鐘舵€侊細DRAFT/PUBLISHED/UNPUBLISHED/DELETED'");
CALL safe_add_column('t_learning_resource', 'publish_at', "DATETIME DEFAULT NULL COMMENT '涓婃灦鏃堕棿'");
CALL safe_add_column('t_learning_resource', 'unpublish_at', "DATETIME DEFAULT NULL COMMENT '涓嬫灦鏃堕棿'");
CALL safe_add_column('t_learning_resource', 'operator_id', "BIGINT DEFAULT NULL COMMENT '鎿嶄綔浜篒D'");
CALL safe_add_column('t_learning_resource', 'description', "TEXT DEFAULT NULL COMMENT '璧勬簮鎻忚堪'");

-- 涓哄凡鏈夋暟鎹帹鏂?source_type
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

-- ========== 2. t_micro_course 鏂板瀛楁 ==========
CALL safe_add_column('t_micro_course', 'source_type', "VARCHAR(32) DEFAULT NULL COMMENT '瑙嗛鏉ユ簮绫诲瀷'");
CALL safe_add_column('t_micro_course', 'subtitle_url', "VARCHAR(512) DEFAULT NULL COMMENT '瀛楀箷URL'");
CALL safe_add_column('t_micro_course', 'quality_levels', "VARCHAR(512) DEFAULT NULL COMMENT '鐢昏川JSON'");
CALL safe_add_column('t_micro_course', 'knowledge_markers', "VARCHAR(1024) DEFAULT NULL COMMENT '鐭ヨ瘑鐐规爣璁癑SON'");
CALL safe_add_column('t_micro_course', 'status', "VARCHAR(16) NOT NULL DEFAULT 'PUBLISHED' COMMENT '鐘舵€?");
CALL safe_add_column('t_micro_course', 'description', "TEXT DEFAULT NULL COMMENT '璇剧▼绠€浠?");

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

-- ========== 3. t_course_watch_record 琛ュ厖瀛楁 ==========
CALL safe_add_column('t_course_watch_record', 'first_watch_time', "DATETIME DEFAULT NULL COMMENT '棣栨瑙傜湅鏃堕棿'");
CALL safe_add_column('t_course_watch_record', 'last_watch_time', "DATETIME DEFAULT NULL COMMENT '鏈€鍚庤鐪嬫椂闂?");

-- ========== 4. t_course_play_log 鎾斁鏄庣粏鏃ュ織 ==========
CREATE TABLE IF NOT EXISTS t_course_play_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL COMMENT '鐢ㄦ埛ID',
    child_id BIGINT NOT NULL COMMENT '瀛愬コID',
    resource_id BIGINT NOT NULL COMMENT '璧勬簮ID',
    session_id VARCHAR(64) NOT NULL COMMENT '鎾斁浼氳瘽ID',
    current_time INT NOT NULL DEFAULT 0 COMMENT '褰撳墠鎾斁浣嶇疆锛堢锛?,
    duration INT NOT NULL DEFAULT 0 COMMENT '瑙嗛鎬绘椂闀匡紙绉掞級',
    percent INT NOT NULL DEFAULT 0 COMMENT '杩涘害鐧惧垎姣?,
    playback_rate DECIMAL(3,2) NOT NULL DEFAULT 1.00 COMMENT '鎾斁鍊嶉€?,
    quality VARCHAR(32) DEFAULT NULL COMMENT '鐢昏川',
    source_type VARCHAR(32) DEFAULT NULL COMMENT '鏉ユ簮绫诲瀷',
    event_type VARCHAR(32) NOT NULL DEFAULT 'HEARTBEAT' COMMENT '浜嬩欢锛歅LAY/PAUSE/SEEK/HEARTBEAT/FINISH',
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    created_at DATETIME DEFAULT NULL,
    INDEX idx_user_resource (user_id, resource_id),
    INDEX idx_child_id (child_id),
    INDEX idx_session (session_id),
    INDEX idx_created (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='鎾斁鏄庣粏鏃ュ織琛?;

-- 娓呯悊杈呭姪瀛樺偍杩囩▼
DROP PROCEDURE IF EXISTS safe_add_column;



-- === Indexes ===
-- indexes for t_ai_exercise_generated
CREATE INDEX IF NOT EXISTS idx_child_id ON t_ai_exercise_generated (child_id);
CREATE INDEX IF NOT EXISTS idx_source ON t_ai_exercise_generated (source);
CREATE INDEX IF NOT EXISTS idx_subject ON t_ai_exercise_generated (subject);
CREATE INDEX IF NOT EXISTS idx_answered ON t_ai_exercise_generated (answered);
CREATE INDEX IF NOT EXISTS idx_created_at ON t_ai_exercise_generated (created_at);

-- indexes for t_ai_grading_record
CREATE INDEX IF NOT EXISTS idx_child_id ON t_ai_grading_record (child_id);
CREATE INDEX IF NOT EXISTS idx_parent_id ON t_ai_grading_record (parent_id);
CREATE INDEX IF NOT EXISTS idx_grading_type ON t_ai_grading_record (grading_type);
CREATE INDEX IF NOT EXISTS idx_subject ON t_ai_grading_record (subject);
CREATE INDEX IF NOT EXISTS idx_created_at ON t_ai_grading_record (created_at);

-- indexes for t_ai_learning_plan
CREATE INDEX IF NOT EXISTS idx_child_id ON t_ai_learning_plan (child_id);
CREATE INDEX IF NOT EXISTS idx_plan_date ON t_ai_learning_plan (plan_date);
CREATE INDEX IF NOT EXISTS idx_child_date ON t_ai_learning_plan (child_id, plan_date);
CREATE INDEX IF NOT EXISTS idx_applied ON t_ai_learning_plan (applied);

-- indexes for t_ai_qa_record
CREATE INDEX IF NOT EXISTS idx_child_id ON t_ai_qa_record (child_id);
CREATE INDEX IF NOT EXISTS idx_parent_id ON t_ai_qa_record (parent_id);
CREATE INDEX IF NOT EXISTS idx_conversation_id ON t_ai_qa_record (conversation_id);
CREATE INDEX IF NOT EXISTS idx_subject ON t_ai_qa_record (subject);
CREATE INDEX IF NOT EXISTS idx_created_at ON t_ai_qa_record (created_at);
CREATE INDEX IF NOT EXISTS idx_child_subject ON t_ai_qa_record (child_id, subject);

-- indexes for t_ai_report
CREATE INDEX IF NOT EXISTS idx_user_id ON t_ai_report (user_id);
CREATE INDEX IF NOT EXISTS idx_created_at ON t_ai_report (created_at);

-- indexes for t_ai_tutor_optimization
CREATE INDEX IF NOT EXISTS idx_user_id ON t_ai_tutor_optimization (user_id);
CREATE INDEX IF NOT EXISTS idx_type ON t_ai_tutor_optimization (optimization_type);

-- indexes for t_audio_material
CREATE INDEX IF NOT EXISTS idx_grade_subject ON t_audio_material (grade, subject);
CREATE INDEX IF NOT EXISTS idx_audio_type ON t_audio_material (audio_type);
CREATE INDEX IF NOT EXISTS idx_textbook_version ON t_audio_material (textbook_version);

-- indexes for t_booking
CREATE INDEX IF NOT EXISTS idx_parent_id ON t_booking (parent_id);
CREATE INDEX IF NOT EXISTS idx_tutor_id ON t_booking (tutor_id);
CREATE INDEX IF NOT EXISTS idx_status ON t_booking (status);
CREATE INDEX IF NOT EXISTS idx_booking_date ON t_booking (booking_date);
CREATE INDEX IF NOT EXISTS idx_tutor_status ON t_booking (tutor_id, status);

-- indexes for t_child
CREATE INDEX IF NOT EXISTS idx_parent_id ON t_child (parent_id);
CREATE INDEX IF NOT EXISTS idx_grade ON t_child (grade);

-- indexes for t_child_achievement
CREATE INDEX IF NOT EXISTS idx_child_id ON t_child_achievement (child_id);
CREATE INDEX IF NOT EXISTS idx_achievement_type ON t_child_achievement (achievement_type);
CREATE INDEX IF NOT EXISTS idx_created_at ON t_child_achievement (created_at);

-- indexes for t_course_watch_record
CREATE UNIQUE INDEX IF NOT EXISTS uk_child_resource ON t_course_watch_record (child_id, resource_id);
CREATE INDEX IF NOT EXISTS idx_child_id ON t_course_watch_record (child_id);
CREATE INDEX IF NOT EXISTS idx_resource_id ON t_course_watch_record (resource_id);
CREATE INDEX IF NOT EXISTS idx_completed ON t_course_watch_record (completed);

-- indexes for t_exercise_answer_record
CREATE INDEX IF NOT EXISTS idx_child_id ON t_exercise_answer_record (child_id);
CREATE INDEX IF NOT EXISTS idx_subject ON t_exercise_answer_record (subject);
CREATE INDEX IF NOT EXISTS idx_resource_id ON t_exercise_answer_record (resource_id);
CREATE INDEX IF NOT EXISTS idx_created_at ON t_exercise_answer_record (created_at);
CREATE INDEX IF NOT EXISTS idx_child_subject ON t_exercise_answer_record (child_id, subject);

-- indexes for t_exercise_bank
CREATE INDEX IF NOT EXISTS idx_grade_subject ON t_exercise_bank (grade, subject);
CREATE INDEX IF NOT EXISTS idx_knowledge_point_id ON t_exercise_bank (knowledge_point_id);
CREATE INDEX IF NOT EXISTS idx_question_type ON t_exercise_bank (question_type);
CREATE INDEX IF NOT EXISTS idx_difficulty ON t_exercise_bank (difficulty);
CREATE INDEX IF NOT EXISTS idx_source_type ON t_exercise_bank (source_type);
CREATE INDEX IF NOT EXISTS idx_vip_only ON t_exercise_bank (vip_only);

-- indexes for t_feature_order
CREATE INDEX IF NOT EXISTS idx_user_id ON t_feature_order (user_id);
CREATE INDEX IF NOT EXISTS idx_feature_type ON t_feature_order (feature_type);
CREATE INDEX IF NOT EXISTS idx_payment_status ON t_feature_order (payment_status);
CREATE INDEX IF NOT EXISTS idx_user_feature ON t_feature_order (user_id, feature_type, payment_status);

-- indexes for t_knowledge_point
CREATE INDEX IF NOT EXISTS idx_grade_subject ON t_knowledge_point (grade, subject);
CREATE INDEX IF NOT EXISTS idx_parent_id ON t_knowledge_point (parent_id);
CREATE INDEX IF NOT EXISTS idx_textbook_version ON t_knowledge_point (textbook_version);
CREATE INDEX IF NOT EXISTS idx_depth ON t_knowledge_point (depth);

-- indexes for t_learning_plan_config
CREATE UNIQUE INDEX IF NOT EXISTS uk_child_id ON t_learning_plan_config (child_id);
CREATE INDEX IF NOT EXISTS idx_created_at ON t_learning_plan_config (created_at);

-- indexes for t_learning_resource
CREATE INDEX IF NOT EXISTS idx_grade_subject ON t_learning_resource (grade, subject);
CREATE INDEX IF NOT EXISTS idx_textbook_version ON t_learning_resource (textbook_version);
CREATE INDEX IF NOT EXISTS idx_resource_type ON t_learning_resource (resource_type);
CREATE INDEX IF NOT EXISTS idx_vip_only ON t_learning_resource (vip_only);
CREATE INDEX IF NOT EXISTS idx_knowledge_point ON t_learning_resource (knowledge_point);

-- indexes for t_learning_task
CREATE INDEX IF NOT EXISTS idx_child_id ON t_learning_task (child_id);
CREATE INDEX IF NOT EXISTS idx_assigned_date ON t_learning_task (assigned_date);
CREATE INDEX IF NOT EXISTS idx_status ON t_learning_task (status);
CREATE INDEX IF NOT EXISTS idx_child_date ON t_learning_task (child_id, assigned_date);
CREATE INDEX IF NOT EXISTS idx_child_status ON t_learning_task (child_id, status);

-- indexes for t_membership
CREATE INDEX IF NOT EXISTS idx_user_id ON t_membership (user_id);
CREATE INDEX IF NOT EXISTS idx_status ON t_membership (status);
CREATE INDEX IF NOT EXISTS idx_expire_time ON t_membership (expire_time);

-- indexes for t_micro_course
CREATE INDEX IF NOT EXISTS idx_grade_subject ON t_micro_course (grade, subject);
CREATE INDEX IF NOT EXISTS idx_textbook_version ON t_micro_course (textbook_version);
CREATE INDEX IF NOT EXISTS idx_chapter_id ON t_micro_course (chapter_id);
CREATE INDEX IF NOT EXISTS idx_knowledge_point_id ON t_micro_course (knowledge_point_id);
CREATE INDEX IF NOT EXISTS idx_vip_only ON t_micro_course (vip_only);
CREATE INDEX IF NOT EXISTS idx_difficulty ON t_micro_course (difficulty);

-- indexes for t_parent_control_config
CREATE UNIQUE INDEX IF NOT EXISTS uk_child_id ON t_parent_control_config (child_id);
CREATE INDEX IF NOT EXISTS idx_parent_id ON t_parent_control_config (parent_id);

-- indexes for t_resource_chapter
CREATE INDEX IF NOT EXISTS idx_grade_subject ON t_resource_chapter (grade, subject);
CREATE INDEX IF NOT EXISTS idx_parent_id ON t_resource_chapter (parent_id);
CREATE INDEX IF NOT EXISTS idx_textbook_version ON t_resource_chapter (textbook_version);

-- indexes for t_study_material
CREATE INDEX IF NOT EXISTS idx_grade_subject ON t_study_material (grade, subject);
CREATE INDEX IF NOT EXISTS idx_material_type ON t_study_material (material_type);
CREATE INDEX IF NOT EXISTS idx_textbook_version ON t_study_material (textbook_version);

-- indexes for t_subject
CREATE UNIQUE INDEX IF NOT EXISTS uk_code ON t_subject (code);

-- indexes for t_test_paper
CREATE INDEX IF NOT EXISTS idx_grade_subject ON t_test_paper (grade, subject);
CREATE INDEX IF NOT EXISTS idx_paper_type ON t_test_paper (paper_type);
CREATE INDEX IF NOT EXISTS idx_textbook_version ON t_test_paper (textbook_version);
CREATE INDEX IF NOT EXISTS idx_vip_only ON t_test_paper (vip_only);

-- indexes for t_tutor_profile
CREATE UNIQUE INDEX IF NOT EXISTS uk_user_id ON t_tutor_profile (user_id);
CREATE INDEX IF NOT EXISTS idx_active_rating ON t_tutor_profile (active, rating);
CREATE INDEX IF NOT EXISTS idx_subjects ON t_tutor_profile (subjects);

-- indexes for t_user
CREATE UNIQUE INDEX IF NOT EXISTS uk_openid ON t_user (openid);
CREATE INDEX IF NOT EXISTS idx_phone ON t_user (phone);
CREATE INDEX IF NOT EXISTS idx_identity_type ON t_user (identity_type);
CREATE INDEX IF NOT EXISTS idx_verify_status ON t_user (verify_status);

-- indexes for t_verify_record
CREATE INDEX IF NOT EXISTS idx_user_id ON t_verify_record (user_id);
CREATE INDEX IF NOT EXISTS idx_verify_status ON t_verify_record (verify_status);
CREATE INDEX IF NOT EXISTS idx_user_status ON t_verify_record (user_id, verify_status);

-- indexes for t_wrong_question_bank
CREATE INDEX IF NOT EXISTS idx_child_id ON t_wrong_question_bank (child_id);
CREATE INDEX IF NOT EXISTS idx_subject ON t_wrong_question_bank (subject);
CREATE INDEX IF NOT EXISTS idx_knowledge_point ON t_wrong_question_bank (knowledge_point);
CREATE INDEX IF NOT EXISTS idx_mastery_level ON t_wrong_question_bank (mastery_level);
CREATE INDEX IF NOT EXISTS idx_in_review_plan ON t_wrong_question_bank (in_review_plan);
CREATE INDEX IF NOT EXISTS idx_child_mastery ON t_wrong_question_bank (child_id, mastery_level);



