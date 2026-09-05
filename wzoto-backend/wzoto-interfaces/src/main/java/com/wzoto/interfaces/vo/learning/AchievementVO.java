package com.wzoto.interfaces.vo.learning;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class AchievementVO {

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
}
