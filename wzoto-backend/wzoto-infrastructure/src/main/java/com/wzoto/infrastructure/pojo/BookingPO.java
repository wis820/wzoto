package com.wzoto.infrastructure.pojo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@TableName("t_booking")
public class BookingPO {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long parentId;
    private Long tutorId;
    private Long tutorProfileId;
    private String subject;
    private LocalDate bookingDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String address;
    private String message;
    private String status;
    private String reply;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;

    @TableLogic
    private Integer deleted;
}