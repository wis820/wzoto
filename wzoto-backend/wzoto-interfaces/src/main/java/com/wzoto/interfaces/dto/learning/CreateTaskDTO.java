package com.wzoto.interfaces.dto.learning;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateTaskDTO {

    @NotBlank(message = "专项类型不能为空")
    private String specialType;

    private Integer count;
}
