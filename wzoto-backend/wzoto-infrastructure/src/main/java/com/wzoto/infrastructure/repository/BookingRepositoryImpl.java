package com.wzoto.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzoto.domain.entity.Booking;
import com.wzoto.domain.repository.BookingRepository;
import com.wzoto.domain.valobj.BookingStatus;
import com.wzoto.infrastructure.mapper.BookingMapper;
import com.wzoto.infrastructure.pojo.BookingPO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class BookingRepositoryImpl implements BookingRepository {

    private final BookingMapper bookingMapper;

    @Override
    public Booking save(Booking booking) {
        BookingPO po = toPO(booking);
        bookingMapper.insert(po);
        booking.setId(po.getId());
        return booking;
    }

    @Override
    public Booking findById(Long id) {
        BookingPO po = bookingMapper.selectById(id);
        return po != null ? toEntity(po) : null;
    }

    @Override
    public List<Booking> findByParentId(Long parentId) {
        LambdaQueryWrapper<BookingPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BookingPO::getParentId, parentId).orderByDesc(BookingPO::getCreatedAt);
        return bookingMapper.selectList(wrapper).stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public List<Booking> findByTutorId(Long tutorId) {
        LambdaQueryWrapper<BookingPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BookingPO::getTutorId, tutorId).orderByDesc(BookingPO::getCreatedAt);
        return bookingMapper.selectList(wrapper).stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public List<Booking> findByTutorIdAndStatus(Long tutorId, BookingStatus status) {
        LambdaQueryWrapper<BookingPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BookingPO::getTutorId, tutorId)
                .eq(BookingPO::getStatus, status.getCode())
                .orderByDesc(BookingPO::getCreatedAt);
        return bookingMapper.selectList(wrapper).stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public List<Booking> findByParentIdAndStatus(Long parentId, BookingStatus status) {
        LambdaQueryWrapper<BookingPO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(BookingPO::getParentId, parentId)
                .eq(BookingPO::getStatus, status.getCode())
                .orderByDesc(BookingPO::getCreatedAt);
        return bookingMapper.selectList(wrapper).stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public Booking update(Booking booking) {
        BookingPO po = toPO(booking);
        bookingMapper.updateById(po);
        return booking;
    }

    private Booking toEntity(BookingPO po) {
        Booking entity = new Booking();
        entity.setId(po.getId());
        entity.setParentId(po.getParentId());
        entity.setTutorId(po.getTutorId());
        entity.setTutorProfileId(po.getTutorProfileId());
        entity.setSubject(po.getSubject());
        entity.setBookingDate(po.getBookingDate());
        entity.setStartTime(po.getStartTime());
        entity.setEndTime(po.getEndTime());
        entity.setAddress(po.getAddress());
        entity.setMessage(po.getMessage());
        entity.setStatus(po.getStatus() != null ? BookingStatus.fromCode(po.getStatus()) : null);
        entity.setReply(po.getReply());
        entity.setCreatedAt(po.getCreatedAt());
        entity.setUpdatedAt(po.getUpdatedAt());
        return entity;
    }

    private BookingPO toPO(Booking entity) {
        BookingPO po = new BookingPO();
        po.setId(entity.getId());
        po.setParentId(entity.getParentId());
        po.setTutorId(entity.getTutorId());
        po.setTutorProfileId(entity.getTutorProfileId());
        po.setSubject(entity.getSubject());
        po.setBookingDate(entity.getBookingDate());
        po.setStartTime(entity.getStartTime());
        po.setEndTime(entity.getEndTime());
        po.setAddress(entity.getAddress());
        po.setMessage(entity.getMessage());
        po.setStatus(entity.getStatus() != null ? entity.getStatus().getCode() : null);
        po.setReply(entity.getReply());
        return po;
    }
}