package com.wzoto.infrastructure.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 家长管控配置PO - 持久化对象
 */
@Data
@TableName("t_parent_control_config")
public class ParentControlConfigPO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long parentId;

    private Long childId;

    private Integer dailyLimitMinutes;

    private Integer restIntervalMinutes;

    private String forbiddenStartTime;

    private String forbiddenEndTime;

    private Boolean locked;

    private Boolean eyeProtectionMode;

    private Boolean blueLightFilter;

    private Boolean postureReminder;

    @TableLogic
    private Boolean deleted;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
