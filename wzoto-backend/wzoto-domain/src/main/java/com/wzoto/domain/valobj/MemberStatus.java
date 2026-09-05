package com.wzoto.domain.valobj;

import lombok.Getter;

/**
 * 会员状态 - 值对象
 */
@Getter
public enum MemberStatus {
    ACTIVE("ACTIVE", "生效中"),
    EXPIRED("EXPIRED", "已过期"),
    CANCELLED("CANCELLED", "已取消");

    private final String code;
    private final String desc;

    MemberStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static MemberStatus fromCode(String code) {
        for (MemberStatus status : values()) {
            if (status.code.equals(code)) return status;
        }
        throw new IllegalArgumentException("未知的会员状态: " + code);
    }
}
