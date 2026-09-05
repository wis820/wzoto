package com.wzoto.interfaces.dto.learning;

import lombok.Data;

@Data
public class LearningPlanConfigDTO {

    private Integer dailyDurationMinutes;

    private Integer chineseWeight;

    private Integer mathWeight;

    private Integer englishWeight;

    private Boolean specialCalculationEnabled;

    private Boolean specialApplicationEnabled;

    private Boolean specialLiteracyEnabled;

    private Boolean specialWordsEnabled;
}
