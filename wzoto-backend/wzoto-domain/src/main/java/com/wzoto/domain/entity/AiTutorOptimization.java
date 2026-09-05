package com.wzoto.domain.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * AI教员优化记录 - 领域实体
 * 包含简历优化和定价分析两种类型
 */
@Data
public class AiTutorOptimization {

    private Long id;
    private Long userId;

    /** 优化类型：RESUME-简历优化, PRICING-定价分析 */
    private String optimizationType;

    private String university;
    private String major;
    private String grade;
    private String subjects;
    private String currentBio;
    private String currentExperience;
    private Integer currentRate;

    /** AI生成的优化结果JSON */
    private String optimizedContent;
    /** 预览摘要 */
    private String previewContent;

    private Boolean isMemberReport;
    private BigDecimal price;
    private String paymentStatus;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * 是否为完整报告（已解锁）
     */
    public boolean isFullReport() {
        return "FREE".equals(paymentStatus) || "PAID".equals(paymentStatus);
    }

    /**
     * 是否为简历优化
     */
    public boolean isResumeOptimization() {
        return "RESUME".equals(optimizationType);
    }

    /**
     * 是否为定价分析
     */
    public boolean isPricingAnalysis() {
        return "PRICING".equals(optimizationType);
    }
}
