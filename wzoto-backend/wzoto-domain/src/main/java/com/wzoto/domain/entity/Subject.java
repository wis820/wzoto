package com.wzoto.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 学科 - 领域实体
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Subject {
    private Long id;
    private String code;
    private String name;
    private String icon;
    private String color;
    private Integer sortOrder;
    private Boolean deleted;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
