package com.wzoto.interfaces.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/** 提交做题答案DTO */
@Data
public class SubmitAnswerDTO {
    @NotNull(message = "题目ID不能为空")
    private Long exerciseId;
    @NotBlank(message = "答案不能为空")
    private String studentAnswer;
}
