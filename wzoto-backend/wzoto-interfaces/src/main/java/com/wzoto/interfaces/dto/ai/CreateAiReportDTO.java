package com.wzoto.interfaces.dto.ai;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 创建AI学情诊断报告 DTO
 */
@Data
public class CreateAiReportDTO {

    /** 子女姓名 */
    private String childName;

    /** 薄弱学科（逗号分隔） */
    @NotBlank(message = "薄弱学科不能为空")
    private String weakSubjects;

    /** 近期考试分数 */
    private String recentScores;

    /** 薄弱知识点描述 */
    @NotBlank(message = "薄弱知识点描述不能为空")
    private String weakPointDesc;

    /** 试卷/错题照片URL（逗号分隔） */
    private String photoUrls;
}
