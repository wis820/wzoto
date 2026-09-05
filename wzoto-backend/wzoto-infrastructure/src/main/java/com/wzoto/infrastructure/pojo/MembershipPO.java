package com.wzoto.infrastructure.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 会员 - 持久化对象
 */
@Data
@TableName("t_membership")
public class MembershipPO {

    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String memberType;
    private String status;
    private LocalDateTime startTime;
    private LocalDateTime expireTime;
    private Boolean autoRenew;
    private String transactionId;
    @TableLogic
    private Boolean deleted;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
