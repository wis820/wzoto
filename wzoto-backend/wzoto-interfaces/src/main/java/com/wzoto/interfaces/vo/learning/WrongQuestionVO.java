package com.wzoto.interfaces.vo.learning;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class WrongQuestionVO {

    private Long id;
    private Long childId;
    private Long resourceId;
    private String subject;
    private String knowledgePoint;
    private Integer mistakeCount;
    private String masteryLevel;
    private String masteryLevelDesc;
    private String masteryColor;
    private Boolean inReviewPlan;
    private String reviewPlanTag;
    private LocalDateTime lastMistakeTime;
}
