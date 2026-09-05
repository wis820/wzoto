package com.wzoto.interfaces.vo.verify;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 认证记录VO
 */
@Data
@Builder
public class VerifyRecordVO {

    private Long id;

    /** 认证类型 */
    private String verifyType;

    /** 认证类型中文 */
    private String verifyTypeDesc;

    /** 真实姓名（脱敏） */
    private String realNameMasked;

    /** 身份证号（脱敏） */
    private String idCardNoMasked;

    /** 学生证照片URL */
    private String studentCardImage;

    /** 审核状态 */
    private String verifyStatus;

    /** 审核状态中文 */
    private String verifyStatusDesc;

    /** 审核备注 */
    private String remark;

    /** 是否加急（P1） */
    private Boolean expedited;

    /** 提交时间 */
    private LocalDateTime createdAt;
}