package com.wzoto.infrastructure.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 课程观看记录PO - 持久化对象
 */
@Data
@TableName("t_course_watch_record")
public class CourseWatchRecordPO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long childId;

    private Long resourceId;

    private Integer watchDurationSeconds;

    private Integer progressPercent;

    private Integer lastPositionSeconds;

    private Boolean completed;

    private LocalDateTime firstWatchTime;

    private LocalDateTime lastWatchTime;

    @TableLogic
    private Boolean deleted;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
