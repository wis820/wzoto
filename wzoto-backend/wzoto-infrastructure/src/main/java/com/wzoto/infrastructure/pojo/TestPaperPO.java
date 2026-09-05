package com.wzoto.infrastructure.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/** 试卷PO */
@Data
@TableName("t_test_paper")
public class TestPaperPO {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String grade;
    private String subject;
    private String textbookVersion;
    private String title;
    private String paperType;
    private String description;
    private Integer totalScore;
    private Integer durationMinutes;
    private String questionsJson;
    private String coverUrl;
    private Boolean vipOnly;
    private Integer useCount;
    private Integer sortOrder;
    @TableLogic
    private Boolean deleted;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
