package com.wzoto.domain.valobj;

import lombok.Getter;

/**
 * AI提问类型 - 值对象
 */
@Getter
public enum AiQaType {

    PHOTO("PHOTO", "拍照搜题"),
    TEXT("TEXT", "文字提问");

    private final String code;
    private final String desc;

    AiQaType(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static AiQaType fromCode(String code) {
        for (AiQaType type : values()) {
            if (type.code.equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("无效的AI提问类型: " + code);
    }
}
