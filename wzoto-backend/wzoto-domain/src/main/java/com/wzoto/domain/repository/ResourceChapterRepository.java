package com.wzoto.domain.repository;

import com.wzoto.domain.entity.ResourceChapter;
import com.wzoto.domain.valobj.GradeType;
import java.util.List;

/** 教材章节仓储接口 */
public interface ResourceChapterRepository {
    List<ResourceChapter> findByGradeAndSubject(GradeType grade, String subject);
    List<ResourceChapter> findByParentId(Long parentId);
    ResourceChapter findById(Long id);
    List<ResourceChapter> findTree(GradeType grade, String subject, String textbookVersion);
}
