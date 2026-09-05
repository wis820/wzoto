package com.wzoto.infrastructure.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/** 知识点PO */
@Data
@TableName("t_knowledge_point")
public class KnowledgePointPO {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long parentId;
    private String grade;
    private String subject;
    private String textbookVersion;
    private String name;
    private String description;
    private Integer depth;
    private Integer sortOrder;
    @TableLogic
    private Boolean deleted;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
