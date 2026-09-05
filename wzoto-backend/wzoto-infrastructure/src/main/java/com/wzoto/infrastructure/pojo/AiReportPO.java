package com.wzoto.infrastructure.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * AI学情诊断报告 - 持久化对象
 */
@Data
@TableName("t_ai_report")
public class AiReportPO {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String childName;
    private String weakSubjects;
    private String recentScores;
    private String weakPointDesc;
    private String photoUrls;
    private String reportContent;
    private String previewContent;
    private String reportType;
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
