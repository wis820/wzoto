package com.wzoto.domain.repository;

import com.wzoto.domain.entity.AiTutorOptimization;

import java.util.List;

/**
 * AI教员优化记录 - 仓储接口
 */
public interface AiTutorOptimizationRepository {

    void save(AiTutorOptimization optimization);

    AiTutorOptimization findById(Long id);

    List<AiTutorOptimization> findByUserId(Long userId);

    void update(AiTutorOptimization optimization);
}
