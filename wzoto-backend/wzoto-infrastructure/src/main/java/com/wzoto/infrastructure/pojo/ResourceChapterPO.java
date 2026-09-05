package com.wzoto.infrastructure.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/** 教材章节PO */
@Data
@TableName("t_resource_chapter")
public class ResourceChapterPO {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long parentId;
    private String grade;
    private String subject;
    private String textbookVersion;
    private String title;
    private Integer depth;
    private Integer sortOrder;
    @TableLogic
    private Boolean deleted;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
