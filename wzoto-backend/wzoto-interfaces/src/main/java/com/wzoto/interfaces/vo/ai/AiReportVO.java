package com.wzoto.interfaces.vo.ai;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * AI学情诊断报告 VO
 */
@Data
@Builder
public class AiReportVO {

    private Long id;
    private String childName;
    private String weakSubjects;
    private String recentScores;
    private String weakPointDesc;
    private String photoUrls;
    /** 完整报告内容（仅会员/已付费可见） */
    private String reportContent;
    /** 预览内容（非会员可见） */
    private String previewContent;
    /** 报告类型：PREVIEW/FULL */
    private String reportType;
    private Boolean isMemberReport;
    private BigDecimal price;
    /** 支付状态：UNPAID/PAID/FREE */
    private String paymentStatus;
    private LocalDateTime createdAt;
}
