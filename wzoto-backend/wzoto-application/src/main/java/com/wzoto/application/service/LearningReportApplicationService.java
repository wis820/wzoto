package com.wzoto.application.service;

import com.wzoto.domain.context.UserContext;
import com.wzoto.domain.entity.Child;
import com.wzoto.domain.entity.WrongQuestionBank;
import com.wzoto.domain.service.ChildDomainService;
import com.wzoto.domain.service.LearningReportDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * 学情报告应用服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LearningReportApplicationService {

    private final LearningReportDomainService learningReportDomainService;
    private final ChildDomainService childDomainService;

    public LearningReportDomainService.DailyReport getDailyReport(Long childId, LocalDate date) {
        validateOwnership(childId);
        return learningReportDomainService.generateDailyReport(childId, date);
    }

    public LearningReportDomainService.WeeklyReport getWeeklyReport(Long childId, LocalDate weekStart) {
        validateOwnership(childId);
        return learningReportDomainService.generateWeeklyReport(childId, weekStart);
    }

    public LearningReportDomainService.MonthlyReport getMonthlyReport(Long childId, int year, int month) {
        validateOwnership(childId);
        return learningReportDomainService.generateMonthlyReport(childId, year, month);
    }

    public List<LearningReportDomainService.WeakPoint> getWeakPoints(Long childId, String subject, int topN) {
        validateOwnership(childId);
        return learningReportDomainService.weakPointsRanking(childId, subject, topN);
    }

    public List<WrongQuestionBank> exportWrongQuestions(Long childId, String subject) {
        validateOwnership(childId);
        return learningReportDomainService.exportWrongQuestions(childId, subject);
    }

    private void validateOwnership(Long childId) {
        Long parentId = UserContext.getCurrentUserId();
        Child child = childDomainService.findAndValidate(parentId, childId);
        if (child == null) {
            throw new IllegalArgumentException("子女档案不存在");
        }
    }
}
