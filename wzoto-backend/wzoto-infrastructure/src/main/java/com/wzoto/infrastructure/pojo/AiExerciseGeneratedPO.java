package com.wzoto.infrastructure.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/** AI生成练习题PO */
@Data
@TableName("t_ai_exercise_generated")
public class AiExerciseGeneratedPO {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long childId;
    private Long parentId;
    private String source;
    private Long sourceId;
    private String subject;
    private String grade;
    private String knowledgePoint;
    private String questionContent;
    private String correctAnswer;
    private String aiExplanation;
    private String difficulty;
    private Boolean answered;
    private Boolean isCorrect;
    private LocalDateTime answeredAt;
    @TableLogic
    private Boolean deleted;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
