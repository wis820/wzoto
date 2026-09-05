package com.wzoto.domain.valobj;

import lombok.Getter;

/**
 * AI批改类型 - 值对象
 */
@Getter
public enum AiGradingType {

    COMPOSITION("COMPOSITION", "作文批改"),
    PRONUNCIATION("PRONUNCIATION", "口语评测");

    private final String code;
    private final String desc;

    AiGradingType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static AiGradingType fromCode(String code) {
        for (AiGradingType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("无效的AI批改类型: " + code);
    }
}
