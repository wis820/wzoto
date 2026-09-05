package com.wzoto.interfaces.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 选择身份请求DTO
 */
@Data
public class SelectIdentityDTO {

    /** 用户ID */
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    /** 身份类型：PARENT / STUDENT */
    @NotBlank(message = "身份类型不能为空")
    private String identityType;
}