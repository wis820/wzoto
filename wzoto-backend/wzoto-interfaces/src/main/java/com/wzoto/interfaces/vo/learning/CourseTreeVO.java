package com.wzoto.interfaces.vo.learning;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * 课程目录树VO - 对标洋葱学院左侧目录
 */
@Data
@Builder
public class CourseTreeVO {

    /** 章节ID */
    private Long id;

    /** 父节点ID */
    private Long parentId;

    /** 标题 */
    private String title;

    /** 年级 */
    private String grade;

    /** 学科 */
    private String subject;

    /** 是否章节节点 */
    private Boolean isChapter;

    /** 排序号 */
    private Integer sortOrder;

    /** 子章节 */
    private List<CourseTreeVO> children;

    /** 该章节下的资源列表（课时） */
    private List<ResourceItem> resources;

    /**
     * 课时资源项
     */
    @Data
    @Builder
    public static class ResourceItem {
        private Long id;
        private String title;
        private String coverUrl;
        private String contentUrl;
        private Integer durationSeconds;
        private String knowledgePoint;
        private String sourceType;
        private Boolean vipOnly;
        private String description;
        /** 断点续播：上次位置（秒） */
        private Integer lastPositionSeconds;
        /** 断点续播：进度百分比 */
        private Integer progressPercent;
        /** 是否已完成 */
        private Boolean completed;
    }
}
