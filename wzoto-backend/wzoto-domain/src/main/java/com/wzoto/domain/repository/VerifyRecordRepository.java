package com.wzoto.domain.repository;

import com.wzoto.domain.entity.VerifyRecord;

import java.util.List;

/**
 * 认证记录仓储接口（领域层定义）
 */
public interface VerifyRecordRepository {

    /** 保存认证记录 */
    VerifyRecord save(VerifyRecord record);

    /** 根据ID查询 */
    VerifyRecord findById(Long id);

    /** 查询用户最新的认证记录 */
    VerifyRecord findLatestByUserId(Long userId);

    /** 查询用户所有认证记录 */
    List<VerifyRecord> findByUserId(Long userId);

    /** 更新认证记录 */
    VerifyRecord update(VerifyRecord record);

    /** 查询待审核记录 */
    List<VerifyRecord> findPendingRecords();
}