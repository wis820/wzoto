package com.wzoto.domain.entity;

import com.wzoto.domain.valobj.GradeType;
import com.wzoto.domain.valobj.TestPaperType;
import com.wzoto.domain.valobj.TextbookVersion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 试卷 - 领域实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TestPaper {
    private Long id;
    private GradeType grade;
    private String subject;
    private TextbookVersion textbookVersion;
    private String title;
    private TestPaperType paperType;
    private String description;
    private Integer totalScore;
    private Integer durationMinutes;
    private String questionsJson;
    private String coverUrl;
    private Boolean vipOnly;
    private Integer useCount;
    private Integer sortOrder;
    private Boolean deleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public boolean isVipOnly() {
        return Boolean.TRUE.equals(this.vipOnly);
    }

    public void incrementUseCount() {
        this.useCount = (this.useCount == null ? 0 : this.useCount) + 1;
    }
}
