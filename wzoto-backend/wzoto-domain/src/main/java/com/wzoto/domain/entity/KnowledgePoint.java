package com.wzoto.domain.entity;

import com.wzoto.domain.valobj.GradeType;
import com.wzoto.domain.valobj.TextbookVersion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 知识点 - 领域实体
 * 树形结构：按年级+学科+教材版本组织
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KnowledgePoint {
    private Long id;
    private Long parentId;
    private GradeType grade;
    private String subject;
    private TextbookVersion textbookVersion;
    private String name;
    private String description;
    private Integer depth;
    private Integer sortOrder;
    private Boolean deleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /** 是否为叶子节点（最底层知识点） */
    public boolean isLeaf() {
        return this.depth != null && this.depth >= 5;
    }
}
