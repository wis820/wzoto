package com.wzoto.infrastructure.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("t_tutor_profile")
public class TutorProfilePO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;
    private String university;
    private String major;
    private String grade;
    private String subjects;
    private Integer hourlyRate;
    private String bio;
    private String experience;
    private String districts;
    private String availableTimes;
    private Double rating;
    private Integer reviewCount;
    private Integer orderCount;
    private Boolean active;

    /** 置顶到期时间（P1: 简历置顶） */
    private LocalDateTime pinnedUntil;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableLogic
    private Integer deleted;
}