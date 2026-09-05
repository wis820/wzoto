package com.wzoto.interfaces.dto.booking;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class CreateBookingDTO {

    @NotNull(message = "教员档案ID不能为空")
    private Long tutorProfileId;

    @NotBlank(message = "辅导科目不能为空")
    private String subject;

    @NotNull(message = "上课日期不能为空")
    private LocalDate bookingDate;

    @NotNull(message = "开始时间不能为空")
    private LocalTime startTime;

    @NotNull(message = "结束时间不能为空")
    private LocalTime endTime;

    private String address;
    private String message;
}