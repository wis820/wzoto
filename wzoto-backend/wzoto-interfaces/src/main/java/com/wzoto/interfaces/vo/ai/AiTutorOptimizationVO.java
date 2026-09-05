package com.wzoto.interfaces.vo.ai;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * AI教员优化记录 VO
 */
@Data
@Builder
public class AiTutorOptimizationVO {

    private Long id;
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
    private LocalDateTime createdAt;
}
