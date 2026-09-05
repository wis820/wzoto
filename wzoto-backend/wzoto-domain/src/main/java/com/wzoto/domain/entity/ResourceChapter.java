package com.wzoto.domain.entity;

import com.wzoto.domain.valobj.GradeType;
import com.wzoto.domain.valobj.TextbookVersion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 教材章节 - 领域实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResourceChapter {
    private Long id;
    private Long parentId;
    private GradeType grade;
    private String subject;
    private TextbookVersion textbookVersion;
    private String title;
    private Integer depth;
    private Integer sortOrder;
    private Boolean deleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
