package com.wzoto.application.service;

import com.wzoto.domain.context.UserContext;
import com.wzoto.domain.entity.Booking;
import com.wzoto.domain.service.BookingDomainService;
import com.wzoto.domain.valobj.BookingStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingApplicationService {

    private final BookingDomainService bookingDomainService;

    public Booking createBooking(Long tutorProfileId, Booking bookingData) {
        Long parentId = UserContext.getCurrentUserId();
        return bookingDomainService.createBooking(parentId, tutorProfileId, bookingData);
    }

    public Booking confirmBooking(Long bookingId) {
        Long tutorId = UserContext.getCurrentUserId();
        return bookingDomainService.confirmBooking(bookingId, tutorId);
    }

    public Booking rejectBooking(Long bookingId, String reason) {
        Long tutorId = UserContext.getCurrentUserId();
        return bookingDomainService.rejectBooking(bookingId, tutorId, reason);
    }

    public Booking cancelBooking(Long bookingId) {
        Long parentId = UserContext.getCurrentUserId();
        return bookingDomainService.cancelBooking(bookingId, parentId);
    }

    public List<Booking> getMyBookings() {
        Long userId = UserContext.getCurrentUserId();
        // 根据身份类型返回不同视角
        String identityType = UserContext.getCurrentIdentityType();
        if ("STUDENT".equals(identityType)) {
            return bookingDomainService.findByTutorId(userId);
        }
        return bookingDomainService.findByParentId(userId);
    }

    public Booking findById(Long id) {
        return bookingDomainService.findById(id);
    }
}