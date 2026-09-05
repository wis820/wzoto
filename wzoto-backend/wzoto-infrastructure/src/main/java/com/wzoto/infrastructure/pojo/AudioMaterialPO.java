package com.wzoto.infrastructure.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/** 音频素材PO */
@Data
@TableName("t_audio_material")
public class AudioMaterialPO {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String grade;
    private String subject;
    private String textbookVersion;
    private String title;
    private String audioType;
    private String audioUrl;
    private Integer durationSeconds;
    private String textContent;
    private String referenceText;
    private Long knowledgePointId;
    private Boolean vipOnly;
    private Integer sortOrder;
    @TableLogic
    private Boolean deleted;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
