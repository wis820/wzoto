package com.wzoto.interfaces.vo.learning;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ControlConfigVO {

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
}
