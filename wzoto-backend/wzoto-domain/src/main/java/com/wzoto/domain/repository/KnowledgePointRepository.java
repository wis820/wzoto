package com.wzoto.domain.repository;

import com.wzoto.domain.entity.KnowledgePoint;
import com.wzoto.domain.valobj.GradeType;
import java.util.List;

/** 知识点仓储接口 */
public interface KnowledgePointRepository {
    List<KnowledgePoint> findByGradeAndSubject(GradeType grade, String subject);
    List<KnowledgePoint> findByParentId(Long parentId);
    KnowledgePoint findById(Long id);
    List<KnowledgePoint> findTree(GradeType grade, String subject, String textbookVersion);
}
