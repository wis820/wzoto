package com.wzoto.interfaces.controller;

import com.wzoto.application.service.LearningReportApplicationService;
import com.wzoto.domain.context.UserContext;
import com.wzoto.domain.entity.WrongQuestionBank;
import com.wzoto.domain.service.LearningReportDomainService;
import com.wzoto.interfaces.common.R;
import com.wzoto.interfaces.vo.learning.LearningReportVO;
import com.wzoto.interfaces.vo.learning.WeakPointVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 学情报告控制器
 */
@Slf4j
@RestController
@RequestMapping("/api/learning/report")
@RequiredArgsConstructor
public class LearningReportController {

    private final LearningReportApplicationService learningReportApplicationService;

    @GetMapping("/{childId}")
    public R<LearningReportVO> getReportOverview(@PathVariable Long childId) {
        log.info("[BI] report_overview|userId={}, childId={}", UserContext.getCurrentUserId(), childId);
        LearningReportDomainService.DailyReport daily = learningReportApplicationService.getDailyReport(childId, LocalDate.now());
        return R.ok(toVO(childId, "overview", daily));
    }

    @GetMapping("/{childId}/daily")
    public R<LearningReportVO> getDailyReport(@PathVariable Long childId,
                                              @RequestParam(required = false) String date) {
        LocalDate reportDate = date != null && !date.isBlank() ? LocalDate.parse(date) : LocalDate.now();
        LearningReportDomainService.DailyReport daily = learningReportApplicationService.getDailyReport(childId, reportDate);
        return R.ok(toVO(childId, "daily", daily));
    }

    @GetMapping("/{childId}/weekly")
    public R<LearningReportVO> getWeeklyReport(@PathVariable Long childId,
                                               @RequestParam(required = false) String weekStart) {
        LocalDate start = weekStart != null && !weekStart.isBlank() ? LocalDate.parse(weekStart) : LocalDate.now().with(java.time.DayOfWeek.MONDAY);
        LearningReportDomainService.WeeklyReport weekly = learningReportApplicationService.getWeeklyReport(childId, start);
        return R.ok(toVO(childId, "weekly", weekly));
    }

    @GetMapping("/{childId}/monthly")
    public R<LearningReportVO> getMonthlyReport(@PathVariable Long childId,
                                                @RequestParam(required = false) Integer year,
                                                @RequestParam(required = false) Integer month) {
        int reportYear = year != null ? year : LocalDate.now().getYear();
        int reportMonth = month != null ? month : LocalDate.now().getMonthValue();
        LearningReportDomainService.MonthlyReport monthly = learningReportApplicationService.getMonthlyReport(childId, reportYear, reportMonth);
        return R.ok(toVO(childId, "monthly", monthly));
    }

    @GetMapping("/{childId}/weak-points")
    public R<List<WeakPointVO>> getWeakPoints(@PathVariable Long childId,
                                              @RequestParam(required = false) String subject,
                                              @RequestParam(required = false, defaultValue = "10") Integer topN) {
        List<LearningReportDomainService.WeakPoint> weakPoints = learningReportApplicationService.getWeakPoints(childId, subject, topN);
        return R.ok(weakPoints.stream()
                .map(wp -> WeakPointVO.builder()
                        .knowledgePoint(wp.knowledgePoint())
                        .mistakeCount(wp.mistakeCount())
                        .build())
                .collect(Collectors.toList()));
    }

    @GetMapping("/{childId}/wrong-questions/export")
    public R<List<WrongQuestionBank>> exportWrongQuestions(@PathVariable Long childId,
                                                           @RequestParam(required = false) String subject) {
        log.info("[BI] wrong_question_export|userId={}, childId={}", UserContext.getCurrentUserId(), childId);
        return R.ok(learningReportApplicationService.exportWrongQuestions(childId, subject));
    }

    private LearningReportVO toVO(Long childId, String reportType,
                                  LearningReportDomainService.DailyReport report) {
        Map<String, String> masteryMap = new HashMap<>();
        if (report.masteryMap() != null) {
            report.masteryMap().forEach((k, v) -> masteryMap.put(k, v != null ? v.getCode() : null));
        }
        return LearningReportVO.builder()
                .childId(childId)
                .reportType(reportType)
                .totalQuestions(report.totalQuestions())
                .correctCount(report.correctCount())
                .accuracy(report.accuracy())
                .watchDurationSeconds(report.watchDurationSeconds())
                .completedVideos(report.completedVideos())
                .subjectAccuracy(report.subjectAccuracy())
                .masteryMap(masteryMap)
                .build();
    }

    private LearningReportVO toVO(Long childId, String reportType,
                                  LearningReportDomainService.WeeklyReport report) {
        Map<String, String> masteryMap = new HashMap<>();
        if (report.masteryMap() != null) {
            report.masteryMap().forEach((k, v) -> masteryMap.put(k, v != null ? v.getCode() : null));
        }
        return LearningReportVO.builder()
                .childId(childId)
                .reportType(reportType)
                .totalQuestions(report.totalQuestions())
                .correctCount(report.correctCount())
                .accuracy(report.accuracy())
                .watchDurationSeconds(report.watchDurationSeconds())
                .completedVideos(report.completedVideos())
                .subjectAccuracy(report.subjectAccuracy())
                .masteryMap(masteryMap)
                .build();
    }

    private LearningReportVO toVO(Long childId, String reportType,
                                  LearningReportDomainService.MonthlyReport report) {
        Map<String, String> masteryMap = new HashMap<>();
        if (report.masteryMap() != null) {
            report.masteryMap().forEach((k, v) -> masteryMap.put(k, v != null ? v.getCode() : null));
        }
        return LearningReportVO.builder()
                .childId(childId)
                .reportType(reportType)
                .totalQuestions(report.totalQuestions())
                .correctCount(report.correctCount())
                .accuracy(report.accuracy())
                .watchDurationSeconds(report.watchDurationSeconds())
                .completedVideos(report.completedVideos())
                .subjectAccuracy(report.subjectAccuracy())
                .masteryMap(masteryMap)
                .build();
    }
}
