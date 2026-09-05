package com.wzoto.interfaces.vo.booking;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@Builder
public class BookingVO {

    private Long id;
    private Long parentId;
    private String parentName;
    private Long tutorId;
    private String tutorName;
    private Long tutorProfileId;
    private String subject;
    private LocalDate bookingDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String address;
    private String message;
    private String status;
    private String statusDesc;
    private String reply;
    private LocalDateTime createdAt;
}