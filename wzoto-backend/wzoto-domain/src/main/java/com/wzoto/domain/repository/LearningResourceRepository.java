package com.wzoto.domain.repository;

import com.wzoto.domain.entity.LearningResource;
import com.wzoto.domain.valobj.GradeType;
import com.wzoto.domain.valobj.LearningResourceType;
import com.wzoto.domain.valobj.TextbookVersion;

import java.util.List;

/**
 * 学习资源仓储接口 - 领域层定义，基础设施层实现
 */
public interface LearningResourceRepository {

    LearningResource save(LearningResource resource);

    LearningResource findById(Long id);

    List<LearningResource> findByGradeAndSubject(GradeType grade, String subject);

    List<LearningResource> findByGradeAndSubjectAndTextbookVersion(GradeType grade, String subject, TextbookVersion textbookVersion);

    List<LearningResource> findByGradeAndSubjectAndType(GradeType grade, String subject, LearningResourceType resourceType);

    List<LearningResource> findByGradeAndSubjectAndVipOnly(GradeType grade, String subject, Boolean vipOnly);

    LearningResource update(LearningResource resource);
}
