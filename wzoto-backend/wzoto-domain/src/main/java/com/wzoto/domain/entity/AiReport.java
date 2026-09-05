package com.wzoto.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * AI学情诊断报告 - 领域实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiReport {

    private Long id;
    private Long userId;
    private String childName;
    private String weakSubjects;
    private String recentScores;
    private String weakPointDesc;
    private String photoUrls;
    /** AI生成完整报告内容（JSON格式） */
    private String reportContent;
    /** AI生成预览内容（非会员可见） */
    private String previewContent;
    /** 报告类型：PREVIEW/FULL */
    private String reportType;
    /** 是否会员免费报告 */
    private Boolean isMemberReport;
    /** 报告价格 */
    private BigDecimal price;
    /** 支付状态：UNPAID/PAID/FREE */
    private String paymentStatus;
    private Boolean deleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ========== 领域行为 ==========

    /**
     * 创建会员免费报告
     */
    public static AiReport createFreeReport(Long userId, String childName, String weakSubjects,
                                             String recentScores, String weakPointDesc,
                                             String photoUrls, String reportContent, String previewContent) {
        return AiReport.builder()
                .userId(userId)
                .childName(childName)
                .weakSubjects(weakSubjects)
                .recentScores(recentScores)
                .weakPointDesc(weakPointDesc)
                .photoUrls(photoUrls)
                .reportContent(reportContent)
                .previewContent(previewContent)
                .reportType("FULL")
                .isMemberReport(true)
                .price(BigDecimal.ZERO)
                .paymentStatus("FREE")
                .deleted(false)
                .build();
    }

    /**
     * 创建付费报告（非会员）
     */
    public static AiReport createPaidReport(Long userId, String childName, String weakSubjects,
                                             String recentScores, String weakPointDesc,
                                             String photoUrls, String reportContent, String previewContent) {
        return AiReport.builder()
                .userId(userId)
                .childName(childName)
                .weakSubjects(weakSubjects)
                .recentScores(recentScores)
                .weakPointDesc(weakPointDesc)
                .photoUrls(photoUrls)
                .reportContent(reportContent)
                .previewContent(previewContent)
                .reportType("PREVIEW")
                .isMemberReport(false)
                .price(new BigDecimal("19.90"))
                .paymentStatus("UNPAID")
                .deleted(false)
                .build();
    }

    /**
     * 付费后解锁完整报告
     */
    public void unlockAfterPayment() {
        this.reportType = "FULL";
        this.paymentStatus = "PAID";
    }

    /**
     * 是否为完整报告
     */
    public boolean isFullReport() {
        return "FULL".equals(this.reportType);
    }
}
