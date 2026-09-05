package com.wzoto.domain.entity;

import com.wzoto.domain.valobj.VerifyStatus;
import com.wzoto.domain.valobj.VerifyType;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 认证记录领域实体
 */
@Data
public class VerifyRecord {

    private Long id;

    /** 用户ID */
    private Long userId;

    /** 认证类型 */
    private VerifyType verifyType;

    /** 真实姓名 */
    private String realName;

    /** 身份证号（脱敏存储） */
    private String idCardNo;

    /** 学生证照片URL（大学生认证用） */
    private String studentCardImage;

    /** 审核状态 */
    private VerifyStatus verifyStatus;

    /** 审核备注 */
    private String remark;

    /** 是否加急（P1: 加急审核功能） */
    private Boolean expedited;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间 */
    private LocalDateTime updatedAt;

    /**
     * 提交认证 - 家长
     */
    public void submitParentVerify(String realName, String idCardNo) {
        this.realName = realName;
        this.idCardNo = idCardNo;
        this.verifyType = VerifyType.PARENT_IDCARD;
        // 家长认证：自动审核通过（简单场景，后续可接入第三方实名API）
        this.verifyStatus = VerifyStatus.APPROVED;
    }

    /**
     * 提交认证 - 大学生（身份证+学生证）
     */
    public void submitStudentVerify(String realName, String idCardNo, String studentCardImage) {
        this.realName = realName;
        this.idCardNo = idCardNo;
        this.studentCardImage = studentCardImage;
        this.verifyType = VerifyType.STUDENT_CARD;
        // 大学生认证：需人工审核
        this.verifyStatus = VerifyStatus.PENDING;
    }

    /**
     * 审核通过
     */
    public void approve(String remark) {
        if (this.verifyStatus != VerifyStatus.PENDING) {
            throw new IllegalStateException("当前状态不允许审核");
        }
        this.verifyStatus = VerifyStatus.APPROVED;
        this.remark = remark;
    }

    /**
     * 审核拒绝
     */
    public void reject(String remark) {
        if (this.verifyStatus != VerifyStatus.PENDING) {
            throw new IllegalStateException("当前状态不允许审核");
        }
        this.verifyStatus = VerifyStatus.REJECTED;
        this.remark = remark;
    }
}