package com.wzoto.infrastructure.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 学习资源PO - 持久化对象
 */
@Data
@TableName("t_learning_resource")
public class LearningResourcePO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String grade;

    private String subject;

    private String textbookVersion;

    private String resourceType;

    private String title;

    private String coverUrl;

    private String contentUrl;

    private Integer durationSeconds;

    private String knowledgePoint;

    private String tags;

    private String sourceType;

    private String subtitleUrl;

    private String qualityLevels;

    private String knowledgeMarkers;

    private String status;

    private java.time.LocalDateTime publishAt;

    private java.time.LocalDateTime unpublishAt;

    private Long operatorId;

    private String description;

    private Boolean vipOnly;

    private Integer sortOrder;

    @TableLogic
    private Boolean deleted;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
