package com.wzoto.domain.valobj;

import lombok.Getter;

/**
 * 身份类型 - 值对象
 * 一经选择不可随意切换
 */
@Getter
public enum IdentityType {
    PARENT("PARENT", "家长"),
    STUDENT("STUDENT", "大学生");

    private final String code;
    private final String desc;

    IdentityType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static IdentityType fromCode(String code) {
        for (IdentityType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("无效的身份类型: " + code);
    }
}
