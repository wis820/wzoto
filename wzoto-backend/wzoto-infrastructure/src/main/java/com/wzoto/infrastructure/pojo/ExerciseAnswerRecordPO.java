package com.wzoto.infrastructure.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 习题作答记录PO - 持久化对象
 */
@Data
@TableName("t_exercise_answer_record")
public class ExerciseAnswerRecordPO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long childId;

    private Long resourceId;

    private Long taskId;

    private String subject;

    private String knowledgePoint;

    private String userAnswer;

    private String correctAnswer;

    private Boolean correct;

    private Integer spentSeconds;

    @TableLogic
    private Boolean deleted;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
