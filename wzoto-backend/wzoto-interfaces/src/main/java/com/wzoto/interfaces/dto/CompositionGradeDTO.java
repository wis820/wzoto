package com.wzoto.interfaces.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/** 作文批改请求DTO */
@Data
public class CompositionGradeDTO {
    @NotNull(message = "childId不能为空")
    private Long childId;
    @NotBlank(message = "学科不能为空")
    private String subject;
    @NotBlank(message = "标题不能为空")
    private String title;
    @NotBlank(message = "作文内容不能为空")
    private String content;
}
