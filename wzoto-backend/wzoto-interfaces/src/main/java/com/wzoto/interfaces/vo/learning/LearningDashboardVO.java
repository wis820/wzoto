package com.wzoto.interfaces.vo.learning;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class LearningDashboardVO {

    private Long childId;
    private String childName;
    private Integer todayTaskCount;
    private Integer completedTaskCount;
    private Integer weeklyWatchDurationSeconds;
    private Double weeklyAccuracy;
    private List<LearningTaskVO> todayTasks;
    private List<WeakPointVO> weakPoints;
    private Map<String, Integer> subjectDuration;
}
