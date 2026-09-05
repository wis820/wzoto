package com.wzoto.infrastructure.repository;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.wzoto.domain.entity.TutorProfile;
import com.wzoto.domain.repository.TutorProfileRepository;
import com.wzoto.infrastructure.mapper.TutorProfileMapper;
import com.wzoto.infrastructure.pojo.TutorProfilePO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class TutorProfileRepositoryImpl implements TutorProfileRepository {

    private final TutorProfileMapper tutorProfileMapper;

    @Override
    public TutorProfile save(TutorProfile profile) {
        TutorProfilePO po = toPO(profile);
        tutorProfileMapper.insert(po);
        profile.setId(po.getId());
        return profile;
    }

    @Override
    public TutorProfile findById(Long id) {
        TutorProfilePO po = tutorProfileMapper.selectById(id);
        return po != null ? toEntity(po) : null;
    }

    @Override
    public TutorProfile findByUserId(Long userId) {
        LambdaQueryWrapper<TutorProfilePO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TutorProfilePO::getUserId, userId).last("LIMIT 1");
        TutorProfilePO po = tutorProfileMapper.selectOne(wrapper);
        return po != null ? toEntity(po) : null;
    }

    @Override
    public TutorProfile update(TutorProfile profile) {
        TutorProfilePO po = toPO(profile);
        tutorProfileMapper.updateById(po);
        return profile;
    }

    @Override
    public List<TutorProfile> searchBySubject(String subject, String district, int page, int size) {
        LambdaQueryWrapper<TutorProfilePO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TutorProfilePO::getActive, true);
        if (subject != null && !subject.isBlank()) {
            wrapper.like(TutorProfilePO::getSubjects, subject);
        }
        if (district != null && !district.isBlank()) {
            wrapper.like(TutorProfilePO::getDistricts, district);
        }
        // P1: 置顶教员优先排序（置顶到期时间未过期的排在前面）
        wrapper.last("ORDER BY (pinned_until IS NOT NULL AND pinned_until > NOW()) DESC, rating DESC LIMIT " + size + " OFFSET " + (page - 1) * size);
        return tutorProfileMapper.selectList(wrapper).stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<TutorProfile> findActiveProfiles(int page, int size) {
        LambdaQueryWrapper<TutorProfilePO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TutorProfilePO::getActive, true);
        // P1: 置顶教员优先排序
        wrapper.last("ORDER BY (pinned_until IS NOT NULL AND pinned_until > NOW()) DESC, rating DESC, order_count DESC LIMIT " + size + " OFFSET " + (page - 1) * size);
        return tutorProfileMapper.selectList(wrapper).stream()
                .map(this::toEntity)
                .collect(Collectors.toList());
    }

    // ========== 转换方法 ==========

    private TutorProfile toEntity(TutorProfilePO po) {
        TutorProfile entity = new TutorProfile();
        entity.setId(po.getId());
        entity.setUserId(po.getUserId());
        entity.setUniversity(po.getUniversity());
        entity.setMajor(po.getMajor());
        entity.setGrade(po.getGrade());
        entity.setSubjects(po.getSubjects());
        entity.setHourlyRate(po.getHourlyRate());
        entity.setBio(po.getBio());
        entity.setExperience(po.getExperience());
        entity.setDistricts(po.getDistricts());
        entity.setAvailableTimes(po.getAvailableTimes());
        entity.setRating(po.getRating());
        entity.setReviewCount(po.getReviewCount());
        entity.setOrderCount(po.getOrderCount());
        entity.setActive(po.getActive());
        entity.setPinnedUntil(po.getPinnedUntil());
        entity.setCreatedAt(po.getCreatedAt());
        entity.setUpdatedAt(po.getUpdatedAt());
        return entity;
    }

    private TutorProfilePO toPO(TutorProfile entity) {
        TutorProfilePO po = new TutorProfilePO();
        po.setId(entity.getId());
        po.setUserId(entity.getUserId());
        po.setUniversity(entity.getUniversity());
        po.setMajor(entity.getMajor());
        po.setGrade(entity.getGrade());
        po.setSubjects(entity.getSubjects());
        po.setHourlyRate(entity.getHourlyRate());
        po.setBio(entity.getBio());
        po.setExperience(entity.getExperience());
        po.setDistricts(entity.getDistricts());
        po.setAvailableTimes(entity.getAvailableTimes());
        po.setRating(entity.getRating());
        po.setReviewCount(entity.getReviewCount());
        po.setOrderCount(entity.getOrderCount());
        po.setActive(entity.getActive());
        po.setPinnedUntil(entity.getPinnedUntil());
        return po;
    }
}