package com.wzoto.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * AI学习规划记录 - 领域实体
 * AI根据学情自动生成每日学习计划
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiLearningPlan {
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
    private Boolean deleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /** 创建AI学习计划 */
    public static AiLearningPlan create(Long childId, Long parentId, LocalDate planDate,
                                         String planContentJson, String weakPointsJson, String aiSuggestion) {
        return AiLearningPlan.builder()
                .childId(childId)
                .parentId(parentId)
                .planDate(planDate)
                .planContentJson(planContentJson)
                .weakPointsJson(weakPointsJson)
                .aiSuggestion(aiSuggestion)
                .applied(false)
                .generatedAt(LocalDateTime.now())
                .deleted(false)
                .build();
    }

    /** 标记为已应用 */
    public void markApplied() {
        this.applied = true;
        this.appliedAt = LocalDateTime.now();
    }
}
