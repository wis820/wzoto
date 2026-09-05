package com.wzoto.interfaces.vo.feature;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 功能订单VO
 */
@Data
@Builder
public class FeatureOrderVO {

    private Long id;

    /** 功能类型 */
    private String featureType;

    /** 功能类型中文 */
    private String featureTypeDesc;

    /** 关联目标ID */
    private Long targetId;

    /** 支付金额 */
    private BigDecimal amount;

    /** 支付状态 */
    private String paymentStatus;

    /** 支付状态中文 */
    private String paymentStatusDesc;

    /** 支付时间 */
    private LocalDateTime paymentTime;

    /** 功能到期时间 */
    private LocalDateTime expireTime;

    /** 创建时间 */
    private LocalDateTime createdAt;
}
