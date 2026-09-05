package com.wzoto.infrastructure.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 错题库PO - 持久化对象
 */
@Data
@TableName("t_wrong_question_bank")
public class WrongQuestionBankPO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long childId;

    private Long resourceId;

    private String subject;

    private String knowledgePoint;

    private Integer mistakeCount;

    private String masteryLevel;

    private Boolean inReviewPlan;

    private String reviewPlanTag;

    private LocalDateTime lastMistakeTime;

    @TableLogic
    private Boolean deleted;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
