package com.wzoto.interfaces.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录结果VO - 返回给前端
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginVO {

    /** JWT Token */
    private String token;

    /** 用户ID */
    private Long userId;

    /** 昵称 */
    private String nickname;

    /** 头像 */
    private String avatar;

    /** 身份类型：PARENT / STUDENT / 空字符串(未选择) */
    private String identityType;

    /** 是否已选择身份 */
    private Boolean hasIdentity;

    /** 实名认证状态：NONE / PENDING / APPROVED / REJECTED */
    private String verifyStatus;

    /** 手机号（脱敏） */
    private String phone;
}