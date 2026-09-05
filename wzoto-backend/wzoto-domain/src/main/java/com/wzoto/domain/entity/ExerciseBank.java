package com.wzoto.domain.entity;

import com.wzoto.domain.valobj.ExerciseDifficulty;
import com.wzoto.domain.valobj.GradeType;
import com.wzoto.domain.valobj.TextbookVersion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 习题库 - 领域实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExerciseBank {
    private Long id;
    private GradeType grade;
    private String subject;
    private TextbookVersion textbookVersion;
    private Long knowledgePointId;
    private String questionType;
    private String questionContent;
    private String options;
    private String correctAnswer;
    private String explanation;
    private ExerciseDifficulty difficulty;
    private String sourceType;
    private Boolean vipOnly;
    private Integer useCount;
    private Integer correctRate;
    private Integer sortOrder;
    private Boolean deleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /** 检查答案是否正确 */
    public boolean checkAnswer(String studentAnswer) {
        if (this.correctAnswer == null || studentAnswer == null) return false;
        return this.correctAnswer.trim().equalsIgnoreCase(studentAnswer.trim());
    }

    /** 增加使用次数 */
    public void incrementUseCount() {
        this.useCount = (this.useCount == null ? 0 : this.useCount) + 1;
    }
}
