package com.wzoto.domain.repository;

import com.wzoto.domain.entity.ExerciseAnswerRecord;

import java.util.List;

/**
 * 习题作答记录仓储接口 - 领域层定义，基础设施层实现
 */
public interface ExerciseAnswerRecordRepository {

    ExerciseAnswerRecord save(ExerciseAnswerRecord record);

    ExerciseAnswerRecord findById(Long id);

    List<ExerciseAnswerRecord> findByChildId(Long childId);

    List<ExerciseAnswerRecord> findByChildIdAndSubject(Long childId, String subject);

    List<ExerciseAnswerRecord> findByChildIdAndTaskId(Long childId, Long taskId);
}
