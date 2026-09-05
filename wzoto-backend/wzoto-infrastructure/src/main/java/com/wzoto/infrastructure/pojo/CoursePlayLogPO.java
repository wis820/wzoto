package com.wzoto.infrastructure.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 播放明细日志PO - 持久化对象
 */
@Data
@TableName("t_course_play_log")
public class CoursePlayLogPO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;

    private Long childId;

    private Long resourceId;

    private String sessionId;

    @TableField("`current_time`")
    private Integer currentTime;

    private Integer duration;

    private Integer percent;

    private BigDecimal playbackRate;

    private String quality;

    private String sourceType;

    private String eventType;

    @TableLogic
    private Boolean deleted;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
