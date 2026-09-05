package com.wzoto.domain.valobj;

import lombok.Getter;

/**
 * 实名认证状态 - 值对象
 */
@Getter
public enum VerifyStatus {
    NONE("NONE", "未审核"),
    PENDING("PENDING", "审核中"),
    APPROVED("APPROVED", "已通过"),
    REJECTED("REJECTED", "已驳回");

    private final String code;
    private final String desc;

    VerifyStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static VerifyStatus fromCode(String code) {
        for (VerifyStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("未知的认证状态: " + code);
    }
}