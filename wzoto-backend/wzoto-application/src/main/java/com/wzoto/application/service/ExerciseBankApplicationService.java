package com.wzoto.application.service;

import com.wzoto.domain.entity.ExerciseBank;
import com.wzoto.domain.entity.TestPaper;
import com.wzoto.domain.service.ExerciseBankDomainService;
import com.wzoto.domain.valobj.GradeType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/** 习题库应用服务 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ExerciseBankApplicationService {

    private final ExerciseBankDomainService exerciseBankDomainService;

    public List<ExerciseBank> listExercises(String gradeCode, String subject, String difficulty, String questionType, int limit) {
        GradeType grade = gradeCode != null ? GradeType.fromCode(gradeCode) : null;
        return exerciseBankDomainService.listExercises(grade, subject, difficulty, questionType, limit);
    }

    public ExerciseBank getExercise(Long id) {
        return exerciseBankDomainService.getExercise(id);
    }

    public boolean submitAnswer(Long exerciseId, String studentAnswer) {
        return exerciseBankDomainService.submitAnswer(exerciseId, studentAnswer);
    }

    public TestPaper getTestPaper(Long id) {
        return exerciseBankDomainService.getTestPaper(id);
    }

    public List<TestPaper> listTestPapers(String gradeCode, String subject, String paperType) {
        GradeType grade = gradeCode != null ? GradeType.fromCode(gradeCode) : null;
        return exerciseBankDomainService.listTestPapers(grade, subject, paperType);
    }
}
