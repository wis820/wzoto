package com.wzoto.interfaces.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/** AI追问请求DTO */
@Data
public class AiQaFollowUpDTO {
    @NotNull(message = "childId不能为空")
    private Long childId;
    @NotBlank(message = "会话ID不能为空")
    private String conversationId;
    @NotBlank(message = "追问内容不能为空")
    private String questionText;
}
