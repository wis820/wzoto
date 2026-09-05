package com.wzoto.infrastructure.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 学习任务记录PO - 持久化对象
 */
@Data
@TableName("t_learning_task")
public class LearningTaskPO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long childId;

    private Long planId;

    private String taskType;

    private String subject;

    private String title;

    private Long resourceId;

    private LocalDate taskDate;

    private String status;

    private LocalDateTime startTime;

    private LocalDateTime completeTime;

    private Integer spentSeconds;

    @TableLogic
    private Boolean deleted;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
