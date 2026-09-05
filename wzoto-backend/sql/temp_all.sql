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
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id),
    INDEX idx_status (status),
    INDEX idx_expire_time (expire_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='浼氬憳琛?;
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
    report_content MEDIUMTEXT COMMENT 'AI鐢熸垚鎶ュ憡瀹屾暣鍐呭锛圝SON鏍煎紡锛?,
    preview_content TEXT COMMENT 'AI鐢熸垚鎶ュ憡棰勮鍐呭锛堥潪浼氬憳鍙锛?,
    report_type VARCHAR(20) NOT NULL DEFAULT 'PREVIEW' COMMENT '鎶ュ憡绫诲瀷锛歅REVIEW棰勮/FULL瀹屾暣',
    is_member_report TINYINT(1) NOT NULL DEFAULT 0 COMMENT '鏄惁浼氬憳鍏嶈垂鎶ュ憡锛?鍚?鏄?,
    price DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '鎶ュ憡浠锋牸',
    payment_status VARCHAR(20) NOT NULL DEFAULT 'UNPAID' COMMENT '鏀粯鐘舵€侊細UNPAID/PAID/FREE',
    deleted TINYINT(1) NOT NULL DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_user_id (user_id),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='AI瀛︽儏璇婃柇鎶ュ憡琛?;
-- =============================================
-- 瀛﹂湼鍒板 - 鍒濆鍖栨祴璇曟暟鎹?
-- 棰勭疆璐﹀彿锛歛dmin/123456, parent/123456, student/123456
-- 鎵ц鍓嶆彁锛歵_user, t_membership, t_ai_report 琛ㄥ凡瀛樺湪
-- =============================================

-- 1. 鎻掑叆娴嬭瘯鐢ㄦ埛锛堝瘑鐮佺粺涓€ 123456锛孊Crypt鍔犲瘑锛?
-- 娉ㄦ剰锛氬綋鍓嶇敤鎴疯〃鏃爌assword瀛楁锛岄€氳繃dev-login浣跨敤nickname鐧诲綍
INSERT IGNORE INTO t_user (id, openid, phone, nickname, avatar, identity_type, verify_status, real_name, deleted, created_at, updated_at)
VALUES
(1, 'dev_admin_openid', '13800000001', '绠＄悊鍛?, '', NULL, 'APPROVED', '绯荤粺绠＄悊鍛?, 0, NOW(), NOW()),
(2, 'dev_parent_openid', '13800000002', '娴嬭瘯瀹堕暱', '', 'PARENT', 'APPROVED', '寮犲缓鍥?, 0, NOW(), NOW()),
(3, 'dev_student_openid', '13800000003', '娴嬭瘯澶у鐢?, '', 'STUDENT', 'APPROVED', '鏉庢槑鍗?, 0, NOW(), NOW()),
(4, 'dev_parent_free_openid', '13800000004', '鍏嶈垂瀹堕暱', '', 'PARENT', 'APPROVED', '鐜嬫祴璇?, 0, NOW(), NOW());

-- 2. 鎻掑叆娴嬭瘯浼氬憳鏁版嵁
-- 瀹堕暱2锛氬勾鍗′細鍛橈紙鏈夋晥鏈熻嚦2027-06-30锛?
INSERT IGNORE INTO t_membership (id, user_id, member_type, status, start_time, expire_time, auto_renew, deleted, created_at, updated_at)
VALUES
(1, 2, 'PARENT_YEAR', 'ACTIVE', '2025-06-01 00:00:00', '2027-06-30 23:59:59', 1, 0, NOW(), NOW());

-- 3. 鎻掑叆娴嬭瘯AI鎶ュ憡鏁版嵁锛堝闀?鐨勪細鍛樺厤璐规姤鍛婏級
INSERT IGNORE INTO t_ai_report (id, user_id, child_name, weak_subjects, recent_scores, weak_point_desc, report_content, preview_content, report_type, is_member_report, price, payment_status, deleted, created_at, updated_at)
VALUES
(1, 2, '灏忔槑', '鍒濅腑鏁板,鍒濅腑鑻辫', '鏁板72,鑻辫68', '浜屽厓涓€娆℃柟绋嬬粍瑙ｄ笉瀵癸紝鑻辫闃呰鐞嗚В澶卞垎涓ラ噸', '{"weaknessAnalysis":[{"subject":"鍒濅腑鏁板","points":["浜屽厓涓€娆℃柟绋嬬粍瑙ｆ硶","涓€鍏冧簩娆℃柟绋嬪簲鐢ㄩ","鍑犱綍璇佹槑杈呭姪绾挎坊鍔?],"severity":"涓瓑"},{"subject":"鍒濅腑鑻辫","points":["闃呰鐞嗚В涓绘棬棰?,"瀹屽舰濉┖涓婁笅鏂囨帹鐞?,"璇硶濉┖鏃舵€佸垽鏂?],"severity":"杈冮噸"}],"suggestion":{"direction":"閲嶇偣绐佺牬鏁板鏂圭▼涓庤嫳璇槄璇伙紝杈呬互鍩虹璇硶宸╁浐","frequency":"寤鸿姣忓懆2娆℃暟瀛?2娆¤嫳璇紝姣忔1.5灏忔椂","duration":"棰勮2-3涓湀鍙鏄庢樉鎻愬崌"},"recommendedSubjects":["鏁板","鑻辫"]}', '鎮ㄧ殑瀛╁瓙鍦ㄥ垵涓暟瀛﹀拰鑻辫鏂归潰瀛樺湪钖勫急鐜妭锛屽缓璁噸鐐瑰姞寮烘柟绋嬭В娉曞拰闃呰鐞嗚В璁粌銆傚畬鏁存姤鍛婂寘鍚缁嗙煡璇嗗浘璋卞拰涓€у寲杈呭鏂规銆?, 'FULL', 1, 0.00, 'FREE', 0, NOW(), NOW());
