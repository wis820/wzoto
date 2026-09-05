package com.wzoto.domain.service;

import com.wzoto.domain.entity.CourseWatchRecord;
import com.wzoto.domain.entity.ExerciseAnswerRecord;
import com.wzoto.domain.entity.WrongQuestionBank;
import com.wzoto.domain.repository.CourseWatchRecordRepository;
import com.wzoto.domain.repository.ExerciseAnswerRecordRepository;
import com.wzoto.domain.repository.WrongQuestionBankRepository;
import com.wzoto.domain.valobj.MasteryLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 学情报告领域服务 - 日/周/月统计、知识点掌握度、薄弱点排行
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LearningReportDomainService {

    private final CourseWatchRecordRepository courseWatchRecordRepository;
    private final ExerciseAnswerRecordRepository exerciseAnswerRecordRepository;
    private final WrongQuestionBankRepository wrongQuestionBankRepository;

    /**
     * 日报告
     */
    public DailyReport generateDailyReport(Long childId, LocalDate date) {
        List<ExerciseAnswerRecord> answers = exerciseAnswerRecordRepository.findByChildId(childId).stream()
                .filter(r -> r.getCreatedAt() != null && r.getCreatedAt().toLocalDate().equals(date))
                .toList();
        List<CourseWatchRecord> watches = courseWatchRecordRepository.findByChildId(childId).stream()
                .filter(r -> r.getLastWatchTime() != null && r.getLastWatchTime().toLocalDate().equals(date))
                .toList();
        return buildReport(childId, answers, watches);
    }

    /**
     * 周报告
     */
    public WeeklyReport generateWeeklyReport(Long childId, LocalDate weekStart) {
        LocalDate weekEnd = weekStart.plusDays(6);
        return generateRangeReport(childId, weekStart.atStartOfDay(), weekEnd.plusDays(1).atStartOfDay());
    }

    /**
     * 月报告
     */
    public MonthlyReport generateMonthlyReport(Long childId, int year, int month) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
        return generateRangeReport(childId, start.atStartOfDay(), end.plusDays(1).atStartOfDay());
    }

    private <T> T generateRangeReport(Long childId, LocalDateTime start, LocalDateTime end) {
        List<ExerciseAnswerRecord> answers = exerciseAnswerRecordRepository.findByChildId(childId).stream()
                .filter(r -> r.getCreatedAt() != null && !r.getCreatedAt().isBefore(start) && r.getCreatedAt().isBefore(end))
                .toList();
        List<CourseWatchRecord> watches = courseWatchRecordRepository.findByChildId(childId).stream()
                .filter(r -> r.getLastWatchTime() != null && !r.getLastWatchTime().isBefore(start) && r.getLastWatchTime().isBefore(end))
                .toList();
        return buildReport(childId, answers, watches);
    }

    @SuppressWarnings("unchecked")
    private <T> T buildReport(Long childId, List<ExerciseAnswerRecord> answers, List<CourseWatchRecord> watches) {
        int totalQuestions = answers.size();
        int correctCount = (int) answers.stream().filter(ExerciseAnswerRecord::isCorrect).count();
        double accuracy = totalQuestions == 0 ? 0.0 : (double) correctCount / totalQuestions;
        int watchDuration = watches.stream().mapToInt(CourseWatchRecord::getWatchDurationSeconds).sum();
        int completedVideos = (int) watches.stream().filter(CourseWatchRecord::isCompleted).count();

        Map<String, List<ExerciseAnswerRecord>> bySubject = answers.stream()
                .collect(Collectors.groupingBy(a -> a.getSubject() != null ? a.getSubject() : "unknown"));
        Map<String, Double> subjectAccuracy = new HashMap<>();
        bySubject.forEach((subject, list) -> {
            int total = list.size();
            int correct = (int) list.stream().filter(ExerciseAnswerRecord::isCorrect).count();
            subjectAccuracy.put(subject, total == 0 ? 0.0 : (double) correct / total);
        });

        Map<String, List<ExerciseAnswerRecord>> byKnowledge = answers.stream()
                .filter(a -> a.getKnowledgePoint() != null)
                .collect(Collectors.groupingBy(ExerciseAnswerRecord::getKnowledgePoint));
        Map<String, MasteryLevel> masteryMap = new HashMap<>();
        byKnowledge.forEach((kp, list) -> {
            int total = list.size();
            int correct = (int) list.stream().filter(ExerciseAnswerRecord::isCorrect).count();
            double rate = total == 0 ? 0.0 : (double) correct / total;
            masteryMap.put(kp, MasteryLevel.fromAccuracy(rate));
        });

        return (T) new DailyReport(totalQuestions, correctCount, accuracy, watchDuration, completedVideos, subjectAccuracy, masteryMap);
    }

    /**
     * 薄弱知识点排行
     */
    public List<WeakPoint> weakPointsRanking(Long childId, String subject, int topN) {
        List<WrongQuestionBank> wrongQuestions = wrongQuestionBankRepository.findByChildId(childId);
        if (subject != null && !subject.isBlank()) {
            wrongQuestions = wrongQuestions.stream()
                    .filter(w -> subject.equals(w.getSubject()))
                    .toList();
        }
        Map<String, Integer> countMap = wrongQuestions.stream()
                .filter(w -> w.getKnowledgePoint() != null)
                .collect(Collectors.toMap(
                        WrongQuestionBank::getKnowledgePoint,
                        w -> w.getMistakeCount() == null ? 0 : w.getMistakeCount(),
                        Integer::sum
                ));
        return countMap.entrySet().stream()
                .map(e -> new WeakPoint(e.getKey(), e.getValue()))
                .sorted(Comparator.comparingInt(WeakPoint::mistakeCount).reversed())
                .limit(topN > 0 ? topN : 10)
                .toList();
    }

    /**
     * 错题导出数据
     */
    public List<WrongQuestionBank> exportWrongQuestions(Long childId, String subject) {
        List<WrongQuestionBank> list = wrongQuestionBankRepository.findByChildId(childId);
        if (subject != null && !subject.isBlank()) {
            list = list.stream().filter(w -> subject.equals(w.getSubject())).toList();
        }
        return list;
    }

    // ========== 报告视图对象 ==========

    public record DailyReport(int totalQuestions, int correctCount, double accuracy,
                              int watchDurationSeconds, int completedVideos,
                              Map<String, Double> subjectAccuracy,
                              Map<String, MasteryLevel> masteryMap) {
    }

    public record WeeklyReport(int totalQuestions, int correctCount, double accuracy,
                               int watchDurationSeconds, int completedVideos,
                               Map<String, Double> subjectAccuracy,
                               Map<String, MasteryLevel> masteryMap) {
    }

    public record MonthlyReport(int totalQuestions, int correctCount, double accuracy,
                                int watchDurationSeconds, int completedVideos,
                                Map<String, Double> subjectAccuracy,
                                Map<String, MasteryLevel> masteryMap) {
    }

    public record WeakPoint(String knowledgePoint, int mistakeCount) {
    }
}
