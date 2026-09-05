package com.wzoto.domain.valobj;

import lombok.Getter;

/**
 * 学习任务类型 - 值对象
 */
@Getter
public enum LearningTaskType {

    VIDEO("VIDEO", "动画微课"),
    EXERCISE("EXERCISE", "同步练习"),
    REVIEW("REVIEW", "复习巩固"),
    SPECIAL_CALCULATION("SPECIAL_CALCULATION", "计算专项"),
    SPECIAL_APPLICATION("SPECIAL_APPLICATION", "应用题专项"),
    SPECIAL_LITERACY("SPECIAL_LITERACY", "识字专项"),
    SPECIAL_WORDS("SPECIAL_WORDS", "背单词专项");

    private final String code;
    private final String desc;

    LearningTaskType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static LearningTaskType fromCode(String code) {
        for (LearningTaskType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("无效的任务类型: " + code);
    }
}
