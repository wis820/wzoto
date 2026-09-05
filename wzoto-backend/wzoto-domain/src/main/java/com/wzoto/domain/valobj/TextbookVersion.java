package com.wzoto.domain.valobj;

import lombok.Getter;

/**
 * 教材版本 - 值对象
 */
@Getter
public enum TextbookVersion {

    RENJIAO("RENJIAO", "人教版"),
    BEISHIDA("BEISHIDA", "北师大版"),
    JIAOSHE("JIAOSHE", "冀教版"),
    SUDAJIAO("SUDAJIAO", "苏教版"),
    WAIXIN("WAIXIN", "外研版"),
    SHANGHAJIAO("SHANGHAJIAO", "沪教版"),
    OTHER("OTHER", "其他");

    private final String code;
    private final String desc;

    TextbookVersion(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static TextbookVersion fromCode(String code) {
        for (TextbookVersion version : values()) {
            if (version.code.equals(code)) {
                return version;
            }
        }
        throw new IllegalArgumentException("无效的教材版本: " + code);
    }
}
