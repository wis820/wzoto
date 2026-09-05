package com.wzoto.domain.valobj;

import lombok.Getter;

/**
 * 小学年级 - 值对象
 */
@Getter
public enum GradeType {

    GRADE_1("GRADE_1", "一年级", 1),
    GRADE_2("GRADE_2", "二年级", 2),
    GRADE_3("GRADE_3", "三年级", 3),
    GRADE_4("GRADE_4", "四年级", 4),
    GRADE_5("GRADE_5", "五年级", 5),
    GRADE_6("GRADE_6", "六年级", 6);

    private final String code;
    private final String desc;
    private final int grade;

    GradeType(String code, String desc, int grade) {
        this.code = code;
        this.desc = desc;
        this.grade = grade;
    }

    public static GradeType fromCode(String code) {
        for (GradeType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("无效的年级: " + code);
    }

    public static GradeType fromGrade(int grade) {
        for (GradeType type : values()) {
            if (type.grade == grade) {
                return type;
            }
        }
        throw new IllegalArgumentException("无效的年级数字: " + grade);
    }
}
