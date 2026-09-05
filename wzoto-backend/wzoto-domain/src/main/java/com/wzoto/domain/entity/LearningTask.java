package com.wzoto.domain.entity;

import com.wzoto.domain.valobj.GradeType;
import com.wzoto.domain.valobj.LearningTaskStatus;
import com.wzoto.domain.valobj.LearningTaskType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 学习任务记录 - 领域实体
 * 核心业务规则：任务状态流转：待完成 -> 进行中 -> 已完成/已跳过
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LearningTask {

    /** 主键ID */
    private Long id;

    /** 子女ID */
    private Long childId;

    /** 学习计划配置ID */
    private Long planId;

    /** 任务类型 */
    private LearningTaskType taskType;

    /** 学科：chinese/math/english */
    private String subject;

    /** 任务标题 */
    private String title;

    /** 关联资源ID */
    private Long resourceId;

    /** 任务日期 */
    private LocalDate taskDate;

    /** 任务状态 */
    private LearningTaskStatus status;

    /** 开始时间 */
    private LocalDateTime startTime;

    /** 完成时间 */
    private LocalDateTime completeTime;

    /** 实际耗时（秒） */
    private Integer spentSeconds;

    /** 是否已删除 */
    private Boolean deleted;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间 */
    private LocalDateTime updatedAt;

    // ========== 领域行为 ==========

    /**
     * 创建学习任务
     */
    public static LearningTask create(Long childId, Long planId, LearningTaskType taskType,
                                      String subject, String title, Long resourceId, LocalDate taskDate) {
        return LearningTask.builder()
                .childId(childId)
                .planId(planId)
                .taskType(taskType)
                .subject(subject)
                .title(title)
                .resourceId(resourceId)
                .taskDate(taskDate)
                .status(LearningTaskStatus.PENDING)
                .deleted(false)
                .build();
    }

    /**
     * 开始任务
     */
    public void start() {
        if (this.status != LearningTaskStatus.PENDING) {
            throw new IllegalStateException("只有待完成的任务才能开始，当前状态：" + this.status.getDesc());
        }
        this.status = LearningTaskStatus.IN_PROGRESS;
        this.startTime = LocalDateTime.now();
    }

    /**
     * 完成任务
     */
    public void complete(Integer spentSeconds) {
        if (this.status == LearningTaskStatus.COMPLETED || this.status == LearningTaskStatus.SKIPPED) {
            throw new IllegalStateException("任务已完成或跳过，不可重复完成");
        }
        this.status = LearningTaskStatus.COMPLETED;
        this.completeTime = LocalDateTime.now();
        if (spentSeconds != null && spentSeconds >= 0) {
            this.spentSeconds = spentSeconds;
        }
    }

    /**
     * 跳过任务
     */
    public void skip() {
        if (this.status == LearningTaskStatus.COMPLETED || this.status == LearningTaskStatus.SKIPPED) {
            throw new IllegalStateException("任务已完成或跳过，不可再次跳过");
        }
        this.status = LearningTaskStatus.SKIPPED;
        this.completeTime = LocalDateTime.now();
    }

    /**
     * 是否已完成
     */
    public boolean isCompleted() {
        return LearningTaskStatus.COMPLETED.equals(this.status);
    }

    /**
     * 是否待完成
     */
    public boolean isPending() {
        return LearningTaskStatus.PENDING.equals(this.status);
    }
}
