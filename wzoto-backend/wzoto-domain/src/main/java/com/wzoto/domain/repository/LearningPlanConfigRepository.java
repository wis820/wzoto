package com.wzoto.domain.repository;

import com.wzoto.domain.entity.LearningPlanConfig;

/**
 * 学习计划配置仓储接口 - 领域层定义，基础设施层实现
 */
public interface LearningPlanConfigRepository {

    LearningPlanConfig save(LearningPlanConfig config);

    LearningPlanConfig findById(Long id);

    LearningPlanConfig findByChildId(Long childId);

    LearningPlanConfig update(LearningPlanConfig config);
}
