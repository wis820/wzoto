package com.wzoto.domain.repository;

import com.wzoto.domain.entity.TestPaper;
import com.wzoto.domain.valobj.GradeType;
import java.util.List;

/** 试卷仓储接口 */
public interface TestPaperRepository {
    TestPaper findById(Long id);
    List<TestPaper> findByGradeAndSubject(GradeType grade, String subject);
    List<TestPaper> findByGradeAndSubjectAndType(GradeType grade, String subject, String paperType);
}
