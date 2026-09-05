package com.wzoto.domain.repository;

import com.wzoto.domain.entity.AiExerciseGenerated;
import java.util.List;

/** AI生成练习题仓储接口 */
public interface AiExerciseGeneratedRepository {
    AiExerciseGenerated save(AiExerciseGenerated exercise);
    AiExerciseGenerated findById(Long id);
    AiExerciseGenerated update(AiExerciseGenerated exercise);
    List<AiExerciseGenerated> findByChildId(Long childId);
    List<AiExerciseGenerated> findByChildIdAndSubject(Long childId, String subject);
    List<AiExerciseGenerated> findUnanswered(Long childId);
}
