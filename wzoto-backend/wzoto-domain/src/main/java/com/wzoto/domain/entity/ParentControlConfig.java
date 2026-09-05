package com.wzoto.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 家长管控配置 - 领域实体
 * 核心业务规则：每日可用时长、单次休息间隔、禁用时段、锁定开关、护眼模式
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParentControlConfig {

    /** 主键ID */
    private Long id;

    /** 家长用户ID */
    private Long parentId;

    /** 子女ID */
    private Long childId;

    /** 每日可用时长（分钟） */
    private Integer dailyLimitMinutes;

    /** 单次休息间隔（分钟） */
    private Integer restIntervalMinutes;

    /** 禁用时段开始（HH:mm） */
    private String forbiddenStartTime;

    /** 禁用时段结束（HH:mm） */
    private String forbiddenEndTime;

    /** 是否锁定 */
    private Boolean locked;

    /** 护眼模式 */
    private Boolean eyeProtectionMode;

    /** 蓝光过滤 */
    private Boolean blueLightFilter;

    /** 坐姿提醒 */
    private Boolean postureReminder;

    /** 是否已删除 */
    private Boolean deleted;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间 */
    private LocalDateTime updatedAt;

    // ========== 领域行为 ==========

    /**
     * 创建默认管控配置
     */
    public static ParentControlConfig createDefault(Long parentId, Long childId) {
        return ParentControlConfig.builder()
                .parentId(parentId)
                .childId(childId)
                .dailyLimitMinutes(60)
                .restIntervalMinutes(20)
                .forbiddenStartTime("22:00")
                .forbiddenEndTime("07:00")
                .locked(false)
                .eyeProtectionMode(false)
                .blueLightFilter(false)
                .postureReminder(false)
                .deleted(false)
                .build();
    }

    /**
     * 更新每日可用时长
     */
    public void updateTimeLimit(Integer dailyLimitMinutes) {
        if (dailyLimitMinutes == null || dailyLimitMinutes < 0 || dailyLimitMinutes > 240) {
            throw new IllegalArgumentException("每日可用时长必须在 0-240 分钟之间");
        }
        this.dailyLimitMinutes = dailyLimitMinutes;
    }

    /**
     * 更新单次休息间隔
     */
    public void updateRestInterval(Integer restIntervalMinutes) {
        if (restIntervalMinutes == null || restIntervalMinutes < 5 || restIntervalMinutes > 120) {
            throw new IllegalArgumentException("单次休息间隔必须在 5-120 分钟之间");
        }
        this.restIntervalMinutes = restIntervalMinutes;
    }

    /**
     * 更新禁用时段
     */
    public void updateForbiddenTime(String startTime, String endTime) {
        if (startTime != null) {
            this.forbiddenStartTime = startTime;
        }
        if (endTime != null) {
            this.forbiddenEndTime = endTime;
        }
    }

    /**
     * 一键锁定
     */
    public void lock() {
        this.locked = true;
    }

    /**
     * 解除锁定
     */
    public void unlock() {
        this.locked = false;
    }

    /**
     * 切换锁定状态
     */
    public void toggleLock() {
        this.locked = !Boolean.TRUE.equals(this.locked);
    }

    /**
     * 切换护眼模式
     */
    public void toggleEyeProtection() {
        this.eyeProtectionMode = !Boolean.TRUE.equals(this.eyeProtectionMode);
    }

    /**
     * 切换蓝光过滤
     */
    public void toggleBlueLightFilter() {
        this.blueLightFilter = !Boolean.TRUE.equals(this.blueLightFilter);
    }

    /**
     * 切换坐姿提醒
     */
    public void togglePostureReminder() {
        this.postureReminder = !Boolean.TRUE.equals(this.postureReminder);
    }

    /**
     * 当前是否处于锁定状态
     */
    public boolean isLocked() {
        return Boolean.TRUE.equals(this.locked);
    }
}
