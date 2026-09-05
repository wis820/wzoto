package com.wzoto.domain.repository;

import com.wzoto.domain.entity.TutorProfile;

import java.util.List;

/**
 * 教员档案仓储接口
 */
public interface TutorProfileRepository {

    TutorProfile save(TutorProfile profile);

    TutorProfile findById(Long id);

    TutorProfile findByUserId(Long userId);

    TutorProfile update(TutorProfile profile);

    /** 按科目搜索上架中的教员 */
    List<TutorProfile> searchBySubject(String subject, String district, int page, int size);

    /** 获取上架教员列表 */
    List<TutorProfile> findActiveProfiles(int page, int size);
}