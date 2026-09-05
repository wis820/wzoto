package com.wzoto.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 教员档案领域实体 - 大学生完善的教学档案
 */
@Data
public class TutorProfile {

    private Long id;

    /** 用户ID（关联t_user） */
    private Long userId;

    /** 学校名称 */
    private String university;

    /** 专业 */
    private String major;

    /** 年级：如 大二、研一 */
    private String grade;

    /** 可辅导科目列表（逗号分隔） */
    private String subjects;

    /** 时薪（元/小时） */
    private Integer hourlyRate;

    /** 个人简介 */
    private String bio;

    /** 教学经验描述 */
    private String experience;

    /** 上课区域（逗号分隔） */
    private String districts;

    /** 上课时段（JSON格式，如 [{"day":"MON","start":"09:00","end":"12:00"}]） */
    private String availableTimes;

    /** 评分（平均分） */
    private Double rating;

    /** 评价数量 */
    private Integer reviewCount;

    /** 完成订单数 */
    private Integer orderCount;

    /** 是否上架（0-下架 1-上架） */
    private Boolean active;

    /** 置顶到期时间（P1: 简历置顶功能） */
    private LocalDateTime pinnedUntil;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间 */
    private LocalDateTime updatedAt;

    /**
     * 创建教员档案
     * 业务规则：只有认证通过的大学生才能创建
     */
    public void create() {
        this.active = true;
        this.rating = 0.0;
        this.reviewCount = 0;
        this.orderCount = 0;
    }

    /**
     * 更新教员档案
     */
    public void updateProfile(TutorProfile update) {
        if (update.getUniversity() != null) this.university = update.getUniversity();
        if (update.getMajor() != null) this.major = update.getMajor();
        if (update.getGrade() != null) this.grade = update.getGrade();
        if (update.getSubjects() != null) this.subjects = update.getSubjects();
        if (update.getHourlyRate() != null) this.hourlyRate = update.getHourlyRate();
        if (update.getBio() != null) this.bio = update.getBio();
        if (update.getExperience() != null) this.experience = update.getExperience();
        if (update.getDistricts() != null) this.districts = update.getDistricts();
        if (update.getAvailableTimes() != null) this.availableTimes = update.getAvailableTimes();
    }

    /**
     * 上架/下架
     */
    public void toggleActive() {
        this.active = !this.active;
    }

    /**
     * 是否当前已置顶（P1: 简历置顶）
     */
    public boolean isPinned() {
        return pinnedUntil != null && pinnedUntil.isAfter(LocalDateTime.now());
    }
}