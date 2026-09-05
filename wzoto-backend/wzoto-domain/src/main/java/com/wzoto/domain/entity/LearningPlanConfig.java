package com.wzoto.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 学习计划配置 - 领域实体
 * 核心业务规则：每日学习总时长由各学科权重拆分，专项练习可独立开关
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LearningPlanConfig {

    /** 主键ID */
    private Long id;

    /** 子女ID */
    private Long childId;

    /** 每日学习时长（分钟） */
    private Integer dailyDurationMinutes;

    /** 语文权重 */
    private Integer chineseWeight;

    /** 数学权重 */
    private Integer mathWeight;

    /** 英语权重 */
    private Integer englishWeight;

    /** 计算专项开关 */
    private Boolean specialCalculationEnabled;

    /** 应用题专项开关 */
    private Boolean specialApplicationEnabled;

    /** 识字专项开关 */
    private Boolean specialLiteracyEnabled;

    /** 背单词专项开关 */
    private Boolean specialWordsEnabled;

    /** 是否已删除 */
    private Boolean deleted;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间 */
    private LocalDateTime updatedAt;

    // ========== 领域行为 ==========

    /**
     * 创建默认学习计划配置
     */
    public static LearningPlanConfig createDefault(Long childId) {
        return LearningPlanConfig.builder()
                .childId(childId)
                .dailyDurationMinutes(30)
                .chineseWeight(30)
                .mathWeight(40)
                .englishWeight(30)
                .specialCalculationEnabled(false)
                .specialApplicationEnabled(false)
                .specialLiteracyEnabled(false)
                .specialWordsEnabled(false)
                .deleted(false)
                .build();
    }

    /**
     * 调整每日学习时长
     */
    public void adjustDuration(Integer dailyDurationMinutes) {
        if (dailyDurationMinutes == null || dailyDurationMinutes < 5 || dailyDurationMinutes > 240) {
            throw new IllegalArgumentException("每日学习时长必须在 5-240 分钟之间");
        }
        this.dailyDurationMinutes = dailyDurationMinutes;
    }

    /**
     * 调整学科权重
     */
    public void adjustSubjectWeight(Integer chineseWeight, Integer mathWeight, Integer englishWeight) {
        if (chineseWeight == null || mathWeight == null || englishWeight == null) {
            throw new IllegalArgumentException("学科权重不能为空");
        }
        int total = chineseWeight + mathWeight + englishWeight;
        if (total <= 0) {
            throw new IllegalArgumentException("学科权重总和必须大于 0");
        }
        this.chineseWeight = chineseWeight;
        this.mathWeight = mathWeight;
        this.englishWeight = englishWeight;
    }

    /**
     * 切换专项练习开关
     */
    public void toggleSpecial(String specialType, Boolean enabled) {
        if (enabled == null) {
            return;
        }
        switch (specialType) {
            case "calculation" -> this.specialCalculationEnabled = enabled;
            case "application" -> this.specialApplicationEnabled = enabled;
            case "literacy" -> this.specialLiteracyEnabled = enabled;
            case "words" -> this.specialWordsEnabled = enabled;
            default -> throw new IllegalArgumentException("未知的专项练习类型: " + specialType);
        }
    }

    /**
     * 获取语文每日时长（分钟）
     */
    public int getChineseDuration() {
        return calculateDuration(this.chineseWeight);
    }

    /**
     * 获取数学每日时长（分钟）
     */
    public int getMathDuration() {
        return calculateDuration(this.mathWeight);
    }

    /**
     * 获取英语每日时长（分钟）
     */
    public int getEnglishDuration() {
        return calculateDuration(this.englishWeight);
    }

    private int calculateDuration(Integer weight) {
        if (this.dailyDurationMinutes == null || weight == null) {
            return 0;
        }
        int total = (this.chineseWeight == null ? 0 : this.chineseWeight)
                + (this.mathWeight == null ? 0 : this.mathWeight)
                + (this.englishWeight == null ? 0 : this.englishWeight);
        if (total == 0) {
            return 0;
        }
        return this.dailyDurationMinutes * weight / total;
    }
}
