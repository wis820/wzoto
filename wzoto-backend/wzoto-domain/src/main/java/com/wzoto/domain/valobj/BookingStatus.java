package com.wzoto.domain.valobj;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 预约状态值对象
 */
@Getter
@AllArgsConstructor
public enum BookingStatus {

    PENDING("PENDING", "待确认"),
    CONFIRMED("CONFIRMED", "已确认"),
    COMPLETED("COMPLETED", "已完成"),
    CANCELLED("CANCELLED", "已取消"),
    REJECTED("REJECTED", "已拒绝");

    private final String code;
    private final String desc;

    public static BookingStatus fromCode(String code) {
        for (BookingStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("未知的预约状态: " + code);
    }
}