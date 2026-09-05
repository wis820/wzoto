package com.wzoto.domain.service;

import com.wzoto.domain.entity.Child;
import com.wzoto.domain.entity.MicroCourse;
import com.wzoto.domain.repository.ChildRepository;
import com.wzoto.domain.repository.MicroCourseRepository;
import com.wzoto.domain.valobj.GradeType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 微课领域服务 - 视频播放权限、进度追踪、推荐算法
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MicroCourseDomainService {

    private final MicroCourseRepository microCourseRepository;
    private final ChildRepository childRepository;

    /**
     * 微课列表（按年级+学科筛选）
     */
    public List<MicroCourse> listCourses(GradeType grade, String subject, Boolean vipOnly) {
        if (vipOnly != null) {
            return microCourseRepository.findByGradeAndSubjectAndVip(grade, subject, vipOnly);
        }
        return microCourseRepository.findByGradeAndSubject(grade, subject);
    }

    /**
     * 微课详情
     */
    public MicroCourse getCourse(Long id) {
        MicroCourse course = microCourseRepository.findById(id);
        if (course == null) {
            throw new IllegalArgumentException("微课不存在");
        }
        return course;
    }

    /**
     * 上报观看进度（增加播放次数）
     */
    public MicroCourse reportProgress(Long courseId) {
        MicroCourse course = microCourseRepository.findById(courseId);
        if (course == null) {
            throw new IllegalArgumentException("微课不存在");
        }
        course.incrementPlayCount();
        return microCourseRepository.update(course);
    }

    /**
     * 推荐微课（按播放量排序）
     */
    public List<MicroCourse> getRecommended(Long parentId, Long childId, int limit) {
        Child child = childRepository.findById(childId);
        if (child == null || !child.belongsTo(parentId)) {
            throw new IllegalArgumentException("无权操作该子女档案");
        }
        GradeType grade = child.getGrade();
        return microCourseRepository.findRecommended(grade, null, limit);
    }
}
