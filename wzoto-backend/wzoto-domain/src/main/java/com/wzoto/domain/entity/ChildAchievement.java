package com.wzoto.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 成长激励 - 领域实体
 * 核心业务规则：勋章、积分、皮肤等激励记录，家长可下发激励任务
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChildAchievement {

    /** 主键ID */
    private Long id;

    /** 子女ID */
    private Long childId;

    /** 勋章类型：medal/points/skin/task */
    private String achievementType;

    /** 勋章编码 */
    private String achievementCode;

    /** 勋章名称 */
    private String achievementName;

    /** 勋章图标URL */
    private String iconUrl;

    /** 积分值 */
    private Integer points;

    /** 皮肤编码 */
    private String skinCode;

    /** 任务描述 */
    private String taskDescription;

    /** 家长下发任务状态：PENDING/COMPLETED */
    private String taskStatus;

    /** 获得时间 */
    private LocalDateTime obtainedAt;

    /** 是否已删除 */
    private Boolean deleted;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间 */
    private LocalDateTime updatedAt;

    // ========== 领域行为 ==========

    /**
     * 创建勋章记录
     */
    public static ChildAchievement createMedal(Long childId, String achievementCode,
                                               String achievementName, String iconUrl) {
        return ChildAchievement.builder()
                .childId(childId)
                .achievementType("medal")
                .achievementCode(achievementCode)
                .achievementName(achievementName)
                .iconUrl(iconUrl)
                .points(0)
                .obtainedAt(LocalDateTime.now())
                .deleted(false)
                .build();
    }

    /**
     * 创建积分记录
     */
    public static ChildAchievement createPoints(Long childId, Integer points, String achievementName) {
        return ChildAchievement.builder()
                .childId(childId)
                .achievementType("points")
                .achievementName(achievementName)
                .points(points)
                .obtainedAt(LocalDateTime.now())
                .deleted(false)
                .build();
    }

    /**
     * 创建皮肤记录
     */
    public static ChildAchievement createSkin(Long childId, String skinCode, String achievementName, String iconUrl) {
        return ChildAchievement.builder()
                .childId(childId)
                .achievementType("skin")
                .achievementCode(skinCode)
                .achievementName(achievementName)
                .iconUrl(iconUrl)
                .skinCode(skinCode)
                .obtainedAt(LocalDateTime.now())
                .deleted(false)
                .build();
    }

    /**
     * 家长下发激励任务
     */
    public static ChildAchievement createTask(Long childId, String achievementName,
                                              String taskDescription, Integer points) {
        return ChildAchievement.builder()
                .childId(childId)
                .achievementType("task")
                .achievementName(achievementName)
                .taskDescription(taskDescription)
                .points(points)
                .taskStatus("PENDING")
                .obtainedAt(LocalDateTime.now())
                .deleted(false)
                .build();
    }

    /**
     * 完成任务
     */
    public void completeTask() {
        if (!"task".equals(this.achievementType)) {
            throw new IllegalStateException("只有任务类型的成就才能被完成");
        }
        if ("COMPLETED".equals(this.taskStatus)) {
            throw new IllegalStateException("任务已完成，请勿重复完成");
        }
        this.taskStatus = "COMPLETED";
    }

    /**
     * 是否已完成
     */
    public boolean isCompleted() {
        return "COMPLETED".equals(this.taskStatus);
    }
}
