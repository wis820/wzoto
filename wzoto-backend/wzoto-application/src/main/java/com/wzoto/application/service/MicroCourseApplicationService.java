package com.wzoto.application.service;

import com.wzoto.domain.context.UserContext;
import com.wzoto.domain.entity.MicroCourse;
import com.wzoto.domain.service.MicroCourseDomainService;
import com.wzoto.domain.valobj.GradeType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/** 微课应用服务 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MicroCourseApplicationService {

    private final MicroCourseDomainService microCourseDomainService;

    public List<MicroCourse> listCourses(String gradeCode, String subject, Boolean vipOnly) {
        GradeType grade = gradeCode != null ? GradeType.fromCode(gradeCode) : null;
        return microCourseDomainService.listCourses(grade, subject, vipOnly);
    }

    public MicroCourse getCourse(Long id) {
        return microCourseDomainService.getCourse(id);
    }

    public MicroCourse reportProgress(Long courseId) {
        return microCourseDomainService.reportProgress(courseId);
    }

    public List<MicroCourse> getRecommended(Long childId, int limit) {
        Long parentId = UserContext.getCurrentUserId();
        return microCourseDomainService.getRecommended(parentId, childId, limit);
    }
}
