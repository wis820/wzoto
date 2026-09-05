package com.wzoto.infrastructure.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 认证记录持久化对象
 */
@Data
@TableName("t_verify_record")
public class VerifyRecordPO {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 用户ID */
    private Long userId;

    /** 认证类型：PARENT_IDCARD / STUDENT_IDCARD / STUDENT_CARD */
    private String verifyType;

    /** 真实姓名 */
    private String realName;

    /** 身份证号（脱敏存储） */
    private String idCardNo;

    /** 学生证照片URL */
    private String studentCardImage;

    /** 审核状态：NONE / PENDING / APPROVED / REJECTED */
    private String verifyStatus;

    /** 审核备注 */
    private String remark;

    /** 是否加急（P1: 加急审核） */
    private Integer expedited;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableLogic
    private Integer deleted;
}