package com.wzoto.domain.valobj;

import lombok.Getter;

/**
 * 支付状态 - 值对象
 */
@Getter
public enum PaymentStatus {

    UNPAID("UNPAID", "待支付"),
    PAID("PAID", "已支付");

    private final String code;
    private final String desc;

    PaymentStatus(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static PaymentStatus fromCode(String code) {
        for (PaymentStatus status : values()) {
            if (status.code.equals(code)) return status;
        }
        throw new IllegalArgumentException("未知的支付状态: " + code);
    }
}
