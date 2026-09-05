package com.wzoto.interfaces.vo.learning;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class LearningResourceVO {

    private Long id;
    private String grade;
    private String gradeDesc;
    private String subject;
    private String textbookVersion;
    private String resourceType;
    private String resourceTypeDesc;
    private String title;
    private String coverUrl;
    private String contentUrl;
    private Integer durationSeconds;
    private String knowledgePoint;
    private String tags;
    private String sourceType;
    private String subtitleUrl;
    private String qualityLevels;
    private String knowledgeMarkers;
    private String status;
    private String description;
    private Integer lastPositionSeconds;
    private Integer progressPercent;
    private Boolean completed;
    private Boolean vipOnly;
    private LocalDateTime createdAt;
}
