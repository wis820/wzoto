package com.wzoto.domain.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 认证类型值对象
 */
@Getter
@AllArgsConstructor
public enum VerifyType {

    PARENT_IDCARD("PARENT_IDCARD", "家长身份证认证"),
    STUDENT_IDCARD("STUDENT_IDCARD", "学生身份证认证"),
    STUDENT_CARD("STUDENT_CARD", "学生证认证");

    private final String code;
    private final String desc;

    public static VerifyType fromCode(String code) {
        for (VerifyType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("未知的认证类型: " + code);
    }
}