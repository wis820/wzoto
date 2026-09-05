package com.wzoto.domain.service;

import com.wzoto.domain.entity.TutorProfile;
import com.wzoto.domain.entity.User;
import com.wzoto.domain.repository.TutorProfileRepository;
import com.wzoto.domain.repository.UserRepository;
import com.wzoto.domain.valobj.IdentityType;
import com.wzoto.domain.valobj.VerifyStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 教员档案领域服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TutorProfileDomainService {

    private final TutorProfileRepository tutorProfileRepository;
    private final UserRepository userRepository;

    /**
     * 创建教员档案
     * 业务规则：认证通过的大学生才能创建
     */
    public TutorProfile createProfile(Long userId, TutorProfile profileData) {
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("用户不存在");
        }
        if (user.getIdentityType() != IdentityType.STUDENT) {
            throw new IllegalArgumentException("仅大学生可创建教员档案");
        }
        if (user.getVerifyStatus() != VerifyStatus.APPROVED) {
            throw new IllegalStateException("请先完成实名认证");
        }

        // 检查是否已有档案
        TutorProfile existing = tutorProfileRepository.findByUserId(userId);
        if (existing != null) {
            throw new IllegalStateException("您已创建教员档案，请直接编辑");
        }

        profileData.setUserId(userId);
        profileData.create();
        TutorProfile saved = tutorProfileRepository.save(profileData);

        log.info("教员档案创建成功, userId={}", userId);
        return saved;
    }

    /**
     * 更新教员档案
     */
    public TutorProfile updateProfile(Long userId, TutorProfile updateData) {
        TutorProfile existing = tutorProfileRepository.findByUserId(userId);
        if (existing == null) {
            throw new IllegalArgumentException("教员档案不存在");
        }
        existing.updateProfile(updateData);
        return tutorProfileRepository.update(existing);
    }

    /**
     * 上架/下架
     */
    public TutorProfile toggleActive(Long userId) {
        TutorProfile profile = tutorProfileRepository.findByUserId(userId);
        if (profile == null) {
            throw new IllegalArgumentException("教员档案不存在");
        }
        profile.toggleActive();
        return tutorProfileRepository.update(profile);
    }

    /**
     * 获取教员档案
     */
    public TutorProfile getProfile(Long userId) {
        return tutorProfileRepository.findByUserId(userId);
    }

    /**
     * 搜索教员
     */
    public List<TutorProfile> searchTutors(String subject, String district, int page, int size) {
        if (subject != null && !subject.isBlank()) {
            return tutorProfileRepository.searchBySubject(subject, district, page, size);
        }
        return tutorProfileRepository.findActiveProfiles(page, size);
    }
}