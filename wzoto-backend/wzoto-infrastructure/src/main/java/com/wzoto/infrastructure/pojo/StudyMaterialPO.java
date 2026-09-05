package com.wzoto.infrastructure.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/** 学习资料PO */
@Data
@TableName("t_study_material")
public class StudyMaterialPO {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String grade;
    private String subject;
    private String textbookVersion;
    private String title;
    private String materialType;
    private String contentUrl;
    private String previewUrl;
    private String description;
    private Long knowledgePointId;
    private Boolean vipOnly;
    private Integer downloadCount;
    private Integer sortOrder;
    @TableLogic
    private Boolean deleted;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
