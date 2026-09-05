package com.wzoto.interfaces.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/** 口语评测请求DTO */
@Data
public class PronunciationEvalDTO {
    @NotNull(message = "childId不能为空")
    private Long childId;
    private String title;
    private String audioUrl;
    private String audioTranscript;
}
