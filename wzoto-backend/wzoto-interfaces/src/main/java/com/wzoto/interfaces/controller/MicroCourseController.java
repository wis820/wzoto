package com.wzoto.interfaces.controller;

import com.wzoto.application.service.MicroCourseApplicationService;
import com.wzoto.domain.context.UserContext;
import com.wzoto.domain.entity.MicroCourse;
import com.wzoto.interfaces.common.R;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** 微课控制器 */
@Slf4j
@RestController
@RequestMapping("/api/course")
@RequiredArgsConstructor
public class MicroCourseController {

    private final MicroCourseApplicationService microCourseApplicationService;

    @GetMapping("/list")
    public R<List<MicroCourse>> listCourses(@RequestParam(required = false) String grade,
                                             @RequestParam(required = false) String subject,
                                             @RequestParam(required = false) Boolean vipOnly) {
        return R.ok(microCourseApplicationService.listCourses(grade, subject, vipOnly));
    }

    @GetMapping("/{id}")
    public R<MicroCourse> getCourse(@PathVariable Long id) {
        return R.ok(microCourseApplicationService.getCourse(id));
    }

    @PostMapping("/progress")
    public R<MicroCourse> reportProgress(@RequestParam Long courseId) {
        log.info("[BI] course_progress|userId={}, courseId={}", UserContext.getCurrentUserId(), courseId);
        return R.ok(microCourseApplicationService.reportProgress(courseId));
    }

    @GetMapping("/recommend/{childId}")
    public R<List<MicroCourse>> getRecommended(@PathVariable Long childId,
                                                @RequestParam(defaultValue = "10") int limit) {
        return R.ok(microCourseApplicationService.getRecommended(childId, limit));
    }
}
