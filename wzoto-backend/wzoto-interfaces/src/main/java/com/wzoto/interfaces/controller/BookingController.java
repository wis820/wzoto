package com.wzoto.interfaces.controller;

import com.wzoto.application.service.BookingApplicationService;
import com.wzoto.domain.context.UserContext;
import com.wzoto.domain.entity.Booking;
import com.wzoto.domain.entity.User;
import com.wzoto.domain.repository.UserRepository;
import com.wzoto.interfaces.common.R;
import com.wzoto.interfaces.dto.booking.CreateBookingDTO;
import com.wzoto.interfaces.vo.booking.BookingVO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/api/booking")
@RequiredArgsConstructor
public class BookingController {

    private final BookingApplicationService bookingApplicationService;
    private final UserRepository userRepository;

    @PostMapping
    public R<BookingVO> createBooking(@Valid @RequestBody CreateBookingDTO dto) {
        log.info("创建预约, parentId={}", UserContext.getCurrentUserId());
        Booking booking = new Booking();
        booking.setSubject(dto.getSubject());
        booking.setBookingDate(dto.getBookingDate());
        booking.setStartTime(dto.getStartTime());
        booking.setEndTime(dto.getEndTime());
        booking.setAddress(dto.getAddress());
        booking.setMessage(dto.getMessage());

        Booking saved = bookingApplicationService.createBooking(dto.getTutorProfileId(), booking);
        return R.ok(toVO(saved));
    }

    @PostMapping("/{id}/confirm")
    public R<BookingVO> confirmBooking(@PathVariable Long id) {
        Booking booking = bookingApplicationService.confirmBooking(id);
        return R.ok(toVO(booking));
    }

    @PostMapping("/{id}/reject")
    public R<BookingVO> rejectBooking(@PathVariable Long id, @RequestParam(required = false) String reason) {
        Booking booking = bookingApplicationService.rejectBooking(id, reason);
        return R.ok(toVO(booking));
    }

    @PostMapping("/{id}/cancel")
    public R<BookingVO> cancelBooking(@PathVariable Long id) {
        Booking booking = bookingApplicationService.cancelBooking(id);
        return R.ok(toVO(booking));
    }

    @GetMapping("/my")
    public R<List<BookingVO>> getMyBookings() {
        List<Booking> bookings = bookingApplicationService.getMyBookings();
        List<BookingVO> voList = bookings.stream().map(this::toVO).collect(Collectors.toList());
        return R.ok(voList);
    }

    @GetMapping("/{id}")
    public R<BookingVO> getBookingById(@PathVariable Long id) {
        Booking booking = bookingApplicationService.findById(id);
        return R.ok(booking != null ? toVO(booking) : null);
    }

    private BookingVO toVO(Booking booking) {
        User parent = userRepository.findById(booking.getParentId());
        User tutor = userRepository.findById(booking.getTutorId());
        return BookingVO.builder()
                .id(booking.getId())
                .parentId(booking.getParentId())
                .parentName(parent != null ? parent.getRealName() : "家长")
                .tutorId(booking.getTutorId())
                .tutorName(tutor != null ? tutor.getRealName() : "教员")
                .tutorProfileId(booking.getTutorProfileId())
                .subject(booking.getSubject())
                .bookingDate(booking.getBookingDate())
                .startTime(booking.getStartTime())
                .endTime(booking.getEndTime())
                .address(booking.getAddress())
                .message(booking.getMessage())
                .status(booking.getStatus() != null ? booking.getStatus().getCode() : null)
                .statusDesc(booking.getStatus() != null ? booking.getStatus().getDesc() : null)
                .reply(booking.getReply())
                .createdAt(booking.getCreatedAt())
                .build();
    }
}