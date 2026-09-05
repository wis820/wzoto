package com.wzoto.application.service;

import com.wzoto.domain.context.UserContext;
import com.wzoto.domain.entity.TutorProfile;
import com.wzoto.domain.service.TutorProfileDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TutorProfileApplicationService {

    private final TutorProfileDomainService tutorProfileDomainService;

    public TutorProfile createProfile(TutorProfile profileData) {
        Long userId = UserContext.getCurrentUserId();
        return tutorProfileDomainService.createProfile(userId, profileData);
    }

    public TutorProfile updateProfile(TutorProfile updateData) {
        Long userId = UserContext.getCurrentUserId();
        return tutorProfileDomainService.updateProfile(userId, updateData);
    }

    public TutorProfile toggleActive() {
        Long userId = UserContext.getCurrentUserId();
        return tutorProfileDomainService.toggleActive(userId);
    }

    public TutorProfile getMyProfile() {
        Long userId = UserContext.getCurrentUserId();
        return tutorProfileDomainService.getProfile(userId);
    }

    public List<TutorProfile> searchTutors(String subject, String district, int page, int size) {
        return tutorProfileDomainService.searchTutors(subject, district, page, size);
    }

    public TutorProfile getProfileById(Long id) {
        return tutorProfileDomainService.getProfile(id);
    }
}