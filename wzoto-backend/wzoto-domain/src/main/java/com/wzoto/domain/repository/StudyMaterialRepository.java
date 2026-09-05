package com.wzoto.domain.repository;

import com.wzoto.domain.entity.StudyMaterial;
import com.wzoto.domain.valobj.GradeType;
import java.util.List;

/** 学习资料仓储接口 */
public interface StudyMaterialRepository {
    StudyMaterial findById(Long id);
    List<StudyMaterial> findByGradeAndSubject(GradeType grade, String subject);
    List<StudyMaterial> findByGradeAndSubjectAndType(GradeType grade, String subject, String materialType);
}
