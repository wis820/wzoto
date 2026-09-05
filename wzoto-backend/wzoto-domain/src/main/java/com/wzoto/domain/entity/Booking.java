package com.wzoto.domain.entity;

import com.wzoto.domain.valobj.BookingStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 预约订单领域实体
 * 核心撮合：家长发起预约 → 大学生确认/拒绝 → 线下完成
 */
@Data
public class Booking {

    private Long id;

    /** 家长用户ID（发起方） */
    private Long parentId;

    /** 教员用户ID（接收方） */
    private Long tutorId;

    /** 教员档案ID */
    private Long tutorProfileId;

    /** 辅导科目 */
    private String subject;

    /** 上课日期 */
    private LocalDate bookingDate;

    /** 开始时间 */
    private LocalTime startTime;

    /** 结束时间 */
    private LocalTime endTime;

    /** 上课地址 */
    private String address;

    /** 家长留言 */
    private String message;

    /** 预约状态 */
    private BookingStatus status;

    /** 教员回复/拒绝原因 */
    private String reply;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间 */
    private LocalDateTime updatedAt;

    /**
     * 创建预约
     */
    public void create() {
        this.status = BookingStatus.PENDING;
    }

    /**
     * 教员确认预约
     */
    public void confirm(Long tutorUserId) {
        if (!this.tutorId.equals(tutorUserId)) {
            throw new IllegalArgumentException("无权操作此预约");
        }
        if (this.status != BookingStatus.PENDING) {
            throw new IllegalStateException("当前状态不允许确认");
        }
        this.status = BookingStatus.CONFIRMED;
    }

    /**
     * 教员拒绝预约
     */
    public void reject(Long tutorUserId, String reason) {
        if (!this.tutorId.equals(tutorUserId)) {
            throw new IllegalArgumentException("无权操作此预约");
        }
        if (this.status != BookingStatus.PENDING) {
            throw new IllegalStateException("当前状态不允许拒绝");
        }
        this.status = BookingStatus.REJECTED;
        this.reply = reason;
    }

    /**
     * 取消预约（家长发起）
     */
    public void cancel(Long parentUserId) {
        if (!this.parentId.equals(parentUserId)) {
            throw new IllegalArgumentException("无权操作此预约");
        }
        if (this.status == BookingStatus.COMPLETED || this.status == BookingStatus.CANCELLED) {
            throw new IllegalStateException("当前状态不允许取消");
        }
        this.status = BookingStatus.CANCELLED;
    }

    /**
     * 完成预约
     */
    public void complete() {
        if (this.status != BookingStatus.CONFIRMED) {
            throw new IllegalStateException("只有已确认的预约才能标记完成");
        }
        this.status = BookingStatus.COMPLETED;
    }
}