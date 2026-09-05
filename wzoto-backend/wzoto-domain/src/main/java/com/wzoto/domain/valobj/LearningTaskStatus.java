package com.wzoto.domain.valobj;

import lombok.Getter;

/**
 * 学习任务状态 - 值对象
 */
@Getter
public enum LearningTaskStatus {

    PENDING("PENDING", "待完成"),
    IN_PROGRESS("IN_PROGRESS", "进行中"),
    COMPLETED("COMPLETED", "已完成"),
    SKIPPED("SKIPPED", "已跳过");

    private final String code;
    private final String desc;

    LearningTaskStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static LearningTaskStatus fromCode(String code) {
        for (LearningTaskStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("无效的任务状态: " + code);
    }
}
