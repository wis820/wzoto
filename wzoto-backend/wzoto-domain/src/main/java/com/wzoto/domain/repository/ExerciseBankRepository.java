package com.wzoto.domain.repository;

import com.wzoto.domain.entity.ExerciseBank;
import com.wzoto.domain.valobj.GradeType;
import java.util.List;

/** 习题库仓储接口 */
public interface ExerciseBankRepository {
    ExerciseBank save(ExerciseBank exercise);
    ExerciseBank findById(Long id);
    ExerciseBank update(ExerciseBank exercise);
    List<ExerciseBank> findByGradeAndSubject(GradeType grade, String subject);
    List<ExerciseBank> findByKnowledgePoint(Long knowledgePointId);
    List<ExerciseBank> findByConditions(GradeType grade, String subject, String difficulty, String questionType, int limit);
}
