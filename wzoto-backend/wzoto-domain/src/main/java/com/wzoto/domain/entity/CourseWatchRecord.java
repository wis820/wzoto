package com.wzoto.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 课程观看记录 - 领域实体
 * 核心业务规则：观看进度百分比 0-100，达到 90% 视为完成
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseWatchRecord {

    /** 主键ID */
    private Long id;

    /** 子女ID */
    private Long childId;

    /** 学习资源ID */
    private Long resourceId;

    /** 观看时长（秒） */
    private Integer watchDurationSeconds;

    /** 进度百分比（0-100） */
    private Integer progressPercent;

    /** 最后观看位置（秒） */
    private Integer lastPositionSeconds;

    /** 是否已完成 */
    private Boolean completed;

    /** 首次观看时间 */
    private LocalDateTime firstWatchTime;

    /** 最后观看时间 */
    private LocalDateTime lastWatchTime;

    /** 是否已删除 */
    private Boolean deleted;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间 */
    private LocalDateTime updatedAt;

    // ========== 领域行为 ==========

    /**
     * 创建观看记录
     */
    public static CourseWatchRecord create(Long childId, Long resourceId) {
        LocalDateTime now = LocalDateTime.now();
        return CourseWatchRecord.builder()
                .childId(childId)
                .resourceId(resourceId)
                .watchDurationSeconds(0)
                .progressPercent(0)
                .lastPositionSeconds(0)
                .completed(false)
                .firstWatchTime(now)
                .lastWatchTime(now)
                .deleted(false)
                .build();
    }

    /**
     * 记录观看进度
     */
    public void recordProgress(Integer watchDurationSeconds, Integer lastPositionSeconds) {
        if (watchDurationSeconds == null || watchDurationSeconds < 0) {
            throw new IllegalArgumentException("观看时长不能为负数");
        }
        if (lastPositionSeconds == null || lastPositionSeconds < 0) {
            throw new IllegalArgumentException("观看位置不能为负数");
        }
        this.watchDurationSeconds = watchDurationSeconds;
        this.lastPositionSeconds = lastPositionSeconds;
        this.lastWatchTime = LocalDateTime.now();
    }

    /**
     * 更新进度百分比
     */
    public void updateProgressPercent(Integer progressPercent) {
        if (progressPercent == null || progressPercent < 0 || progressPercent > 100) {
            throw new IllegalArgumentException("进度百分比必须在 0-100 之间");
        }
        this.progressPercent = progressPercent;
        if (progressPercent >= 90) {
            this.completed = true;
        }
    }

    /**
     * 标记已完成
     */
    public void markCompleted() {
        this.completed = true;
        this.progressPercent = 100;
        this.lastWatchTime = LocalDateTime.now();
    }

    /**
     * 是否已学完
     */
    public boolean isCompleted() {
        return Boolean.TRUE.equals(this.completed);
    }
}
