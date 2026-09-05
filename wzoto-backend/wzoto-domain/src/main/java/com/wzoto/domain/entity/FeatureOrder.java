package com.wzoto.domain.entity;

import com.wzoto.domain.valobj.FeatureType;
import com.wzoto.domain.valobj.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 功能订单 - 领域实体 (P1: 单次付费购买)
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeatureOrder {

    private Long id;
    private Long userId;
    private FeatureType featureType;
    private Long targetId;
    private BigDecimal amount;
    private PaymentStatus paymentStatus;
    private LocalDateTime paymentTime;
    private String transactionId;
    private LocalDateTime expireTime;
    private Boolean deleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ========== 领域行为 ==========

    /**
     * 创建功能订单
     */
    public static FeatureOrder create(Long userId, FeatureType featureType, Long targetId) {
        LocalDateTime now = LocalDateTime.now();
        return FeatureOrder.builder()
                .userId(userId)
                .featureType(featureType)
                .targetId(targetId)
                .amount(featureType.getPrice())
                .paymentStatus(PaymentStatus.UNPAID)
                .deleted(false)
                .build();
    }

    /**
     * 支付成功
     */
    public void pay(String transactionId) {
        if (this.paymentStatus == PaymentStatus.PAID) {
            throw new IllegalStateException("订单已支付，请勿重复支付");
        }
        this.paymentStatus = PaymentStatus.PAID;
        this.paymentTime = LocalDateTime.now();
        this.transactionId = transactionId;
        // 如果是时效性功能，设置到期时间
        if (this.featureType.isTimeLimited()) {
            this.expireTime = this.paymentTime.plusDays(this.featureType.getDurationDays());
        }
    }

    /**
     * 是否已支付
     */
    public boolean isPaid() {
        return PaymentStatus.PAID.equals(this.paymentStatus);
    }

    /**
     * 置顶是否仍在有效期内
     */
    public boolean isPinActive() {
        return isPaid() && this.featureType == FeatureType.RESUME_PIN
                && this.expireTime != null && this.expireTime.isAfter(LocalDateTime.now());
    }
}
