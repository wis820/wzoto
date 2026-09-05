package com.wzoto.domain.valobj;

import lombok.Getter;

/**
 * 学科类型 - 值对象
 */
@Getter
public enum SubjectType {

    MATH("MATH", "数学", "📐", "#4F6EF7"),
    CHINESE("CHINESE", "语文", "📖", "#52C41A"),
    ENGLISH("ENGLISH", "英语", "🔤", "#FAAD14");

    private final String code;
    private final String desc;
    private final String icon;
    private final String color;

    SubjectType(String code, String desc, String icon, String color) {
        this.code = code;
        this.desc = desc;
        this.icon = icon;
        this.color = color;
    }

    public static SubjectType fromCode(String code) {
        for (SubjectType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("无效的学科: " + code);
    }
}
