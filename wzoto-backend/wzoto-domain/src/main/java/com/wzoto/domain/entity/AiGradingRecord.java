package com.wzoto.domain.entity;

import com.wzoto.domain.valobj.AiGradingType;
import com.wzoto.domain.valobj.GradeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * AI批改记录 - 领域实体（作文批改/口语评测）
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiGradingRecord {
    private Long id;
    private Long childId;
    private Long parentId;
    private AiGradingType gradingType;
    private String subject;
    private GradeType grade;
    private String title;
    private String contentText;
    private String contentAudioUrl;
    private String aiResultJson;
    private Integer score;
    private Integer errorCount;
    private String suggestion;
    private Boolean deleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /** 创建作文批改记录 */
    public static AiGradingRecord createComposition(Long childId, Long parentId, String subject,
                                                     GradeType grade, String title, String content) {
        return AiGradingRecord.builder()
                .childId(childId)
                .parentId(parentId)
                .gradingType(AiGradingType.COMPOSITION)
                .subject(subject)
                .grade(grade)
                .title(title)
                .contentText(content)
                .deleted(false)
                .build();
    }

    /** 创建口语评测记录 */
    public static AiGradingRecord createPronunciation(Long childId, Long parentId,
                                                       GradeType grade, String title, String audioUrl) {
        return AiGradingRecord.builder()
                .childId(childId)
                .parentId(parentId)
                .gradingType(AiGradingType.PRONUNCIATION)
                .subject("ENGLISH")
                .grade(grade)
                .title(title)
                .contentAudioUrl(audioUrl)
                .deleted(false)
                .build();
    }
}
