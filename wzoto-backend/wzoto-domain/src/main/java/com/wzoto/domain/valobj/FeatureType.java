package com.wzoto.domain.valobj;

import lombok.Getter;

import java.math.BigDecimal;

/**
 * 功能类型 - 值对象 (P1: 单次付费购买)
 */
@Getter
public enum FeatureType {

    RESUME_PIN("RESUME_PIN", "简历置顶", new BigDecimal("29.00"), 7),
    EXPEDITE_VERIFY("EXPEDITE_VERIFY", "加急审核", new BigDecimal("15.00"), 0),
    FULL_COURSE_UNLOCK("FULL_COURSE_UNLOCK", "全量动画课解锁", new BigDecimal("19.90"), 30),
    AI_ANSWER_UNLOCK("AI_ANSWER_UNLOCK", "AI答疑解锁", new BigDecimal("9.90"), 30),
    AI_QA_DAILY("AI_QA_DAILY", "AI答疑每日配额", new BigDecimal("4.90"), 1),
    COMPOSITION_GRADE("COMPOSITION_GRADE", "AI作文批改", new BigDecimal("2.90"), 0),
    ORAL_EVAL("ORAL_EVAL", "AI口语评测", new BigDecimal("1.90"), 0),
    WRONG_BOOK_PRINT("WRONG_BOOK_PRINT", "错题本打印导出", new BigDecimal("4.90"), 0);

    private final String code;
    private final String desc;
    private final BigDecimal price;
    /** 有效天数（0表示永久/一次性） */
    private final int durationDays;

    FeatureType(String code, String desc, BigDecimal price, int durationDays) {
        this.code = code;
        this.desc = desc;
        this.price = price;
        this.durationDays = durationDays;
    }

    public static FeatureType fromCode(String code) {
        for (FeatureType type : values()) {
            if (type.code.equals(code)) return type;
        }
        throw new IllegalArgumentException("未知的功能类型: " + code);
    }

    /** 是否为时效性功能（有持续有效期） */
    public boolean isTimeLimited() {
        return this.durationDays > 0;
    }
}
