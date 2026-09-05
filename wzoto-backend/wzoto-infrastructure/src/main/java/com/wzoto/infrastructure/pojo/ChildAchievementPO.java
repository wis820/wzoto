package com.wzoto.infrastructure.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 成长激励PO - 持久化对象
 */
@Data
@TableName("t_child_achievement")
public class ChildAchievementPO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long childId;

    private String achievementType;

    private String achievementCode;

    private String achievementName;

    private String iconUrl;

    private Integer points;

    private String skinCode;

    private String taskDescription;

    private String taskStatus;

    private LocalDateTime obtainedAt;

    @TableLogic
    private Boolean deleted;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
