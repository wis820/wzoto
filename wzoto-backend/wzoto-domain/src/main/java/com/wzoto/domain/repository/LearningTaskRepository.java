package com.wzoto.domain.repository;

import com.wzoto.domain.entity.LearningTask;
import com.wzoto.domain.valobj.LearningTaskStatus;

import java.time.LocalDate;
import java.util.List;

/**
 * 学习任务记录仓储接口 - 领域层定义，基础设施层实现
 */
public interface LearningTaskRepository {

    LearningTask save(LearningTask task);

    LearningTask findById(Long id);

    List<LearningTask> findByChildId(Long childId);

    List<LearningTask> findByChildIdAndTaskDate(Long childId, LocalDate taskDate);

    List<LearningTask> findByChildIdAndStatus(Long childId, LearningTaskStatus status);

    LearningTask update(LearningTask task);

    int batchSave(List<LearningTask> tasks);
}
