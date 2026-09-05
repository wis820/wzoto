package com.wzoto.domain.valobj;

import lombok.Getter;

/**
 * 会员类型 - 值对象
 */
@Getter
public enum MemberType {
    PARENT_MONTH("PARENT_MONTH", "家长月卡", 39.00, 30),
    PARENT_YEAR("PARENT_YEAR", "家长年卡", 299.00, 365),
    STUDENT_MONTH("STUDENT_MONTH", "教员月卡", 29.00, 30),
    LEARNING_MONTH("LEARNING_MONTH", "学习会员月卡", 39.00, 30),
    LEARNING_YEAR("LEARNING_YEAR", "学习会员年卡", 299.00, 365);

    private final String code;
    private final String desc;
    private final double price;
    private final int days;

    MemberType(String code, String desc, double price, int days) {
        this.code = code;
        this.desc = desc;
        this.price = price;
        this.days = days;
    }

    public static MemberType fromCode(String code) {
        for (MemberType type : values()) {
            if (type.code.equals(code)) return type;
        }
        throw new IllegalArgumentException("未知的会员类型: " + code);
    }

    /** 是否为家长会员类型 */
    public boolean isParentType() {
        return this == PARENT_MONTH || this == PARENT_YEAR;
    }

    /** 是否为大学生会员类型 */
    public boolean isStudentType() {
        return this == STUDENT_MONTH;
    }

    /** 是否为学习会员类型 */
    public boolean isLearningType() {
        return this == LEARNING_MONTH || this == LEARNING_YEAR;
    }
}
