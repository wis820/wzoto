package com.wzoto.domain.entity;

import com.wzoto.domain.valobj.ExerciseDifficulty;
import com.wzoto.domain.valobj.GradeType;
import com.wzoto.domain.valobj.TextbookVersion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 微课视频 - 领域实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MicroCourse {
    private Long id;
    private GradeType grade;
    private String subject;
    private TextbookVersion textbookVersion;
    private Long chapterId;
    private Long knowledgePointId;
    private String title;
    private String description;
    private String coverUrl;
    private String videoUrl;
    private Integer durationSeconds;
    private String resolution;
    private Integer fileSizeMb;
    private ExerciseDifficulty difficulty;
    private Boolean vipOnly;
    private Integer playCount;
    private Integer sortOrder;
    private Boolean deleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /** 增加播放次数 */
    public void incrementPlayCount() {
        this.playCount = (this.playCount == null ? 0 : this.playCount) + 1;
    }

    /** 是否为VIP资源 */
    public boolean isVipOnly() {
        return Boolean.TRUE.equals(this.vipOnly);
    }
}
