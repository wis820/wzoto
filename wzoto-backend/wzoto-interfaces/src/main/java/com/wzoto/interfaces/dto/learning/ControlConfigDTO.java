package com.wzoto.interfaces.dto.learning;

import lombok.Data;

@Data
public class ControlConfigDTO {

    private Integer dailyLimitMinutes;

    private Integer restIntervalMinutes;

    private String forbiddenStartTime;

    private String forbiddenEndTime;

    private Boolean eyeProtectionMode;

    private Boolean blueLightFilter;

    private Boolean postureReminder;
}
