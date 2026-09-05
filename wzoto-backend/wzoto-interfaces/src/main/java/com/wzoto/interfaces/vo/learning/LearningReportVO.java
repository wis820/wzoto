package com.wzoto.interfaces.vo.learning;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class LearningReportVO {

    private Long childId;
    private String reportType;
    private Integer totalQuestions;
    private Integer correctCount;
    private Double accuracy;
    private Integer watchDurationSeconds;
    private Integer completedVideos;
    private Map<String, Double> subjectAccuracy;
    private Map<String, String> masteryMap;
}
