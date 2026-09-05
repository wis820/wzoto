package com.wzoto.domain.valobj;

import lombok.Getter;

/**
 * 试卷类型 - 值对象
 */
@Getter
public enum TestPaperType {

    UNIT("UNIT", "单元测试"),
    MID_TERM("MID_TERM", "期中考试"),
    FINAL("FINAL", "期末考试"),
    MOCK("MOCK", "模拟测试");

    private final String code;
    private final String desc;

    TestPaperType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static TestPaperType fromCode(String code) {
        for (TestPaperType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("无效的试卷类型: " + code);
    }
}
