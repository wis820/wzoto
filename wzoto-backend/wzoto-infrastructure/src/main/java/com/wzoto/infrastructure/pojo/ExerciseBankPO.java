package com.wzoto.infrastructure.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/** 习题库PO */
@Data
@TableName("t_exercise_bank")
public class ExerciseBankPO {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String grade;
    private String subject;
    private String textbookVersion;
    private Long knowledgePointId;
    private String questionType;
    private String questionContent;
    private String options;
    private String correctAnswer;
    private String explanation;
    private String difficulty;
    private String sourceType;
    private Boolean vipOnly;
    private Integer useCount;
    private Integer correctRate;
    private Integer sortOrder;
    @TableLogic
    private Boolean deleted;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
