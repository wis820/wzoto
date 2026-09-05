package com.wzoto.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 习题作答记录 - 领域实体
 * 核心业务规则：一道题同一孩子可多次作答，记录每次答案与耗时
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseAnswerRecord {

    /** 主键ID */
    private Long id;

    /** 子女ID */
    private Long childId;

    /** 学习资源ID（题库题目） */
    private Long resourceId;

    /** 任务ID */
    private Long taskId;

    /** 学科：chinese/math/english */
    private String subject;

    /** 知识点 */
    private String knowledgePoint;

    /** 用户答案 */
    private String userAnswer;

    /** 正确答案 */
    private String correctAnswer;

    /** 是否正确 */
    private Boolean correct;

    /** 耗时（秒） */
    private Integer spentSeconds;

    /** 是否已删除 */
    private Boolean deleted;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间 */
    private LocalDateTime updatedAt;

    // ========== 领域行为 ==========

    /**
     * 提交作答
     */
    public static ExerciseAnswerRecord submit(Long childId, Long resourceId, Long taskId,
                                              String subject, String knowledgePoint,
                                              String userAnswer, String correctAnswer, Integer spentSeconds) {
        boolean correct = correctAnswer != null && correctAnswer.equals(userAnswer);
        return ExerciseAnswerRecord.builder()
                .childId(childId)
                .resourceId(resourceId)
                .taskId(taskId)
                .subject(subject)
                .knowledgePoint(knowledgePoint)
                .userAnswer(userAnswer)
                .correctAnswer(correctAnswer)
                .correct(correct)
                .spentSeconds(spentSeconds != null ? spentSeconds : 0)
                .deleted(false)
                .build();
    }

    /**
     * 是否正确
     */
    public boolean isCorrect() {
        return Boolean.TRUE.equals(this.correct);
    }

    /**
     * 更新耗时
     */
    public void updateSpentSeconds(Integer spentSeconds) {
        if (spentSeconds == null || spentSeconds < 0) {
            throw new IllegalArgumentException("耗时不能为负数");
        }
        this.spentSeconds = spentSeconds;
    }
}
