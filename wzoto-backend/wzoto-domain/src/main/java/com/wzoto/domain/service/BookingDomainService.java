package com.wzoto.domain.service;

import com.wzoto.domain.entity.Booking;
import com.wzoto.domain.entity.TutorProfile;
import com.wzoto.domain.entity.User;
import com.wzoto.domain.repository.BookingRepository;
import com.wzoto.domain.repository.TutorProfileRepository;
import com.wzoto.domain.repository.UserRepository;
import com.wzoto.domain.valobj.BookingStatus;
import com.wzoto.domain.valobj.IdentityType;
import com.wzoto.domain.valobj.VerifyStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookingDomainService {

    private final BookingRepository bookingRepository;
    private final TutorProfileRepository tutorProfileRepository;
    private final UserRepository userRepository;

    /**
     * 家长发起预约
     * 业务规则：家长已认证才能预约
     */
    public Booking createBooking(Long parentId, Long tutorProfileId, Booking bookingData) {
        User parent = userRepository.findById(parentId);
        if (parent == null || parent.getIdentityType() != IdentityType.PARENT) {
            throw new IllegalArgumentException("仅家长可发起预约");
        }
        if (parent.getVerifyStatus() != VerifyStatus.APPROVED) {
            throw new IllegalStateException("请先完成实名认证");
        }

        TutorProfile profile = tutorProfileRepository.findById(tutorProfileId);
        if (profile == null || !profile.getActive()) {
            throw new IllegalArgumentException("教员档案不存在或已下架");
        }

        bookingData.setParentId(parentId);
        bookingData.setTutorId(profile.getUserId());
        bookingData.setTutorProfileId(tutorProfileId);
        bookingData.create();

        Booking saved = bookingRepository.save(bookingData);
        log.info("预约创建成功, parentId={}, tutorId={}", parentId, profile.getUserId());
        return saved;
    }

    /**
     * 教员确认预约
     */
    public Booking confirmBooking(Long bookingId, Long tutorUserId) {
        Booking booking = bookingRepository.findById(bookingId);
        if (booking == null) {
            throw new IllegalArgumentException("预约不存在");
        }
        booking.confirm(tutorUserId);
        return bookingRepository.update(booking);
    }

    /**
     * 教员拒绝预约
     */
    public Booking rejectBooking(Long bookingId, Long tutorUserId, String reason) {
        Booking booking = bookingRepository.findById(bookingId);
        if (booking == null) {
            throw new IllegalArgumentException("预约不存在");
        }
        booking.reject(tutorUserId, reason);
        return bookingRepository.update(booking);
    }

    /**
     * 家长取消预约
     */
    public Booking cancelBooking(Long bookingId, Long parentUserId) {
        Booking booking = bookingRepository.findById(bookingId);
        if (booking == null) {
            throw new IllegalArgumentException("预约不存在");
        }
        booking.cancel(parentUserId);
        return bookingRepository.update(booking);
    }

    /**
     * 完成预约
     */
    public Booking completeBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId);
        if (booking == null) {
            throw new IllegalArgumentException("预约不存在");
        }
        booking.complete();
        return bookingRepository.update(booking);
    }

    public Booking findById(Long id) {
        return bookingRepository.findById(id);
    }

    public List<Booking> findByParentId(Long parentId) {
        return bookingRepository.findByParentId(parentId);
    }

    public List<Booking> findByTutorId(Long tutorId) {
        return bookingRepository.findByTutorId(tutorId);
    }

    public List<Booking> findByTutorIdAndStatus(Long tutorId, BookingStatus status) {
        return bookingRepository.findByTutorIdAndStatus(tutorId, status);
    }

    public List<Booking> findByParentIdAndStatus(Long parentId, BookingStatus status) {
        return bookingRepository.findByParentIdAndStatus(parentId, status);
    }
}