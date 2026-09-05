package com.wzoto.domain.valobj;

import lombok.Getter;

/**
 * 学习资源类型 - 值对象
 */
@Getter
public enum LearningResourceType {

    VIDEO("VIDEO", "动画微课"),
    EXERCISE("EXERCISE", "互动练习"),
    PDF("PDF", "PDF资料"),
    QUESTION("QUESTION", "题库题目");

    private final String code;
    private final String desc;

    LearningResourceType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static LearningResourceType fromCode(String code) {
        for (LearningResourceType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("无效的资源类型: " + code);
    }
}
