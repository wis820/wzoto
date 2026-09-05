package com.wzoto.infrastructure.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/** 微课视频PO */
@Data
@TableName("t_micro_course")
public class MicroCoursePO {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String grade;
    private String subject;
    private String textbookVersion;
    private Long chapterId;
    private Long knowledgePointId;
    private String title;
    private String description;
    private String coverUrl;
    private String videoUrl;
    private Integer durationSeconds;
    private String resolution;
    private Integer fileSizeMb;
    private String difficulty;
    private Boolean vipOnly;
    private Integer playCount;
    private Integer sortOrder;
    @TableLogic
    private Boolean deleted;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
