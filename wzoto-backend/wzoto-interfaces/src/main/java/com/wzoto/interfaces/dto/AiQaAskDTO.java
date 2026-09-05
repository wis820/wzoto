package com.wzoto.interfaces.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/** AI提问请求DTO */
@Data
public class AiQaAskDTO {
    @NotNull(message = "childId不能为空")
    private Long childId;
    @NotBlank(message = "学科不能为空")
    private String subject;
    /** PHOTO / TEXT */
    private String qaType;
    private String questionText;
    private String questionImageUrl;
}
