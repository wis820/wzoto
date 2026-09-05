package com.wzoto.domain.repository;

import com.wzoto.domain.entity.Booking;
import com.wzoto.domain.valobj.BookingStatus;

import java.util.List;

public interface BookingRepository {

    Booking save(Booking booking);

    Booking findById(Long id);

    /** 家长查看我的预约 */
    List<Booking> findByParentId(Long parentId);

    /** 教员查看收到的预约 */
    List<Booking> findByTutorId(Long tutorId);

    /** 按状态查询 */
    List<Booking> findByTutorIdAndStatus(Long tutorId, BookingStatus status);

    List<Booking> findByParentIdAndStatus(Long parentId, BookingStatus status);

    Booking update(Booking booking);
}