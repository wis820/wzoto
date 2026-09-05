package com.wzoto.domain.entity;

import com.wzoto.domain.valobj.IdentityType;
import com.wzoto.domain.valobj.VerifyStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户 - 领域实体
 * 核心业务规则：身份一经选择不可随意切换
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    /** 主键ID */
    private Long id;

    /** 微信openid */
    private String openid;

    /** 微信unionid */
    private String unionid;

    /** 微信session_key（不持久化到数据库，仅运行时使用） */
    private transient String sessionKey;

    /** 手机号 */
    private String phone;

    /** 昵称 */
    private String nickname;

    /** 头像URL */
    private String avatar;

    /** 身份类型：PARENT/STUDENT */
    private IdentityType identityType;

    /** 实名认证状态 */
    private VerifyStatus verifyStatus;

    /** 真实姓名（认证后填写） */
    private String realName;

    /** 是否已删除 */
    private Boolean deleted;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间 */
    private LocalDateTime updatedAt;

    // ========== 领域行为 ==========

    /**
     * 选择身份 - 核心业务规则：一经选择不可随意切换
     */
    public void selectIdentity(IdentityType type) {
        if (this.identityType != null) {
            throw new IllegalStateException("身份一经选择不可随意切换，当前身份：" + this.identityType.getDesc());
        }
        this.identityType = type;
    }

    /**
     * 绑定手机号
     */
    public void bindPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 是否已选择身份
     */
    public boolean hasIdentity() {
        return this.identityType != null;
    }

    /**
     * 是否已实名认证
     */
    public boolean isVerified() {
        return VerifyStatus.APPROVED.equals(this.verifyStatus);
    }
}