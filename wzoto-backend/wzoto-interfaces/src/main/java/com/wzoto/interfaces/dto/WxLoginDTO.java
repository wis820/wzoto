package com.wzoto.interfaces.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 微信登录请求DTO
 */
@Data
public class WxLoginDTO {

    /** 微信登录凭证code */
    @NotBlank(message = "微信登录凭证不能为空")
    private String code;
}