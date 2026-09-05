package com.wzoto.domain.entity;

import com.wzoto.domain.valobj.GradeType;
import com.wzoto.domain.valobj.TextbookVersion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 学习资料 - 领域实体（生字卡片/思维导图/PDF）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudyMaterial {
    private Long id;
    private GradeType grade;
    private String subject;
    private TextbookVersion textbookVersion;
    private String title;
    private String materialType;
    private String contentUrl;
    private String previewUrl;
    private String description;
    private Long knowledgePointId;
    private Boolean vipOnly;
    private Integer downloadCount;
    private Integer sortOrder;
    private Boolean deleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
