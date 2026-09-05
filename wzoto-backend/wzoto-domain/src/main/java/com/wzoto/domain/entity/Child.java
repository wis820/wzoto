package com.wzoto.domain.entity;

import com.wzoto.domain.valobj.GradeType;
import com.wzoto.domain.valobj.TextbookVersion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 子女档案 - 领域实体
 * 核心业务规则：子女必须归属某位家长，年级与教材版本可独立维护
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Child {

    /** 主键ID */
    private Long id;

    /** 家长用户ID */
    private Long parentId;

    /** 子女姓名 */
    private String name;

    /** 年级 */
    private GradeType grade;

    /** 教材版本 */
    private TextbookVersion textbookVersion;

    /** 学校名称 */
    private String school;

    /** 头像URL */
    private String avatar;

    /** 是否已删除 */
    private Boolean deleted;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间 */
    private LocalDateTime updatedAt;

    // ========== 领域行为 ==========

    /**
     * 创建子女档案
     */
    public static Child create(Long parentId, String name, GradeType grade, TextbookVersion textbookVersion) {
        return Child.builder()
                .parentId(parentId)
                .name(name)
                .grade(grade)
                .textbookVersion(textbookVersion)
                .deleted(false)
                .build();
    }

    /**
     * 更新基本信息
     */
    public void update(String name, GradeType grade, TextbookVersion textbookVersion, String school, String avatar) {
        if (name != null && !name.isBlank()) {
            this.name = name;
        }
        if (grade != null) {
            this.grade = grade;
        }
        if (textbookVersion != null) {
            this.textbookVersion = textbookVersion;
        }
        if (school != null) {
            this.school = school;
        }
        if (avatar != null) {
            this.avatar = avatar;
        }
    }

    /**
     * 更新年级
     */
    public void updateGrade(GradeType grade) {
        if (grade == null) {
            throw new IllegalArgumentException("年级不能为空");
        }
        this.grade = grade;
    }

    /**
     * 绑定/切换教材版本
     */
    public void bindTextbook(TextbookVersion textbookVersion) {
        if (textbookVersion == null) {
            throw new IllegalArgumentException("教材版本不能为空");
        }
        this.textbookVersion = textbookVersion;
    }

    /**
     * 校验是否属于指定家长
     */
    public boolean belongsTo(Long parentId) {
        return this.parentId != null && this.parentId.equals(parentId);
    }

    /**
     * 逻辑删除
     */
    public void markDeleted() {
        this.deleted = true;
    }
}
