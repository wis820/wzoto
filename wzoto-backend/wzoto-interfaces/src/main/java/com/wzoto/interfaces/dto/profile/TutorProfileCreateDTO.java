package com.wzoto.interfaces.dto.profile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TutorProfileCreateDTO {

    @NotBlank(message = "学校名称不能为空")
    private String university;

    @NotBlank(message = "专业不能为空")
    private String major;

    private String grade;

    @NotBlank(message = "辅导科目不能为空")
    private String subjects;

    @NotNull(message = "时薪不能为空")
    private Integer hourlyRate;

    private String bio;

    private String experience;

    private String districts;

    private String availableTimes;
}