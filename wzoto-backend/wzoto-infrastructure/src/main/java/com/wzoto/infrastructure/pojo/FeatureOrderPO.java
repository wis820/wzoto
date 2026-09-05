package com.wzoto.infrastructure.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 功能订单持久化对象 (P1: 单次付费购买)
 */
@Data
@TableName("t_feature_order")
public class FeatureOrderPO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    /** 功能类型：RESUME_PIN / EXPEDITE_VERIFY */
    private String featureType;

    /** 关联目标ID */
    private Long targetId;

    /** 支付金额 */
    private BigDecimal amount;

    /** 支付状态：UNPAID / PAID */
    private String paymentStatus;

    /** 支付时间 */
    private LocalDateTime paymentTime;

    /** 微信支付交易号 */
    private String transactionId;

    /** 功能到期时间 */
    private LocalDateTime expireTime;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableLogic
    private Integer deleted;
}
