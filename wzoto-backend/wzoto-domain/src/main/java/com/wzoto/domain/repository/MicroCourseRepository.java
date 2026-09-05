package com.wzoto.domain.repository;

import com.wzoto.domain.entity.MicroCourse;
import com.wzoto.domain.valobj.GradeType;
import java.util.List;

/** 微课视频仓储接口 */
public interface MicroCourseRepository {
    MicroCourse save(MicroCourse course);
    MicroCourse findById(Long id);
    MicroCourse update(MicroCourse course);
    List<MicroCourse> findByGradeAndSubject(GradeType grade, String subject);
    List<MicroCourse> findByGradeAndSubjectAndVip(GradeType grade, String subject, Boolean vipOnly);
    List<MicroCourse> findRecommended(GradeType grade, String subject, int limit);
}
