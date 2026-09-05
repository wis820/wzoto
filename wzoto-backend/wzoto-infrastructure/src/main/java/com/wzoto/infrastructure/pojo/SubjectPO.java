package com.wzoto.infrastructure.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/** 学科PO */
@Data
@TableName("t_subject")
public class SubjectPO {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String code;
    private String name;
    private String icon;
    private String color;
    private Integer sortOrder;
    @TableLogic
    private Boolean deleted;
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
