package com.wzoto.interfaces.dto.ai;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 创建定价分析 DTO
 */
@Data
public class CreatePricingAnalysisDTO {

    @NotBlank(message = "学校名称不能为空")
    private String university;

    @NotBlank(message = "专业不能为空")
    private String major;

    private String grade;

    @NotBlank(message = "辅导科目不能为空")
    private String subjects;

    private String experience;

    @NotNull(message = "当前时薪不能为空")
    private Integer currentRate;
}
