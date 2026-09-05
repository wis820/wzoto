package com.wzoto.interfaces.vo;

import lombok.Builder;
import lombok.Data;

/**
 * 当前用户信息VO
 */
@Data
@Builder
public class UserInfoVO {

    /** 用户ID */
    private Long userId;

    /** 微信openid */
    private String openid;

    /** 手机号 */
    private String phone;

    /** 昵称 */
    private String nickname;

    /** 头像 */
    private String avatar;

    /** 身份类型：PARENT / STUDENT */
    private String identityType;

    /** 身份类型中文 */
    private String identityTypeDesc;

    /** 是否已选择身份 */
    private Boolean hasIdentity;

    /** 实名认证状态：NONE / PENDING / APPROVED / REJECTED */
    private String verifyStatus;

    /** 认证状态中文 */
    private String verifyStatusDesc;

    /** 真实姓名（已认证才返回） */
    private String realName;
}