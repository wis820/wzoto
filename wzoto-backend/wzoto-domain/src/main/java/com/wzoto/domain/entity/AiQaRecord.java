package com.wzoto.domain.entity;

import com.wzoto.domain.valobj.AiQaType;
import com.wzoto.domain.valobj.GradeType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * AI提问记录 - 领域实体
 * 支持拍照搜题+文字提问，AI分步启发式答疑
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AiQaRecord {
    private Long id;
    private Long childId;
    private Long parentId;
    private String conversationId;
    private String subject;
    private GradeType grade;
    private AiQaType qaType;
    private String questionText;
    private String questionImageUrl;
    private String ocrText;
    private String aiResponseJson;
    private String knowledgeTags;
    private Boolean isFollowUp;
    private Integer stepCount;
    private Boolean deleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /** 创建新提问 */
    public static AiQaRecord create(Long childId, Long parentId, String conversationId,
                                     String subject, GradeType grade, AiQaType qaType,
                                     String questionText, String questionImageUrl) {
        return AiQaRecord.builder()
                .childId(childId)
                .parentId(parentId)
                .conversationId(conversationId)
                .subject(subject)
                .grade(grade)
                .qaType(qaType)
                .questionText(questionText)
                .questionImageUrl(questionImageUrl)
                .isFollowUp(false)
                .deleted(false)
                .build();
    }

    /** 创建追问 */
    public static AiQaRecord createFollowUp(Long childId, Long parentId, String conversationId,
                                             String subject, GradeType grade, String questionText) {
        return AiQaRecord.builder()
                .childId(childId)
                .parentId(parentId)
                .conversationId(conversationId)
                .subject(subject)
                .grade(grade)
                .qaType(AiQaType.TEXT)
                .questionText(questionText)
                .isFollowUp(true)
                .deleted(false)
                .build();
    }
}
