package com.wzoto.domain.entity;

import com.wzoto.domain.valobj.GradeType;
import com.wzoto.domain.valobj.TextbookVersion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 音频素材 - 领域实体（跟读/听力/朗读）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AudioMaterial {
    private Long id;
    private GradeType grade;
    private String subject;
    private TextbookVersion textbookVersion;
    private String title;
    private String audioType;
    private String audioUrl;
    private Integer durationSeconds;
    private String textContent;
    private String referenceText;
    private Long knowledgePointId;
    private Boolean vipOnly;
    private Integer sortOrder;
    private Boolean deleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
