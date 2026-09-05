package com.wzoto.infrastructure.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/** AI学习规划PO */
@Data
@TableName("t_ai_learning_plan")
public class AiLearningPlanPO {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long childId;
    private Long parentId;
    private LocalDate planDate;
    private String planContentJson;
    private String weakPointsJson;
    private String aiSuggestion;
    private Boolean applied;
    private LocalDateTime appliedAt;
    private LocalDateTime generatedAt;
    @TableLogic
    private Boolean deleted;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
