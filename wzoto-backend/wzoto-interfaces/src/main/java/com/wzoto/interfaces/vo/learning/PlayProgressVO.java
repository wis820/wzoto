package com.wzoto.interfaces.vo.learning;

import lombok.Builder;
import lombok.Data;

/**
 * 播放进度VO - 断点续播信息
 */
@Data
@Builder
public class PlayProgressVO {

    /** 资源ID */
    private Long resourceId;

    /** 上次播放位置（秒） */
    private Integer lastPositionSeconds;

    /** 进度百分比 */
    private Integer progressPercent;

    /** 是否已完成 */
    private Boolean completed;

    /** 累计观看时长（秒） */
    private Integer watchDurationSeconds;
}
