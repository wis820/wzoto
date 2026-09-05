package com.wzoto.domain.entity;

import com.wzoto.domain.valobj.MasteryLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 错题库 - 领域实体
 * 核心业务规则：错题记录错误次数，可标记掌握状态并加入复习计划
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WrongQuestionBank {

    /** 主键ID */
    private Long id;

    /** 子女ID */
    private Long childId;

    /** 学习资源ID（题库题目） */
    private Long resourceId;

    /** 学科：chinese/math/english */
    private String subject;

    /** 知识点 */
    private String knowledgePoint;

    /** 错误次数 */
    private Integer mistakeCount;

    /** 掌握度：NOT_MASTERED/GENERAL/PROFICIENT */
    private MasteryLevel masteryLevel;

    /** 是否已加入复习计划 */
    private Boolean inReviewPlan;

    /** 复习计划标识 */
    private String reviewPlanTag;

    /** 最近错误时间 */
    private LocalDateTime lastMistakeTime;

    /** 是否已删除 */
    private Boolean deleted;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间 */
    private LocalDateTime updatedAt;

    // ========== 领域行为 ==========

    /**
     * 创建错题记录
     */
    public static WrongQuestionBank create(Long childId, Long resourceId, String subject, String knowledgePoint) {
        LocalDateTime now = LocalDateTime.now();
        return WrongQuestionBank.builder()
                .childId(childId)
                .resourceId(resourceId)
                .subject(subject)
                .knowledgePoint(knowledgePoint)
                .mistakeCount(1)
                .masteryLevel(MasteryLevel.NOT_MASTERED)
                .inReviewPlan(false)
                .lastMistakeTime(now)
                .deleted(false)
                .build();
    }

    /**
     * 新增一次错误
     */
    public void addMistake() {
        this.mistakeCount = (this.mistakeCount == null ? 0 : this.mistakeCount) + 1;
        this.lastMistakeTime = LocalDateTime.now();
        recalculateMastery();
    }

    /**
     * 标记已掌握
     */
    public void markMastered() {
        this.masteryLevel = MasteryLevel.PROFICIENT;
    }

    /**
     * 加入复习计划
     */
    public void addToReviewPlan(String reviewPlanTag) {
        this.inReviewPlan = true;
        this.reviewPlanTag = reviewPlanTag;
    }

    /**
     * 移除复习计划
     */
    public void removeFromReviewPlan() {
        this.inReviewPlan = false;
        this.reviewPlanTag = null;
    }

    /**
     * 根据错误次数重新计算掌握度
     */
    public void recalculateMastery() {
        int count = this.mistakeCount == null ? 0 : this.mistakeCount;
        if (count >= 3) {
            this.masteryLevel = MasteryLevel.NOT_MASTERED;
        } else if (count >= 1) {
            this.masteryLevel = MasteryLevel.GENERAL;
        } else {
            this.masteryLevel = MasteryLevel.PROFICIENT;
        }
    }

    /**
     * 是否已掌握
     */
    public boolean isMastered() {
        return MasteryLevel.PROFICIENT.equals(this.masteryLevel);
    }
}
