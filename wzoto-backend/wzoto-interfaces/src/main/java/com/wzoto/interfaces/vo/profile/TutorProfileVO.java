package com.wzoto.interfaces.vo.profile;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TutorProfileVO {

    private Long id;
    private Long userId;
    private String nickname;
    private String avatar;
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

    /** 是否已置顶（P1） */
    private Boolean isPinned;

    /** 置顶到期时间（P1） */
    private LocalDateTime pinnedUntil;

    private LocalDateTime createdAt;
}