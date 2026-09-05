package com.wzoto.interfaces.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 绑定手机号请求DTO
 */
@Data
public class BindPhoneDTO {

    /** 用户ID */
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    /** 手机号 */
    @NotBlank(message = "手机号不能为空")
    private String phone;
}