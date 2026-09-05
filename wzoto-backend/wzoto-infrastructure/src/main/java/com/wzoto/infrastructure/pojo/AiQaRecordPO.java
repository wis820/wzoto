package com.wzoto.infrastructure.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/** AI提问记录PO */
@Data
@TableName("t_ai_qa_record")
public class AiQaRecordPO {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long childId;
    private Long parentId;
    private String conversationId;
    private String subject;
    private String grade;
    private String qaType;
    private String questionText;
    private String questionImageUrl;
    private String ocrText;
    private String aiResponseJson;
    private String knowledgeTags;
    private Boolean isFollowUp;
    private Integer stepCount;
    @TableLogic
    private Boolean deleted;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
