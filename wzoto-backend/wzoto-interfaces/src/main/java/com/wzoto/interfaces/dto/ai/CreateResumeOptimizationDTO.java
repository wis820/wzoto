package com.wzoto.interfaces.dto.ai;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 创建简历优化 DTO
 */
@Data
public class CreateResumeOptimizationDTO {

    @NotBlank(message = "学校名称不能为空")
    private String university;

    @NotBlank(message = "专业不能为空")
    private String major;

    private String grade;

    @NotBlank(message = "辅导科目不能为空")
    private String subjects;

    private String bio;

    private String experience;
}
