package com.wzoto.domain.entity;

import com.wzoto.domain.valobj.ExerciseDifficulty;
import com.wzoto.domain.valobj.GradeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * AI生成练习题 - 领域实体
 * 根据错题/薄弱点自动生成针对性练习
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiExerciseGenerated {
    private Long id;
    private Long childId;
    private Long parentId;
    private String source;
    private Long sourceId;
    private String subject;
    private GradeType grade;
    private String knowledgePoint;
    private String questionContent;
    private String correctAnswer;
    private String aiExplanation;
    private ExerciseDifficulty difficulty;
    private Boolean answered;
    private Boolean isCorrect;
    private LocalDateTime answeredAt;
    private Boolean deleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /** 标记已作答 */
    public void markAnswered(boolean correct) {
        this.answered = true;
        this.isCorrect = correct;
        this.answeredAt = LocalDateTime.now();
    }
}
