package com.wzoto.domain.service;

import com.wzoto.domain.entity.ExerciseBank;
import com.wzoto.domain.entity.TestPaper;
import com.wzoto.domain.repository.ExerciseBankRepository;
import com.wzoto.domain.repository.TestPaperRepository;
import com.wzoto.domain.valobj.GradeType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 习题库领域服务 - 按知识点/难度查询题目、组卷、自动批改
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ExerciseBankDomainService {

    private final ExerciseBankRepository exerciseBankRepository;
    private final TestPaperRepository testPaperRepository;

    /**
     * 按条件查询习题
     */
    public List<ExerciseBank> listExercises(GradeType grade, String subject,
                                             String difficulty, String questionType, int limit) {
        return exerciseBankRepository.findByConditions(grade, subject, difficulty, questionType, limit);
    }

    /**
     * 获取题目详情
     */
    public ExerciseBank getExercise(Long id) {
        ExerciseBank exercise = exerciseBankRepository.findById(id);
        if (exercise == null) {
            throw new IllegalArgumentException("题目不存在");
        }
        return exercise;
    }

    /**
     * 提交做题结果（自动批改）
     */
    public boolean submitAnswer(Long exerciseId, String studentAnswer) {
        ExerciseBank exercise = exerciseBankRepository.findById(exerciseId);
        if (exercise == null) {
            throw new IllegalArgumentException("题目不存在");
        }
        exercise.incrementUseCount();
        boolean correct = exercise.checkAnswer(studentAnswer);
        exerciseBankRepository.update(exercise);
        log.info("做题结果: exerciseId={}, correct={}", exerciseId, correct);
        return correct;
    }

    /**
     * 获取试卷详情
     */
    public TestPaper getTestPaper(Long id) {
        TestPaper paper = testPaperRepository.findById(id);
        if (paper == null) {
            throw new IllegalArgumentException("试卷不存在");
        }
        return paper;
    }

    /**
     * 获取试卷列表
     */
    public List<TestPaper> listTestPapers(GradeType grade, String subject, String paperType) {
        if (paperType != null) {
            return testPaperRepository.findByGradeAndSubjectAndType(grade, subject, paperType);
        }
        return testPaperRepository.findByGradeAndSubject(grade, subject);
    }

    /**
     * 按知识点查询题目
     */
    public List<ExerciseBank> findByKnowledgePoint(Long knowledgePointId) {
        return exerciseBankRepository.findByKnowledgePoint(knowledgePointId);
    }
}
