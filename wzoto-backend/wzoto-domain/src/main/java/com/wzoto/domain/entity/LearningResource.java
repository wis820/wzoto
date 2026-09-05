package com.wzoto.domain.entity;

import com.wzoto.domain.valobj.GradeType;
import com.wzoto.domain.valobj.LearningResourceType;
import com.wzoto.domain.valobj.TextbookVersion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 学习资源 - 领域实体
 * 核心业务规则：资源按年级、学科、教材版本分类，VIP 资源需会员或单次解锁
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LearningResource {

    /** 主键ID */
    private Long id;

    /** 年级 */
    private GradeType grade;

    /** 学科：chinese/math/english */
    private String subject;

    /** 教材版本 */
    private TextbookVersion textbookVersion;

    /** 资源类型 */
    private LearningResourceType resourceType;

    /** 标题 */
    private String title;

    /** 封面URL */
    private String coverUrl;

    /** 内容URL */
    private String contentUrl;

    /** 时长（秒） */
    private Integer durationSeconds;

    /** 知识点 */
    private String knowledgePoint;

    /** 标签（逗号分隔） */
    private String tags;

    /** 视频来源类型：MP4/BILIBILI/SMARTEDU/UPLOAD/CUSTOM */
    private String sourceType;

    /** 字幕URL */
    private String subtitleUrl;

    /** 画质等级JSON */
    private String qualityLevels;

    /** 知识点标记JSON */
    private String knowledgeMarkers;

    /** 资源状态 */
    private String status;

    /** 描述 */
    private String description;

    /** 是否仅 VIP 可用 */
    private Boolean vipOnly;

    /** 排序权重 */
    private Integer sortOrder;

    /** 是否已删除 */
    private Boolean deleted;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间 */
    private LocalDateTime updatedAt;

    // ========== 领域行为 ==========

    /**
     * 创建学习资源
     */
    public static LearningResource create(GradeType grade, String subject, TextbookVersion textbookVersion,
                                          LearningResourceType resourceType, String title) {
        return LearningResource.builder()
                .grade(grade)
                .subject(subject)
                .textbookVersion(textbookVersion)
                .resourceType(resourceType)
                .title(title)
                .vipOnly(false)
                .sortOrder(0)
                .deleted(false)
                .build();
    }

    /**
     * 标记为 VIP 资源
     */
    public void markVipOnly() {
        this.vipOnly = true;
    }

    /**
     * 取消 VIP 限制
     */
    public void markFree() {
        this.vipOnly = false;
    }

    /**
     * 更新排序权重
     */
    public void updateSortOrder(Integer sortOrder) {
        if (sortOrder == null) {
            throw new IllegalArgumentException("排序权重不能为空");
        }
        this.sortOrder = sortOrder;
    }

    /**
     * 是否 VIP 资源
     */
    public boolean isVipOnly() {
        return Boolean.TRUE.equals(this.vipOnly);
    }

    /**
     * 是否为视频资源
     */
    public boolean isVideo() {
        return LearningResourceType.VIDEO.equals(this.resourceType);
    }

    /**
     * 是否为习题资源
     */
    public boolean isExercise() {
        return LearningResourceType.EXERCISE.equals(this.resourceType);
    }
}
