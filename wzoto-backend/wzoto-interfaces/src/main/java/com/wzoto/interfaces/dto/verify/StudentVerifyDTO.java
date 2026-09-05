package com.wzoto.interfaces.dto.verify;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 大学生实名认证DTO
 */
@Data
public class StudentVerifyDTO {

    @NotBlank(message = "真实姓名不能为空")
    private String realName;

    @NotBlank(message = "身份证号不能为空")
    @Pattern(regexp = "^\\d{17}[\\dXx]$", message = "身份证号格式不正确")
    private String idCardNo;

    /** 学生证照片URL（由文件上传接口返回） */
    private String studentCardImage;
}