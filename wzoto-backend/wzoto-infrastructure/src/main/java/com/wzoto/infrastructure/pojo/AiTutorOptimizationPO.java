package com.wzoto.infrastructure.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * AI教员优化记录 - 持久化对象
 */
@Data
@TableName("t_ai_tutor_optimization")
public class AiTutorOptimizationPO {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String optimizationType;
    private String university;
    private String major;
    private String grade;
    private String subjects;
    private String currentBio;
    private String currentExperience;
    private Integer currentRate;
    private String optimizedContent;
    private String previewContent;
    private Boolean isMemberReport;
    private BigDecimal price;
    private String paymentStatus;
    @TableLogic
    private Boolean deleted;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
