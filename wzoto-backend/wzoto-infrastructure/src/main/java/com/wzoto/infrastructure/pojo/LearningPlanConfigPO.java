package com.wzoto.infrastructure.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 学习计划配置PO - 持久化对象
 */
@Data
@TableName("t_learning_plan_config")
public class LearningPlanConfigPO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long childId;

    private Integer dailyDurationMinutes;

    private Integer chineseWeight;

    private Integer mathWeight;

    private Integer englishWeight;

    private Boolean specialCalculationEnabled;

    private Boolean specialApplicationEnabled;

    private Boolean specialLiteracyEnabled;

    private Boolean specialWordsEnabled;

    @TableLogic
    private Boolean deleted;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
