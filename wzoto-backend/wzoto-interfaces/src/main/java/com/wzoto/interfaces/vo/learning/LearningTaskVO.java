package com.wzoto.interfaces.vo.learning;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class LearningTaskVO {

    private Long id;
    private Long childId;
    private String taskType;
    private String taskTypeDesc;
    private String subject;
    private String title;
    private Long resourceId;
    private LocalDate taskDate;
    private String status;
    private String statusDesc;
    private LocalDateTime startTime;
    private LocalDateTime completeTime;
    private Integer spentSeconds;
}
