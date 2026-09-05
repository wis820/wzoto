package com.wzoto.interfaces.dto.child;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateChildDTO {

    @NotBlank(message = "子女姓名不能为空")
    private String name;

    @NotBlank(message = "年级不能为空")
    private String grade;

    @NotBlank(message = "教材版本不能为空")
    private String textbookVersion;

    private String school;

    private String avatar;
}
