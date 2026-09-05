package com.wzoto.interfaces.vo.child;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ChildVO {

    private Long id;
    private Long parentId;
    private String name;
    private String grade;
    private String gradeDesc;
    private String textbookVersion;
    private String textbookVersionDesc;
    private String school;
    private String avatar;
    private LocalDateTime createdAt;
}
