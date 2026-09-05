package com.wzoto.domain.repository;

import com.wzoto.domain.entity.AiLearningPlan;
import java.time.LocalDate;
import java.util.List;

/** AI学习规划仓储接口 */
public interface AiLearningPlanRepository {
    AiLearningPlan save(AiLearningPlan plan);
    AiLearningPlan findById(Long id);
    AiLearningPlan update(AiLearningPlan plan);
    List<AiLearningPlan> findByChildId(Long childId);
    AiLearningPlan findByChildIdAndDate(Long childId, LocalDate planDate);
}
