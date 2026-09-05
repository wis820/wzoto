package com.wzoto.infrastructure.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/** AI批改记录PO */
@Data
@TableName("t_ai_grading_record")
public class AiGradingRecordPO {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long childId;
    private Long parentId;
    private String gradingType;
    private String subject;
    private String grade;
    private String title;
    private String contentText;
    private String contentAudioUrl;
    private String aiResultJson;
    private Integer score;
    private Integer errorCount;
    private String suggestion;
    @TableLogic
    private Boolean deleted;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
